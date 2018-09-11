package com.hhly.commoncore.remote.cooperate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.base.rabbitmq.provider.MessageProvider;
import com.hhly.commoncore.base.util.RedisUtil;
import com.hhly.commoncore.base.util.UserUtil;
import com.hhly.commoncore.http.lottocore.order.OrderService;
import com.hhly.commoncore.http.usercore.passport.ThirdPartyLoginService;
import com.hhly.commoncore.local.lotto.service.LotteryIssueService;
import com.hhly.commoncore.persistence.cooperate.dao.CoOperateExchangeRecordDaoMapper;
import com.hhly.commoncore.persistence.cooperate.dao.CoOperateLotteryDaoMapper;
import com.hhly.commoncore.persistence.cooperate.po.CoOperateExchangeRecordPO;
import com.hhly.commoncore.persistence.cooperate.po.CoOperateLotteryPO;
import com.hhly.commoncore.persistence.lottery.dao.LotteryTypeDaoMapper;
import com.hhly.commoncore.remote.cooperate.service.ICoOperateAddOrderService;
import com.hhly.commoncore.remote.cooperate.service.ICoOperateCdkeyService;
import com.hhly.commoncore.remote.cooperate.service.ICoOperateChannelService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.common.CoOperateEnum;
import com.hhly.skeleton.base.common.OrderEnum;
import com.hhly.skeleton.base.common.OrderEnum.NumberCode;
import com.hhly.skeleton.base.common.OrderFlowInfoEnum;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.constants.MessageApiCodeConstants;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.constants.UserConstants.LoginTypeEnum;
import com.hhly.skeleton.base.exception.ResultJsonException;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.LottoRandomUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.OrderNoUtil;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateCdkeyQueueBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateOrderInfoBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateOrderInfoVO;
import com.hhly.skeleton.lotto.base.issue.bo.CurrentAndPreIssueBO;
import com.hhly.skeleton.lotto.base.lottery.bo.LotteryBO;
import com.hhly.skeleton.lotto.base.lottery.vo.LotteryVO;
import com.hhly.skeleton.lotto.base.order.vo.OrderDetailVO;
import com.hhly.skeleton.user.bo.UserInfoBO;
import com.hhly.skeleton.user.vo.TPInfoVO;

import net.sf.json.JSONObject;

/**
 * 积分兑换下单
 *
 * @author huangchengfang1219
 * @date 2018年3月29日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service("coOperateAddOrderService")
public class CoOperateAddOrderServiceImpl implements ICoOperateAddOrderService {

	private static final Logger logger = LoggerFactory.getLogger(CoOperateChannelLoginServiceImpl.class);

	@Autowired
	private CoOperateExchangeRecordDaoMapper coOperateExchangeRecordDaoMapper;
	@Autowired
	private CoOperateLotteryDaoMapper coOperateLotteryDaoMapper;
	@Autowired
	private LotteryTypeDaoMapper lotteryTypeDaoMapper;
	@Autowired
	private ICoOperateChannelService coOperateChannelService;
	@Autowired
	private ICoOperateCdkeyService coOperateCdkeyService;
	@Autowired
	private LotteryIssueService lotteryIssueService;
	@Autowired
	private ThirdPartyLoginService thirdPartyLoginService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private UserUtil userUtil;
	@Autowired
	private MessageProvider messageProvider;
	@Value("${split_queue}")
	private String splitQueue;
	@Value("${orderflow_queue}")
	private String orderFlowQueue;

	@Override
	public CoOperateOrderInfoBO addSimpleOrder(CoOperateOrderInfoVO vo) {
		if (vo.getMultipleNum() == null) {
			vo.setMultipleNum(Constants.NUM_1);
		}
		List<String> lottos = LottoRandomUtil.randomMulti(vo.getLotteryCode(), vo.getNum().intValue());
		// 组装参数
		int codeWay = OrderEnum.CodeWay.MACHINE.getValue();
		List<OrderDetailVO> detailList = new ArrayList<>(lottos.size());
		Integer lotteryChildCode = LottoRandomUtil.getDefaultChildCode(vo.getLotteryCode());
		for (String lotto : lottos) {
			OrderDetailVO detailVO = new OrderDetailVO();
			detailVO.setAmount(2d);
			detailVO.setBuyNumber(1);
			detailVO.setCodeWay(codeWay);
			detailVO.setContentType(OrderEnum.BetContentType.SINGLE.getValue());
			detailVO.setLotteryChildCode(lotteryChildCode);
			detailVO.setMultiple(1);
			detailVO.setPlanContent(lotto);
			detailList.add(detailVO);
		}
		vo.setOrderDetailList(detailList);
		vo.setOrderAmount((double) (Constants.NUM_2 * detailList.size()));
		return addOrder(vo);
	}

	@Override
	public CoOperateOrderInfoBO addOrder(CoOperateOrderInfoVO vo) {
		CoOperateChannelBO channelBO = coOperateChannelService.findAndValidChannel(vo.getChannelId());
		CoOperateLotteryPO lottery = findLottery(vo.getLotteryCode(), channelBO.getChannelId());
		countCdkeyNum(vo);
		coOperateChannelService.updateBalance(vo.getNum(), channelBO, lottery);
		List<CoOperateCdkeyQueueBO> cdkeyList = null;
		try {
			cdkeyList = coOperateCdkeyService.findCdkeyList(vo.getNum());
			String myCdkey = coOperateCdkeyService.updatForAddMyCdkey(vo.getLotteryCode(), cdkeyList,
					CoOperateEnum.CdkeyStatusEnum.EXCHANGED.getValue());
			vo.setRedeemCode(myCdkey);
			addExchangeRecord(vo, channelBO, lottery, cdkeyList);
			UserInfoBO userInfo = doChannelLoginUser(vo);
			String orderCode = addOrder(vo, userInfo);
			updateExchangeRecord(vo, orderCode);
			sendMessage(vo, orderCode);
			CoOperateOrderInfoBO orderInfo = new CoOperateOrderInfoBO();
			orderInfo.setOrderCode(orderCode);
			orderInfo.setCdkey(myCdkey);
			return orderInfo;
		} catch (Exception e) {
			logger.error("下单失败，回滚积分兑换码", e);
			coOperateCdkeyService.rollbackCdkeyList(cdkeyList);
			throw e;
		}
	}

	/**
	 * 查询彩种
	 * 
	 * @param vo
	 * @param channelBO
	 * @return
	 */
	private CoOperateLotteryPO findLottery(Integer lotteryCode, String channelId) {
		CoOperateLotteryPO lotteryBO = coOperateLotteryDaoMapper.findByChannel(lotteryCode, channelId);
		// 判断彩种
		if (lotteryBO == null) {
			throw new ResultJsonException(ResultBO.errApi(MessageApiCodeConstants.COOPERATE_CHANNEL_LOTTERY_NOT_FOUND));
		}
		return lotteryBO;
	}

	/**
	 * 计算订单所需兑换码数量
	 * 
	 * @param vo
	 */
	private void countCdkeyNum(CoOperateOrderInfoVO vo) {
		Long count = 0L;
		for (OrderDetailVO orderDetailVO : vo.getOrderDetailList()) {
			if (orderDetailVO.getBuyNumber() == null) {
				orderDetailVO.setBuyNumber(Constants.NUM_1);
			}
			if (orderDetailVO.getMultiple() == null) {
				orderDetailVO.setMultiple(Constants.NUM_1);
			}
			if (orderDetailVO.getCodeWay() == null) {
				orderDetailVO.setCodeWay(OrderEnum.CodeWay.HAND.getValue());
			}
			count += orderDetailVO.getBuyNumber() * orderDetailVO.getMultiple();
		}
		if (vo.getMultipleNum() == 0) {
			vo.setMultipleNum(Constants.NUM_1);
		}
		count *= vo.getMultipleNum();
		vo.setNum(count);
	}

	private UserInfoBO doChannelLoginUser(CoOperateOrderInfoVO vo) {
		String tokenKey = new StringBuilder(CacheConstants.C_CORE_COOPERATE_USER_TOKEN).append(vo.getChannelId())
				.append(SymbolConstants.UNDERLINE).append(vo.getOutUserId()).toString();
		String token = redisUtil.getString(tokenKey);
		if (!ObjectUtil.isBlank(token) && userUtil.getUserIdByToken(token) != null) {
			UserInfoBO userInfo = new UserInfoBO();
			userInfo.setToken(token);
			return userInfo;
		}
		TPInfoVO tpInfoVO = new TPInfoVO();
		tpInfoVO.setChannelId(vo.getChannelId());
		tpInfoVO.setOpenid(vo.getOutUserId());
		tpInfoVO.setPlatform(vo.getPlatform());
		tpInfoVO.setType(LoginTypeEnum.CHANNEL.getKey());
		tpInfoVO.setMobile(vo.getMobile());
		tpInfoVO.setIp(vo.getIp());
		tpInfoVO.setOrderNum((short) Constants.NUM_1);// 积分兑换渠道登录时，查询顶级渠道，渠道级别需要减1
		UserInfoBO userInfo = thirdPartyLoginService.tpChannelLogin(tpInfoVO);
		redisUtil.addString(tokenKey, userInfo.getToken(), CacheConstants.TWO_HOURS);
		return userInfo;
	}

	private String addOrder(CoOperateOrderInfoVO vo, UserInfoBO userInfo) {
		if (ObjectUtil.isBlank(vo.getLotteryIssue())) {
			CurrentAndPreIssueBO issueBO = lotteryIssueService.findIssueAndPreIssueByCode(vo.getLotteryCode());
			vo.setLotteryIssue(issueBO.getIssueCode());
			vo.setLotteryName(issueBO.getLotteryName());
		} else {
			LotteryVO lotteryVO = new LotteryVO();
			lotteryVO.setLotteryCode(vo.getLotteryCode());
			LotteryBO lotteryBO = lotteryTypeDaoMapper.findSingleFront(lotteryVO);
			vo.setLotteryName(lotteryBO.getLotteryName());
		}
		if (vo.getIsDltAdd() == null) {
			vo.setIsDltAdd(Constants.NO);
		}
		if (vo.getBuyType() == null) {
			vo.setBuyType(OrderEnum.BuyType.BUY.getValue());
		}
		vo.setToken(userInfo.getToken());
		String orderCode = orderService.addOrder(vo);
		return orderCode;
	}

	/**
	 * 插入流水
	 */
	private void addExchangeRecord(CoOperateOrderInfoVO vo, CoOperateChannelBO channelBO, CoOperateLotteryPO lottery,
			List<CoOperateCdkeyQueueBO> cdkeyList) {
		// 添加一条主流水（兑换流水）
		Date exchangeOverTime = DateUtil.addDay(new Date(), Constants.NUM_90);
		String serialNumber = OrderNoUtil.getOrderNo(NumberCode.RUNNING_WATER_OUT);
		CoOperateExchangeRecordPO exchangePO = new CoOperateExchangeRecordPO();
		exchangePO.setSerialNumber(serialNumber);
		exchangePO.setMyCdkey(vo.getRedeemCode());
		exchangePO.setChannelId(channelBO.getChannelId());
		exchangePO.setPayClass(CoOperateEnum.ExchangeRecordPayClass.EXCHANGE.getValue());
		exchangePO.setChannelBalance(channelBO.getBalance());
		exchangePO.setSerialType(CoOperateEnum.SerialType.MERCHANT_RECHARGE.getValue());
		exchangePO.setExchangeOverTime(exchangeOverTime);// 主流水也写入过期时间
		BigDecimal exchange = new BigDecimal(lottery.getExchangeBalance()).multiply(new BigDecimal(vo.getNum()));
		exchange.setScale(Constants.NUM_10, BigDecimal.ROUND_HALF_UP);
		exchangePO.setPayAmount(exchange.doubleValue());
		exchangePO.setOrderInfo("待兑换");
		exchangePO.setCreateBy("lotto-common-core");
		exchangePO.setModifyBy("lotto-common-core");
		coOperateExchangeRecordDaoMapper.insert(exchangePO);
		// 添加子流水（兑换码详情流水）
		List<CoOperateExchangeRecordPO> exchangePOList = new ArrayList<>();
		for (CoOperateCdkeyQueueBO cdkey : cdkeyList) {
			exchangePO = new CoOperateExchangeRecordPO();
			exchangePO.setpSerial(serialNumber);
			exchangePO.setPayClass(CoOperateEnum.ExchangeRecordPayClass.EXCHANGE.getValue());
			exchangePO.setCdkeyId(cdkey.getId());
			exchangePO.setChannelId(channelBO.getChannelId());
			exchangePO.setExchangeOverTime(exchangeOverTime);
			exchangePO.setSerialType(CoOperateEnum.SerialType.USE_CDKEY.getValue());
			exchangePO.setPayAmount(lottery.getExchangeBalance());
			exchangePO.setCreateBy("lotto-common-core");
			exchangePO.setModifyBy("lotto-common-core");
			exchangePOList.add(exchangePO);
		}
		coOperateExchangeRecordDaoMapper.insertBatch(exchangePOList);
	}

	/**
	 * 更新兑换码
	 * 
	 * @param vo
	 * @param orderCode
	 */
	private void updateExchangeRecord(CoOperateOrderInfoVO vo, String orderCode) {
		String myCdkey = vo.getRedeemCode();
		CoOperateExchangeRecordPO exchangePO = new CoOperateExchangeRecordPO();
		exchangePO.setOrderCode(orderCode);
		exchangePO.setOrderInfo(String.format("%s-兑换|%s|%s", vo.getLotteryName(), vo.getLotteryIssue(), myCdkey));
		exchangePO.setMyCdkey(myCdkey);
		exchangePO.setModifyBy("lotto-common-core");
		coOperateExchangeRecordDaoMapper.updateOrderCode(exchangePO);
		exchangePO.setOrderInfo(vo.getLotteryName());
		coOperateExchangeRecordDaoMapper.updateCdkeyOrderCode(exchangePO);
	}

	/**
	 * 发送通知
	 * 
	 * @param orderCode
	 */
	private void sendMessage(CoOperateOrderInfoVO vo, String orderCode) {
		JSONObject orderFlow = new JSONObject();
		orderFlow.put("orderCode", orderCode);
		orderFlow.put("createTime", DateUtil.convertDateToStr(new Date()));
		orderFlow.put("status", OrderFlowInfoEnum.StatusEnum.PAY_SUCCESS.getKey());
		orderFlow.put("buyType", vo.getBuyType());
		messageProvider.sendMessage(orderFlowQueue, orderFlow.toString());
		messageProvider.sendMessage(splitQueue, orderCode + "#1");
	}

}

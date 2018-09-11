package com.hhly.commoncore.remote.cooperate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.base.rabbitmq.provider.MessageProvider;
import com.hhly.commoncore.base.util.UserUtil;
import com.hhly.commoncore.http.lottocore.order.OrderService;
import com.hhly.commoncore.persistence.cooperate.dao.CoOperateCdkeyDaoMapper;
import com.hhly.commoncore.persistence.cooperate.dao.CoOperateExchangeRecordDaoMapper;
import com.hhly.commoncore.persistence.cooperate.dao.CoOperateLotteryDaoMapper;
import com.hhly.commoncore.persistence.cooperate.po.CoOperateCdkeyPO;
import com.hhly.commoncore.persistence.cooperate.po.CoOperateExchangeRecordPO;
import com.hhly.commoncore.persistence.cooperate.po.CoOperateLotteryPO;
import com.hhly.commoncore.persistence.issue.dao.LotteryIssueDaoMapper;
import com.hhly.commoncore.persistence.lottery.dao.LotteryTypeDaoMapper;
import com.hhly.commoncore.remote.cooperate.service.ICoOperateCdkeyService;
import com.hhly.commoncore.remote.cooperate.service.ICoOperateChannelService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.common.CoOperateEnum;
import com.hhly.skeleton.base.common.LotteryEnum;
import com.hhly.skeleton.base.common.OrderEnum;
import com.hhly.skeleton.base.common.OrderEnum.NumberCode;
import com.hhly.skeleton.base.common.OrderFlowInfoEnum;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.constants.MessageApiCodeConstants;
import com.hhly.skeleton.base.constants.MessageCodeConstants;
import com.hhly.skeleton.base.exception.ResultJsonException;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.LottoRandomUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.OrderNoUtil;
import com.hhly.skeleton.base.util.RegularValidateUtil;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateCdkeyCountBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateCdkeyExchangeBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateCdkeyQueueBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateLotteryCdkeyBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateOrderBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateCdkeyExchangeVO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateCdkeyVO;
import com.hhly.skeleton.lotto.base.issue.bo.CurrentAndPreIssueBO;
import com.hhly.skeleton.lotto.base.lottery.bo.LotteryBO;
import com.hhly.skeleton.lotto.base.lottery.vo.LotteryVO;
import com.hhly.skeleton.lotto.base.order.vo.OrderDetailVO;
import com.hhly.skeleton.lotto.base.order.vo.OrderInfoVO;
import com.hhly.skeleton.user.bo.UserInfoBO;

import net.sf.json.JSONObject;

@Service("coOperateCdkeyService")
public class CoOperateCdkeyServiceImpl implements ICoOperateCdkeyService {

	private static final Logger logger = LoggerFactory.getLogger(CoOperateCdkeyServiceImpl.class);

	@Autowired
	private CoOperateCdkeyDaoMapper coOperateCdkeyDaoMapper;
	@Autowired
	private CoOperateLotteryDaoMapper coOperateLotteryDaoMapper;
	@Autowired
	private CoOperateExchangeRecordDaoMapper coOperateExchangeRecordDaoMapper;
	@Autowired
	private LotteryIssueDaoMapper lotteryIssueDaoMapper;
	@Autowired
	private LotteryTypeDaoMapper lotteryTypeDaoMapper;
	@Autowired
	private ICoOperateChannelService coOperateChannelService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private RedissonClient redissonClient;
	@Autowired
	private UserUtil userUtil;
	@Autowired
	private MessageProvider messageProvider;
	@Autowired
	private RedisTemplate<String, CoOperateCdkeyQueueBO> redisTemplate;
	@Value("${split_queue}")
	private String splitQueue;
	@Value("${orderflow_queue}")
	private String orderFlowQueue;
	@Value("${lotto_core_url}")
	private String lottoCoreUrl;

	@Override
	public CoOperateLotteryCdkeyBO doObtainCdkey(CoOperateCdkeyVO vo) {
		if (vo.getNum() < 0 || vo.getNum() > 1000) {
			throw new ResultJsonException(ResultBO.errApi(MessageApiCodeConstants.COOPERATE_CDKEY_NUM_ERROR));
		}
		List<CoOperateCdkeyQueueBO> cdkeyList = null;
		try {
			CoOperateChannelBO channelBO = coOperateChannelService.findAndValidChannel(vo.getChannelId());
			CoOperateLotteryPO lottery = findLottery(vo, channelBO);
			coOperateChannelService.updateBalance(vo.getNum(), channelBO, lottery);
			cdkeyList = findCdkeyList(vo.getNum());
			return doMergeCdkey(vo, channelBO, lottery, cdkeyList);
		} catch (Exception e) {
			rollbackCdkeyList(cdkeyList);
			throw e;
		}
	}

	@Override
	public CoOperateOrderBO doExchangeCdkey(CoOperateCdkeyExchangeVO vo) {
		UserInfoBO userInfo = userUtil.getUserByToken(vo.getToken());
		if (ObjectUtil.isBlank(userInfo)) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.TOKEN_LOSE_SERVICE));
		}
		if (!vo.getCdkey().matches(RegularValidateUtil.REGULAR_CDKEY)) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.CDK_FORMAT_IS_ERROR));
		}
		vo.setCdkey(vo.getCdkey().toUpperCase());
		RLock cdkeyExchangelLock = null;
		try {
			cdkeyExchangelLock = redissonClient.getLock(getCdkeyExchangelLock(vo.getCdkey()));
			lock(cdkeyExchangelLock);
			CoOperateCdkeyCountBO countBO = coOperateCdkeyDaoMapper.findCountForExchange(vo.getCdkey());
			// 校验兑换码
			validCdkeyForExchange(vo, countBO);
			vo.setCdkey(countBO.getCdkey());
			//
			CurrentAndPreIssueBO issueBO = lotteryIssueDaoMapper.findIssueAndPreIssueByCode(countBO.getLotteryCode());
			// 下单
			String orderCode = doPorcessOrder(vo, countBO, issueBO);
			// 保存订单号
			coOperateCdkeyDaoMapper.updateExchanged(vo.getCdkey());
			CoOperateExchangeRecordPO exchangePO = new CoOperateExchangeRecordPO();
			exchangePO.setOrderCode(orderCode);
			exchangePO.setOrderInfo(String.format("%s-兑换|%s|%s", issueBO.getLotteryName(), issueBO.getIssueCode(), vo.getCdkey()));
			exchangePO.setMyCdkey(vo.getCdkey());
			exchangePO.setModifyBy("lotto-common-core");
			coOperateExchangeRecordDaoMapper.updateOrderCode(exchangePO);
			exchangePO.setOrderInfo(issueBO.getLotteryName());
			coOperateExchangeRecordDaoMapper.updateCdkeyOrderCode(exchangePO);
			// 发送MQ通知拆票
			sendMessage(orderCode);
			// 构建返回数据
			CoOperateOrderBO orderInfo = new CoOperateOrderBO();
			orderInfo.setOrderCode(orderCode);
			orderInfo.setBuyType(OrderEnum.BuyType.BUY.getValue());
			orderInfo.setLotteryCode(countBO.getLotteryCode());
			orderInfo.setCdkeyType(CoOperateEnum.CdkeyType.JF.getValue());
			return orderInfo;
		} catch (InterruptedException e) {
			logger.error("获取分布式锁异常", e);
			throw new RuntimeException("获取分布式锁异常", e);
		} finally {
			unlock(cdkeyExchangelLock);
		}
	}

	@Override
	public CoOperateCdkeyExchangeBO findAndValidCdkey(CoOperateCdkeyExchangeVO vo) {
		Integer userId = userUtil.getUserIdByToken(vo.getToken());
		if (userId == null) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.TOKEN_LOSE_SERVICE));
		}
		if (!vo.getCdkey().matches(RegularValidateUtil.REGULAR_CDKEY)) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.CDK_FORMAT_IS_ERROR));
		}
		vo.setCdkey(vo.getCdkey().toUpperCase());
		vo.setPlanContentList(null);
		CoOperateCdkeyCountBO countBO = coOperateCdkeyDaoMapper.findCountForExchange(vo.getCdkey());
		// 校验兑换码
		validCdkeyForExchange(vo, countBO);
		CoOperateCdkeyExchangeBO bo = new CoOperateCdkeyExchangeBO();
		bo.setLotteryCode(countBO.getLotteryCode());
		bo.setExchangeNum(countBO.getCanExchangeNum());
		LotteryVO lotteryVO = new LotteryVO();
		lotteryVO.setLotteryCode(countBO.getLotteryCode());
		LotteryBO lotteryBO = lotteryTypeDaoMapper.findSingleFront(lotteryVO);
		bo.setLotteryName(lotteryBO.getLotteryName());
		return bo;
	}

	@Override
	public List<CoOperateCdkeyQueueBO> findCdkeyList(long num) {
		RLock cdkeyLock = null;
		String key = CacheConstants.C_CORE_COOPERATE_CDKEY_QUEUE;
		try {
			cdkeyLock = redissonClient.getLock(CacheConstants.C_CORE_COOPERATE_CDKEY_LOCK);
			lock(cdkeyLock);
			Long length = redisTemplate.opsForList().size(key);
			if (length < num) {
				throw new ResultJsonException(ResultBO.errApi(MessageApiCodeConstants.COOPERATE_CHANNEL_CDKEY_NOT_ENOUGH));
			}
			Date nowTime = new Date();
			List<CoOperateCdkeyQueueBO> cdkeyList = new ArrayList<>();
			while (cdkeyList.size() < num && redisTemplate.opsForList().size(key) > 0) {
				long range = num - cdkeyList.size();
				List<CoOperateCdkeyQueueBO> eachCdkeyList = redisTemplate.opsForList().range(key, 0, range - 1);
				eachCdkeyList.forEach(cdkey -> {
					if (cdkey.getOverTime() == null || cdkey.getOverTime().after(nowTime)) {
						cdkeyList.add(cdkey);
					}
				});
				redisTemplate.opsForList().trim(key, range, -1);
			}
			if (cdkeyList.size() != num) {
				// 还原已取出兑换码
				if (cdkeyList.size() > 0) {
					redisTemplate.opsForList().leftPushAll(CacheConstants.C_CORE_COOPERATE_CDKEY_QUEUE, cdkeyList);
				}
				throw new ResultJsonException(ResultBO.errApi(MessageApiCodeConstants.COOPERATE_CHANNEL_CDKEY_NOT_ENOUGH));
			}
			return cdkeyList;
		} catch (InterruptedException e) {
			logger.error("获取分布式锁异常", e);
			throw new RuntimeException("获取分布式锁异常", e);
		} finally {
			unlock(cdkeyLock);
		}
	}

	@Override
	public String getRandomCdkey() {
		boolean exists = true;
		String cdkey = null;
		do {
			cdkey = OrderNoUtil.getOrderNo(NumberCode.CDKEY_INTEGRAL);
			int count = coOperateCdkeyDaoMapper.findCountByMyCdkey(cdkey);
			exists = count > 0;
		} while (exists);
		return cdkey;
	}

	@Override
	public String updatForAddMyCdkey(Integer lotteryCode, List<CoOperateCdkeyQueueBO> cdkeyList, Short status) {
		String myCdkey = getRandomCdkey();
		// 更新本站兑换码
		CoOperateCdkeyPO cdkeyPO = new CoOperateCdkeyPO();
		cdkeyPO.setMyCdkey(myCdkey);
		cdkeyPO.setLotteryCode(lotteryCode);
		cdkeyPO.setStatus(status);
		cdkeyPO.setModifyBy("lotto-common-core");
		coOperateCdkeyDaoMapper.updateMyCdkey(cdkeyPO, cdkeyList);
		return myCdkey;
	}

	/**
	 * 查询彩种
	 * 
	 * @param vo
	 * @param channelBO
	 * @return
	 */
	private CoOperateLotteryPO findLottery(CoOperateCdkeyVO vo, CoOperateChannelBO channelBO) {
		CoOperateLotteryPO lotteryBO = coOperateLotteryDaoMapper.findByChannel(vo.getLotteryCode(), channelBO.getChannelId());
		// 判断彩种
		if (lotteryBO == null) {
			throw new ResultJsonException(ResultBO.errApi(MessageApiCodeConstants.COOPERATE_CHANNEL_LOTTERY_NOT_FOUND));
		}
		return lotteryBO;
	}

	@Override
	public void rollbackCdkeyList(List<CoOperateCdkeyQueueBO> cdkeyList) {
		if (ObjectUtil.isBlank(cdkeyList)) {
			return;
		}
		RLock cdkeyLock = null;
		logger.error("获取积分兑换码失败，回滚积分兑换码");
		try {
			cdkeyLock = redissonClient.getLock(CacheConstants.C_CORE_COOPERATE_CDKEY_LOCK);
			lock(cdkeyLock);
			redisTemplate.opsForList().leftPushAll(CacheConstants.C_CORE_COOPERATE_CDKEY_QUEUE, cdkeyList);
		} catch (InterruptedException e) {
			logger.error("获取分布式锁异常", e);
			throw new RuntimeException("获取分布式锁异常", e);
		} finally {
			unlock(cdkeyLock);
		}
	}

	/**
	 * 合并中心的兑换码，生成本站兑换码
	 * 
	 * @param vo
	 * @param channelBO
	 * @param lottery
	 * @param cdkeyList
	 */
	private CoOperateLotteryCdkeyBO doMergeCdkey(CoOperateCdkeyVO vo, CoOperateChannelBO channelBO, CoOperateLotteryPO lottery,
			List<CoOperateCdkeyQueueBO> cdkeyList) {
		// 生成本站兑换码
		String myCdkey = updatForAddMyCdkey(vo.getLotteryCode(), cdkeyList, CoOperateEnum.CdkeyStatusEnum.UNEXCHANGE.getValue());

		// 添加一条主流水（兑换流水）
		Date exchangeOverTime = DateUtil.addDay(new Date(), Constants.NUM_90);
		String serialNumber = OrderNoUtil.getOrderNo(NumberCode.RUNNING_WATER_OUT);
		CoOperateExchangeRecordPO exchangePO = new CoOperateExchangeRecordPO();
		exchangePO.setSerialNumber(serialNumber);
		exchangePO.setMyCdkey(myCdkey);
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
		// 返回参数
		CoOperateLotteryCdkeyBO lotteryCdkeyBO = new CoOperateLotteryCdkeyBO();
		lotteryCdkeyBO.setCdkey(myCdkey);
		lotteryCdkeyBO.setOverTime(exchangeOverTime);
		lotteryCdkeyBO.setLotteryCode(lottery.getLotteryCode());
		lotteryCdkeyBO.setLotteryName(lottery.getLotteryName());
		return lotteryCdkeyBO;
	}

	/**
	 * 兑换码兑换分布式锁
	 * 
	 * @param cdkey
	 * @return
	 */
	private String getCdkeyExchangelLock(String cdkey) {
		return CacheConstants.C_CORE_COOPERATE_CDKEY_EXCHANGE_LOCK + cdkey;
	}

	/**
	 * 加锁
	 * 
	 * @param lock
	 * @throws InterruptedException
	 */
	private void lock(RLock lock) throws InterruptedException {
		// 尝试加锁，最多等待30秒，上锁以后10秒自动解锁
		boolean isLock = lock.tryLock(30, 10, TimeUnit.SECONDS);
		if (!isLock) {
			logger.error("加锁失败：{}", lock.getName());
			throw new RuntimeException("加锁失败:" + lock.getName());
		}
	}

	private void unlock(RLock lock) {
		if (lock != null) {
			try {
				lock.unlock();
			} catch (Exception e) {
				logger.error("解锁失败", e);
			}
		}
	}

	/**
	 * 校验
	 * 
	 * @param cdkey
	 */
	private void validCdkeyForExchange(CoOperateCdkeyExchangeVO vo, CoOperateCdkeyCountBO cdkeyCount) {
		if (cdkeyCount == null || cdkeyCount.getTotalNum() == 0) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.CDK_IS_NOT_FIND));
		}
		if (cdkeyCount.getExchangedNum() != null && cdkeyCount.getExchangedNum() > 0) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.THE_CDK_IS_USED));
		}
		if (cdkeyCount.getOverTimeNum() != null && cdkeyCount.getOverTimeNum() > 0) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.CDK_IS_OVERTIME));
		}
		if (cdkeyCount.getCanExchangeNum() == null || cdkeyCount.getCanExchangeNum() == 0) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.CDK_IS_ERR));
		}
		if (!ObjectUtil.isBlank(vo.getPlanContentList()) && vo.getPlanContentList().size() != cdkeyCount.getCanExchangeNum()) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.CDK_PLAN_CONTENT_NOT_MATCH, cdkeyCount.getCanExchangeNum()));
		}
	}

	/**
	 * 执行下单操作
	 * 
	 * @param vo
	 * @param lotteryCode
	 * @return
	 */
	private String doPorcessOrder(CoOperateCdkeyExchangeVO vo, CoOperateCdkeyCountBO countBO, CurrentAndPreIssueBO issueBO) {
		Integer lotteryCode = countBO.getLotteryCode();
		LotteryVO lotteryVO = new LotteryVO();
		lotteryVO.setLotteryCode(countBO.getLotteryCode());
		lotteryVO.setCurrentIssue(LotteryEnum.ConIssue.CURRENT.getValue());
		if (issueBO == null || issueBO.getIssueCode() == null) {
			logger.error("当前期为空:{}", countBO.getLotteryCode());
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.CURRENT_ISSUE_EMPTY));
		}
		// 组装下单参数
		OrderInfoVO orderInfoVO = new OrderInfoVO();
		orderInfoVO.setLotteryCode(lotteryCode);
		orderInfoVO.setLotteryIssue(issueBO.getIssueCode());
		orderInfoVO.setBuyType(OrderEnum.BuyType.BUY.getValue());
		orderInfoVO.setPlatform(vo.getPlatform());
		orderInfoVO.setChannelId(countBO.getChannelId());
		orderInfoVO.setIsDltAdd((short) Constants.NUM_0);
		orderInfoVO.setMultipleNum(Constants.NUM_1);
		orderInfoVO.setToken(vo.getToken());
		orderInfoVO.setRedeemCode(vo.getCdkey());
		List<OrderDetailVO> detailList = buildOrderDetailList(vo, lotteryCode, countBO.getCanExchangeNum());
		orderInfoVO.setOrderDetailList(detailList);
		orderInfoVO.setOrderAmount((double) (Constants.NUM_2 * detailList.size()));
		// 调用下单接口下单
		return orderService.addOrder(orderInfoVO);
	}

	private List<OrderDetailVO> buildOrderDetailList(CoOperateCdkeyExchangeVO vo, Integer lotteryCode, Integer num) {
		List<String> lottos = vo.getPlanContentList();
		// 如果参数没有带投注内容过来，则随机投注内容
		int codeWay = OrderEnum.CodeWay.HAND.getValue();
		if (ObjectUtil.isBlank(lottos)) {
			lottos = LottoRandomUtil.randomMulti(lotteryCode, num);
			codeWay = OrderEnum.CodeWay.MACHINE.getValue();
		}
		// 组装参数
		List<OrderDetailVO> detailList = new ArrayList<>(lottos.size());
		Integer lotteryChildCode = LottoRandomUtil.getDefaultChildCode(lotteryCode);
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
		return detailList;
	}

	/**
	 * 发送通知
	 * 
	 * @param orderCode
	 */
	private void sendMessage(String orderCode) {
		JSONObject orderFlow = new JSONObject();
		orderFlow.put("orderCode", orderCode);
		orderFlow.put("createTime", DateUtil.convertDateToStr(new Date()));
		orderFlow.put("status", OrderFlowInfoEnum.StatusEnum.PAY_SUCCESS.getKey());
		orderFlow.put("buyType", OrderEnum.BuyType.BUY.getValue());
		messageProvider.sendMessage(orderFlowQueue, orderFlow.toString());
		messageProvider.sendMessage(splitQueue, orderCode + "#1");
	}

	@Override
	public void doRefreshCdkeyQueue() {
		logger.info("开始重新加载兑换码队列:{}", System.currentTimeMillis());
		List<CoOperateCdkeyQueueBO> cdkeyQueueBOList = coOperateCdkeyDaoMapper.findUnallocatedList();
		logger.info("查询到未分配兑换码数量:{}", cdkeyQueueBOList.size());
		RLock cdkeyLock = null;
		try {
			cdkeyLock = redissonClient.getLock(CacheConstants.C_CORE_COOPERATE_CDKEY_LOCK);
			lock(cdkeyLock);
			redisTemplate.delete(CacheConstants.C_CORE_COOPERATE_CDKEY_QUEUE);
			if (!ObjectUtil.isBlank(cdkeyQueueBOList)) {
				redisTemplate.opsForList().rightPushAll(CacheConstants.C_CORE_COOPERATE_CDKEY_QUEUE, cdkeyQueueBOList);
			}
			logger.info("重新加载兑换码队列结束:{}", System.currentTimeMillis());
		} catch (InterruptedException e) {
			logger.error("获取分布式锁异常", e);
			throw new RuntimeException("获取分布式锁异常", e);
		} finally {
			unlock(cdkeyLock);
		}
	}

}

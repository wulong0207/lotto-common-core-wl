package com.hhly.commoncore.remote.cooperate.service.impl;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.base.util.CoOperateChannelUtil;
import com.hhly.commoncore.persistence.cooperate.dao.CoOperateChannelDaoMapper;
import com.hhly.commoncore.persistence.dic.dao.DicDataDetailDaoMapper;
import com.hhly.commoncore.persistence.order.dao.OrderInfoDaoMapper;
import com.hhly.commoncore.persistence.trans.dao.TransUserDaoMapper;
import com.hhly.commoncore.remote.cooperate.service.ICoOperateProxyService;
import com.hhly.skeleton.base.bo.PagingBO;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.common.CoOperateEnum;
import com.hhly.skeleton.base.common.OrderEnum;
import com.hhly.skeleton.base.common.OrderEnum.AddStatus;
import com.hhly.skeleton.base.common.OrderEnum.OrderStatus;
import com.hhly.skeleton.base.common.OrderEnum.OrderWinningStatus;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.constants.MessageCodeConstants;
import com.hhly.skeleton.base.constants.OrderGroupConstants;
import com.hhly.skeleton.base.exception.ResultJsonException;
import com.hhly.skeleton.base.page.IPageService;
import com.hhly.skeleton.base.page.ISimplePage;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateProxyInfoBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateProxyOrderBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateProxyRechargeBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateProxyOrderVO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateProxyRechargeVO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateProxyVO;
import com.hhly.skeleton.lotto.base.dic.bo.DicDataDetailBO;
import com.hhly.skeleton.lotto.base.dic.vo.DicDataDetailVO;

@Service("coOperateProxyService")
public class CoOperateProxyServiceImpl implements ICoOperateProxyService {

	@Autowired
	private CoOperateChannelUtil coOperateChannelUtil;
	@Autowired
	private IPageService pageService;
	@Autowired
	private TransUserDaoMapper transUserDaoMapper;
	@Autowired
	private OrderInfoDaoMapper orderInfoDaoMapper;
	@Autowired
	private CoOperateChannelDaoMapper coOperateChannelDaoMapper;
	@Autowired
	private DicDataDetailDaoMapper dicDataDetailDaoMapper;

	@Override
	public CoOperateProxyInfoBO findProxyInfo(CoOperateProxyVO vo) {
		judgeToken(vo);
		CoOperateChannelBO channelBO = coOperateChannelDaoMapper.findByChannelId(vo.getQueryChannelId());
		CoOperateProxyInfoBO proxyInfoBO = new CoOperateProxyInfoBO();
		proxyInfoBO.setChannelId(channelBO.getChannelId());
		proxyInfoBO.setChannelName(channelBO.getChannelName());
		proxyInfoBO.setBalance(channelBO.getBalance());
		return proxyInfoBO;
	}

	@Override
	public PagingBO<CoOperateProxyRechargeBO> findRechargeList(CoOperateProxyRechargeVO vo) {
		judgeToken(vo);
		return pageService.getPageData(vo, new ISimplePage<CoOperateProxyRechargeBO>() {

			@Override
			public int getTotal() {
				return transUserDaoMapper.findRechargeTotal(vo);
			}

			@Override
			public List<CoOperateProxyRechargeBO> getData() {
				List<CoOperateProxyRechargeBO> rechargeList = transUserDaoMapper.findRechargeList(vo);
				rechargeList.forEach(recharge -> {
					resetRechargeInfo(recharge);
				});
				return rechargeList;
			}
		});
	}

	@Override
	public PagingBO<CoOperateProxyOrderBO> findOrderList(CoOperateProxyOrderVO vo) {
		judgeToken(vo);
		vo.setCompanyUserId(getCompanyUserId());
		return pageService.getPageData(vo, new ISimplePage<CoOperateProxyOrderBO>() {

			@Override
			public int getTotal() {
				return orderInfoDaoMapper.findOrderTotalByProxy(vo);
			}

			@Override
			public List<CoOperateProxyOrderBO> getData() {
				List<CoOperateProxyOrderBO> orderList = orderInfoDaoMapper.findOrderListByProxy(vo);
				orderList.forEach(order -> {
					resetOrderInfo(order);
				});
				return orderList;
			}
		});
	}

	@Override
	public List<CoOperateProxyRechargeBO> findRechargeListAll(CoOperateProxyRechargeVO vo) {
		judgeToken(vo);
		List<CoOperateProxyRechargeBO> rechargeList = transUserDaoMapper.findRechargeList(vo);
		rechargeList.forEach(recharge -> {
			resetRechargeInfo(recharge);
		});
		return rechargeList;
	}

	@Override
	public List<CoOperateProxyOrderBO> findOrderListAll(CoOperateProxyOrderVO vo) {
		judgeToken(vo);
		vo.setCompanyUserId(getCompanyUserId());
		List<CoOperateProxyOrderBO> orderList = orderInfoDaoMapper.findOrderListByProxy(vo);
		orderList.forEach(order -> {
			resetOrderInfo(order);
		});
		return orderList;
	}

	// 判断代理登录情况
	private void judgeToken(CoOperateProxyVO vo) {
		if (!ObjectUtil.isBlank(vo.getChannelToken())) {
			vo.setQueryChannelId(coOperateChannelUtil.getChannelIdByToken(vo.getChannelToken()));
		}
		if (ObjectUtil.isBlank(vo.getQueryChannelId())) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.TOKEN_LOSE_SERVICE));
		}
	}

	private void resetRechargeInfo(CoOperateProxyRechargeBO recharge) {
		short transStatus = recharge.getTransStatus() == null ? 0 : recharge.getTransStatus().shortValue();
		if (transStatus == Constants.YES) {
			recharge.setStatus(CoOperateEnum.ProxyRechargeStatus.SUCCESS.getValue());
		} else {
			recharge.setStatus(CoOperateEnum.ProxyRechargeStatus.FAIL.getValue());
		}
		// 支付类型
		recharge.setPayType(getPayType(recharge.getCashAmount(), recharge.getRedTransAmount()));
	}

	private void resetOrderInfo(CoOperateProxyOrderBO order) {
		if (OrderEnum.BuyType.BUY_CHASE_PLAN.getValue() == order.getBuyType().shortValue()) {
			// 追号计划
			order.setStatus(getAddOrderStatus(order));
		} else {
			// 代购
			order.setStatus(getOrderStatus(order));
		}
		double preBonus = order.getPreBonus() == null ? 0 : order.getPreBonus();
		if (!CoOperateEnum.ProxyOrderStatus.WINING.getValue().equals(order.getStatus())
				&& !(CoOperateEnum.ProxyOrderStatus.CHASING.getValue().equals(order.getStatus()) && preBonus > 0)) {
			// 已中奖和追号中奖金大于0时显示中奖金额，其它时候不返回中奖金额
			order.setPreBonus(null);
		}
		// 支付类型
		order.setPayType(getPayType(order.getCashAmount(), order.getRedTransAmount()));
	}

	// 合并代购订单状态
	private Short getOrderStatus(CoOperateProxyOrderBO order) {
		short orderStatus = order.getOrderStatus() == null ? 0 : order.getOrderStatus().shortValue();
		short winningStatus = order.getWinningStatus() == null ? 0 : order.getWinningStatus();
		short[] orderStatusArr = {};
		// 订单状态：待上传, 待拆票，拆票中，待出票，出票中，拆票失败 --> 等待出票
		orderStatusArr = new short[] { OrderStatus.WAITING_UPLOAD.getValue(), OrderStatus.WAITING_SPLIT_TICKET.getValue(),
				OrderStatus.SPLITING_TICKET.getValue(), OrderStatus.WAITING_TICKET.getValue(), OrderStatus.TICKETING.getValue(),
				OrderStatus.SPLITING_FAIL.getValue(), };
		if (ArrayUtils.contains(orderStatusArr, orderStatus)) {
			return CoOperateEnum.ProxyOrderStatus.WAIT_TICKET.getValue();
		}
		// 订单状态：出票失败、撤单中、已撤单 --> 出票失败（退款）
		orderStatusArr = new short[] { OrderStatus.FAILING_TICKET.getValue(), OrderStatus.WITHDRAW.getValue(),
				OrderStatus.WITHDRAWING.getValue(), };
		if (ArrayUtils.contains(orderStatusArr, orderStatus)) {
			return CoOperateEnum.ProxyOrderStatus.TICKET_FAIL.getValue();
		}
		if (orderStatus != OrderStatus.TICKETED.getValue()) {
			return null;
		}
		// 订单状态:已出票，中奖状态:未开奖 --> 等待开奖
		if (winningStatus == OrderWinningStatus.NOT_DRAW_WINNING.getValue()) {
			return CoOperateEnum.ProxyOrderStatus.WAIT_WINING.getValue();
		}
		// 订单状态:已出票，中奖状态:已中奖、已派奖 --> 已中奖
		if (winningStatus == OrderWinningStatus.WINNING.getValue() || winningStatus == OrderWinningStatus.GET_WINNING.getValue()) {
			return CoOperateEnum.ProxyOrderStatus.WINING.getValue();
		}
		// 订单状态:已出票，中奖状态:未中奖 --> 未中奖
		if (winningStatus == OrderWinningStatus.NOT_WINNING.getValue()) {
			return CoOperateEnum.ProxyOrderStatus.NOT_WINING.getValue();
		}
		return null;
	}

	// 合并追号计划订单状态
	private Short getAddOrderStatus(CoOperateProxyOrderBO order) {
		short addStatus = order.getAddStatus() == null ? 0 : order.getAddStatus();
		// 追号状态:追号中 --> 追号中
		if (AddStatus.CHASING.getKey().equals(addStatus)) {
			return CoOperateEnum.ProxyOrderStatus.CHASING.getValue();
		}
		// 追号状态:中奖停追 --> 已中奖
		if (AddStatus.CHASE_STOP.getKey().equals(addStatus)) {
			return CoOperateEnum.ProxyOrderStatus.WINING.getValue();
		}
		// 追号状态:追号结束 --> 未中奖
		if (AddStatus.CHASE_FINISH.getKey().equals(addStatus)) {
			return CoOperateEnum.ProxyOrderStatus.NOT_WINING.getValue();
		}
		// 追号状态:用户撤单 --> 用户撤单
		if (AddStatus.USER_CANCEL.getKey().equals(addStatus)) {
			return CoOperateEnum.ProxyOrderStatus.USER_CANCEL.getValue();
		}
		// 追号状态:系统撤单 --> 系统撤单
		if (AddStatus.SYSTEM_UNDO.getKey().equals(addStatus)) {
			return CoOperateEnum.ProxyOrderStatus.SYSTEM_UNDO.getValue();
		}
		return null;
	}

	private Short getPayType(Double cashAmount, Double redTransAmount) {
		boolean isUseCash = cashAmount != null && cashAmount > 0;
		boolean isUseRed = redTransAmount != null && redTransAmount > 0;
		if (isUseCash && isUseRed) {
			return CoOperateEnum.PayType.MIX.getValue();
		} else if (isUseCash) {
			return CoOperateEnum.PayType.CASH.getValue();
		} else if (isUseRed) {
			return CoOperateEnum.PayType.RED.getValue();
		}
		return CoOperateEnum.PayType.CASH.getValue();
	}

	// 查询合买公司保底用户ID，查询订单列表时排除该ID
	private Integer getCompanyUserId() {
		DicDataDetailVO dicVO = new DicDataDetailVO();
		dicVO.setDicCode(OrderGroupConstants.COMPANY_USER_DIC_CODE);
		DicDataDetailBO dicBO = dicDataDetailDaoMapper.findSingle(dicVO);
		return dicBO == null ? null : Integer.parseInt(dicBO.getDicDataValue());
	}

}

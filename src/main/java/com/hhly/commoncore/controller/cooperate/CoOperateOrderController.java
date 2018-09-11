package com.hhly.commoncore.controller.cooperate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.commoncore.remote.cooperate.service.ICoOperateAddOrderService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.exception.Assert;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateOrderInfoBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateOrderInfoVO;

/**
 * 订单
 *
 * @author huangchengfang1219
 * @date 2018年3月31日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RestController
@RequestMapping("/cooperate/order")
public class CoOperateOrderController {

	@Autowired
	private ICoOperateAddOrderService coOperateAddOrderService;

	@RequestMapping(value = "/add/simple", method = RequestMethod.POST)
	public ResultBO<?> addSimpleOrder(@RequestBody CoOperateOrderInfoVO orderInfoVO) {
		Assert.paramNotNull(orderInfoVO);
		Assert.paramNotNull(orderInfoVO.getChannelId(), "channelId");
		Assert.paramNotNull(orderInfoVO.getOutUserId(), "outUserId");
		Assert.paramNotNull(orderInfoVO.getNum(), "num");
		Assert.paramNotNull(orderInfoVO.getLotteryCode(), "lotteryCode");
		Assert.paramNotNull(orderInfoVO.getPlatform(), "platform");
		Assert.paramNotNull(orderInfoVO.getIp(), "ip");
		CoOperateOrderInfoBO orderInfoBO = coOperateAddOrderService.addSimpleOrder(orderInfoVO);
		return ResultBO.ok(orderInfoBO);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResultBO<?> addOrder(@RequestBody CoOperateOrderInfoVO orderInfoVO) {
		Assert.paramNotNull(orderInfoVO);
		Assert.paramNotNull(orderInfoVO.getChannelId(), "channelId");
		Assert.paramNotNull(orderInfoVO.getOutUserId(), "outUserId");
		Assert.paramNotNull(orderInfoVO.getLotteryCode(), "lotteryCode");
		Assert.paramNotNull(orderInfoVO.getOrderDetailList(), "orderDetailList");
		Assert.paramNotNull(orderInfoVO.getPlatform(), "platform");
		Assert.paramNotNull(orderInfoVO.getIp(), "ip");
		CoOperateOrderInfoBO orderInfoBO = coOperateAddOrderService.addOrder(orderInfoVO);
		return ResultBO.ok(orderInfoBO);
	}
}

package com.hhly.commoncore.controller.cooperate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.commoncore.remote.cooperate.service.ICoOperateAgencyService;
import com.hhly.commoncore.remote.cooperate.service.ICoOperateProxyService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateAgencyVO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateProxyOrderVO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateProxyRechargeVO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateProxyVO;

/**
 * 代理中介
 *
 * @author lidecheng
 * @date 2018年4月25日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RestController
@RequestMapping("/cooperate/proxyagency")
public class CoOperateProxyAgencyController {
	@Autowired
	private ICoOperateAgencyService coOperateAgencyService;
	@Autowired
	private ICoOperateProxyService coOperateProxyService;

	/**
	 * 查询渠道的订单列表
	 * 
	 * @param channelId
	 * @return
	 */
	@RequestMapping(value = "/order/list", method = RequestMethod.POST)
	public ResultBO<?> findChannelOrderList(@RequestBody CoOperateProxyOrderVO vo) {
		if (vo.getPageIndex() == null) {
			vo.setPageIndex(Constants.NUM_0);
		}
		if (vo.getPageSize() == null) {
			vo.setPageSize(Constants.NUM_10);
		}
		validAgencyChannel(vo);
		return ResultBO.ok(coOperateProxyService.findOrderList(vo));
	}

	/**
	 * 查询渠道的充值列表
	 * 
	 * @param channelId
	 * @return
	 */
	@RequestMapping(value = "/recharge/list", method = RequestMethod.POST)
	public ResultBO<?> findChannelRechargeList(@RequestBody CoOperateProxyRechargeVO vo) {
		if (vo.getPageIndex() == null) {
			vo.setPageIndex(Constants.NUM_0);
		}
		if (vo.getPageSize() == null) {
			vo.setPageSize(Constants.NUM_10);
		}
		validAgencyChannel(vo);
		return ResultBO.ok(coOperateProxyService.findRechargeList(vo));
	}

	/**
	 * 查询渠道的订单列表
	 * 
	 * @param channelId
	 * @return
	 */
	@RequestMapping(value = "/order/listall", method = RequestMethod.POST)
	public ResultBO<?> findChannelOrderListAll(@RequestBody CoOperateProxyOrderVO vo) {
		validAgencyChannel(vo);
		return ResultBO.ok(coOperateProxyService.findOrderListAll(vo));
	}

	/**
	 * 查询渠道的充值列表
	 * 
	 * @param channelId
	 * @return
	 */
	@RequestMapping(value = "/recharge/listall", method = RequestMethod.POST)
	public ResultBO<?> findChannelRechargeListAll(@RequestBody CoOperateProxyRechargeVO vo) {
		validAgencyChannel(vo);
		return ResultBO.ok(coOperateProxyService.findRechargeListAll(vo));
	}

	private void validAgencyChannel(CoOperateProxyVO vo) {
		CoOperateAgencyVO agencyVO = new CoOperateAgencyVO();
		agencyVO.setChannelToken(vo.getChannelToken());
		agencyVO.setQueryChannelId(vo.getQueryChannelId());
		coOperateAgencyService.getAgencyChannel(agencyVO);
		vo.setChannelToken(null);
	}

}

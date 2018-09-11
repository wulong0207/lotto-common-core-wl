package com.hhly.commoncore.controller.cooperate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.commoncore.remote.cooperate.service.ICoOperateAgencyService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.exception.Assert;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateAgencyVO;

/**
 * 中介
 *
 * @author huangchengfang1219
 * @date 2018年3月28日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RestController
@RequestMapping("/cooperate/agency")
public class CoOperateAgencyController {

	@Autowired
	private ICoOperateAgencyService coOperateAgencyService;

	/**
	 * 查询中介下的渠道列表
	 * 
	 * @param channelId
	 * @return
	 */
	@RequestMapping(value = "info", method = RequestMethod.POST)
	public ResultBO<?> findAgencyChannelList(@RequestBody CoOperateAgencyVO vo) {
		Assert.paramNotNull(vo);
		Assert.paramNotNull(vo.getChannelToken(), "channelToken");
		return ResultBO.ok(coOperateAgencyService.findAgencyInfo(vo));
	}

	/**
	 * 查询渠道的订单列表
	 * 
	 * @param channelId
	 * @return
	 */
	@RequestMapping(value = "channel/orders", method = RequestMethod.POST)
	public ResultBO<?> findChannelOrderList(@RequestBody CoOperateAgencyVO vo) {
		Assert.paramNotNull(vo);
		Assert.paramNotNull(vo.getChannelToken(), "channelToken");
		Assert.paramNotNull(vo.getQueryChannelId(), "queryChannelId");
		if (vo.getPageIndex() == null) {
			vo.setPageIndex(Constants.NUM_0);
		}
		if (vo.getPageSize() == null) {
			vo.setPageSize(Constants.NUM_10);
		}
		return ResultBO.ok(coOperateAgencyService.findOrderList(vo));
	}
}

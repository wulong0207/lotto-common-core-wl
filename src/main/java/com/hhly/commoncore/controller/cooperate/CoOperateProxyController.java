package com.hhly.commoncore.controller.cooperate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.commoncore.remote.cooperate.service.ICoOperateProxyService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateProxyOrderVO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateProxyRechargeVO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateProxyVO;

/**
 * 代理商户
 *
 * @author huangchengfang1219
 * @date 2018年4月24日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RestController
@RequestMapping("/cooperate/proxy")
public class CoOperateProxyController {

	@Autowired
	private ICoOperateProxyService coOperateProxyService;

	@RequestMapping(value = "info", method = RequestMethod.POST)
	public Object findProxyInfo(@RequestBody CoOperateProxyVO vo) {
		return ResultBO.ok(coOperateProxyService.findProxyInfo(vo));
	}

	@RequestMapping(value = "recharge/list", method = RequestMethod.POST)
	public Object findRechargeList(@RequestBody CoOperateProxyRechargeVO vo) {
		if (vo.getPageIndex() == null) {
			vo.setPageIndex(Constants.NUM_0);
		}
		if (vo.getPageSize() == null) {
			vo.setPageSize(Constants.NUM_10);
		}
		return ResultBO.ok(coOperateProxyService.findRechargeList(vo));
	}

	@RequestMapping(value = "order/list", method = RequestMethod.POST)
	public Object findOrderList(@RequestBody CoOperateProxyOrderVO vo) {
		if (vo.getPageIndex() == null) {
			vo.setPageIndex(Constants.NUM_0);
		}
		if (vo.getPageSize() == null) {
			vo.setPageSize(Constants.NUM_10);
		}
		return ResultBO.ok(coOperateProxyService.findOrderList(vo));
	}

	@RequestMapping(value = "recharge/listall", method = RequestMethod.POST)
	public Object findRechargeListAll(@RequestBody CoOperateProxyRechargeVO vo) {
		return ResultBO.ok(coOperateProxyService.findRechargeListAll(vo));
	}

	@RequestMapping(value = "order/listall", method = RequestMethod.POST)
	public Object findOrderListAll(@RequestBody CoOperateProxyOrderVO vo) {
		return ResultBO.ok(coOperateProxyService.findOrderListAll(vo));
	}
}

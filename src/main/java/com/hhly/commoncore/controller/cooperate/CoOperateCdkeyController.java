package com.hhly.commoncore.controller.cooperate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.commoncore.remote.cooperate.service.ICoOperateCdkeyService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.exception.Assert;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateLotteryCdkeyBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateCdkeyExchangeVO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateCdkeyVO;

/**
 * 
 * 积分兑换
 * 
 * @author huangchengfang1219
 * @date 2018年3月9日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RestController
@RequestMapping("/cooperate/cdkey")
public class CoOperateCdkeyController {

	@Autowired
	private ICoOperateCdkeyService coOperateCdkeyService;

	/**
	 * 兑换码详情
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "valid", method = RequestMethod.POST)
	public ResultBO<?> valid(@RequestBody CoOperateCdkeyExchangeVO vo) {
		Assert.paramNotNull(vo.getCdkey(), "cdkey");
		Assert.paramNotNull(vo.getToken(), "token");
		return ResultBO.ok(coOperateCdkeyService.findAndValidCdkey(vo));
	}

	/**
	 * 兑换
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "exchange", method = RequestMethod.POST)
	public ResultBO<?> exchange(@RequestBody CoOperateCdkeyExchangeVO vo) {
		return ResultBO.ok(coOperateCdkeyService.doExchangeCdkey(vo));
	}

	/**
	 * 获取CDKey
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "obtain", method = RequestMethod.POST)
	public ResultBO<?> getCdkey(@RequestBody CoOperateCdkeyVO vo) {
		Assert.paramNotNull(vo);
		Assert.paramNotNull(vo.getChannelId(), "channelId");
		Assert.paramNotNull(vo.getLotteryCode(), "lotteryCode");
		Assert.paramNotNull(vo.getNum(), "num");
		CoOperateLotteryCdkeyBO cdkeyBO = coOperateCdkeyService.doObtainCdkey(vo);
		return ResultBO.ok(cdkeyBO);
	}

	/**
	 * 刷新队列
	 * 
	 * @return
	 */
	@RequestMapping(value = "queue/refresh")
	public ResultBO<?> refreshQueue() {
		coOperateCdkeyService.doRefreshCdkeyQueue();
		return ResultBO.ok();
	}
}

package com.hhly.commoncore.controller.cooperate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.commoncore.remote.cooperate.service.ICoOperateChannelLoginService;
import com.hhly.commoncore.remote.cooperate.service.ICoOperateChannelService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.constants.MessageCodeConstants;
import com.hhly.skeleton.base.exception.Assert;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelAndLogBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelInfoBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelLotteryBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateChannelInfoVO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateRecordQueryVO;

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
@RequestMapping("/cooperate/channel")
public class CoOperateChannelController {

	@Autowired
	private ICoOperateChannelService coOperateChannelService;
	@Autowired
	private ICoOperateChannelLoginService coOperateChannelLoginService;

	@RequestMapping(value = "{channelId}", method = RequestMethod.GET)
	public ResultBO<?> getChannel(@PathVariable("channelId") String channelId) {
		CoOperateChannelBO channelBO = coOperateChannelService.findByChannelId(channelId);
		if (channelBO == null) {
			return ResultBO.err(MessageCodeConstants.DATA_NOT_EXIST);
		}
		return ResultBO.ok(channelBO);
	}

	/**
	 * 登录
	 * 
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ResultBO<?> login(@RequestBody CoOperateChannelInfoVO vo) {
		Assert.paramNotNull(vo);
		Assert.paramNotNull(vo.getChannelName());
		Assert.paramNotNull(vo.getIp(), "ip");
		Assert.paramNotNull(vo.getPassword(), "password");
		Assert.paramNotNull(vo.getPlatform(), "platform");
		return ResultBO.ok(coOperateChannelLoginService.doLogin(vo));
	}

	/**
	 * 退出登录
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "logout", method = RequestMethod.POST)
	public ResultBO<?> logout(@RequestBody CoOperateChannelInfoVO vo) {
		Assert.paramNotNull(vo);
		Assert.paramNotNull(vo.getChannelToken(), "channelToken");
		coOperateChannelLoginService.doLogout(vo.getChannelToken());
		return ResultBO.ok();
	}
	
	/**
	 * 返回登录用户基本信息
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "info/base", method = RequestMethod.POST)
	public ResultBO<?> findBaseInfo(@RequestBody CoOperateChannelInfoVO vo) {
		Assert.paramNotNull(vo);
		Assert.paramNotNull(vo.getChannelToken(), "channelToken");
		return ResultBO.ok(coOperateChannelLoginService.findByToken(vo.getChannelToken()));
	}
	
	/**
	 * 查询渠道商户基本信息
	 * @param channelId
	 */
	@RequestMapping(value = "findChannelAndHis", method = RequestMethod.POST)
	public ResultBO<?> getChannelAndHis(@RequestBody CoOperateChannelInfoBO infoBO){
		CoOperateChannelAndLogBO channelBO =coOperateChannelService.findChannelAndHis(infoBO.getChannelId());
		if (channelBO == null) {
			return ResultBO.err(MessageCodeConstants.DATA_NOT_EXIST);
		}
		channelBO.setLastLoginTime(infoBO.getLastLoginTime());
		channelBO.setLastLoginIp(infoBO.getLastLoginIp());
		channelBO.setMerchantType(channelBO.getMerchantType());
		return ResultBO.ok(channelBO);
	}
	
	
	/**
	 * 修改密码
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "modify/password", method = RequestMethod.POST)
	public ResultBO<?> modifyPassword(@RequestBody CoOperateChannelInfoVO vo){
		Assert.paramNotNull(vo);
		Assert.paramNotNull(vo.getChannelToken(), "channelToken");
		Assert.paramNotNull(vo.getPassword(), "password");
		Assert.paramNotNull(vo.getNewPassword(), "newPassword");
		coOperateChannelLoginService.updatePassword(vo);
		return ResultBO.ok();
	}
	
	/**
	 * 查询余额类渠道订单信息
	 * @param channelId
	 */
	@RequestMapping(value = "findMerchantBalanceInfo", method = RequestMethod.POST)
	public ResultBO<?> findMerchantBalanceInfo(@RequestBody CoOperateRecordQueryVO vo){
		return ResultBO.ok(coOperateChannelService.findMerchantBalanceInfo(vo));
	}
	
	/**
	 * 查询库存类渠道订单信息
	 * @param channelId
	 */
	@RequestMapping(value = "findMerchantNumInfo", method = RequestMethod.POST)
	public ResultBO<?> findMerchantNumInfo(@RequestBody CoOperateRecordQueryVO vo){
		return ResultBO.ok(coOperateChannelService.findMerchantNumInfo(vo));
	}
	
	/**
	 * 查询渠道商户基本信息+彩种信息
	 * @param channelId
	 */
	@RequestMapping(value = "findChannelHisAndLott", method = RequestMethod.POST)
	public ResultBO<?> getChannelHisAndLottery(@RequestBody CoOperateChannelInfoBO infoBO){
		CoOperateChannelAndLogBO channelBO =coOperateChannelService.findChannelAndHis(infoBO.getChannelId());
		if (channelBO == null) {
			return ResultBO.err(MessageCodeConstants.DATA_NOT_EXIST);
		}
		channelBO.setLastLoginTime(infoBO.getLastLoginTime());
		channelBO.setLastLoginIp(infoBO.getLastLoginIp());
		channelBO.setMerchantType(channelBO.getMerchantType());
		List<CoOperateChannelLotteryBO> lotteryList = coOperateChannelService.findChannelLotteryInfo(infoBO.getChannelId());
		channelBO.setLotteryList(lotteryList);
		return ResultBO.ok(channelBO);
	}
	
}

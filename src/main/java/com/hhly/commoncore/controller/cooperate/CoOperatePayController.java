
package com.hhly.commoncore.controller.cooperate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hhly.commoncore.local.cooperate.CoOperatePayService;
import com.hhly.skeleton.base.exception.Assert;

/**
 * @desc    
 * @author  cheng chen
 * @date    2018年4月28日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RestController
@RequestMapping("/cooperate/pay")
public class CoOperatePayController {

	@Autowired
	CoOperatePayService coOperatePayService;
	
	@RequestMapping(value = "recharge", method = RequestMethod.POST)
	public Object recharge(@RequestBody JSONObject json){
		Integer userId = json.getInteger("userId");
		String channelId = json.getString("channelId");
		String transCode = json.getString("transCode");
		Double amount = json.getDouble("amount"); 
		Assert.notNull(userId);
		Assert.notNull(channelId);
		Assert.notNull(transCode);
		Assert.notNull(amount);
		return coOperatePayService.recharge(userId,channelId,amount,transCode);
	}
}

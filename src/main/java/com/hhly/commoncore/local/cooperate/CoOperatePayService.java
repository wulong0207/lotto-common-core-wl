
package com.hhly.commoncore.local.cooperate;

import com.hhly.skeleton.base.bo.ResultBO;

/**
 * @desc    
 * @author  cheng chen
 * @date    2018年4月28日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface CoOperatePayService {

	ResultBO<?> recharge(Integer userId, String channelId, Double amount, String transCode);
}

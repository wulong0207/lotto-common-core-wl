package com.hhly.commoncore.persistence.cooperate.dao;

import org.apache.ibatis.annotations.Param;

import com.hhly.commoncore.persistence.cooperate.po.CoOperateLotteryPO;

public interface CoOperateLotteryDaoMapper {

	/**
	 * 查询渠道彩种配置信息
	 * 
	 * @param lotteryCode
	 * @return
	 */
	CoOperateLotteryPO findByChannel(@Param("lotteryCode") Integer lotteryCode, @Param("channelId") String channelId);
	
}

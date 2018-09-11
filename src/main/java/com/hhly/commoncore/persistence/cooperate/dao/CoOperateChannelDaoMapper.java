package com.hhly.commoncore.persistence.cooperate.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.commoncore.persistence.cooperate.po.CoOperateChannelPO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelAndLogBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelBO;

public interface CoOperateChannelDaoMapper {

	/**
	 * 根据ID查找渠道信息
	 * 
	 * @param id
	 * @return
	 */
	CoOperateChannelBO findByChannelId(@Param("channelId") String channelId);
	
	/**
	 * 查询渠道余额
	 * @param id
	 * @return
	 */
	CoOperateChannelBO findBalanceById(@Param("id") Integer id);

	/**
	 * 更新
	 * 
	 * @param channelPO
	 */
	int updateBalance(@Param("id") Integer id, @Param("exchangeAmount") Double exchangeAmount);
	
	/**
	 * 查询渠道信息和历史登录信息
	 * @param channelId
	 */
	CoOperateChannelAndLogBO findChannelAndHis(@Param("channelId") String channelId);
	
	/**
	 * 根据渠道名称查找渠道信息
	 * @param channelName
	 * @return
	 */
	CoOperateChannelBO findByChannelName(@Param("channelName") String channelName);
	
	/**
	 * 修改密码
	 * @param channelPO
	 */
	void updatePassword(CoOperateChannelPO channelPO);
	
	/**
	 * 查询中介商户代理的渠道列表
	 * @param channelId
	 * @return
	 */
	List<CoOperateChannelBO> findAgencyChannelList(@Param("channelId") String channelId);
}

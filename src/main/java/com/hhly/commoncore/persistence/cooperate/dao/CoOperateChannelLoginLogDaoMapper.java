package com.hhly.commoncore.persistence.cooperate.dao;

import com.hhly.commoncore.persistence.cooperate.po.CoOperateChannelLoginLogPO;

/**
 * 渠道登录日志
 *
 * @author huangchengfang1219
 * @date 2018年3月24日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface CoOperateChannelLoginLogDaoMapper {

	/**
	 * 查询最后一次登录日志
	 * 
	 * @param channelId
	 * @return
	 */
	CoOperateChannelLoginLogPO findLastByChannelId(String channelId);

	/**
	 * 插入登录日志
	 * 
	 * @param po
	 */
	void insert(CoOperateChannelLoginLogPO po);
}

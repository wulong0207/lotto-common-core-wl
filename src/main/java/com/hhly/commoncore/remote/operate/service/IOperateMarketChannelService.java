package com.hhly.commoncore.remote.operate.service;

import com.hhly.skeleton.lotto.base.operate.bo.OperateMarketChannelBO;

public interface IOperateMarketChannelService {

	/**
	 * 查询渠道信息
	 * 
	 * @param channelId
	 * @return
	 */
	OperateMarketChannelBO findByChannelId(String channelId);
}

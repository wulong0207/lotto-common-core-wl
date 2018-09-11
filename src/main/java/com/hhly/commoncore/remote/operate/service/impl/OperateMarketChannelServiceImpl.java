package com.hhly.commoncore.remote.operate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.persistence.operate.dao.OperateMarketChannelDaoMapper;
import com.hhly.commoncore.remote.operate.service.IOperateMarketChannelService;
import com.hhly.skeleton.lotto.base.operate.bo.OperateMarketChannelBO;

@Service("operateMarketChannelService")
public class OperateMarketChannelServiceImpl implements IOperateMarketChannelService {

	@Autowired
	private OperateMarketChannelDaoMapper operateMarketChannelDaoMapper;

	@Override
	public OperateMarketChannelBO findByChannelId(String channelId) {
		return operateMarketChannelDaoMapper.findByChannelId(channelId);
	}

}

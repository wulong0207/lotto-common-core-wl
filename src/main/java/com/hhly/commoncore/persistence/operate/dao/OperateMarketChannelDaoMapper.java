package com.hhly.commoncore.persistence.operate.dao;

import com.hhly.skeleton.lotto.base.operate.bo.OperateLottSoftwareVersionBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateMarketChannelBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateLottSoftwareVersionVO;

public interface OperateMarketChannelDaoMapper {

	/**
	 * 根据渠道查询appurl
	 * @param vo
	 * @return
	 */
	OperateLottSoftwareVersionBO findVersionByChannel(OperateLottSoftwareVersionVO vo);
	
	/**
	 * 查询渠道信息
	 * @param channelId
	 * @return
	 */
	OperateMarketChannelBO findByChannelId(String channelId);
}
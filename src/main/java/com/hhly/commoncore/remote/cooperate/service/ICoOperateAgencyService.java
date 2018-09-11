package com.hhly.commoncore.remote.cooperate.service;

import java.util.List;

import com.hhly.skeleton.base.bo.PagingBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateAgencyInfoBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelInfoBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelOrderBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateAgencyVO;

/**
 * 渠道中介
 *
 * @author huangchengfang1219
 * @date 2018年3月28日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface ICoOperateAgencyService {

	/**
	 * 查询中介账号信息
	 * 
	 * @param vo
	 * @return
	 */
	CoOperateAgencyInfoBO findAgencyInfo(CoOperateAgencyVO vo);

	/**
	 * 查询可见渠道列表
	 * 
	 * @param vo
	 * @return
	 */
	List<CoOperateChannelInfoBO> findAgencyChannelList(CoOperateAgencyVO vo);

	/**
	 * 查询订单列表
	 * 
	 * @param vo
	 * @return
	 */
	PagingBO<CoOperateChannelOrderBO> findOrderList(CoOperateAgencyVO vo);
	
	/**
	 * 校验并获取中介下的渠道
	 * 
	 * @param vo
	 */
	CoOperateChannelInfoBO getAgencyChannel(CoOperateAgencyVO vo);
}

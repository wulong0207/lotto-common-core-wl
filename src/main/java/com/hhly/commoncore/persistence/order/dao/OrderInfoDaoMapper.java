package com.hhly.commoncore.persistence.order.dao;

import java.util.List;

import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateProxyOrderBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateProxyOrderVO;
import com.hhly.skeleton.lotto.base.order.bo.UserWinInfoBO;

/**
 * @author huangb
 *
 * @Date 2016年11月30日
 *
 * @Desc 订单处理数据接口
 */
public interface OrderInfoDaoMapper {

	/**
	 * 用户中奖轮播信息
	 * 
	 * @return
	 */
	List<UserWinInfoBO> queryUserWinInfo();
	
	/**
	 * 查询代理商户订单信息
	 * 
	 * @param vo
	 * @return
	 */
	int findOrderTotalByProxy(CoOperateProxyOrderVO vo);

	/**
	 * 查询代理商户订单信息
	 * 
	 * @param vo
	 * @return
	 */
	List<CoOperateProxyOrderBO> findOrderListByProxy(CoOperateProxyOrderVO vo);
}
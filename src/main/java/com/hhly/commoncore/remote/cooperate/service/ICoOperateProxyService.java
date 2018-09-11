package com.hhly.commoncore.remote.cooperate.service;

import java.util.List;

import com.hhly.skeleton.base.bo.PagingBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateProxyInfoBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateProxyOrderBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateProxyRechargeBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateProxyOrderVO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateProxyRechargeVO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateProxyVO;

/**
 * 代理商户接口
 *
 * @author huangchengfang1219
 * @date 2018年4月24日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface ICoOperateProxyService {

	/**
	 * 渠道信息
	 * 
	 * @param vo
	 * @return
	 */
	CoOperateProxyInfoBO findProxyInfo(CoOperateProxyVO vo);

	/**
	 * 查询代理商户渠道用户的充值记录
	 * 
	 * @param vo
	 * @return
	 */
	PagingBO<CoOperateProxyRechargeBO> findRechargeList(CoOperateProxyRechargeVO vo);

	/**
	 * 查询代理商户渠道用户的订单列表
	 * 
	 * @param vo
	 * @return
	 */
	PagingBO<CoOperateProxyOrderBO> findOrderList(CoOperateProxyOrderVO vo);

	/**
	 * 查询代理商户渠道用户的充值记录, 用于导出，不分页
	 * 
	 * @param vo
	 * @return
	 */
	List<CoOperateProxyRechargeBO> findRechargeListAll(CoOperateProxyRechargeVO vo);

	/**
	 * 查询代理商户渠道用户的订单列表, 用于导出，不分页
	 * 
	 * @param vo
	 * @return
	 */
	List<CoOperateProxyOrderBO> findOrderListAll(CoOperateProxyOrderVO vo);
}

package com.hhly.commoncore.persistence.trans.dao;

import java.util.List;

import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateProxyRechargeBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateProxyRechargeVO;

public interface TransUserDaoMapper {

	/**
	 * 从用户流水表查找充值记录总数
	 * 
	 * @param vo
	 * @return
	 */
	int findRechargeTotal(CoOperateProxyRechargeVO vo);

	/**
	 * 从用户流水表查找充值记录
	 * 
	 * @param vo
	 * @return
	 */
	List<CoOperateProxyRechargeBO> findRechargeList(CoOperateProxyRechargeVO vo);
}

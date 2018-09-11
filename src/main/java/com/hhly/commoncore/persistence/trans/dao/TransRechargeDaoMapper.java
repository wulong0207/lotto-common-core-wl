package com.hhly.commoncore.persistence.trans.dao;

import java.util.List;

import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateProxyRechargeBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateProxyRechargeVO;

public interface TransRechargeDaoMapper {

	/**
	 * 查找充值记录总数
	 * 
	 * @param vo
	 * @return
	 */
	int findTotal(CoOperateProxyRechargeVO vo);

	/**
	 * 查找充值记录
	 * 
	 * @param vo
	 * @return
	 */
	List<CoOperateProxyRechargeBO> findList(CoOperateProxyRechargeVO vo);
}

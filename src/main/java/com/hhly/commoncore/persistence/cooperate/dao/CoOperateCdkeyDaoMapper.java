package com.hhly.commoncore.persistence.cooperate.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.commoncore.persistence.cooperate.po.CoOperateCdkeyPO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateCdkeyCountBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateCdkeyQueueBO;

public interface CoOperateCdkeyDaoMapper {

	/**
	 * 批量更新本站兑换码
	 * 
	 * @param cdkeyList
	 * @return
	 */
	int updateMyCdkey(@Param("cdkeyPO") CoOperateCdkeyPO cdkeyPO, @Param("cdkeyList") List<CoOperateCdkeyQueueBO> cdkeyList);

	/**
	 * 查询本站兑换码，保证本站兑换码不重复
	 * 
	 * @param mycdkey
	 * @return
	 */
	int findCountByMyCdkey(@Param("myCdkey") String myCdkey);

	/**
	 * 查询指定数量的未分配兑换码
	 * 
	 * @param cdkeyVO
	 * @return
	 */
	List<CoOperateCdkeyQueueBO> findUnallocatedList();

	/**
	 * 查询本站兑换码数量
	 * 
	 * @param myCdkey
	 * @return
	 */
	CoOperateCdkeyCountBO findCountForExchange(@Param("myCdkey") String myCdkey);

	/**
	 * 修改为已兑换
	 * 
	 * @param myCdkey
	 */
	void updateExchanged(@Param("myCdkey") String myCdkey);

}

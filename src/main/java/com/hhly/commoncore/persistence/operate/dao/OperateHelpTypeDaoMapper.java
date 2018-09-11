package com.hhly.commoncore.persistence.operate.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.skeleton.lotto.base.operate.bo.OperateHelpLotteryTypeBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateHelpTypeBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateHelpTypeVO;

public interface OperateHelpTypeDaoMapper {

	/**
	 * 查找栏目
	 * 
	 * @return
	 */
	List<OperateHelpTypeBO> findList(OperateHelpTypeVO vo);

	/**
	 * 查询一个栏目
	 * 
	 * @param code
	 * @return
	 */
	OperateHelpTypeBO findByCode(@Param("code") String code);

	/**
	 * 根据编码查找父栏目
	 * 
	 * @param id
	 * @return
	 */
	OperateHelpTypeBO findParentByCode(@Param("code") String code);

	/**
	 * 查询彩种介绍列表
	 * 
	 * @return
	 */
	List<OperateHelpLotteryTypeBO> findLotteryList();
}

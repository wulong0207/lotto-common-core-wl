package com.hhly.commoncore.persistence.dic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.skeleton.lotto.base.dic.bo.DicDataDetailBO;
import com.hhly.skeleton.lotto.base.dic.vo.DicDataDetailVO;


public interface DicDataDetailDaoMapper {

	List<DicDataDetailBO> findSimple(@Param("code")String code);
    
	/**
	 * @param dicDataDetailVO
	 *            参数对象
	 * @return 单个查询对象
	 * @desc 查询唯一的数据记录
	 */
	DicDataDetailBO findSingle(DicDataDetailVO dicDataDetailVO);


}
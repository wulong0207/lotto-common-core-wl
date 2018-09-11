package com.hhly.commoncore.persistence.operate.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.skeleton.lotto.base.operate.bo.OperateMsgBO;

public interface OperateMsgInfoDaoMapper {
	

	
	/**查询短信通知信息*/
	List<OperateMsgBO> findHomeMsg(@Param("userId") Integer userId );

}
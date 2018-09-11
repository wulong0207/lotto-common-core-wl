package com.hhly.commoncore.persistence.operate.dao;

import com.hhly.commoncore.persistence.operate.po.OperateHelpCorrectPO;

/**
 * @desc
 * @author cheng chen
 * @date 2017年4月21日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface OperateHelpCorrectDaoMapper {

	/**
	 * 插入反馈
	 * 
	 * @param po
	 * @return
	 */
	int insert(OperateHelpCorrectPO po);
}
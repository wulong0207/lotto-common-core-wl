package com.hhly.commoncore.persistence.operate.dao;

import java.util.List;

import com.hhly.skeleton.lotto.base.operate.bo.OperateFastBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateFastVO;

/**
 * @desc    快投模块管理详情
 * @author  lidecheng
 * @date    2017年2月23日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface OperateFastInfoDaoMapper {

	/**
	 * 查询快投信息
	 * @param operateFastVO
	 * @return
	 */
	List<OperateFastBO> findOperFastInfo(OperateFastVO operateFastVO);
}

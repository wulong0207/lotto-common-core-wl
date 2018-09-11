package com.hhly.commoncore.remote.operate.service;

import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateFastBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateFastVO;

/**
 * @desc 快投运营管理服务
 * @author lidecheng
 * @date 2017年3月8日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface IOperateFastService {	
	/**
	 * 查询快投信息+上一期开奖信息
	 * @param operateFastVO
	 * @return
	 */
	ResultBO<OperateFastBO> findOperFastIssueDetail(OperateFastVO operateFastVO);
}

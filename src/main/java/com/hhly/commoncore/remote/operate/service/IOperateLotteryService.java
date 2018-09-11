package com.hhly.commoncore.remote.operate.service;

import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateLotteryBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateLotteryLottVO;

/**
 * @desc 彩种运营管理服务
 * @author lidecheng
 * @date 2017年3月8日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface IOperateLotteryService {	

	/**
	 * 获取PC主页彩种运营管理信息
	 * @return
	 */
	ResultBO<OperateLotteryBO> findHomeOperLottery(OperateLotteryLottVO vo);
}

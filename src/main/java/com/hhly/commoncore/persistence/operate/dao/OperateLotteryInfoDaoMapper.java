package com.hhly.commoncore.persistence.operate.dao;

import java.util.List;

import com.hhly.skeleton.cms.operatemgr.bo.OperateLotteryInfoBO;
import com.hhly.skeleton.cms.operatemgr.vo.OperateLotteryInfoVO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateLotteryBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateLotteryLottVO;

/**
 * @desc    彩种运营详情
 * @author  Tony Wang
 * @date    2017年2月21日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface OperateLotteryInfoDaoMapper {

	/**
	 * 获取主页彩种运营管理信息
	 * @return
	 */
	List<OperateLotteryBO> findPCHomeOperLottery(OperateLotteryLottVO vo);
}

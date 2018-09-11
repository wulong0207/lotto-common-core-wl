package com.hhly.commoncore.local.lotto.service;

import java.util.Date;
import java.util.List;

import com.hhly.skeleton.lotto.base.lottery.bo.LotteryBO;
import com.hhly.skeleton.lotto.base.lottery.vo.LotteryVO;

/**
 * @author Administrator on
 * @version 1.0
 * @desc 彩种service
 * @date 2017/4/28.
 * @company 益彩网络科技有限公司
 */
public interface LotteryService {

	/**
	 * 查询高频彩当天最大截止销售时间
	 * 
	 * @param lotteryCode
	 * @return
	 */
	Date findMaxEndDrawTime(int lotteryCode);

	/**
	 * 通过开奖彩种类型查询彩种集合
	 * 
	 * @param lotteryVO
	 * @return
	 * @date 2017年9月23日上午11:27:42
	 * @author cheng.chen
	 */
	List<LotteryBO> queryLotterySelectList(LotteryVO vo);

}

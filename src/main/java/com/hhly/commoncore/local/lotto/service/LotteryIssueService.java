package com.hhly.commoncore.local.lotto.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.skeleton.lotto.base.issue.bo.CurrentAndPreIssueBO;
import com.hhly.skeleton.lotto.base.lottery.bo.LotteryIssueBaseBO;
import com.hhly.skeleton.lotto.base.lottery.vo.LotteryVO;

public interface LotteryIssueService {

	/**
	 * 查询当前期和上一期信息
	 * 
	 * @return
	 */
	CurrentAndPreIssueBO findIssueAndPreIssueByCode(@Param("lotteryCode") int lotteryCode);

	/**
	 * 根据彩种编码查询所有彩期
	 * 
	 * @param lotteryCode
	 * @return
	 * @date 2017年9月23日上午11:24:02
	 * @author cheng.chen
	 */
	List<String> queryIssueByLottery(LotteryVO lotteryVO);

	/**
	 * 查询彩种首页信息
	 * 
	 * @param lotteryCode
	 * @return
	 */
	LotteryIssueBaseBO findLotteryIssueBase(Integer lotteryCode);
}

package com.hhly.commoncore.persistence.issue.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.skeleton.lotto.base.issue.bo.CurrentAndPreIssueBO;
import com.hhly.skeleton.lotto.base.issue.bo.IssueBO;
import com.hhly.skeleton.lotto.base.issue.bo.IssueDrawBO;
import com.hhly.skeleton.lotto.base.lottery.vo.LotteryVO;

/**
 * @desc 彩期
 * @author jiangwei
 * @date 2017-2-16
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface LotteryIssueDaoMapper {

	/**
	 * 查询当前期和上一期信息
	 * 
	 * @return
	 */
	CurrentAndPreIssueBO findIssueAndPreIssueByCode(@Param("lotteryCode") int lotteryCode);

	/**
	 * 查询所有当前期和上一期信息列表
	 * 
	 * @return
	 */
	List<CurrentAndPreIssueBO> findAllIssueAndPreIssue();

	/**
	 * 彩种信息
	 * 
	 * @param lotteryCode
	 * @return
	 */
	IssueBO findSingleFront(LotteryVO lotteryVO);

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
	 * 查询最新开奖彩期
	 * 
	 * @param lotteryVO
	 * @return
	 */
	IssueDrawBO findLatestDrawIssue(LotteryVO lotteryVO);
}
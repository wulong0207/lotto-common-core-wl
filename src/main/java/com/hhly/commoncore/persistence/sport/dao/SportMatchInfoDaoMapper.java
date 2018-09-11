package com.hhly.commoncore.persistence.sport.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.skeleton.lotto.base.sport.bo.MatchDataBO;

public interface SportMatchInfoDaoMapper {

	/**
	 * 查找赛事名称列表
	 * 
	 * @param lotteryCode
	 * @return
	 */
	List<MatchDataBO> findRaceList(@Param("lotteryCode") Integer lotteryCode);
}

package com.hhly.commoncore.remote.sport.service;

import java.util.List;

import com.hhly.skeleton.lotto.base.sport.bo.MatchDataBO;

/**
 * 竞技彩赛事查询
 *
 * @author huangchengfang1219
 * @date 2017年11月10日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface ISportMatchService {

	/**
	 * 根据彩种查询赛事名称列表
	 * 
	 * @param lotteryCode
	 * @return
	 */
	List<MatchDataBO> findRaceList(Integer lotteryCode);
	
}

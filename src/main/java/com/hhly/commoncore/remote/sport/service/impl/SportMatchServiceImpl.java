package com.hhly.commoncore.remote.sport.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.persistence.sport.dao.SportMatchInfoDaoMapper;
import com.hhly.commoncore.remote.sport.service.ISportMatchService;
import com.hhly.skeleton.lotto.base.sport.bo.MatchDataBO;
import com.hhly.skeleton.lotto.base.sport.bo.SPMessageBO;

@Service("sportMatchService")
public class SportMatchServiceImpl implements ISportMatchService {

	@Autowired
	private SportMatchInfoDaoMapper sportMatchInfoDaoMapper;

	@Override
	public List<MatchDataBO> findRaceList(Integer lotteryCode) {
		if (lotteryCode == null) {
			return null;
		}
		return sportMatchInfoDaoMapper.findRaceList(lotteryCode);
	}

}

package com.hhly.commoncore.local.sport.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.local.sport.service.ISportDataService;
import com.hhly.commoncore.persistence.sport.dao.SportDataFbSpDaoMapper;
import com.hhly.skeleton.base.common.LotteryEnum;
import com.hhly.skeleton.lotto.base.sport.bo.SportNewestDataFbBO;
import com.hhly.skeleton.lotto.base.sport.vo.SportNewestDataVO;

@Service
public class SportDataServiceImpl implements ISportDataService{
	
	@Autowired
	private SportDataFbSpDaoMapper sportDataFbSpDaoMapper;

	@Override
	public List<SportNewestDataFbBO> findFbNewestData(SportNewestDataVO vo) {
		vo.setLotteryCode(LotteryEnum.Lottery.FB.getName());
		return sportDataFbSpDaoMapper.findFbNewestData(vo);
	}

}

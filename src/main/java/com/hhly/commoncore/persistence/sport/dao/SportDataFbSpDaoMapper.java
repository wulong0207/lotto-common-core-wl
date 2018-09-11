package com.hhly.commoncore.persistence.sport.dao;

import java.util.List;

import com.hhly.skeleton.lotto.base.sport.bo.SportNewestDataFbBO;
import com.hhly.skeleton.lotto.base.sport.vo.SportNewestDataVO;

public interface SportDataFbSpDaoMapper {

	/**
	 * 查询竞彩足球最新数据
	 * 
	 * @param vo
	 * @return
	 */
	List<SportNewestDataFbBO> findFbNewestData(SportNewestDataVO vo);
}

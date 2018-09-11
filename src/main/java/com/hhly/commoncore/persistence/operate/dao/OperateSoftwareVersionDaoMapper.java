package com.hhly.commoncore.persistence.operate.dao;

import com.hhly.skeleton.lotto.base.operate.bo.OperateLottSoftwareVersionBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateLottSoftwareVersionVO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateSoftwareVersionVO;

public interface OperateSoftwareVersionDaoMapper {
	
	/**
	 * 根据客户端信息查询最新的版本号
	 * @param type
	 * @return
	 */
	OperateLottSoftwareVersionBO findNewVersion(OperateLottSoftwareVersionVO vo);

	OperateLottSoftwareVersionBO findSingle(OperateSoftwareVersionVO vo);
}
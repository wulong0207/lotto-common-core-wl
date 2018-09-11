package com.hhly.commoncore.persistence.operate.dao;

import com.hhly.commoncore.persistence.operate.po.OperateHelpAbleLogPO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateHelpAbleBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateHelpAbleVO;

/**
 * 帮助中心用户满意度记录
 *
 * @author huangchengfang1219
 * @date 2018年2月23日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface OperateHelpAbleLogDaoMapper {

	/**
	 * 查询用户对文章的满意记录
	 * 
	 * @param vo
	 * @return
	 */
	OperateHelpAbleBO findSingle(OperateHelpAbleVO vo);

	/**
	 * 插入用户满意记录
	 * 
	 * @param po
	 */
	void insert(OperateHelpAbleLogPO po);
}

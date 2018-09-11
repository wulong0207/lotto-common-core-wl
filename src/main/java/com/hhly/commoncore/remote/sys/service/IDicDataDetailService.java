package com.hhly.commoncore.remote.sys.service;

import java.util.List;

import com.hhly.skeleton.lotto.base.dic.bo.DicDataDetailBO;
import com.hhly.skeleton.lotto.base.dic.vo.DicDataDetailVO;

/**
 * @author huangb
 *
 * @date 2016年11月15日
 *
 * @desc 数据字典详情的服务接口
 */
public interface IDicDataDetailService {
	
	/**
	 * @param dicDataDetailVO
	 *            参数对象
	 * @return 单个查询对象
	 * @throws Exception
	 * @desc 查询唯一的数据记录
	 */
	DicDataDetailBO findSingle(DicDataDetailVO dicDataDetailVO);
	
	/**
	 * 通过编码查询字典集合
	 * @return
	 * @date 2018年1月12日下午4:32:48
	 * @author cheng.chen
	 */
	List<DicDataDetailBO> findSimple(String code);
}

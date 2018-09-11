package com.hhly.commoncore.remote.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.persistence.dic.dao.DicDataDetailDaoMapper;
import com.hhly.commoncore.remote.sys.service.IDicDataDetailService;
import com.hhly.skeleton.lotto.base.dic.bo.DicDataDetailBO;
import com.hhly.skeleton.lotto.base.dic.vo.DicDataDetailVO;

/**
 * @author huangb
 *
 * @date 2016年11月15日
 *
 * @desc 数据字典详情的服务
 */
@Service("dicDataDetailService")
public class DicDataDetailServiceImpl implements IDicDataDetailService {

	@Autowired
	private DicDataDetailDaoMapper dicDataDetailDaoMapper;

	@Override
	public DicDataDetailBO findSingle(DicDataDetailVO dicDataDetailVO){
		return dicDataDetailDaoMapper.findSingle(dicDataDetailVO);
	}

	@Override
	public List<DicDataDetailBO> findSimple(String code) {
		return dicDataDetailDaoMapper.findSimple(code);
	}
}

package com.hhly.commoncore.remote.operate.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.persistence.operate.dao.OperateMsgInfoDaoMapper;
import com.hhly.commoncore.remote.operate.service.IOperateMsgService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateMsgBO;


/**
 * @desc 通知管理服务实现类
 * @author lidecheng
 * @date 2017年3月8日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service("operateMsgService")
public class OperateMsgServiceImpl  implements IOperateMsgService{	
	@Autowired
	private OperateMsgInfoDaoMapper operateMsgInfoDaoMapper;


	
	/**
	 * 获取PC主页通知信息
	 * @return
	 */
	public ResultBO<List<OperateMsgBO>> findOperMsgList(Integer userId){		
		return new ResultBO<List<OperateMsgBO>>(operateMsgInfoDaoMapper.findHomeMsg(userId));
	}
}
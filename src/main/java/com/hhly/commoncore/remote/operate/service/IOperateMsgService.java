package com.hhly.commoncore.remote.operate.service;

import java.util.List;

import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateMsgBO;

/**
 * @desc 通知信息管理服务
 * @author lidecheng
 * @date 2017年3月8日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface IOperateMsgService {	

	/**
	 * 获取PC主页信息通知
	 * @return
	 */
	ResultBO<List<OperateMsgBO>> findOperMsgList(Integer userId);
}

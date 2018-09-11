package com.hhly.commoncore.remote.operate.service;
import java.util.List;

import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateLottSoftwareVersionBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateLottSoftwareVersionVO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateSoftwareVersionVO;

/**
 * @desc 运营管理服务
 * @author lidecheng
 * @date 2017年3月8日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface IOperateService {	

	/**
	 * 查询版本信息
	 * @param vo
	 * @return
	 */
	ResultBO<OperateLottSoftwareVersionBO> findNewVersion(OperateLottSoftwareVersionVO vo);
	
	/**
	 * @desc   查询operate_software_version表单条记录
	 * @author Tony Wang
	 * @create 2017年9月12日
	 * @param vo
	 * @return 
	 */
	OperateLottSoftwareVersionBO findSoftwareVersionSingle(OperateSoftwareVersionVO vo);


	/**
	 * 查询渠道版本信息
	 * @param vo
	 * @return
	 */
	ResultBO<OperateLottSoftwareVersionBO> findVersionByChannelId(OperateLottSoftwareVersionVO vo);
	
	/**
	 * 首页中奖轮播信息
	 * @return
	 */
	ResultBO<List<String>> findUserWinInfo();
	/**
	 * 查询系统开关
	 * @return
	 */
	ResultBO<Integer> findOpenStatus();
}

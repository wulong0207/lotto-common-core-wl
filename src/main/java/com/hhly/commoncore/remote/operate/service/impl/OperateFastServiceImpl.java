package com.hhly.commoncore.remote.operate.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.hhly.commoncore.persistence.operate.dao.OperateFastInfoDaoMapper;
import com.hhly.commoncore.remote.cache.service.ILotteryIssueCacheService;
import com.hhly.commoncore.remote.operate.service.IOperateFastService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.constants.MessageCodeConstants;
import com.hhly.skeleton.base.exception.ResultJsonException;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.lotto.base.issue.bo.CurrentAndPreIssueBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateFastBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateFastLottBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateFastVO;

/**
 * @desc 运营管理服务实现类
 * @author lidecheng
 * @date 2017年3月8日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service("operateFastService")
public class OperateFastServiceImpl  implements IOperateFastService{
	
	@Autowired
	private OperateFastInfoDaoMapper operateFastInfoDaoMapper;
	
	@Autowired
	private ILotteryIssueCacheService lotteryIssueCacheService;	
	
	@Value("${before_file_url}")
	protected String beforeFileUrl;
	

	/**
	 * 获取主页快投信息+上一期开奖信息
	 */
	@Override
	public ResultBO<OperateFastBO> findOperFastIssueDetail(OperateFastVO operateFastVO) {
		OperateFastBO  operfastBO = findOperFastInfo(operateFastVO);
		if(operfastBO==null){
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.DATA_NOT_FOUND_SYS));
		}
		List<OperateFastLottBO>  fastList = operfastBO.getFastInfoList();
		setFastLottery(fastList);//设置快投彩种信息
		for(OperateFastLottBO fastBO : fastList){//设置快投各种玩法	
			fastBO.setLotteryLogoUrl(beforeFileUrl+fastBO.getLotteryLogoUrl());
		}
		return new ResultBO<OperateFastBO>(operfastBO);
	}
	
	/***
	 * 获取快投信息
	 * @param operateFastVO
	 * @return
	 */
	OperateFastBO findOperFastInfo(OperateFastVO operateFastVO){
		operateFastVO.setIsDefault(Constants.NO);//查询非默认彩种
		List<OperateFastBO>   list= operateFastInfoDaoMapper.findOperFastInfo(operateFastVO);
		OperateFastBO  fastBO = getFastInfoByNowDate(list);
		if(fastBO==null){
			operateFastVO.setIsDefault(Constants.YES);//查询默认彩种
			list = operateFastInfoDaoMapper.findOperFastInfo(operateFastVO);
			fastBO = getFastInfoByNowDate(list);
		}	
		return fastBO;
	}
	/**
	 * 根据当前时间获取需要的快投信息
	 * @param list
	 * @return
	 */
	OperateFastBO getFastInfoByNowDate(List<OperateFastBO>  list){
		OperateFastBO fastBO = null;		
		String nowstr = DateUtil.dayForWeek()+DateUtil.getNow("HH:mm:ss");
		for(OperateFastBO bo : list){
			if(GetOnlineWeekCheck(bo.getOnlineWeek(),bo.getOnlineTime(),bo.getOfflineWeek(),bo.getOfflineTime(),nowstr)){
				fastBO  = bo;
				break;
			}
		}
		return fastBO;
	}
	/**
	 * 判断当前时间是否在时间区间之内
	 * @param onlineWeek：上线周期
	 * @param onlineTime：上线时间
	 * @param offlineWeek：下线周期
	 * @param offlineTime：下线时间
	 * @param nowstr
	 * @return
	 */
	boolean GetOnlineWeekCheck(Short onlineWeek,String onlineTime,Short offlineWeek,String offlineTime,String nowstr){
		String upstr =onlineWeek+onlineTime;
		String downstr=offlineWeek+offlineTime;
		if(onlineWeek<=offlineWeek){
			if(nowstr.compareTo(upstr)>0 && nowstr.compareTo(downstr)<=0){
				return true;				
			}								
		}else{
			//下线时间小于上线时间
			String endstr = 7+"23:59:59";//星期天结束时间				
			String startstr = 1+"00:00:00";//星期1开始时间
			if((nowstr.compareTo(upstr)>0 && nowstr.compareTo(endstr)<=0)||
					(nowstr.compareTo(startstr)>0 && nowstr.compareTo(downstr)<=0)){
				return true;
			}
		}
		return false;
	}
	
	
	
		
	
	/**
	 * 设置快投数据的彩种信息
	 * @param fastList
	 */
	@SuppressWarnings("unchecked")
	void setFastLottery(List<OperateFastLottBO>  fastList){
		List<CurrentAndPreIssueBO>  lotteryIssuelist = (List<CurrentAndPreIssueBO>)lotteryIssueCacheService.getAllCurrentAndPreIssue().getData();
		for(OperateFastLottBO fastBO : fastList){
			for(CurrentAndPreIssueBO fastLottBO: lotteryIssuelist){
				if(fastBO.getTypeId().intValue()==fastLottBO.getLotteryCode().intValue()){
					fastBO.setPreAndIssueLott(fastLottBO);
				}
			}
		}	
	}
}
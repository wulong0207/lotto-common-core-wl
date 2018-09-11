package com.hhly.commoncore.remote.operate.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.local.lotto.service.LotteryService;
import com.hhly.commoncore.persistence.operate.dao.OperateLotteryInfoDaoMapper;
import com.hhly.commoncore.remote.cache.service.ILotteryIssueCacheService;
import com.hhly.commoncore.remote.operate.service.IOperateLotteryService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.common.LotteryEnum.LotteryCategory;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.constants.MessageCodeConstants;
import com.hhly.skeleton.base.exception.ResultJsonException;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.StringUtil;
import com.hhly.skeleton.lotto.base.issue.bo.CurrentAndPreIssueBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateLotteryBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateLotteryDetailBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateLotteryLottVO;

/**
 * @desc 彩种运营管理服务实现类
 * @author lidecheng
 * @date 2017年3月8日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service("operateLotteryService")
public class OperateLotteryServiceImpl  implements IOperateLotteryService{
	@Autowired
	private OperateLotteryInfoDaoMapper operateLotteryInfoDaoMapper;	
	@Autowired
    private LotteryService lotteryService;	
	@Autowired
	private ILotteryIssueCacheService lotteryIssueCacheService;
	
	@Value("${before_file_url}")
	protected String beforeFileUrl;
	

	/**
	 * 获取主页彩种运营管理信息
	 * @return
	 */
	@Override
	public ResultBO<OperateLotteryBO> findHomeOperLottery(OperateLotteryLottVO vo) {		
		OperateLotteryBO  operateLotteryBO = doOperLottery(vo);//彩种信息处理
		if(operateLotteryBO==null){
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.DATA_NOT_FOUND_SYS));
		}
		List<OperateLotteryDetailBO>   operLottList = operateLotteryBO.getLotteryInfoList();
		setOperLottery(operLottList);//设置彩种信息	
		//设置图片链接
		for(OperateLotteryDetailBO bo: operLottList){
			bo.setLotteryLogoUrl(beforeFileUrl+bo.getLotteryLogoUrl());
			if(bo.getOperUrl()!=null)
			bo.setOperUrl(beforeFileUrl+bo.getOperUrl());
			if(!StringUtil.isBlank(bo.getLotteryChildCode())){
				bo.setLotteryLogoUrl(null);//子玩法设置logo图片为空
			}
		}
		return new ResultBO<OperateLotteryBO>(operateLotteryBO);	
	}
	/**
	 * 设置彩种运营数据的彩种信息,并处理过期彩种信息
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	private void setOperLottery(List<OperateLotteryDetailBO>  list){
		List<CurrentAndPreIssueBO>  lotteryIssuelist = (List<CurrentAndPreIssueBO>)lotteryIssueCacheService.getAllCurrentAndPreIssue().getData();
		Long time = new Date().getTime();
		Iterator<OperateLotteryDetailBO> it= list.iterator();  	
		//判断彩种是否过期,过期删除
		while(it.hasNext()){  		  
			OperateLotteryDetailBO bo = it.next();
			if(bo.getOfflineTime()!=null&&bo.getOfflineTime().getTime()<time){
				it.remove();  
			}							  
		} 
		//添加彩种运营的奖池和截止销售时间
		for(int i=0;i<list.size() ;i++){
			OperateLotteryDetailBO bo =list.get(i); 
			for(CurrentAndPreIssueBO lottBO: lotteryIssuelist){
				if(bo.getTypeId().intValue()==lottBO.getLotteryCode().intValue()){
					bo.setSaleEndTime(lottBO.getSaleEndTime());
					bo.setJackpotAmount(lottBO.getPreJackpot());
					if(lottBO.getLotteryCategory()==LotteryCategory.HIGH.getValue())//高频彩赋值最后一期时间
					bo.setSaleEndTime(lotteryService.findMaxEndDrawTime(lottBO.getLotteryCode()));

				}
			}			
		}	
	}
	/**
	 * 彩种运营信息处理
	 * @param vo
	 * @return
	 */
	private OperateLotteryBO doOperLottery(OperateLotteryLottVO vo){	
		vo.setIsDefault(Constants.NO);//查询非默认彩种
		List<OperateLotteryBO>   list= operateLotteryInfoDaoMapper.findPCHomeOperLottery(vo);
		OperateLotteryBO   operLotteryBO = getLotteryInfoByNowDate(list);
		if(operLotteryBO==null){
			vo.setIsDefault(Constants.YES);//查询默认彩种
			list = operateLotteryInfoDaoMapper.findPCHomeOperLottery(vo);
			operLotteryBO = getLotteryInfoByNowDate(list);
		}				
		return operLotteryBO;	
	}
	
	/**
	 * 根据当前时间获取需要的彩种运营信息
	 * @param list
	 * @return
	 */
	private	OperateLotteryBO getLotteryInfoByNowDate(List<OperateLotteryBO>  list){
		OperateLotteryBO bO = null;		
		String nowstr = DateUtil.dayForWeek()+DateUtil.getNow("HH:mm:ss");
		for(OperateLotteryBO bo : list){
			if(GetOnlineWeekCheck(bo.getOnlineWeek(),bo.getOnlineTime(),bo.getOfflineWeek(),bo.getOfflineTime(),nowstr)){
				bO  = bo;
				break;
			}
		}
		return bO;
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
	private boolean GetOnlineWeekCheck(Short onlineWeek,String onlineTime,Short offlineWeek,String offlineTime,String nowstr){
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
}
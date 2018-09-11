package com.hhly.commoncore.remote.lotto.service;
import java.util.Date;
import java.util.List;

import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.lotto.base.sport.bo.JcMathSPBO;
import com.hhly.skeleton.lotto.base.sport.bo.JcMathWinSPBO;
import com.hhly.skeleton.lotto.base.sport.bo.JclqMainDataBO;
import com.hhly.skeleton.lotto.base.sport.bo.JczqMainDataBO;
import com.hhly.skeleton.lotto.base.sport.vo.JcParamVO;

/**
 * 
 * @author lidecheng

 * @date 2017年2月6日 下午4:01:10

 * @desc  竞彩足球前端数据接口
 *
 */
public interface IJcDataCommService {
	
    /**
     * 查询竞彩蓝球推荐赛事信息
     * @param lotteryCode
     * @param issueCode
     * @param saleStatus 查询状态
     * @return
     */
	ResultBO<List<JcMathSPBO>>  findSportMatchBBSPInfo(int lotteryCode,String issueCode,String queryDate,Integer saleStatus);
    
    /**
     * 查询快投单场致胜信息
     * @param lotteryCode
     * @param issueCode
     * @param saleEndTime
     * @return
     */
	ResultBO<JcMathWinSPBO> findSportFBMatchDCZSInfo(long id,int lotteryCode,String issueCode,Date saleEndTime,Date startTime, String systemCode);
    /**
     * 查询竞彩足球推荐赛事信息
     * @param lotteryCode
     * @param issueCode
     * @param saleStatus 查询状态
     * @return
     */
	ResultBO<List<JcMathSPBO>>  findSportMatchFBSPInfo(int lotteryCode,String issueCode,String queryDate,Integer saleStatus);
   
}

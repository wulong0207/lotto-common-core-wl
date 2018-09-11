package com.hhly.commoncore.remote.lotto.service.impl;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.hhly.commoncore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.commoncore.remote.lotto.service.IJcDataCommService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.util.NumberFormatUtil;
import com.hhly.skeleton.base.util.StringUtil;
import com.hhly.skeleton.lotto.base.sport.bo.JcMathSPBO;
import com.hhly.skeleton.lotto.base.sport.bo.JcMathWinSPBO;

@Service("jcDataCommService")
public class JcDataCommServiceImpl implements IJcDataCommService {

    @Value("${before_file_url}")
    protected String beforeFileUrl;
    @Autowired
    private SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper;


    /**
     * 查询快投单场致胜信息
     *
     * @param lotteryCode
     * @param issueCode
     * @param saleEndTime
     * @return
     */
    public ResultBO<JcMathWinSPBO> findSportFBMatchDCZSInfo(long id, int lotteryCode, String issueCode, Date saleEndTime, Date startTime, String systemCode) {
    	JcMathWinSPBO  jcMathWinSPBO =null;   
    	//查询当前投注赛事之前的sp值最大的比赛
    	jcMathWinSPBO= sportAgainstInfoDaoMapper.findSportFBMatchDCZSInfo(lotteryCode, issueCode, startTime, systemCode,Constants.NUM_1);   	
    	if(jcMathWinSPBO==null){
    		//查询单场至胜时间相同情况下配对赛事信息（首页接口）
    		JcMathWinSPBO jcMathWinSPBOTimeBO = sportAgainstInfoDaoMapper.findSportFBMatchDCZSLatelyTime(id, lotteryCode, issueCode, startTime, systemCode);
	        if(jcMathWinSPBOTimeBO!=null){
	        	jcMathWinSPBO= sportAgainstInfoDaoMapper.findSportFBMatchDCZSInfo(lotteryCode, issueCode, jcMathWinSPBOTimeBO.getStartTime(), systemCode,Constants.NUM_2);
	        }
    	}                   
        if(jcMathWinSPBO!=null){
        	if (!StringUtil.isBlank(jcMathWinSPBO.getHomeLogoUrl())) {
                jcMathWinSPBO.setHomeLogoUrl(beforeFileUrl + jcMathWinSPBO.getHomeLogoUrl());
            }
            if (!StringUtil.isBlank(jcMathWinSPBO.getVisitiLogoUrl())) {
                jcMathWinSPBO.setVisitiLogoUrl(beforeFileUrl + jcMathWinSPBO.getVisitiLogoUrl());
            }
        }        
        return ResultBO.ok(jcMathWinSPBO);
    }
    
    /**
     * 查询竞彩足球对阵信息
     *
     * @return
     */
    public ResultBO<List<JcMathSPBO>> findSportMatchFBSPInfo(int lotteryCode, String issueCode, String queryDate,Integer saleStatus) {
        List<JcMathSPBO> list = sportAgainstInfoDaoMapper.findSportMatchFBSPInfo(lotteryCode, issueCode, queryDate,saleStatus);
        for (JcMathSPBO bo : list) {
            if (!StringUtil.isBlank(bo.getHomeLogoUrl()) && !bo.getHomeLogoUrl().contains("http"))
                bo.setHomeLogoUrl(beforeFileUrl + bo.getHomeLogoUrl());
            if (!StringUtil.isBlank(bo.getVisitiLogoUrl()) && !bo.getVisitiLogoUrl().contains("http"))
                bo.setVisitiLogoUrl(beforeFileUrl + bo.getVisitiLogoUrl());
            bo.setNewestSpDraw(NumberFormatUtil.format(bo.getNewestSpDraw()));
            bo.setNewestSpWin(NumberFormatUtil.format(bo.getNewestSpWin()));
            bo.setNewestSpFail(NumberFormatUtil.format(bo.getNewestSpFail()));
        }
        return ResultBO.ok(list);
    }
    
    /**
     * 查询竞彩篮球对阵信息
     *
     * @return
     */
    public ResultBO<List<JcMathSPBO>> findSportMatchBBSPInfo(int lotteryCode, String issueCode, String queryDate,Integer saleStatus) {
        List<JcMathSPBO> list = sportAgainstInfoDaoMapper.findSportMatchBBSPInfo(lotteryCode, issueCode, queryDate,saleStatus);
        for (JcMathSPBO bo : list) {
        	if (!StringUtil.isBlank(bo.getHomeLogoUrl()) && !bo.getHomeLogoUrl().contains("http"))
                bo.setHomeLogoUrl(beforeFileUrl + bo.getHomeLogoUrl());
            if (!StringUtil.isBlank(bo.getVisitiLogoUrl()) && !bo.getVisitiLogoUrl().contains("http"))
                bo.setVisitiLogoUrl(beforeFileUrl + bo.getVisitiLogoUrl());
            bo.setNewestSpDraw(NumberFormatUtil.format(bo.getNewestSpDraw()));
            bo.setNewestSpWin(NumberFormatUtil.format(bo.getNewestSpWin()));
            bo.setNewestSpFail(NumberFormatUtil.format(bo.getNewestSpFail()));
        }
        return ResultBO.ok(list);
    }    
    
}

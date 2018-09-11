package com.hhly.commoncore.persistence.sport.dao;



import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.skeleton.lotto.base.sport.bo.JcMathSPBO;
import com.hhly.skeleton.lotto.base.sport.bo.JcMathWinSPBO;


public interface SportAgainstInfoDaoMapper {



    /**
     * 查询竞彩篮球对阵sp信息
     * @param systemCode
     * @param lotteryCode
     * @return
     */
    List<JcMathSPBO> findSportMatchBBSPInfo(@Param("lotteryCode")Integer lotteryCode,@Param("issueCode")String issueCode,@Param("queryDate")String queryDate,@Param("saleStatus")Integer saleStatus);
    /**
     * 查询竞彩足球对阵sp信息
     * @param systemCode
     * @param lotteryCode
     * @return
     */
    List<JcMathSPBO> findSportMatchFBSPInfo(@Param("lotteryCode")Integer lotteryCode,@Param("issueCode")String issueCode,@Param("queryDate")String queryDate,@Param("saleStatus")Integer saleStatus);

    /**
     * 查询单场至胜时间相同情况下配对赛事信息（首页接口）
     * @param lotteryCode
     * @param issueCode
     * @return queryType 1查询配对赛事之前的比赛，2配对赛事之后的比赛
     */
    JcMathWinSPBO findSportFBMatchDCZSInfo(@Param("lotteryCode")Integer lotteryCode,@Param("issueCode")String issueCode,@Param("date")Date date, @Param("systemCode")String systemCode, @Param("queryType")Integer queryType);
    /**
     * 查询单场至胜比赛时间最相近的配对赛事信息（首页接口）
     * @param lotteryCode
     * @param issueCode
     * @return
     */
    JcMathWinSPBO findSportFBMatchDCZSLatelyTime(@Param("id")long id,@Param("lotteryCode")Integer lotteryCode,@Param("issueCode")String issueCode,@Param("date")Date date, @Param("systemCode")String systemCode);

}
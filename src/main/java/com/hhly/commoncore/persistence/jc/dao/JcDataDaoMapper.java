package com.hhly.commoncore.persistence.jc.dao;

import java.util.List;

import com.hhly.skeleton.lotto.base.sport.bo.JclqDaoBO;
import com.hhly.skeleton.lotto.base.sport.bo.JczqDaoBO;
import com.hhly.skeleton.lotto.base.sport.bo.MatchDataBO;
import com.hhly.skeleton.lotto.base.sport.vo.JcParamVO;

/**
 * @auth lgs on
 * @date 2017/2/22.
 * @desc 竞彩显示持久层
 * @compay 益彩网络科技有限公司
 * @vsersion 1.0
 */
public interface JcDataDaoMapper {

    List<JczqDaoBO> findJczqData(JcParamVO vo);
    
    Integer findJcSaleEndDataTotal(JcParamVO vo);

    List<JczqDaoBO> findJczqSaleEndData(JcParamVO vo);

    List<MatchDataBO> findMatchTotal(JcParamVO vo);

    List<JclqDaoBO> findJclqData(JcParamVO vo);

    List<JclqDaoBO> findJclqSaleEndData(JcParamVO vo);

 
}
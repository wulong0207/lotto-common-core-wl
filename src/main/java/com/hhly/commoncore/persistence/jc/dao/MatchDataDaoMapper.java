package com.hhly.commoncore.persistence.jc.dao;

import java.util.List;

import com.hhly.skeleton.lotto.base.sport.bo.MatchDataBO;
import com.hhly.skeleton.lotto.base.sport.vo.JcParamVO;

/**
 * @auth lgs on
 * @date 2017/2/22.
 * @desc 竞彩足球首页显示持久层
 * @compay 益彩网络科技有限公司
 * @vsersion 1.0
 */
public interface MatchDataDaoMapper {

    List<MatchDataBO> findMatchData(JcParamVO vo);
}
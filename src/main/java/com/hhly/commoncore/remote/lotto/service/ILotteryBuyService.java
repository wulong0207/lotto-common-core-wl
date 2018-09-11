package com.hhly.commoncore.remote.lotto.service;


import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateAdVO;

/**
 * @author zhouyang
 * @version 1.0
 * @desc 购彩大厅接口
 * @date 2017/4/28.
 * @company 益彩网络科技有限公司
 */
public interface ILotteryBuyService {

    /**
     * 查询彩种信息
     * @return
     */
    ResultBO<?> findLotteryList(OperateAdVO vo);

    /**
     * 查询开奖信息
     * @return
     */
    ResultBO<?> findDrawList();
    
}

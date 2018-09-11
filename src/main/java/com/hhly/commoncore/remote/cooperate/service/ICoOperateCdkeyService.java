package com.hhly.commoncore.remote.cooperate.service;

import java.util.List;

import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateCdkeyExchangeBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateCdkeyQueueBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateLotteryCdkeyBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateOrderBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateCdkeyExchangeVO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateCdkeyVO;

/**
 * 积分兑换
 *
 * @author huangchengfang1219
 * @date 2018年3月6日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface ICoOperateCdkeyService {

	/**
	 * 获取兑换码
	 * 
	 * @param vo
	 * @return
	 */
	CoOperateLotteryCdkeyBO doObtainCdkey(CoOperateCdkeyVO vo);

	/**
	 * 兑换订单
	 * 
	 * @param vo
	 */
	CoOperateOrderBO doExchangeCdkey(CoOperateCdkeyExchangeVO vo);

	/**
	 * 查询兑换码信息
	 * 
	 * @param vo
	 * @return
	 */
	CoOperateCdkeyExchangeBO findAndValidCdkey(CoOperateCdkeyExchangeVO vo);

	/**
	 * 刷新兑换码队列
	 */
	void doRefreshCdkeyQueue();

	/**
	 * 获取本站兑换码
	 * 
	 * @param num
	 * @return
	 */
	List<CoOperateCdkeyQueueBO> findCdkeyList(long num);

	/**
	 * 随机兑换码
	 * 
	 * @return
	 */
	String getRandomCdkey();

	/**
	 * 更新本站兑换码
	 * 
	 * @param cdkeyList
	 */
	String updatForAddMyCdkey(Integer lotteryCode, List<CoOperateCdkeyQueueBO> cdkeyList, Short status);

	/**
	 * 将已去除的兑换码重新插入到队列里
	 * @param cdkeyList
	 */
	void rollbackCdkeyList(List<CoOperateCdkeyQueueBO> cdkeyList);
}

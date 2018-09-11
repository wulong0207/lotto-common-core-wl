package com.hhly.commoncore.remote.cooperate.service;

import java.util.List;

import com.hhly.commoncore.persistence.cooperate.po.CoOperateLotteryPO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelAndLogBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelLotteryBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelMerPageBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelMerRecordBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateRecordQueryVO;

/**
 * 积分兑换
 *
 * @author huangchengfang1219
 * @date 2018年3月6日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface ICoOperateChannelService {

	/**
	 * 根据channelId查找积分兑换渠道
	 * 
	 * @param channelId
	 * @return
	 */
	CoOperateChannelBO findByChannelId(String channelId);

	/**
	 * 根据channelId查找积分兑换渠道,并校验积分兑换渠道是否可以调用接口
	 * 
	 * @param channelId
	 * @return
	 */
	CoOperateChannelBO findAndValidChannel(String channelId);

	/**
	 * 查询渠道账户信息
	 * 
	 * @param channelId
	 * @return
	 */
	CoOperateChannelAndLogBO findChannelAndHis(String channelId);

	/**
	 * 查询渠道商户余额类信息
	 * 
	 * @param vo
	 * @return
	 */
	CoOperateChannelMerPageBO<CoOperateChannelMerRecordBO> findMerchantBalanceInfo(CoOperateRecordQueryVO vo);

	/**
	 * 查询渠道商户库存类信息
	 * 
	 * @param vo
	 * @return
	 */
	CoOperateChannelMerPageBO<CoOperateChannelMerRecordBO> findMerchantNumInfo(CoOperateRecordQueryVO vo);

	/**
	 * 查询渠道商户彩种信息
	 * 
	 * @param vo
	 * @return
	 */
	List<CoOperateChannelLotteryBO> findChannelLotteryInfo(String channelId);

	/**
	 * 扣减余额
	 * 
	 * @param vo
	 * @param channelBO
	 * @param lottery
	 */
	void updateBalance(long num, CoOperateChannelBO channelBO, CoOperateLotteryPO lottery);
}

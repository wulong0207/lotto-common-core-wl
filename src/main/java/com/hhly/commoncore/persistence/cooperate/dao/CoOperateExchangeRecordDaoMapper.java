package com.hhly.commoncore.persistence.cooperate.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.commoncore.persistence.cooperate.po.CoOperateExchangeRecordPO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelLotteryBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelMerRecordBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelOrderBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateAgencyVO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateRecordQueryVO;

public interface CoOperateExchangeRecordDaoMapper {
	
	/**
	 * 插入流水记录，返回主键ID
	 * @param po
	 * @return
	 */
	void insert(CoOperateExchangeRecordPO po);

	/**
	 * 批量插入流水
	 * 
	 * @param exchangeList
	 */
	void insertBatch(@Param("exchangePOList") List<CoOperateExchangeRecordPO> exchangePOList);

	/**
	 * 保存订单号到兑换流水
	 * 
	 * @param po
	 */
	void updateOrderCode(CoOperateExchangeRecordPO exchangePO);
	

	/**
	 * 保存订单号到兑换码
	 * 
	 * @param po
	 */
	void updateCdkeyOrderCode(CoOperateExchangeRecordPO exchangePO);
	
	/**
	 * 查询渠道商户余额类信息
	 * @param vo
	 * @return
	 */
	List<CoOperateChannelMerRecordBO> findMerchantBalanceInfo(CoOperateRecordQueryVO vo);
	
	/**
	 * 统计渠道商户余额类信息
	 * @param vo
	 * @return
	 */
	Integer findMerchantBalanceTotal(CoOperateRecordQueryVO vo);
	
	
	/**
	 * 查询渠道商户库存类信息
	 * @param vo
	 * @return
	 */
	List<CoOperateChannelMerRecordBO> findMerchantNumInfo(CoOperateRecordQueryVO vo);
	
	/**
	 * 统计渠道商户库存类信息
	 * @param vo
	 * @return
	 */
	Integer findMerchantNumTotal(CoOperateRecordQueryVO vo);
	
	
	/**
	 * 查询商户彩种信息
	 * @param channelId
	 * @return
	 */
	List<CoOperateChannelLotteryBO> findMerchantLotteryInfo(@Param("channelId") String channelId);
	
	
	/**
	 * 查询商户余额彩种信息
	 * @param channelId
	 * @return
	 */
	List<CoOperateChannelLotteryBO> findMerchBalaLottoCombobox(@Param("channelId") String channelId);
	
	/**
	 * 查询商户库存彩种信息
	 * @param channelId
	 * @return
	 */
	List<CoOperateChannelLotteryBO> findMerchantLotteryCombobox(@Param("channelId") String channelId);
	
	/**
	 * 查询渠道订单总数
	 * @param vo
	 * @return
	 */
	int findChannelOrderTotal(CoOperateAgencyVO vo);
	
	/**
	 * 查询渠道订单列表
	 * @param vo
	 * @return
	 */
	List<CoOperateChannelOrderBO> findChannelOrderList(CoOperateAgencyVO vo);
}

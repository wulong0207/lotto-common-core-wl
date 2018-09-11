package com.hhly.commoncore.remote.cooperate.service;

import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateOrderInfoBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateOrderInfoVO;

/**
 * 积分兑换下单
 *
 * @author huangchengfang1219
 * @date 2018年3月29日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface ICoOperateAddOrderService {

	/**
	 * 快速下单，简易下单，机选下单
	 * 
	 * @param vo
	 */
	CoOperateOrderInfoBO addSimpleOrder(CoOperateOrderInfoVO vo);

	/**
	 * 兑换码下单
	 * 
	 * @param vo
	 */
	CoOperateOrderInfoBO addOrder(CoOperateOrderInfoVO vo);
}

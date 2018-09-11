package com.hhly.commoncore.http.lottocore.order;

import com.hhly.skeleton.lotto.base.order.vo.OrderInfoVO;

/**
 * 调用lotto-core订单服务接口
 *
 * @author huangchengfang1219
 * @date 2018年3月30日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface OrderService {

	/**
	 * @param orderInfo
	 *            订单信息
	 * @return
	 * @throws Exception
	 * @Desc 添加订单信息
	 */
	String addOrder(OrderInfoVO orderInfo);

}

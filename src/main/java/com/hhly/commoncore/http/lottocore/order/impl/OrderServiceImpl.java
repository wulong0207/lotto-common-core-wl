package com.hhly.commoncore.http.lottocore.order.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hhly.commoncore.http.lottocore.order.OrderService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.exception.ResultJsonException;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.lotto.base.order.bo.OrderInfoBO;
import com.hhly.skeleton.lotto.base.order.vo.OrderInfoVO;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	@Autowired
	private RestTemplate restTemplate;
	@Value("${lotto_core_url}")
	private String lottoCoreUrl;

	@Override
	public String addOrder(OrderInfoVO orderInfoVO) {
		// 使用HTTP调用下单接口
		String jsonStr = restTemplate.postForEntity(lottoCoreUrl + "order/add", orderInfoVO, String.class).getBody();
		ResultBO<OrderInfoBO> result = JsonUtil.jsonToJackObject(jsonStr, new TypeReference<ResultBO<OrderInfoBO>>() {
		});
		// 判断下单状态
		if (result.isError() || result.getData() == null) {
			logger.error("调用下单接口失败:{}", result.getMessage());
			throw new ResultJsonException(result);
		}
		String orderCode = result.getData().getOrderCode();
		logger.info("调用接口下单成功:{}", orderCode);
		return orderCode;
	}

}

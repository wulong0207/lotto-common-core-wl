
package com.hhly.commoncore.local.cooperate.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.hhly.commoncore.local.cooperate.CoOperatePayService;
import com.hhly.commoncore.persistence.cooperate.dao.CoOperateChannelDaoMapper;
import com.hhly.commoncore.persistence.dic.dao.DicDataDetailDaoMapper;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.constants.MessageApiCodeConstants;
import com.hhly.skeleton.base.exception.ResultJsonException;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelBO;
import com.hhly.skeleton.lotto.base.dic.bo.DicDataDetailBO;
import com.hhly.skeleton.lotto.base.dic.vo.DicDataDetailVO;

/**
 * @desc
 * @author cheng chen
 * @date 2018年4月28日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class CoOperatePayServiceImpl implements CoOperatePayService {

	public static final String COOPERATE_PAY_ACTIVITY_DIC_CODE = "9998";

	@Value("${lotto_pay_url}")
	private String lottoPayUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private CoOperateChannelDaoMapper coOperateChannelDaoMapper;

	@Autowired
	private DicDataDetailDaoMapper dicDataDetailDaoMapper;

	@Override
	public ResultBO<?> recharge(Integer userId, String channelId, Double amount, String transCode) {

		CoOperateChannelBO channelBO = coOperateChannelDaoMapper.findByChannelId(channelId);
		if (ObjectUtil.isBlank(channelBO))
			throw new ResultJsonException(ResultBO.errApi(MessageApiCodeConstants.COOPERATE_CHANNEL_NOT_FOUND));
		Double singleAmount = channelBO.getSingleRechargeAmount() == null ? 600d : channelBO.getSingleRechargeAmount();
		if (amount > singleAmount)
			throw new ResultJsonException(
					ResultBO.errApi(MessageApiCodeConstants.COOPERATE_CHANNEL_PAY_RECHARGE_SINGLE_CEIL, singleAmount.toString()));

		int count = coOperateChannelDaoMapper.updateBalance(channelBO.getId(), amount);
		// 余额扣除失败，余额不足
		if (count == 0) {
			throw new ResultJsonException(ResultBO.errApi(MessageApiCodeConstants.COOPERATE_CHANNEL_BALANCE_NOT_ENOUGH));
		}
		DicDataDetailVO dicDataDetailVO = new DicDataDetailVO();
		dicDataDetailVO.setDicCode(COOPERATE_PAY_ACTIVITY_DIC_CODE);
		dicDataDetailVO.setDicDataName(channelId);
		DicDataDetailBO dicDataDetailBO = dicDataDetailDaoMapper.findSingle(dicDataDetailVO);
		JSONObject paramJson = new JSONObject();
		paramJson.put("userId", userId);
		paramJson.put("amount", amount);
		paramJson.put("rechargeType", channelBO.getRechargeType() == null ? 2 : channelBO.getRechargeType());
		paramJson.put("agentTradeNo", transCode);
		paramJson.put("channelId", channelId);
		if (dicDataDetailBO != null && !ObjectUtil.isBlank(dicDataDetailBO.getDicDataValue())) {
			paramJson.put("activityCode", dicDataDetailBO.getDicDataValue());
		}
		paramJson.put("remark", channelBO.getChannelName() + "充值");
		String url = lottoPayUrl + "recharge/agentRecharge";
		ResultBO<?> payResult = restTemplate.postForEntity(url, paramJson, ResultBO.class).getBody();
		if (payResult.isError())
			throw new ResultJsonException(ResultBO.errApi(MessageApiCodeConstants.COOPERATE_CHANNEL_PAY_RECHARGE_FAIL));
		return payResult;
	}
}

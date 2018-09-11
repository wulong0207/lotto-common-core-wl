package com.hhly.commoncore.controller.operate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.commoncore.remote.operate.service.IOperateMarketChannelService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.constants.MessageCodeConstants;
import com.hhly.skeleton.lotto.base.operate.bo.OperateMarketChannelBO;

/**
 * 市场渠道
 *
 * @author huangchengfang1219
 * @date 2018年3月30日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RestController
@RequestMapping("/operate/channel")
public class OperateMarketChannelController {

	@Autowired
	private IOperateMarketChannelService operateMarketChannelService;

	@RequestMapping(value = "{channelId}", method = RequestMethod.GET)
	public ResultBO<?> getChannel(@PathVariable("channelId") String channelId) {
		OperateMarketChannelBO channelBO = operateMarketChannelService.findByChannelId(channelId);
		if (channelBO == null) {
			return ResultBO.err(MessageCodeConstants.DATA_NOT_EXIST);
		}
		return ResultBO.ok(channelBO);
	}
}

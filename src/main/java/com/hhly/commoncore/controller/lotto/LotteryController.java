package com.hhly.commoncore.controller.lotto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.commoncore.local.lotto.service.LotteryService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.constants.MessageCodeConstants;
import com.hhly.skeleton.base.exception.Assert;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.lotto.base.lottery.vo.LotteryVO;

@RestController
@RequestMapping("lottery")
public class LotteryController {

	@Autowired
	private LotteryService lotteryService;

	@RequestMapping(value = "list", method = { RequestMethod.POST })
	public Object list(@RequestBody LotteryVO vo) {
		if (ObjectUtil.isBlank(vo))
			return ResultBO.err(MessageCodeConstants.PARAM_IS_FIELD);

		Assert.paramNotNull(vo.getDrawType(), "drawType");

		return ResultBO.ok(lotteryService.queryLotterySelectList(vo));
	}
}

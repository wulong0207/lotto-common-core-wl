package com.hhly.commoncore.controller.lotto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.commoncore.local.lotto.service.LotteryIssueService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.common.LotteryEnum.Lottery;
import com.hhly.skeleton.base.constants.MessageCodeConstants;
import com.hhly.skeleton.base.exception.Assert;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.lotto.base.lottery.vo.LotteryVO;

@RestController
@RequestMapping("lottery/issue")
public class LotteryIssueController {

	@Autowired
	private LotteryIssueService lotteryIssueService;

	@RequestMapping(value = "/list", method = { RequestMethod.POST })
	public Object queryIssueByLottery(@RequestBody LotteryVO lotteryVO) {
		if (ObjectUtil.isBlank(lotteryVO))
			return ResultBO.err(MessageCodeConstants.PARAM_IS_FIELD);
		Assert.paramNotNull(lotteryVO.getLotteryCode(), "lotteryCode");
		return ResultBO.ok(lotteryIssueService.queryIssueByLottery(lotteryVO));
	}

	@RequestMapping(value = "/base/{lotteryCode}", method = { RequestMethod.GET })
	public Object findLotteryIssueBase(@PathVariable Integer lotteryCode) {
		Assert.paramLegal(Lottery.contain(lotteryCode), "lotteryCode");
		return ResultBO.ok(lotteryIssueService.findLotteryIssueBase(lotteryCode));
	}
}

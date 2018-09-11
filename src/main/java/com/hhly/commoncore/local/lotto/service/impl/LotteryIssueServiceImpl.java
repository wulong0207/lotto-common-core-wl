package com.hhly.commoncore.local.lotto.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.base.util.RedisUtil;
import com.hhly.commoncore.local.lotto.service.LotteryIssueService;
import com.hhly.commoncore.persistence.issue.dao.LotteryIssueDaoMapper;
import com.hhly.skeleton.base.common.LotteryEnum.ConIssue;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.exception.Assert;
import com.hhly.skeleton.lotto.base.issue.bo.CurrentAndPreIssueBO;
import com.hhly.skeleton.lotto.base.issue.bo.IssueBO;
import com.hhly.skeleton.lotto.base.issue.bo.IssueDrawBO;
import com.hhly.skeleton.lotto.base.lottery.bo.LotteryIssueBaseBO;
import com.hhly.skeleton.lotto.base.lottery.vo.LotteryVO;

@Service("lotteryIssueService")
public class LotteryIssueServiceImpl implements LotteryIssueService {

	@Autowired
	private LotteryIssueDaoMapper lotteryIssueDaoMapper;
	@Autowired
	private RedisUtil redisUtil;

	@Override
	public CurrentAndPreIssueBO findIssueAndPreIssueByCode(int lotteryCode) {
		CurrentAndPreIssueBO issueBO = lotteryIssueDaoMapper.findIssueAndPreIssueByCode(lotteryCode);
		return issueBO;
	}

	@Override
	public List<String> queryIssueByLottery(LotteryVO lotteryVO) {
		Assert.paramNotNull(lotteryVO, "lotteryCode");
		Assert.paramNotNull(lotteryVO.getLotteryCode(), "lotteryCode");
		Integer qryFlag = lotteryVO.getQryFlag();
		if (qryFlag == null) {
			lotteryVO.setQryFlag(Constants.NUM_1);
		} else {
			Assert.paramLegal(qryFlag == 1 || qryFlag == 2, "qryFlag");
		}
		return lotteryIssueDaoMapper.queryIssueByLottery(lotteryVO);
	}

	@Override
	public LotteryIssueBaseBO findLotteryIssueBase(Integer lotteryCode) {
		// 1.缓存获取
		String key = CacheConstants.getLotteryTopKey(lotteryCode, null);
		LotteryIssueBaseBO target = (LotteryIssueBaseBO) redisUtil.getObj(key);
		if (target != null) {
			return target;
		}
		// 目前该接口只提供给lotto-draw使用，只需要当前期和最新开奖期信息，其它地方调lotto-core使用
		// 由于信息不全，不将接口放入缓存
		IssueBO curIssue = lotteryIssueDaoMapper.findSingleFront(new LotteryVO(lotteryCode, ConIssue.CURRENT.getValue()));
		IssueDrawBO latestIssue = lotteryIssueDaoMapper.findLatestDrawIssue(new LotteryVO(lotteryCode));
		LotteryIssueBaseBO lotteryIssueBase = new LotteryIssueBaseBO();
		lotteryIssueBase.setCurIssue(curIssue);
		lotteryIssueBase.setLatestIssue(latestIssue);
		return lotteryIssueBase;
	}

}

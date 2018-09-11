package com.hhly.commoncore.remote.cooperate.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.base.util.CoOperateChannelUtil;
import com.hhly.commoncore.base.util.RedisUtil;
import com.hhly.commoncore.persistence.cooperate.dao.CoOperateChannelDaoMapper;
import com.hhly.commoncore.persistence.cooperate.dao.CoOperateExchangeRecordDaoMapper;
import com.hhly.commoncore.remote.cooperate.service.ICoOperateAgencyService;
import com.hhly.skeleton.base.bo.PagingBO;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.common.CoOperateEnum;
import com.hhly.skeleton.base.common.OrderEnum;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.constants.MessageCodeConstants;
import com.hhly.skeleton.base.exception.ResultJsonException;
import com.hhly.skeleton.base.page.IPageService;
import com.hhly.skeleton.base.page.ISimplePage;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateAgencyInfoBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelInfoBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelOrderBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateAgencyVO;

@Service("coOperateAgencyService")
public class CoOperateAgencyServiceImpl implements ICoOperateAgencyService {

	@Autowired
	private CoOperateChannelDaoMapper coOperateChannelDaoMapper;
	@Autowired
	private CoOperateExchangeRecordDaoMapper coOperateExchangeRecordDaoMapper;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private CoOperateChannelUtil coOperateChannelUtil;
	@Autowired
	private IPageService pageService;

	@Override
	public CoOperateAgencyInfoBO findAgencyInfo(CoOperateAgencyVO vo) {
		CoOperateAgencyInfoBO infoBO = new CoOperateAgencyInfoBO();
		infoBO.setAgencyChannelList(findAgencyChannelList(vo));
		return infoBO;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CoOperateChannelInfoBO> findAgencyChannelList(CoOperateAgencyVO vo) {
		CoOperateChannelInfoBO channelInfoBO = coOperateChannelUtil.getChannelByToken(vo.getChannelToken());
		if (ObjectUtil.isBlank(channelInfoBO)) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.TOKEN_LOSE_SERVICE));
		}
		if (!CoOperateEnum.MerchantType.AGENT.getValue().equals(channelInfoBO.getMerchantType())&&!CoOperateEnum.MerchantType.CO_PROXY.getValue().equals(channelInfoBO.getMerchantType())) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.COOPERATE_CHANNEL_AGENCY_TYPE_ERROR));
		}
		String key = CacheConstants.C_CORE_COOPERATE_AGENCY_CHANNEL_LIST + channelInfoBO.getChannelId();
		List<CoOperateChannelInfoBO> agencyChannelInfoList = (List<CoOperateChannelInfoBO>) redisUtil.getObj(key);
		if (agencyChannelInfoList != null) {
			return agencyChannelInfoList;
		}
		List<CoOperateChannelBO> agencyChannelBOList = coOperateChannelDaoMapper.findAgencyChannelList(channelInfoBO.getChannelId());
		agencyChannelInfoList = new ArrayList<>();
		for (CoOperateChannelBO agencyChannelBO : agencyChannelBOList) {
			CoOperateChannelInfoBO infoBO = new CoOperateChannelInfoBO();
			infoBO.setChannelId(agencyChannelBO.getChannelId());
			infoBO.setChannelName(agencyChannelBO.getChannelName());
			infoBO.setMerchantType(CoOperateEnum.MerchantType.getMerchantTypeValue(agencyChannelBO));
			agencyChannelInfoList.add(infoBO);
		}
		redisUtil.addObj(key, agencyChannelInfoList, CacheConstants.ONE_DAY);
		return agencyChannelInfoList;
	}

	@Override
	public PagingBO<CoOperateChannelOrderBO> findOrderList(CoOperateAgencyVO vo) {
		getAgencyChannel(vo);
		return pageService.getPageData(vo, new ISimplePage<CoOperateChannelOrderBO>() {

			@Override
			public int getTotal() {
				return coOperateExchangeRecordDaoMapper.findChannelOrderTotal(vo);
			}

			@Override
			public List<CoOperateChannelOrderBO> getData() {
				List<CoOperateChannelOrderBO> orderList = coOperateExchangeRecordDaoMapper.findChannelOrderList(vo);
				orderList.forEach(channelOrderBO -> {
					doSetChannelOrder(channelOrderBO);
				});
				return orderList;
			}
		});
	}

	/**
	 * 校验并获取中介下的渠道
	 * 
	 * @param vo
	 */
	public CoOperateChannelInfoBO getAgencyChannel(CoOperateAgencyVO vo) {
		List<CoOperateChannelInfoBO> agencyChannelList = findAgencyChannelList(vo);
		for (CoOperateChannelInfoBO agencyChannel : agencyChannelList) {
			if (agencyChannel.getChannelId().equals(vo.getQueryChannelId())) {
				return agencyChannel;
			}
		}
		throw new ResultJsonException(ResultBO.err(MessageCodeConstants.COOPERATE_CHANNEL_AGENCY_AUTH_ERROR, vo.getQueryChannelId()));
	}

	private void doSetChannelOrder(CoOperateChannelOrderBO channelOrderBO) {
		// 订单状态
		short cdkeyStatus = channelOrderBO.getCdkeyStatus() == null ? 0 : channelOrderBO.getCdkeyStatus();
		short winningStatus = channelOrderBO.getWinningStatus() == null ? 0 : channelOrderBO.getWinningStatus();
		if (CoOperateEnum.CdkeyStatusEnum.UNEXCHANGE.getValue().equals(cdkeyStatus)
				|| CoOperateEnum.CdkeyStatusEnum.ALLOCATED.getValue().equals(cdkeyStatus)) {
			// 兑换码状态：待兑换、已分配-->未兑换
			channelOrderBO.setStatus(CoOperateEnum.ChannelOrderStatus.UNEXCHANGE.getValue());
		} else if (CoOperateEnum.CdkeyStatusEnum.EXPIRED.getValue().equals(cdkeyStatus)
				|| (channelOrderBO.getExchangeOverTime() != null && channelOrderBO.getExchangeOverTime().before(new Date()))) {
			// 兑换码状态：已过期，或者过期时间<当前时间-->已过期
			channelOrderBO.setStatus(CoOperateEnum.ChannelOrderStatus.EXPIRED.getValue());
		} else if (CoOperateEnum.CdkeyStatusEnum.EXCHANGED.getValue().equals(cdkeyStatus)
				&& OrderEnum.OrderWinningStatus.NOT_DRAW_WINNING.getValue() == winningStatus) {
			// 兑换码状态：已兑换，中奖状态：未开奖-->已兑换未开奖
			channelOrderBO.setStatus(CoOperateEnum.ChannelOrderStatus.WAIT_WINING.getValue());
		} else if (CoOperateEnum.CdkeyStatusEnum.EXCHANGED.getValue().equals(cdkeyStatus)
				&& (OrderEnum.OrderWinningStatus.WINNING.getValue() == winningStatus
						|| OrderEnum.OrderWinningStatus.GET_WINNING.getValue() == winningStatus)) {
			// 兑换码状态：已兑换，中奖状态：已中奖、已派奖-->已中奖
			channelOrderBO.setStatus(CoOperateEnum.ChannelOrderStatus.WINING.getValue());
		} else if (CoOperateEnum.CdkeyStatusEnum.EXCHANGED.getValue().equals(cdkeyStatus)
				&& OrderEnum.OrderWinningStatus.NOT_WINNING.getValue() == winningStatus) {
			// 兑换码状态：已兑换，中奖状态：未中奖-->未中奖
			channelOrderBO.setStatus(CoOperateEnum.ChannelOrderStatus.NOT_WINING.getValue());
		}
		if (!CoOperateEnum.ChannelOrderStatus.WINING.getValue().equals(channelOrderBO.getStatus())) {
			channelOrderBO.setPreBonus(null);
		}
	}

}

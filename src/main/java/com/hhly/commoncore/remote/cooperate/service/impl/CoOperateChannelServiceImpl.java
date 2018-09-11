package com.hhly.commoncore.remote.cooperate.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.persistence.cooperate.dao.CoOperateChannelDaoMapper;
import com.hhly.commoncore.persistence.cooperate.dao.CoOperateExchangeRecordDaoMapper;
import com.hhly.commoncore.persistence.cooperate.po.CoOperateLotteryPO;
import com.hhly.commoncore.remote.cooperate.service.ICoOperateChannelService;
import com.hhly.skeleton.base.bo.PagingBO;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.common.CoOperateEnum;
import com.hhly.skeleton.base.common.CoOperateEnum.ChannelBMerchantStatus;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.constants.MessageApiCodeConstants;
import com.hhly.skeleton.base.exception.ResultJsonException;
import com.hhly.skeleton.base.page.IPageService;
import com.hhly.skeleton.base.page.ISimplePage;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelAndLogBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelLotteryBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelMerPageBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelMerRecordBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateRecordQueryVO;

@Service("coOperateChannelService")
public class CoOperateChannelServiceImpl implements ICoOperateChannelService {
	@Value("${before_file_url}")
	protected String beforeFileUrl;
	@Autowired
	private IPageService pageService;
	@Autowired
	private CoOperateExchangeRecordDaoMapper coOperateExchangeRecordDaoMapper;
	@Autowired
	private CoOperateChannelDaoMapper coOperateChannelDaoMapper;

	@Override
	public CoOperateChannelBO findByChannelId(String channelId) {
		return coOperateChannelDaoMapper.findByChannelId(channelId);
	}

	@Override
	public CoOperateChannelBO findAndValidChannel(String channelId) {
		CoOperateChannelBO channelBO = coOperateChannelDaoMapper.findByChannelId(channelId);
		// 判断渠道
		if (channelBO == null) {
			throw new ResultJsonException(ResultBO.errApi(MessageApiCodeConstants.COOPERATE_CHANNEL_NOT_FOUND));
		}
		// 判断商户结算类型
		if (!CoOperateEnum.ChannelType.NORMAL.getValue().equals(channelBO.getType())) {// 非普通商户
			throw new ResultJsonException(ResultBO.errApi(MessageApiCodeConstants.COOPERATE_CHANNEL_NOT_SUPPORT));
		}
		short settlementType = channelBO.getSettlementType() == null ? 0 : channelBO.getSettlementType();
		if (settlementType != CoOperateEnum.SettlementType.BALANCE_CAN_ARREARS.getValue()
				&& settlementType != CoOperateEnum.SettlementType.BALANCE_CANNOT_ARREARS.getValue()) {
			// 非余额结算类型不支持接口调用
			throw new ResultJsonException(ResultBO.errApi(MessageApiCodeConstants.COOPERATE_CHANNEL_NOT_SUPPORT));
		}
		// 判断渠道开始、结束合作时间
		Date nowTime = new Date();
		if (channelBO.getCoopStartTime() != null && channelBO.getCoopStartTime().after(nowTime)) {
			throw new ResultJsonException(ResultBO.errApi(MessageApiCodeConstants.COOPERATE_CHANNEL_NOT_BEGIN));
		}
		if (channelBO.getCoopEndTime() != null && channelBO.getCoopEndTime().before(nowTime)) {
			throw new ResultJsonException(ResultBO.errApi(MessageApiCodeConstants.COOPERATE_CHANNEL_IS_END));
		}
		// 判断渠道状态
		if (channelBO.getChannelStatus() != null && channelBO.getChannelStatus().shortValue() != Constants.YES) {
			throw new ResultJsonException(ResultBO.errApi(MessageApiCodeConstants.COOPERATE_CHANNEL_NOT_START));
		}
		return channelBO;
	}

	@Override
	public CoOperateChannelAndLogBO findChannelAndHis(String channelId) {
		return coOperateChannelDaoMapper.findChannelAndHis(channelId);
	}

	@Override
	public CoOperateChannelMerPageBO<CoOperateChannelMerRecordBO> findMerchantBalanceInfo(final CoOperateRecordQueryVO vo) {
		PagingBO<CoOperateChannelMerRecordBO> pageData = pageService.getPageData(vo, new ISimplePage<CoOperateChannelMerRecordBO>() {

			@Override
			public int getTotal() {
				return coOperateExchangeRecordDaoMapper.findMerchantBalanceTotal(vo);
			}

			@Override
			public List<CoOperateChannelMerRecordBO> getData() {
				List<CoOperateChannelMerRecordBO> list = coOperateExchangeRecordDaoMapper.findMerchantBalanceInfo(vo);
				if (list != null) {
					for (CoOperateChannelMerRecordBO bo : list) {
						if (bo.getStatus() != null) {
							switch (bo.getStatus()) {
							case 1:
							case 3:
								bo.setStatus(ChannelBMerchantStatus.UNEXCHANGE.getValue());
								break;
							case 4:
								bo.setStatus(ChannelBMerchantStatus.EXCHANGED.getValue());
								break;
							case 5:
								bo.setStatus(ChannelBMerchantStatus.EXPIRED.getValue());
								break;
							default:
								break;
							}
						} else {
							if (bo.getPayClass() == 1 && bo.getSerialType() == 1) {
								bo.setStatus(ChannelBMerchantStatus.SUCCESS.getValue());
							}
						}
					}
				}
				return list;
			}

		});
		List<CoOperateChannelLotteryBO> lotteryList = coOperateExchangeRecordDaoMapper.findMerchBalaLottoCombobox(vo.getChannelId());
		return new CoOperateChannelMerPageBO<CoOperateChannelMerRecordBO>(lotteryList, pageData.getTotal(), pageData.getData());
	}

	@Override
	public CoOperateChannelMerPageBO<CoOperateChannelMerRecordBO> findMerchantNumInfo(final CoOperateRecordQueryVO vo) {
		PagingBO<CoOperateChannelMerRecordBO> pageData = pageService.getPageData(vo, new ISimplePage<CoOperateChannelMerRecordBO>() {
			@Override
			public int getTotal() {
				return coOperateExchangeRecordDaoMapper.findMerchantNumTotal(vo);
			}

			@Override
			public List<CoOperateChannelMerRecordBO> getData() {
				List<CoOperateChannelMerRecordBO> list = coOperateExchangeRecordDaoMapper.findMerchantNumInfo(vo);
				if (list != null) {
					for (CoOperateChannelMerRecordBO bo : list) {
						if (bo.getStatus() != null) {
							switch (bo.getStatus()) {
							case 1:
							case 3:
								bo.setStatus(ChannelBMerchantStatus.UNEXCHANGE.getValue());
								break;
							case 4:
								bo.setStatus(ChannelBMerchantStatus.EXCHANGED.getValue());
								break;
							case 5:
								bo.setStatus(ChannelBMerchantStatus.EXPIRED.getValue());
								break;
							default:
								break;
							}
						} else {
							if (bo.getPayClass() == 1 && bo.getSerialType() == 1) {
								bo.setStatus(ChannelBMerchantStatus.SUCCESS.getValue());
							}
						}
					}
				}
				return list;
			}
		});
		List<CoOperateChannelLotteryBO> lotteryList = coOperateExchangeRecordDaoMapper.findMerchantLotteryCombobox(vo.getChannelId());
		return new CoOperateChannelMerPageBO<CoOperateChannelMerRecordBO>(lotteryList, pageData.getTotal(), pageData.getData());
	}

	@Override
	public List<CoOperateChannelLotteryBO> findChannelLotteryInfo(String channelId) {
		List<CoOperateChannelLotteryBO> list = coOperateExchangeRecordDaoMapper.findMerchantLotteryInfo(channelId);
		if (list != null) {
			for (CoOperateChannelLotteryBO bo : list) {
				if (!ObjectUtil.isBlank(bo.getLotteryLogoUrl())) {
					bo.setLotteryLogoUrl(beforeFileUrl + bo.getLotteryLogoUrl());
				}
			}
		}
		return list;
	}

	@Override
	public void updateBalance(long num, CoOperateChannelBO channelBO, CoOperateLotteryPO lottery) {
		Double exchangeBalance = lottery.getExchangeBalance();
		if (exchangeBalance == null) {
			exchangeBalance = 2d;
			lottery.setExchangeBalance(exchangeBalance);
		}
		// 交易金额
		BigDecimal exchangeAmount = new BigDecimal(exchangeBalance).multiply(new BigDecimal(num));
		exchangeAmount.setScale(Constants.NUM_10, BigDecimal.ROUND_HALF_UP);
		int count = coOperateChannelDaoMapper.updateBalance(channelBO.getId(), exchangeAmount.doubleValue());
		// 余额扣除失败，余额不足
		if (count == 0) {
			throw new ResultJsonException(ResultBO.errApi(MessageApiCodeConstants.COOPERATE_CHANNEL_BALANCE_NOT_ENOUGH));
		}
		CoOperateChannelBO newChannelBO = coOperateChannelDaoMapper.findBalanceById(channelBO.getId());
		channelBO.setBalance(newChannelBO.getBalance());
	}

}

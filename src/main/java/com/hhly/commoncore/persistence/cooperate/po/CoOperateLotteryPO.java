package com.hhly.commoncore.persistence.cooperate.po;

import java.util.Date;

/**
 * 兑换码表
 *
 * @author huangchengfang1219
 * @date 2018年3月6日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class CoOperateLotteryPO {

	private Integer id;

	private Integer channelId;

	private Integer lotteryCode;

	private String lotteryName;

	private Double denomination;

	private Double exchangeBalance;

	private String createBy;

	private Date createTime;

	private String modifyBy;

	private Date modifyTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Integer getLotteryCode() {
		return lotteryCode;
	}

	public void setLotteryCode(Integer lotteryCode) {
		this.lotteryCode = lotteryCode;
	}

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public Double getDenomination() {
		return denomination;
	}

	public void setDenomination(Double denomination) {
		this.denomination = denomination;
	}

	public Double getExchangeBalance() {
		return exchangeBalance;
	}

	public void setExchangeBalance(Double exchangeBalance) {
		this.exchangeBalance = exchangeBalance;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

}

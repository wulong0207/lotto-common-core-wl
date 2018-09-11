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
public class CoOperateCdkeyPO {

	private Integer id;

	private String lottoCdkey;

	private String myCdkey;

	private Integer lotteryCode;

	private Short status;

	private Integer exchangeChannel;

	private Date overTime;

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

	public String getLottoCdkey() {
		return lottoCdkey;
	}

	public void setLottoCdkey(String lottoCdkey) {
		this.lottoCdkey = lottoCdkey;
	}

	public String getMyCdkey() {
		return myCdkey;
	}

	public void setMyCdkey(String myCdkey) {
		this.myCdkey = myCdkey;
	}

	public Integer getLotteryCode() {
		return lotteryCode;
	}

	public void setLotteryCode(Integer lotteryCode) {
		this.lotteryCode = lotteryCode;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Integer getExchangeChannel() {
		return exchangeChannel;
	}

	public void setExchangeChannel(Integer exchangeChannel) {
		this.exchangeChannel = exchangeChannel;
	}

	public Date getOverTime() {
		return overTime;
	}

	public void setOverTime(Date overTime) {
		this.overTime = overTime;
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

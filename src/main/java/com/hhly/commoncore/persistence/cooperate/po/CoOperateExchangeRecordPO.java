package com.hhly.commoncore.persistence.cooperate.po;

import java.util.Date;

/**
 * 兑换记录表
 *
 * @author huangchengfang1219
 * @date 2018年3月6日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class CoOperateExchangeRecordPO {

	private Integer id;

	/**
	 * 父流水号
	 */
	private String pSerial;

	private Short payClass;

	private Double channelBalance;

	private String serialNumber;

	private Short serialType;

	private Integer cdkeyId;

	private String myCdkey;

	private String channelId;

	private Date exchangeRecordTime;

	private Date exchangeOverTime;

	private String orderCode;

	private Double payAmount;

	private String orderInfo;

	private String createBy;

	private Date createTime;

	private String modifyBy;

	private Date modifyTime;

	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getpSerial() {
		return pSerial;
	}

	public void setpSerial(String pSerial) {
		this.pSerial = pSerial;
	}

	public Short getPayClass() {
		return payClass;
	}

	public void setPayClass(Short payClass) {
		this.payClass = payClass;
	}

	public Double getChannelBalance() {
		return channelBalance;
	}

	public void setChannelBalance(Double channelBalance) {
		this.channelBalance = channelBalance;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Short getSerialType() {
		return serialType;
	}

	public void setSerialType(Short serialType) {
		this.serialType = serialType;
	}

	public Integer getCdkeyId() {
		return cdkeyId;
	}

	public void setCdkeyId(Integer cdkeyId) {
		this.cdkeyId = cdkeyId;
	}

	public String getMyCdkey() {
		return myCdkey;
	}

	public void setMyCdkey(String myCdkey) {
		this.myCdkey = myCdkey;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Date getExchangeRecordTime() {
		return exchangeRecordTime;
	}

	public void setExchangeRecordTime(Date exchangeRecordTime) {
		this.exchangeRecordTime = exchangeRecordTime;
	}

	public Date getExchangeOverTime() {
		return exchangeOverTime;
	}

	public void setExchangeOverTime(Date exchangeOverTime) {
		this.exchangeOverTime = exchangeOverTime;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}

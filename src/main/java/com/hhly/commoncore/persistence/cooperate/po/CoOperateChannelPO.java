package com.hhly.commoncore.persistence.cooperate.po;

import java.util.Date;

/**
 * 商户渠道表
 *
 * @author huangchengfang1219
 * @date 2018年3月6日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class CoOperateChannelPO {

	private Integer id;

	private String cooperateName;

	private String marketChannelId;

	private String password;

	private String rcode;

	private Double balance;

	private Integer ticketChannel;

	private Date actionTime;

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

	public String getCooperateName() {
		return cooperateName;
	}

	public void setCooperateName(String cooperateName) {
		this.cooperateName = cooperateName;
	}

	public String getMarketChannelId() {
		return marketChannelId;
	}

	public void setMarketChannelId(String marketChannelId) {
		this.marketChannelId = marketChannelId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRcode() {
		return rcode;
	}

	public void setRcode(String rcode) {
		this.rcode = rcode;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getTicketChannel() {
		return ticketChannel;
	}

	public void setTicketChannel(Integer ticketChannel) {
		this.ticketChannel = ticketChannel;
	}

	public Date getActionTime() {
		return actionTime;
	}

	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
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

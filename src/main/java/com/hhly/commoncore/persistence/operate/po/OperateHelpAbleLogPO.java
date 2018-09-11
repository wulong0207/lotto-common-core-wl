package com.hhly.commoncore.persistence.operate.po;

import java.io.Serializable;
import java.util.Date;

public class OperateHelpAbleLogPO implements Serializable {

	private static final long serialVersionUID = 2025339646570064958L;

	/**
	 * 主键ID
	 */
	private Integer id;

	/**
	 * 帮助ID
	 */
	private Integer helpId;

	/**
	 * 用户ID
	 */
	private Integer userId;

	/**
	 * 用户ID
	 */
	private String ip;

	/**
	 * 是否满意
	 */
	private Short isAble;

	/**
	 * 创建时间
	 */
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getHelpId() {
		return helpId;
	}

	public void setHelpId(Integer helpId) {
		this.helpId = helpId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Short getIsAble() {
		return isAble;
	}

	public void setIsAble(Short isAble) {
		this.isAble = isAble;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}

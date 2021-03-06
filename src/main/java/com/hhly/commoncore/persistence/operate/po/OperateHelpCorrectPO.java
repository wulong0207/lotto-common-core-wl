package com.hhly.commoncore.persistence.operate.po;

import java.io.Serializable;
import java.util.Date;

/**
 * @desc
 * @author cheng chen
 * @date 2017年4月21日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class OperateHelpCorrectPO implements Serializable {
   
	private static final long serialVersionUID = -2400945895044993671L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;
    
    /**
     * 手机号
     */
    private String cusMobile;

    /**
     * 帮助文章ID
     */
    private Integer helpId;
    
    /**
     * 帮助栏目ID
     */
    private Integer helpTypeId;

    /**
     * 来源
     */
    private String sources;

    /**
     * 纠正内容/意见建议
     */
    private String correct;

    /**
     * 处理状态:0未处理;1已经处理
     */
    private Integer status;
    
    /**
     * 类型:1纠正内容;2意见建议
     */
    private Integer type;

    /**
     * null
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;
    /**
     * 修改人
     */
    private String modifyBy;

    /**
     * null
     */
    private Date updateTime;

    /**
     * null
     */
    private Date createTime;

    /**
     * null
     */
    private Date modifyTime;

    /**
     * 主键ID
     * @return ID 主键ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 主键ID
     * @param id 主键ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 用户ID
     * @return USER_ID 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 用户ID
     * @param userId 用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    /**
     * 手机号
     * @return
     */
    public String getCusMobile() {
		return cusMobile;
	}

    /**
     * 手机号
     * @param cusMobile
     */
	public void setCusMobile(String cusMobile) {
		this.cusMobile = cusMobile;
	}

	/**
     * 帮助文章ID
     * @return HELP_ID 帮助文章ID
     */
    public Integer getHelpId() {
        return helpId;
    }

    /**
     * 帮助文章ID
     * @param helpId 帮助文章ID
     */
    public void setHelpId(Integer helpId) {
        this.helpId = helpId;
    }
    
    /**
     * 帮助栏目ID
     * @return
     */
    public Integer getHelpTypeId() {
		return helpTypeId;
	}

    /**
     * 帮助栏目ID
     * @param helpTypeId
     */
	public void setHelpTypeId(Integer helpTypeId) {
		this.helpTypeId = helpTypeId;
	}

	/**
     * 来源
     * @return SOURCES 来源
     */
    public String getSources() {
        return sources;
    }

    /**
     * 来源
     * @param sources 来源
     */
    public void setSources(String sources) {
        this.sources = sources;
    }

    /**
     * 纠正内容/意见建议
     * @return CORRECT 纠正内容/意见建议
     */
    public String getCorrect() {
        return correct;
    }

    /**
     * 纠正内容/意见建议
     * @param correct 纠正内容/意见建议
     */
    public void setCorrect(String correct) {
        this.correct = correct == null ? null : correct.trim();
    }

    /**
     * 处理状态:0未处理;1已经处理
     * @return STATUS 处理状态:0未处理;1已经处理
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 处理状态:0未处理;1已经处理
     * @param status 处理状态:0未处理;1已经处理
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
       
    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	/**
     * null
     * @return REMARK null
     */
    public String getRemark() {
        return remark;
    }

    /**
     * null
     * @param remark null
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
        
    public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
     * null
     * @return MODIFY_BY null
     */
    public String getModifyBy() {
        return modifyBy;
    }

    /**
     * null
     * @param modifyBy null
     */
    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    /**
     * null
     * @return UPDATE_TIME null
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * null
     * @param updateTime null
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * null
     * @return CREATE_TIME null
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * null
     * @param createTime null
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * null
     * @return MODIFY_TIME null
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * null
     * @param modifyTime null
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
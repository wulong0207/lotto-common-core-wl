package com.hhly.commoncore.base.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.exception.Assert;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelInfoBO;

/**
 * 积分兑换渠道工具类，获取渠道登录信息
 *
 * @author huangchengfang1219
 * @date 2018年3月24日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Component("coOperateChannelUtil")
public class CoOperateChannelUtil {

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 从缓存中获取渠道ID
	 * 
	 * @param token
	 * @return
	 */
	public String getChannelIdByToken(String token) {
		return redisUtil.getString(getTokenKey(token));
	}

	/**
	 * 从缓存中获取用户信息
	 * 
	 * @param token
	 * @return
	 */
	public CoOperateChannelInfoBO getChannelByToken(String token) {
		String channelId = redisUtil.getString(getTokenKey(token));
		if (ObjectUtil.isBlank(channelId)) {
			return null;
		}
		CoOperateChannelInfoBO channelInfo = (CoOperateChannelInfoBO) redisUtil.getObj(getChannelIdKey(channelId));
		if (channelInfo == null) {
			clearChannelToken(token);
			return null;
		}
		return channelInfo;
	}

	/**
	 * 根据用户ID获取用户缓存信息
	 * 
	 * @param channelId
	 * @return
	 */
	public CoOperateChannelInfoBO getChannelById(String channelId) {
		return (CoOperateChannelInfoBO) redisUtil.getObj(getChannelIdKey(channelId));
	}

	/**
	 * 清除缓存的token
	 * 
	 * @param token
	 */
	public void clearChannelToken(String token) {
		redisUtil.delString(getTokenKey(token));
	}

	/**
	 * 清除用户缓存信息
	 * 
	 * @param channelId
	 */
	public void clearChannelById(String channelId) {
		redisUtil.delObj(getChannelIdKey(channelId));
	}

	/**
	 * 使用token缓存用户信息
	 * 
	 * @param token
	 * @param CoOperateChannelInfoBO
	 */
	public void addChannelCacheByToken(String token, CoOperateChannelInfoBO CoOperateChannelInfoBO, long seconds) {
		Assert.paramNotNull(CoOperateChannelInfoBO.getChannelId(), "id");
		redisUtil.addString(getTokenKey(token), CoOperateChannelInfoBO.getChannelId(), seconds);
		redisUtil.addObj(getChannelIdKey(CoOperateChannelInfoBO.getChannelId()), CoOperateChannelInfoBO, CacheConstants.ONE_DAY);
	}

	private String getTokenKey(String token) {
		return CacheConstants.C_CORE_COOPERATE_CHANNEL_INFO_TOKEN + token;
	}

	private String getChannelIdKey(String channelId) {
		return CacheConstants.C_CORE_COOPERATE_CHANNEL_INFO_ID + channelId;
	}
}

package com.hhly.commoncore.remote.cache.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.base.util.UserUtil;
import com.hhly.commoncore.remote.cache.service.IUserInfoCacheService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.constants.MessageCodeConstants;
import com.hhly.skeleton.base.exception.ResultJsonException;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.user.bo.UserInfoBO;

/**
 * 操作用户相关缓存数据接口服务
 * 
 * @author longguoyou
 * @date 2017年5月16日
 * @compay 益彩网络科技有限公司
 */
@Service("userInfoCacheService")
public class UserInfoCacheServiceImpl implements IUserInfoCacheService {

	@Autowired
	private UserUtil userUtil;

	@Override
	public ResultBO<?> checkToken(String token) {
		if (ObjectUtil.isBlank(token))
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.TOKEN_IS_NULL_FIELD));
		UserInfoBO userInfoBO = userUtil.getUserByToken(token);
		if (ObjectUtil.isBlank(userInfoBO))
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.TOKEN_LOSE_SERVICE));
		return ResultBO.ok(userInfoBO);
	}
}

package com.hhly.commoncore.remote.cooperate.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.base.util.CoOperateChannelUtil;
import com.hhly.commoncore.base.util.RedisUtil;
import com.hhly.commoncore.persistence.cooperate.dao.CoOperateChannelDaoMapper;
import com.hhly.commoncore.persistence.cooperate.dao.CoOperateChannelLoginLogDaoMapper;
import com.hhly.commoncore.persistence.cooperate.po.CoOperateChannelLoginLogPO;
import com.hhly.commoncore.persistence.cooperate.po.CoOperateChannelPO;
import com.hhly.commoncore.remote.cooperate.service.ICoOperateChannelLoginService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.common.CoOperateEnum;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.constants.CoOperateConstants;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.constants.MessageCodeConstants;
import com.hhly.skeleton.base.exception.ResultJsonException;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.EncryptUtil;
import com.hhly.skeleton.base.util.RegularValidateUtil;
import com.hhly.skeleton.base.util.TokenUtil;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelInfoBO;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelLoginErrorBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateChannelInfoVO;

@Service("coOperateChannelLoginService")
public class CoOperateChannelLoginServiceImpl implements ICoOperateChannelLoginService {

	private static final Logger logger = LoggerFactory.getLogger(CoOperateChannelLoginServiceImpl.class);

	@Autowired
	private CoOperateChannelDaoMapper coOperateChannelDaoMapper;
	@Autowired
	private CoOperateChannelLoginLogDaoMapper coOperateChannelLoginLogDaoMapper;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private CoOperateChannelUtil coOperateChannelUtil;

	@Override
	public CoOperateChannelInfoBO doLogin(CoOperateChannelInfoVO vo) {
		CoOperateChannelBO channelBO = coOperateChannelDaoMapper.findByChannelName(vo.getChannelName());
		validateChannelLogin(vo, channelBO);
		CoOperateChannelInfoBO infoBO = new CoOperateChannelInfoBO();
		infoBO.setChannelId(channelBO.getChannelId());
		infoBO.setChannelName(channelBO.getChannelName());
		// 渠道商户类型
		infoBO.setMerchantType(CoOperateEnum.MerchantType.getMerchantTypeValue(channelBO));
		// 判断是否是初始密码
		if (EncryptUtil.createDefaultPassword(channelBO.getRcode()).equals(channelBO.getPassword())) {
			infoBO.setIsMandatoryPassword(Constants.YES);
		} else {
			infoBO.setIsMandatoryPassword(Constants.NO);
		}
		// 查询最后登录时间
		CoOperateChannelLoginLogPO lastLoginLogPO = coOperateChannelLoginLogDaoMapper.findLastByChannelId(channelBO.getChannelId());
		if (lastLoginLogPO != null) {
			infoBO.setLastLoginTime(lastLoginLogPO.getLoginTime());
			infoBO.setLastLoginIp(lastLoginLogPO.getIp());
		}
		// 创建token
		String token = TokenUtil.createTokenStr(vo.getPlatform());
		infoBO.setChannelToken(token);
		coOperateChannelUtil.addChannelCacheByToken(token, infoBO, CacheConstants.TWO_HOURS);
		// 记录登录日志
		CoOperateChannelLoginLogPO thisLoginLogPO = new CoOperateChannelLoginLogPO();
		thisLoginLogPO.setChannelId(channelBO.getChannelId());
		thisLoginLogPO.setIp(vo.getIp());
		thisLoginLogPO.setPlatform(vo.getPlatform());
		coOperateChannelLoginLogDaoMapper.insert(thisLoginLogPO);
		return infoBO;
	}

	/**
	 * 校验渠道信息
	 * 
	 * @param vo
	 * @param channelBO
	 */
	private void validateChannelLogin(CoOperateChannelInfoVO vo, CoOperateChannelBO channelBO) {
		if (channelBO == null) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.COOPERATE_CHANNEL_NOT_FOUND));
		}
		// 校验密码错误次数
		String loginErrorKey = CacheConstants.C_CORE_COOPERATE_CHANNEL_ERROR + channelBO.getChannelId();
		CoOperateChannelLoginErrorBO loginErrorBO = (CoOperateChannelLoginErrorBO) redisUtil.getObj(loginErrorKey);
		if (loginErrorBO != null && !DateUtil.isToday(loginErrorBO.getCacheTime())) {
			loginErrorBO = null;
			redisUtil.delObj(loginErrorKey);
		}
		int loginErrorCount = loginErrorBO == null ? Constants.NUM_0 : loginErrorBO.getCount();
		if (loginErrorCount >= CoOperateConstants.MAX_LOGIN_ERROR_COUNT) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.COOPERATE_CHANNEL_PASSWORD_ERROR_OVER));
		}
		// 校验密码
		try {
			if (!EncryptUtil.encrypt(vo.getPassword(), channelBO.getRcode()).equals(channelBO.getPassword())) {
				// 密码错误10次时返回错误10次，账号禁用，否则返回密码错误
				String errorCode = (++loginErrorCount >= CoOperateConstants.MAX_LOGIN_ERROR_COUNT)
						? MessageCodeConstants.COOPERATE_CHANNEL_PASSWORD_ERROR_OVER
						: MessageCodeConstants.COOPERATE_CHANNEL_PASSWORD_ERROR;
				if (loginErrorBO == null) {
					loginErrorBO = new CoOperateChannelLoginErrorBO();
				}
				loginErrorBO.setCount(loginErrorCount);
				loginErrorBO.setCacheTime(new Date());
				redisUtil.addObj(loginErrorKey, loginErrorBO, CacheConstants.ONE_DAY);// 记录错误次数
				throw new ResultJsonException(ResultBO.err(errorCode));
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			logger.error("登录密码校验失败:{}, {}", channelBO.getPassword(), vo.getPassword());
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.COOPERATE_CHANNEL_PASSWORD_ERROR));
		}
		// 合作时间范围外和禁用的渠道都返回渠道已禁用
		Date nowTime = new Date();
		if (channelBO.getCoopStartTime() != null && channelBO.getCoopStartTime().after(nowTime)) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.COOPERATE_CHANNEL_FORBID));
		}
		if (channelBO.getCoopEndTime() != null && channelBO.getCoopEndTime().before(nowTime)) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.COOPERATE_CHANNEL_FORBID));
		}
		if (channelBO.getChannelStatus() != null && channelBO.getChannelStatus().shortValue() != Constants.YES) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.COOPERATE_CHANNEL_FORBID));
		}
	}

	@Override
	public void doLogout(String token) {
		coOperateChannelUtil.clearChannelToken(token);
	}

	@Override
	public void updatePassword(CoOperateChannelInfoVO vo) {
		// 登录验证
		CoOperateChannelInfoBO infoBO = coOperateChannelUtil.getChannelByToken(vo.getChannelToken());
		if (infoBO == null) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.TOKEN_LOSE_SERVICE));
		}
		// 密码格式校验，新密码应该为加密后的128为加密字符串
		if (!vo.getNewPassword().matches(RegularValidateUtil.REGULAR_PASSWORD)) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.PASSWORD_FORMAT_ERROR_FIELD));
		}
		// 密码验证
		CoOperateChannelBO channelBO = coOperateChannelDaoMapper.findByChannelId(infoBO.getChannelId());
		if (channelBO == null) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.TOKEN_LOSE_SERVICE));
		}
		try {
			if (!EncryptUtil.encrypt(vo.getPassword(), channelBO.getRcode()).equals(channelBO.getPassword())) {
				throw new ResultJsonException(ResultBO.err(MessageCodeConstants.COOPERATE_CHANNEL_OLD_PASSOWRD_ERROR));
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			logger.error("原密码校验失败:{}, {}", channelBO.getPassword(), vo.getPassword());
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.COOPERATE_CHANNEL_OLD_PASSOWRD_ERROR));
		}
		try {
			// 修改密码
			String rcode = EncryptUtil.getSalt();
			String newPassword = EncryptUtil.encrypt(vo.getNewPassword(), rcode);
			CoOperateChannelPO channelPO = new CoOperateChannelPO();
			channelPO.setId(channelBO.getId());
			channelPO.setPassword(newPassword);
			channelPO.setRcode(rcode);
			coOperateChannelDaoMapper.updatePassword(channelPO);
			// 更新缓存
			if (EncryptUtil.createDefaultPassword(rcode).equals(newPassword)) {
				infoBO.setIsMandatoryPassword(Constants.YES);
			} else {
				infoBO.setIsMandatoryPassword(Constants.NO);
			}
			coOperateChannelUtil.addChannelCacheByToken(vo.getChannelToken(), infoBO, CacheConstants.TWO_HOURS);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			logger.error("修改密码失败:{}", channelBO.getChannelId(), e);
			throw new RuntimeException("修改密码失败:" + channelBO.getChannelId(), e);
		}
	}

	@Override
	public CoOperateChannelInfoBO findByToken(String token) {
		CoOperateChannelInfoBO infoBO = coOperateChannelUtil.getChannelByToken(token);
		if (infoBO == null) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.TOKEN_LOSE_SERVICE));
		}
		return infoBO;
	}

}

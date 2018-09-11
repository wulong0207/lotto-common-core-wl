package com.hhly.commoncore.http.usercore.passport.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hhly.commoncore.http.usercore.passport.ThirdPartyLoginService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.exception.ResultJsonException;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.user.bo.UserInfoBO;
import com.hhly.skeleton.user.vo.TPInfoVO;

@Service("thirdPartyLoginService")
public class ThirdPartyLoginServiceImpl implements ThirdPartyLoginService {

	private static final Logger logger = LoggerFactory.getLogger(ThirdPartyLoginServiceImpl.class);
	@Autowired
	private RestTemplate restTemplate;
	@Value("${user_core_url}")
	private String userCoreUrl;

	@Override
	public UserInfoBO tpChannelLogin(TPInfoVO tpInfoVO) {
		String jsonStr = restTemplate.postForEntity(userCoreUrl + "passport/login/channel", tpInfoVO, String.class).getBody();
		ResultBO<UserInfoBO> result = JsonUtil.jsonToJackObject(jsonStr, new TypeReference<ResultBO<UserInfoBO>>() {
		});
		if (!result.isOK()) {
			logger.error("渠道登录接口调用失败:{}", result.getMessage());
			throw new ResultJsonException(result);
		}
		logger.debug("渠道登录成功:{}", result.getData().getId());
		return result.getData();
	}

}

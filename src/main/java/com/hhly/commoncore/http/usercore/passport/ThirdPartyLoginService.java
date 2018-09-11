package com.hhly.commoncore.http.usercore.passport;

import com.hhly.skeleton.user.bo.UserInfoBO;
import com.hhly.skeleton.user.vo.TPInfoVO;

/**
 * 调用user-core第三方登录相关接口
 *
 * @author huangchengfang1219
 * @date 2018年3月30日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface ThirdPartyLoginService {

	/**
	 * 第三方-渠道登录
	 * 
	 * @param tpInfoVO
	 * @return
	 * @throws Exception
	 * @date 2017年7月8日下午5:17:45
	 * @author cheng.chen
	 */
	UserInfoBO tpChannelLogin(TPInfoVO tpInfoVO);

}

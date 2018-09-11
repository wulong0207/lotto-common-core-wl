package com.hhly.commoncore.remote.cooperate.service;

import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateChannelInfoBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateChannelInfoVO;

/**
 * 渠道登录接口
 *
 * @author huangchengfang1219
 * @date 2018年3月24日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface ICoOperateChannelLoginService {

	/**
	 * 用户登录
	 * 
	 * @param vo
	 * @return
	 */
	CoOperateChannelInfoBO doLogin(CoOperateChannelInfoVO vo);

	/**
	 * 用户退出登录
	 * 
	 * @param token
	 */
	void doLogout(String token);
	
	/**
	 * 修改密码
	 * @param vo
	 */
	void updatePassword(CoOperateChannelInfoVO vo);
	
	/**
	 * 根据token获取用户信息
	 * @param token
	 * @return
	 */
	CoOperateChannelInfoBO findByToken(String token);

}

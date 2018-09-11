package com.hhly.commoncore.remote.operate.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.base.util.MoneyFormatUtil;
import com.hhly.commoncore.base.util.RedisUtil;
import com.hhly.commoncore.persistence.dic.dao.DicDataDetailDaoMapper;
import com.hhly.commoncore.persistence.operate.dao.OperateMarketChannelDaoMapper;
import com.hhly.commoncore.persistence.operate.dao.OperateSoftwareVersionDaoMapper;
import com.hhly.commoncore.persistence.order.dao.OrderInfoDaoMapper;
import com.hhly.commoncore.remote.operate.service.IOperateService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.common.LotteryEnum.Lottery;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.constants.UserConstants;
import com.hhly.skeleton.base.model.DicDataEnum;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.StringUtil;
import com.hhly.skeleton.lotto.base.dic.bo.DicDataDetailBO;
import com.hhly.skeleton.lotto.base.dic.vo.DicDataDetailVO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateLottSoftwareVersionBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateLottSoftwareVersionVO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateSoftwareVersionVO;
import com.hhly.skeleton.lotto.base.order.bo.UserWinInfoBO;


/**
 * @desc 运营管理服务实现类
 * @author lidecheng
 * @date 2017年3月8日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service("operateService")
public class OperateServiceImpl  implements IOperateService{
	@Autowired
	private OrderInfoDaoMapper orderInfoDaoMapper;		
	@Autowired
	private OperateSoftwareVersionDaoMapper operateSoftwareVersionDaoMapper;
	@Autowired
    private RedisUtil objectRedisUtil;
	@Autowired 
	private OperateMarketChannelDaoMapper operateMarketChannelDaoMapper;
	
	@Autowired
	private DicDataDetailDaoMapper dicDataDetailDaoMapper;
	@Value("${before_file_url}")
	protected String beforeFileUrl;
	
	@Value("${mobile_tag_file_url}")
	protected String mobileTagFileUrl;

	@Override
	public ResultBO<OperateLottSoftwareVersionBO> findNewVersion(OperateLottSoftwareVersionVO vo) {	
		// 返回能'同步官方'的版本,能同步官方的只有一个
		//  0：否；1：是
		vo.setSynOfficial(1);
		OperateLottSoftwareVersionBO versionBO  = operateSoftwareVersionDaoMapper.findNewVersion(vo);
		return new ResultBO<OperateLottSoftwareVersionBO>(versionBO);
	}	
	
	@Override
	public OperateLottSoftwareVersionBO findSoftwareVersionSingle(OperateSoftwareVersionVO vo) {
		return operateSoftwareVersionDaoMapper.findSingle(vo);
	}
	
	/**
	 * 查询中奖状态
	 * @return
	 */
	public ResultBO<Integer> findOpenStatus(){
		DicDataDetailVO dicDataDetailVO = new DicDataDetailVO();
		dicDataDetailVO.setDicCode(DicDataEnum.OPEN_STATUS.getDicCode());
		DicDataDetailBO dicBO =  dicDataDetailDaoMapper.findSingle(dicDataDetailVO);
		Integer status = Integer.valueOf(dicBO.getDicDataValue());
		return  ResultBO.ok(status);
	}
	/**
	 * 首页中奖轮播信息
	 * @return
	 */
	public ResultBO<List<String>> findUserWinInfo() {
		List<UserWinInfoBO> winInfoList = orderInfoDaoMapper.queryUserWinInfo();
		List<String> list = new ArrayList<>();
		for (int i = 0; i<winInfoList.size(); i++) {
			StringBuffer str = new StringBuffer();
			if (!ObjectUtil.isBlank(winInfoList.get(i).getNickName()) && !ObjectUtil.isBlank(winInfoList.get(i).getPreBonus()) && !ObjectUtil.isBlank(winInfoList.get(i).getLotteryName())) {
				str.append(UserConstants.CONGRATULATION );
				str.append(StringUtil.encrptionNickname(winInfoList.get(i).getNickName()));
				str.append(UserConstants.IN);
				if (winInfoList.get(i).getLotteryCode().equals(Lottery.FB.getName())) {
					str.append(Lottery.FB.getDesc());
				} else if (winInfoList.get(i).getLotteryCode().equals(Lottery.BB.getName())) {
					str.append(Lottery.BB.getDesc());
				} else {
					str.append(winInfoList.get(i).getLotteryName());
				}
				str.append(UserConstants.WIN );
				str.append(MoneyFormatUtil.dispose(winInfoList.get(i).getPreBonus()));
				list.add(str.toString());
			}
		}
		return ResultBO.ok(list);
	}

	/**
	 * 查询渠道版本信息
	 * @param vo
	 * @return
	 */
	@Override
	public ResultBO<OperateLottSoftwareVersionBO> findVersionByChannelId(OperateLottSoftwareVersionVO vo) {
		OperateLottSoftwareVersionBO bo = operateMarketChannelDaoMapper.findVersionByChannel(vo);
		if(bo==null||ObjectUtil.isBlank(bo.getWapAppUrl())){
			vo.setChannelId(Constants.BASE_CHINAL);
			bo = operateMarketChannelDaoMapper.findVersionByChannel(vo);
		}
		bo.setWapAppUrl(mobileTagFileUrl+bo.getWapAppUrl());
		return ResultBO.ok(bo);
	}
}
package com.hhly.commoncore.remote.operate.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.base.util.RedisUtil;
import com.hhly.commoncore.persistence.dic.dao.DicDataDetailDaoMapper;
import com.hhly.commoncore.persistence.operate.dao.OperateAdDaoMapper;
import com.hhly.commoncore.remote.operate.service.IOperateAdService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.constants.MessageCodeConstants;
import com.hhly.skeleton.base.exception.ResultJsonException;
import com.hhly.skeleton.base.model.DicDataEnum;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.StringUtil;
import com.hhly.skeleton.lotto.base.dic.bo.DicDataDetailBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateAdLottoBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateAdVO;
import com.hhly.skeleton.user.bo.UserInfoBO;

/**
 * @desc 广告管理服务实现类
 * @author lidecheng
 * @date 2017年3月8日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service("operateAdService")
public class OperateAdServiceImpl  implements IOperateAdService{
	@Autowired
	private OperateAdDaoMapper operateAdDaoMapper;

	@Autowired
    private RedisUtil objectRedisUtil;
	
	@Autowired
	private DicDataDetailDaoMapper dicDataDetailDaoMapper;
	
	@Value("${before_file_url}")
	protected String beforeFileUrl;

	/*****************查询广告信息*************************/	
	@Override	
	public ResultBO<List<OperateAdLottoBO>> findHomeOperAd(OperateAdVO vo) {		
		List<OperateAdLottoBO> lottoList = operateAdDaoMapper.findHomeOperAd(vo);
		List<OperateAdLottoBO> defaultList = operateAdDaoMapper.findDefaultAd(vo);
		List<DicDataDetailBO> datalist = dicDataDetailDaoMapper.findSimple(DicDataEnum.OPERATE_AD_POSITION.getDicCode());
		getNoAdList(datalist,lottoList);
		setDetailAdList(datalist,defaultList,lottoList);		
		//添加key
		for(OperateAdLottoBO bo:lottoList){
			bo.setAdImgUrl(beforeFileUrl+bo.getAdImgUrl());
		}
		return new ResultBO<List<OperateAdLottoBO>>(lottoList);
	}
	/**
	 * 获取没有配置的广告类型
	 * @param lottoList
	 */
	void getNoAdList(List<DicDataDetailBO> dataList,List<OperateAdLottoBO> lottoList){
		List<DicDataDetailBO> newDataList = new ArrayList<DicDataDetailBO>();
		for(int i=0;i<dataList.size();i++){
			String value = dataList.get(i).getDicDataValue();
			for(OperateAdLottoBO lottoBO: lottoList){
				if(value.equals(lottoBO.getPosition().toString())){
					newDataList.add(dataList.get(i));
				}
			}
		}
		dataList.removeAll(newDataList);
	}
	
	/**
	 * 将没有配置的广告类型从默认广告中取出赋值给前端
	 * @param lottoList
	 */
	void setDetailAdList(List<DicDataDetailBO> dataList,List<OperateAdLottoBO> defaultList,  List<OperateAdLottoBO> lottoList){
		for(int i=0;i<dataList.size();i++){
			String value = dataList.get(i).getDicDataValue();
			for(OperateAdLottoBO detailBO: defaultList){
				if(value.equals(detailBO.getPosition().toString())){
					lottoList.add(detailBO);
				}
			}
		}
	}
}
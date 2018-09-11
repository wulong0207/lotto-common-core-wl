package com.hhly.commoncore.local.lotto.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.base.util.RedisUtil;
import com.hhly.commoncore.local.lotto.service.LotteryService;
import com.hhly.commoncore.persistence.lottery.dao.LotteryTypeDaoMapper;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.lotto.base.lottery.bo.LotteryBO;
import com.hhly.skeleton.lotto.base.lottery.bo.LotteryTypeBO;
import com.hhly.skeleton.lotto.base.lottery.vo.LotteryVO;

/**
 * @author lgs on
 * @version 1.0
 * @desc 彩种服务实行类
 * @date 2017/4/28.
 * @company 益彩网络科技有限公司
 */
@Service("lotteryService")
public class LotteryServiceImpl implements LotteryService {

	@Autowired
	private LotteryTypeDaoMapper lotteryTypeDaoMapper;

	@Value("${before_file_url}")
	protected String beforeFileUrl;
	@Autowired
	private RedisUtil objectRedisUtil;

	/***
	 * 查询高频彩当天最大截止销售时间
	 * 
	 * @param lotteryCode
	 * @return
	 */
	@Override
	public Date findMaxEndDrawTime(int lotteryCode) {
		String key = CacheConstants.C_COMM_LOTTERY_FIND_HIGH_MAX_ENDDRAWTIME + lotteryCode;
		Date date = objectRedisUtil.getObj(key, new Date());
		if (date == null) {
			List<LotteryTypeBO> list = lotteryTypeDaoMapper.findAllLotteryType();
			for (LotteryTypeBO bo : list) {
				if (bo.getLotteryCode() == lotteryCode) {
					String endSailTime = bo.getEndSailTime();// 官方截止销售时间
																// 1|00:05|0,2|00:05|0,3|00:05|0,4|00:05|0,5|00:05|0,6|00:05|0,7|00:05|0
					String sailDayCycle = bo.getSailDayCycle();// 销售日销售周期
																// 1-23|300,24-24|29100,25-96|600,97-120|300
					Integer buyEndTime = bo.getBuyEndTime();// 官方截止时间距离的秒数
					if (endSailTime == null || sailDayCycle == null || buyEndTime == null) {
						break;
					}
					int week = DateUtil.dayForWeek();
					String startTime = ""; // 起始时间
					int seconds = 0;// 总共耗时多少秒
					// 获取符合条件的起始时间
					String endSailTimes[] = endSailTime.split(SymbolConstants.COMMA);
					for (String sailStr : endSailTimes) {
						String sailStrs[] = sailStr.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
						if (Integer.valueOf(sailStrs[0]) == week) {
							startTime = sailStrs[1];
							break;
						}
					}
					// 获取所有的时间段信息
					String sailDayCycles[] = sailDayCycle.split(SymbolConstants.COMMA);
					for (String sailDayStr : sailDayCycles) {
						String sailDayStrs[] = sailDayStr.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
						int second = Integer.valueOf(sailDayStrs[1]);// 时间
						String nums[] = sailDayStrs[0].split(SymbolConstants.TRAVERSE_SLASH);
						int startNum = Integer.valueOf(nums[0]);// 起始期数
						int endNum = Integer.valueOf(nums[1]);// 结束期数
						seconds += (endNum - startNum + 1) * second;
					}
					seconds += buyEndTime;
					String startTimes[] = startTime.split(SymbolConstants.COLON);
					Date startdate = DateUtil.getDaySetTime(Integer.valueOf(startTimes[0]), Integer.valueOf(startTimes[1]), 0);
					date = DateUtil.addSecond(startdate, seconds);
					objectRedisUtil.addObj(key, date, (long) Constants.DAY_1);
					break;
				}
			}

		}

		return date;

	}

	@Override
	public List<LotteryBO> queryLotterySelectList(LotteryVO vo) {
		return lotteryTypeDaoMapper.queryLotterySelectList(vo);
	}

}

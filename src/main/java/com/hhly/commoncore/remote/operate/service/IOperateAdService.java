package com.hhly.commoncore.remote.operate.service;

import java.util.List;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateAdLottoBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateAdVO;

/**
 * @desc 广告管理服务
 * @author lidecheng
 * @date 2017年3月8日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface IOperateAdService {	

	/**查询主页广告信息
	 * @param platform
	 * @return
	 */
	ResultBO<List<OperateAdLottoBO>> findHomeOperAd(OperateAdVO vo);
}

package com.hhly.commoncore.controller.sport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.commoncore.local.sport.service.ISportDataService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.lotto.base.sport.vo.SportNewestDataVO;

/**
 * 查询竞彩最新数据
 *
 * @author huangchengfang1219
 * @date 2018年8月7日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RestController
@RequestMapping("/sport/newest")
public class SportNewestController {
	
	@Autowired
	private ISportDataService sportDataService;

	
	/**
	 * 竞彩足球
	 * @return
	 */
	@RequestMapping(value="football")
	public Object football(SportNewestDataVO vo) {
		return ResultBO.ok(sportDataService.findFbNewestData(vo));
	}
}

package com.hhly.commoncore.controller.sport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.commoncore.remote.sport.service.ISportMatchService;
import com.hhly.skeleton.base.bo.ResultBO;

@RestController
@RequestMapping("/sport/match")
public class SportMatchController {

	@Autowired
	private ISportMatchService sportMatchService;

	@RequestMapping("/races/{lotteryCode}")
	public Object races(@PathVariable Integer lotteryCode) {
		return ResultBO.ok(sportMatchService.findRaceList(lotteryCode));
	}
}

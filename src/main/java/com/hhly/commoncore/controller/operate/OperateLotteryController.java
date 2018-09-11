package com.hhly.commoncore.controller.operate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.commoncore.remote.operate.service.IOperateLotteryService;
import com.hhly.skeleton.lotto.base.operate.vo.OperateLotteryLottVO;

@RestController
@RequestMapping("/operate/lottery")
public class OperateLotteryController {

	@Autowired
	private IOperateLotteryService operateLotteryService;

	@RequestMapping(value = "/home", method = RequestMethod.POST)
	public Object findHomeOperLottery(@RequestBody OperateLotteryLottVO vo) {
		return operateLotteryService.findHomeOperLottery(vo);
	}
}

package com.hhly.commoncore.controller.operate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.commoncore.remote.operate.service.IOperateArticleService;
import com.hhly.skeleton.lotto.base.operate.vo.OperateArticleLottVO;

@RestController
@RequestMapping("/operate/article")
public class OperateArticleController {

	@Autowired
	private IOperateArticleService operateArticleService;

	@RequestMapping(value = "news/bytypes", method = RequestMethod.POST)
	public Object findNewsByTypeList(@RequestBody List<OperateArticleLottVO> voList) {
		return operateArticleService.findNewsByTypeList(voList);
	}
}


package com.hhly.commoncore.persistence.dic.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.hhly.skeleton.lotto.base.dic.bo.DicDataDetailBO;
import com.hhly.skeleton.lotto.base.dic.vo.DicDataDetailVO;


/**
 * @desc    
 * @author  cheng chen
 * @date    2017年6月5日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)    
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager") 
public class DicDataDetailDaoMapperTest {
	
	@Autowired
	DicDataDetailDaoMapper dicDataDetailDaoMapper;


	@Test
	public void testFindSimple() {
		String code = "101";
		List<DicDataDetailBO> list = dicDataDetailDaoMapper.findSimple(code);
		for (DicDataDetailBO bo : list) {
			System.out.println("dicCode : " + bo.getDicCode() + " dicName : " + bo.getDicDataName());
		}
	}




	@Test
	public void testFindSingle() {
		DicDataDetailVO vo = new DicDataDetailVO();
		vo.setId(6);
		DicDataDetailBO bo = dicDataDetailDaoMapper.findSingle(vo);
		System.out.println("data : " + JSONObject.toJSONString(bo));
	}

}

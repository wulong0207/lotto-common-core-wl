package com.hhly.commoncore.persistence.operate.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hhly.skeleton.lotto.base.operate.bo.ClickBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateArticleBaseBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateArticleLottBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateArticleLottVO;

@RunWith(SpringJUnit4ClassRunner.class)    
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class OperateArticleDaoMapperTest {

	@Autowired
	private OperateArticleDaoMapper mapper;
	
	@Test
	public void testFindArticleByTop() {
		OperateArticleLottVO vo = new OperateArticleLottVO();
		vo.setStartRow(1);
		vo.setPageSize(10);
		List<OperateArticleLottBO> bos = mapper.findArticleByTop(vo);
		System.out.println(bos);
	}

	@Test
	public void testFindNewArticle() {
		OperateArticleLottVO vo = new OperateArticleLottVO();
		vo.setStartRow(1);
		vo.setPageSize(10);
		List<OperateArticleLottBO> bos = mapper.findNewArticle(vo);
		System.out.println(bos);
	}

	@Test
	public void testFindArticle() {
		OperateArticleLottVO vo = new OperateArticleLottVO();
		vo.setId(200L);
		OperateArticleLottBO bo = mapper.findArticle(vo);
		System.out.println(bo);
	}

	@Test
	public void testFindArticleByTypeList() {
		OperateArticleLottVO vo = new OperateArticleLottVO();
		vo.setTypeCode("1.1");
		OperateArticleLottVO vo2 = new OperateArticleLottVO();
		vo2.setTypeCode("1.2");
		List<OperateArticleLottVO> vos = new ArrayList<>();
		vos.add(vo);
		vos.add(vo2);
		List<OperateArticleBaseBO> bos = mapper.findArticleByTypeList(vos);
		System.out.println(bos);
	}

	@Test
	public void testFindNewsByTypeList() {
		OperateArticleLottVO vo = new OperateArticleLottVO();
		vo.setTypeCode("1.1");
		OperateArticleLottVO vo2 = new OperateArticleLottVO();
		vo2.setTypeCode("1.2");
		List<OperateArticleLottVO> vos = new ArrayList<>();
		vos.add(vo);
		vos.add(vo2);
		List<OperateArticleLottBO> bos = mapper.findNewsByTypeList(vos);
		System.out.println(bos);
	}


	@Test
	public void testUpdateClickList() {
		ClickBO bo = new ClickBO();
		ClickBO bo2 = new ClickBO();
		List<ClickBO> bos = new ArrayList<>();
		bos.add(bo);
		bos.add(bo2);
		int ret = mapper.updateClickList(bos);
		System.out.println(ret);
	}

	@Test
	public void testFindArticleLabel() {
		OperateArticleLottVO vo = new OperateArticleLottVO();
		List<OperateArticleLottBO> bos = mapper.findArticleLabel(vo);
		System.out.println(bos);
	}

	@Test
	public void testFindMaxArticleId() {
		String maxArticleId = mapper.findMaxArticleId("20170605");
		System.out.println(maxArticleId);
	}

	@Test
	public void testFindSidebarNews() {
		OperateArticleLottVO vo = new OperateArticleLottVO();
		vo.setRownum(10);
		List<OperateArticleLottBO> bos = mapper.findSidebarNews(vo);
		System.out.println(bos);
	}

	@Test
	public void testFindClick() {
		List<ClickBO> bos = mapper.findClick();
		System.out.println(bos);
	}

}

package com.hhly.commoncore.remote.operate.service;

import java.util.List;
import java.util.Map;

import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateArticleBaseBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateArticleLottBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateArticleTypeLottBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateArticleUserBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateArticleLottVO;
import com.hhly.skeleton.lotto.mobile.news.bo.MobialNewsDetailPageBO;
import com.hhly.skeleton.lotto.mobile.news.bo.MobialNewsHomeBO;

/**
 * @desc 资讯文章管理服务
 * @author lidecheng
 * @date 2017年3月8日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface IOperateArticleService {	

	/**
	 * 查询pc首页新闻资讯信息
	 * @return
	 */
	ResultBO<Map<String,Object>> findPCHomeArticle();
	
	/**
	 * 查询手机首页新闻资讯信息
	 * @return
	 */
	List<OperateArticleLottBO>  findMobailHomeArticle(short platFrom);
	
	/**
	 * 根据多个栏目查询资讯信息pc主页用
	 * @param voList
	 * @return
	 */
	ResultBO<Map<String,List<OperateArticleBaseBO>>> findArticleByTypeList(List<OperateArticleLottVO> voList);
	
	/**
	 * 根据多个栏目查询资讯信息
	 * @param voList
	 * @return
	 */
	ResultBO<Map<String,List<OperateArticleLottBO>>> findNewsByTypeList(List<OperateArticleLottVO> voList);
	
	/**
	 * 根据父栏目查询子栏目信息
	 * @param typeCode
	 * @return
	 */
	ResultBO<List<OperateArticleTypeLottBO>>  findListByFaType(String typeCode);
	
	/**
	 * 查询某个栏目的资讯信息
	 * @param articleVO
	 * @return
	 */
	ResultBO<List<OperateArticleLottBO>> findArticleByTop(OperateArticleLottVO articleVO);
	 
	 /**
	  * 查询专栏信息
	  * @param vo
	  * @return
	  */
	 ResultBO<List<OperateArticleUserBO>> findExpertByCode(OperateArticleLottVO vo);			
	
	
	/**
	 * 查询标签相似的信息
	 */
	ResultBO<List<OperateArticleLottBO>>  findMobailArticleLabel(OperateArticleLottVO artVo);
	/**
	 * 更新文章点击量
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	ResultBO updateAriClick(OperateArticleLottVO vo);
	
	/**
	 * 查询资讯所有栏目信息
	 * @return
	 */
	ResultBO<List<OperateArticleBaseBO>> findNewsTypeList();
	
	/**
	 * 查询所有栏目关系
	 * @return
	 */
	ResultBO<List<OperateArticleTypeLottBO>> findNewTypeRelatList(OperateArticleLottVO artVo);	
	/**
	 * 查询侧边栏资讯头条信息 ，查询规则查当前节点和子节点下创建的信息
	 */
	ResultBO<List<OperateArticleLottBO>> findSidebarNews(OperateArticleLottVO vo);
}

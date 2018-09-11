package com.hhly.commoncore.persistence.operate.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.skeleton.lotto.base.operate.bo.ClickBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateArticleBaseBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateArticleLottBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateArticleLottVO;
import com.hhly.skeleton.lotto.base.sys.bo.CmsUserBO;

/**
 * @desc    文章管理
 * @author  Tony Wang
 * @date    2017年3月3日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface OperateArticleDaoMapper {
	

	/**
	 * 根据类型查询新闻资讯详细信息
	 * @param articleVO
	 * @return
	 */
	List<OperateArticleLottBO> findArticleByTop(OperateArticleLottVO articleVO);
	
	/**
	 * 查询最新的资讯信息
	 * @param articleVO
	 * @return
	 */
	List<OperateArticleLottBO> findNewArticle(OperateArticleLottVO articleVO);
	
	/**
	 * 查询新闻资讯详细信息
	 * @param articleVO
	 * @return
	 */
	OperateArticleLottBO findArticle(OperateArticleLottVO articleVO);
	
	/**
	 * pc主页根据多个栏目查询资讯基本信息
	 * @param voList
	 * @return
	 */
	List<OperateArticleBaseBO> findArticleByTypeList(List<OperateArticleLottVO> voList);
	
	/**
	 * 根据多个栏目查询资讯信息
	 * @param voList
	 * @return
	 */
	List<OperateArticleLottBO> findNewsByTypeList(List<OperateArticleLottVO> voList);

	
	/**
	 * 批量更新点击量
	 */
	int updateClickList(List<ClickBO> list); 
	
	/**
	 * 根据栏目类型查询该栏目下的创建用户信息
	 */
	List<CmsUserBO> findUserByArtType(OperateArticleLottVO vo);
	
	/**
	 * 查询专栏下的每个专家的资讯信息
	 * @param vo
	 * @return
	 */
	List<OperateArticleBaseBO> findArticleByUserList(List<OperateArticleLottVO> vo);
	
	

	/**
	 * 查询关键字相同情况下的资讯信息
	 * @param vo
	 * @return
	 */
	List<OperateArticleLottBO> findArticleLabel(OperateArticleLottVO vo);

	/**
	 * @desc   查询最大编号
	 * @author Tony Wang
	 * @create 2017年4月27日
	 * @param articleIdLike
	 * @return 
	 */
	String findMaxArticleId(@Param("articleIdLike") String articleIdLike);
	
	/**
	 * 查询侧边栏资讯头条信息 ，查询规则查当前节点和子节点下创建的信息
	 */
	List<OperateArticleLottBO> findSidebarNews(OperateArticleLottVO vo);
	/**
	 * 查询发布时间大于一周前的点击信息
	 * @return
	 */
	List<ClickBO> findClick();
}

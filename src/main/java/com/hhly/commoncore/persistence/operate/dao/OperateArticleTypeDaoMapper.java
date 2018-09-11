package com.hhly.commoncore.persistence.operate.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.skeleton.lotto.base.operate.bo.OperateArticleBaseBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateArticleLottBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateArticleTypeLottBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateArticleLottVO;

/**
 * @desc    文章栏目Dao
 * @author  Tony Wang
 * @date    2017年3月2日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface OperateArticleTypeDaoMapper {


	
	/**
	 * 根据栏目查询子栏目信息 
	 * @return
	 */
	List<OperateArticleTypeLottBO> findListByFaType(@Param("typeCode") String typeCode);
	
	
	/**
	 * 查询资讯所有的栏目层级关系
	 * @return
	 */
	List<OperateArticleLottBO> findNewTypeRelatList(OperateArticleLottVO artVo);
	
	/**
	 * 查询资讯所有的栏目信息
	 * @return
	 */
	List<OperateArticleBaseBO> findNewsTypeList();
}

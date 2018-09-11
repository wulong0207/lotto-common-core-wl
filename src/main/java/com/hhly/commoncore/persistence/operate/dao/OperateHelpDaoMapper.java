package com.hhly.commoncore.persistence.operate.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.skeleton.lotto.base.operate.bo.OperateHelpBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateHelpVO;

public interface OperateHelpDaoMapper {

	/**
	 * 关键字搜索
	 * 
	 * @param keyword
	 * @return
	 */
	List<String> findKeyword(OperateHelpVO vo);

	/**
	 * 搜索帮助内容总条数
	 * 
	 * @param vo
	 * @return
	 */
	int searchTotal(OperateHelpVO vo);

	/**
	 * 搜索帮助内容
	 * 
	 * @param vo
	 * @return
	 */
	List<OperateHelpBO> search(OperateHelpVO vo);

	/**
	 * 查找帮助文章总数，栏目编码typeCode必填
	 * 
	 * @param vo
	 * @return
	 */
	int findTotal(OperateHelpVO vo);

	/**
	 * 查找帮助文章列表，栏目编码typeCode必填
	 * 
	 * @param vo
	 * @return
	 */
	List<OperateHelpBO> findList(OperateHelpVO vo);

	/**
	 * 根据ID查找帮助文章内容
	 * 
	 * @param id
	 * @return
	 */
	OperateHelpBO findById(OperateHelpVO vo);

	/**
	 * 根据关键词查找标签列表
	 * 
	 * @param vo
	 * @return
	 */
	List<String> findLabelListByType(OperateHelpVO vo);

	/**
	 * 根据关键词搜索标签列表
	 * 
	 * @param vo
	 * @return
	 */
	List<String> findLabelListByKeyword(OperateHelpVO vo);

	/**
	 * 查询栏目下第一篇文章
	 * 
	 * @param vo
	 * @return
	 */
	OperateHelpBO findByTypeCode(OperateHelpVO vo);

	/**
	 * 修改文章满意度
	 * 
	 * @param able
	 */
	int updateHelpAble(@Param("id") Integer id, @Param("isAble") Short isAble);
}

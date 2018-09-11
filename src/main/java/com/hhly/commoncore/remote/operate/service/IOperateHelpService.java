package com.hhly.commoncore.remote.operate.service;

import java.util.List;

import com.hhly.skeleton.base.bo.PagingBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateHelpAbleBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateHelpBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateHelpLotteryBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateHelpTypeBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateHelpAbleVO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateHelpCorrectVO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateHelpTypeVO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateHelpVO;

/**
 * 帮助中心
 *
 * @author huangchengfang1219
 * @date 2017年11月4日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface IOperateHelpService {

	/**
	 * 查找热搜词
	 * 
	 * @return
	 */
	List<String> findHotwords(Integer num);

	/**
	 * 查找关键字，用于搜索引导
	 * 
	 * @param keyword
	 * @return
	 */
	List<String> findKeywords(OperateHelpVO vo);

	/**
	 * 查找栏目
	 * 
	 * @return
	 */
	List<OperateHelpTypeBO> findTypeTree();

	/**
	 * 查找栏目
	 * 
	 * @return
	 */
	List<OperateHelpTypeBO> findTypeList(OperateHelpTypeVO vo);

	/**
	 * 搜索帮助中心
	 * 
	 * @return
	 */
	PagingBO<OperateHelpBO> search(OperateHelpVO vo);

	/**
	 * 根据栏目编码查询栏目
	 * 
	 * @param voList
	 * @return
	 */
	List<OperateHelpTypeBO> findHelpByTypeList(List<OperateHelpVO> voList);

	/**
	 * 分页查找帮助
	 * 
	 * @param vo
	 * @return
	 */
	PagingBO<OperateHelpBO> findHelpPage(OperateHelpVO vo);

	/**
	 * 根据帮助文章ID查询帮助文章内容
	 * 
	 * @param id
	 * @return
	 */
	OperateHelpBO findHelpById(OperateHelpVO vo);

	/**
	 * 添加反馈意见
	 * 
	 * @param vo
	 * @return
	 */
	int addHelpCorrect(OperateHelpCorrectVO vo);

	/**
	 * 根据关键字或栏目查询相近词(标签)
	 * 
	 * @param vo
	 * @return
	 */
	List<String> findRelatedwords(OperateHelpVO vo);

	/**
	 * 查询彩种列表
	 * 
	 * @return
	 */
	List<OperateHelpLotteryBO> findLotteryList();

	/**
	 * 查询子栏目
	 * 
	 * @param vo
	 * @return
	 */
	OperateHelpTypeBO findChildType(String typeCode);

	/**
	 * 查找栏目下的第一篇文章
	 * 
	 * @return
	 */
	OperateHelpBO findHelpByTypeCode(OperateHelpVO vo);

	/**
	 * 查询文章用户满意状态
	 * 
	 * @param vo
	 */
	OperateHelpAbleBO findHelpAble(OperateHelpAbleVO vo);

	/**
	 * 用户满意度调查
	 * 
	 * @param vo
	 */
	void updateHelpAble(OperateHelpAbleVO vo);

}

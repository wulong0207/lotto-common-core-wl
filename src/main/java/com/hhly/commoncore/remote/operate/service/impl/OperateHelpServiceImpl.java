package com.hhly.commoncore.remote.operate.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.base.util.UserUtil;
import com.hhly.commoncore.persistence.dic.dao.DicDataDetailDaoMapper;
import com.hhly.commoncore.persistence.lottery.dao.LotteryTypeDaoMapper;
import com.hhly.commoncore.persistence.operate.dao.OperateHelpAbleLogDaoMapper;
import com.hhly.commoncore.persistence.operate.dao.OperateHelpCorrectDaoMapper;
import com.hhly.commoncore.persistence.operate.dao.OperateHelpDaoMapper;
import com.hhly.commoncore.persistence.operate.dao.OperateHelpTypeDaoMapper;
import com.hhly.commoncore.persistence.operate.po.OperateHelpAbleLogPO;
import com.hhly.commoncore.persistence.operate.po.OperateHelpCorrectPO;
import com.hhly.commoncore.remote.operate.service.IOperateHelpService;
import com.hhly.skeleton.base.bo.PagingBO;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.common.OperateHelpEnum;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.constants.HelpConstants;
import com.hhly.skeleton.base.constants.MessageCodeConstants;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.exception.Assert;
import com.hhly.skeleton.base.exception.ResultJsonException;
import com.hhly.skeleton.base.page.IPageService;
import com.hhly.skeleton.base.page.ISimplePage;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.SQLUtil;
import com.hhly.skeleton.lotto.base.dic.bo.DicDataDetailBO;
import com.hhly.skeleton.lotto.base.lottery.bo.LotteryBO;
import com.hhly.skeleton.lotto.base.lottery.vo.LotteryVO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateHelpAbleBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateHelpBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateHelpLotteryBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateHelpLotteryTypeBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateHelpTypeBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateHelpAbleVO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateHelpCorrectVO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateHelpTypeVO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateHelpVO;
import com.hhly.skeleton.user.bo.UserInfoBO;

@Service("operateHelpService")
public class OperateHelpServiceImpl implements IOperateHelpService {

	@Autowired
	private OperateHelpDaoMapper operateHelpDaoMapper;
	@Autowired
	private OperateHelpTypeDaoMapper operateHelpTypeDaoMapper;
	@Autowired
	private OperateHelpCorrectDaoMapper operateHelpCorrectDaoMapper;
	@Autowired
	private OperateHelpAbleLogDaoMapper operateHelpAbleLogDaoMapper;
	@Autowired
	private DicDataDetailDaoMapper dicDataDetailDaoMapper;
	@Autowired
	private LotteryTypeDaoMapper lotteryTypeDaoMapper;
	@Autowired
	private IPageService pageService;
	@Autowired
	private UserUtil userUtil;
	@Value("${before_file_url}")
	protected String beforeFileUrl;

	@Override
	public List<String> findHotwords(Integer num) {
		// 热搜词暂时存放数据字典中，编码2601
		List<DicDataDetailBO> datas = dicDataDetailDaoMapper.findSimple(HelpConstants.DIC_HOTEWORD);
		if (ObjectUtil.isBlank(datas)) {
			return Collections.emptyList();
		}
		if (num != null && datas.size() > num) {
			datas = datas.subList(0, num);
		}
		List<String> hotwords = new ArrayList<String>();
		for (DicDataDetailBO data : datas) {
			hotwords.add(data.getDicDataName());
		}
		return hotwords;
	}

	@Override
	public List<String> findKeywords(OperateHelpVO vo) {
		String keyword = SQLUtil.escape(vo.getKeyword());
		vo.setKeyword(keyword);
		return operateHelpDaoMapper.findKeyword(vo);
	}

	@Override
	public List<OperateHelpTypeBO> findTypeTree() {
		OperateHelpTypeVO vo = new OperateHelpTypeVO();
		vo.setIsVirtual(Constants.NO);
		vo.setExcludeTypeCode(HelpConstants.TYPE_CODE_LOTTERY_CHILD);
		List<OperateHelpTypeBO> typeList = operateHelpTypeDaoMapper.findList(vo);
		if (ObjectUtil.isBlank(typeList)) {
			return Collections.emptyList();
		}
		// 组合树型
		List<OperateHelpTypeBO> typeTree = toTypeTree(typeList);
		return typeTree;
	}

	@Override
	public List<OperateHelpTypeBO> findTypeList(OperateHelpTypeVO vo) {
		if (vo == null) {
			vo = new OperateHelpTypeVO();
			vo.setMenu((short) Constants.NUM_3);
		}
		vo.setIsVirtual(Constants.NO);
		vo.setExcludeTypeCode(HelpConstants.TYPE_CODE_LOTTERY_CHILD);
		List<OperateHelpTypeBO> typeList = operateHelpTypeDaoMapper.findList(vo);
		if (ObjectUtil.isBlank(typeList)) {
			return Collections.emptyList();
		}
		return typeList;
	}

	@Override
	public PagingBO<OperateHelpBO> search(final OperateHelpVO vo) {
		Assert.paramNotNull(vo.getKeyword(), "keyword");
		vo.setKeyword(SQLUtil.escape(vo.getKeyword()));
		if (vo.getPageSize() == null) {
			vo.setPageSize(HelpConstants.DEFAULT_HELP_SIZE);
		}
		if (vo.getPageIndex() == null) {
			vo.setPageIndex(Constants.NUM_0);
		}
		PagingBO<OperateHelpBO> pageData = pageService.getPageData(vo, new ISimplePage<OperateHelpBO>() {

			@Override
			public int getTotal() {
				return operateHelpDaoMapper.searchTotal(vo);
			}

			@Override
			public List<OperateHelpBO> getData() {
				return operateHelpDaoMapper.search(vo);
			}

		});
		return pageData;
	}

	@Override
	public List<OperateHelpTypeBO> findHelpByTypeList(List<OperateHelpVO> voList) {
		if (ObjectUtil.isBlank(voList)) {
			return null;
		}
		List<OperateHelpTypeBO> helpTypeList = new ArrayList<OperateHelpTypeBO>();
		for (OperateHelpVO vo : voList) {
			helpTypeList.add(findHelpToType(vo));
		}
		return helpTypeList;
	}

	@Override
	public PagingBO<OperateHelpBO> findHelpPage(final OperateHelpVO vo) {
		if (vo.getPageSize() == null) {
			vo.setPageSize(HelpConstants.DEFAULT_HELP_SIZE);
		}
		if (vo.getPageIndex() == null) {
			vo.setPageIndex(Constants.NUM_0);
		}
		PagingBO<OperateHelpBO> pageData = pageService.getPageData(vo, new ISimplePage<OperateHelpBO>() {

			@Override
			public int getTotal() {
				return operateHelpDaoMapper.findTotal(vo);
			}

			@Override
			public List<OperateHelpBO> getData() {
				return operateHelpDaoMapper.findList(vo);
			}

		});
		return pageData;
	}

	@Override
	public OperateHelpBO findHelpById(OperateHelpVO vo) {
		Assert.paramNotNull(vo);
		Assert.paramNotNull(vo.getId(), "id");
		if (vo.getPlatform() == null) {
			vo.setPlatform(Constants.NUM_1);
		}
		OperateHelpBO help = operateHelpDaoMapper.findById(vo);
		if (help == null) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.DATA_NOT_FOUND_SYS));
		}
		// 父级分类
		OperateHelpTypeBO parentType = null;
		String typeCode = help.getTypeCode();
		List<OperateHelpTypeBO> parentTypeList = new ArrayList<OperateHelpTypeBO>();
		while ((parentType = operateHelpTypeDaoMapper.findParentByCode(typeCode)) != null) {
			parentTypeList.add(parentType);
			typeCode = parentType.getTypeCode();
		}
		Collections.reverse(parentTypeList);
		StringBuilder typeAll = new StringBuilder();
		for (OperateHelpTypeBO type : parentTypeList) {
			typeAll.append(type.getTypeCode()).append("|").append(type.getTypeName()).append(",");
		}
		typeAll.append(help.getTypeCode()).append("|").append(help.getTypeName());
		help.setTypeAll(typeAll.toString());
		// 上一篇、下一篇
		findAdjoinHelp(help, vo);
		return help;
	}

	@Override
	public int addHelpCorrect(OperateHelpCorrectVO vo) {
		OperateHelpCorrectPO po = new OperateHelpCorrectPO();
		UserInfoBO tokenInfo = null;
		if (!ObjectUtil.isBlank(vo.getToken())) {
			tokenInfo = userUtil.getUserByToken(vo.getToken());
		}
		if (tokenInfo != null) {
			po.setUserId(tokenInfo.getId());
		}
		if (!ObjectUtil.isBlank(vo.getMobile())) {
			po.setCusMobile(vo.getMobile());
		}
		// 验证栏目编码
		if (!ObjectUtil.isBlank(vo.getHelpTypeCode())) {
			OperateHelpTypeBO helpType = operateHelpTypeDaoMapper.findByCode(vo.getHelpTypeCode());
			Assert.paramLegal(helpType != null, "helpTypeCode");
			po.setHelpTypeId(helpType.getId());
		}
		po.setHelpId(vo.getHelpId());
		if (vo.getSources() != null) {
			po.setSources(vo.getSources().toString());
		}
		po.setCorrect(vo.getContent());
		po.setType(vo.getType());
		// 创建人
		if (!ObjectUtil.isBlank(vo.getMobile())) {
			po.setCreateBy(vo.getMobile());
		} else if (tokenInfo != null && !ObjectUtil.isBlank(tokenInfo.getAccount())) {
			po.setCreateBy(tokenInfo.getAccount());
		} else if (tokenInfo == null) {
			po.setCreateBy(HelpConstants.TOURIST_CREATE_BY + vo.getIp());
		}
		return operateHelpCorrectDaoMapper.insert(po);
	}

	@Override
	public List<String> findRelatedwords(OperateHelpVO vo) {
		Assert.paramNotNull(vo);
		List<String> labelList = null;
		if (!ObjectUtil.isBlank(vo.getTypeCode())) {
			labelList = operateHelpDaoMapper.findLabelListByType(vo);
		} else if (!ObjectUtil.isBlank(vo.getKeyword())) {
			labelList = operateHelpDaoMapper.findLabelListByKeyword(vo);
		}
		if (ObjectUtil.isBlank(labelList)) {
			return Collections.emptyList();
		}
		List<String> returnLabelList = new ArrayList<String>();
		A: for (String labels : labelList) {
			if (ObjectUtil.isBlank(labels)) {
				continue;
			}
			for (String label : labels.split(SymbolConstants.COMMA)) {
				if (returnLabelList.contains(label)) {
					continue;
				}
				returnLabelList.add(label);
				if (returnLabelList.size() >= HelpConstants.DEFAULT_HELP_TYPE_HOTWORD_NUM) {
					break A;
				}
			}
		}
		return returnLabelList;
	}

	@Override
	public List<OperateHelpLotteryBO> findLotteryList() {
		List<OperateHelpLotteryTypeBO> lotteryTypeList = operateHelpTypeDaoMapper.findLotteryList();
		if (ObjectUtil.isBlank(lotteryTypeList)) {
			return Collections.emptyList();
		}
		Map<Short, OperateHelpLotteryBO> helpLotteryMap = new HashMap<>();
		for (OperateHelpLotteryTypeBO lotteryType : lotteryTypeList) {
			toHelpLottery(helpLotteryMap, lotteryType);
		}
		return new ArrayList<OperateHelpLotteryBO>(helpLotteryMap.values());
	}

	@Override
	public OperateHelpTypeBO findChildType(String typeCode) {
		OperateHelpTypeBO helpType = operateHelpTypeDaoMapper.findByCode(typeCode);
		if (helpType == null) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.DATA_NOT_FOUND_SYS));
		}
		// 彩种介绍，将栏目名称换成彩种名称
		if (helpType.getTypeCode().matches("1\\.4\\.[\\d]+") && !ObjectUtil.isBlank(helpType.getIdentifiers())
				&& helpType.getIdentifiers().trim().matches("\\d+")) {
			LotteryVO lotteryVO = new LotteryVO();
			lotteryVO.setLotteryCode(Integer.parseInt(helpType.getIdentifiers().trim()));
			LotteryBO lotteryBO = lotteryTypeDaoMapper.findSingleFront(lotteryVO);
			if (lotteryBO != null) {
				helpType.setTypeName(lotteryBO.getLotteryName());
			}
		}
		// 查找子栏目
		OperateHelpTypeVO queryVO = new OperateHelpTypeVO();
		queryVO.setParentTypeCode(typeCode);
		List<OperateHelpTypeBO> childTypeList = operateHelpTypeDaoMapper.findList(queryVO);
		helpType.setChildNode(childTypeList);
		return helpType;
	}

	@Override
	public OperateHelpBO findHelpByTypeCode(OperateHelpVO vo) {
		Assert.paramNotNull(vo);
		Assert.paramNotNull(vo.getTypeCode(), "typeCode");
		if (vo.getPlatform() == null) {
			vo.setPlatform(Constants.NUM_1);
		}
		OperateHelpBO help = operateHelpDaoMapper.findByTypeCode(vo);
		if (help == null) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.DATA_NOT_FOUND_SYS));
		}
		return help;
	}

	@Override
	public OperateHelpAbleBO findHelpAble(OperateHelpAbleVO vo) {
		if (!ObjectUtil.isBlank(vo.getToken())) {
			Integer userId = userUtil.getUserIdByToken(vo.getToken());
			if (userId != null) {
				vo.setUserId(userId);
			}
		}
		return operateHelpAbleLogDaoMapper.findSingle(vo);
	}

	@Override
	public void updateHelpAble(OperateHelpAbleVO vo) {
		OperateHelpAbleBO able = operateHelpAbleLogDaoMapper.findSingle(vo);
		// 同一IP、用户只能评价一次
		if (able != null) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.HELP_ABLE_REPEAT));
		}
		// 文章满意度或不满意度增加
		int count = operateHelpDaoMapper.updateHelpAble(vo.getHelpId(), vo.getIsAble());
		if (count <= 0) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.DATA_NOT_EXIST));
		}
		// 插入记录
		OperateHelpAbleLogPO po = new OperateHelpAbleLogPO();
		po.setHelpId(vo.getHelpId());
		po.setIp(vo.getIp());
		if (!ObjectUtil.isBlank(vo.getToken())) {
			Integer userId = userUtil.getUserIdByToken(vo.getToken());
			if (userId != null) {
				po.setUserId(userId);
			}
		}
		po.setIsAble(vo.getIsAble());
		operateHelpAbleLogDaoMapper.insert(po);

	}

	/**
	 * 分类组成树形结构
	 * 
	 * @param typeList
	 * @return
	 */
	private List<OperateHelpTypeBO> toTypeTree(List<OperateHelpTypeBO> typeList) {
		List<OperateHelpTypeBO> typeTree = new ArrayList<OperateHelpTypeBO>();
		for (OperateHelpTypeBO type1 : typeList) {
			if (type1.getParentId() == null || type1.getParentId() == Constants.NUM_F_1) {
				typeTree.add(type1);
			}
			for (OperateHelpTypeBO type2 : typeList) {
				if (type2.getParentId() != null && type2.getParentId().equals(type1.getId())) {
					if (type1.getChildNode() == null) {
						type1.setChildNode(new ArrayList<OperateHelpTypeBO>());
					}
					type1.getChildNode().add(type2);
				}
			}
		}
		return typeTree;
	}

	private OperateHelpTypeBO findHelpToType(OperateHelpVO vo) {
		Assert.paramNotNull(vo.getTypeCode(), "typeCode");
		Assert.paramNotNull(vo.getPlatform(), "platform");
		if (vo.getRownum() == null) {
			vo.setRownum(HelpConstants.DEFAULT_HELP_SIZE);
		}
		OperateHelpTypeBO helpType = operateHelpTypeDaoMapper.findByCode(vo.getTypeCode());
		if (helpType == null) {
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.DATA_NOT_FOUND_SYS));
		}
		vo.setPageIndex(Constants.NUM_0);
		vo.setPageSize(vo.getRownum());
		List<OperateHelpBO> helpList = operateHelpDaoMapper.findList(vo);
		helpType.setHelpList(helpList);
		return helpType;
	}

	/**
	 * 查找相邻的文章
	 * 
	 * @param help
	 */
	private void findAdjoinHelp(OperateHelpBO help, OperateHelpVO vo) {
		OperateHelpVO helpVO = new OperateHelpVO();
		helpVO.setTypeCode(help.getTypeCode());
		helpVO.setPlatform(vo.getPlatform());
		List<OperateHelpBO> helpList = operateHelpDaoMapper.findList(helpVO);
		if (ObjectUtil.isBlank(helpList) || helpList.size() <= 1) {
			return;
		}
		int index = -1;
		int helpListSize = helpList.size();
		for (int i = 0; i < helpListSize; i++) {
			if (help.getId() != null && help.getId().equals(helpList.get(i).getId())) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			return;
		}
		OperateHelpBO nextHelp = null;
		OperateHelpBO prevHelp = null;
		if (index == 0) {
			nextHelp = helpList.get(index + 1);
			prevHelp = helpList.get(helpListSize - 1);
		} else if (index == helpListSize - 1) {
			nextHelp = helpList.get(0);
			prevHelp = helpList.get(helpListSize - 2);
		} else {
			nextHelp = helpList.get(index + 1);
			prevHelp = helpList.get(index - 1);
		}
		if (nextHelp != null) {
			OperateHelpBO nextHelpNew = new OperateHelpBO();
			nextHelpNew.setId(nextHelp.getId());
			nextHelpNew.setTitle(nextHelp.getTitle());
			help.setNextHelp(nextHelpNew);
		}
		if (prevHelp != null) {
			OperateHelpBO prevHelpNew = new OperateHelpBO();
			prevHelpNew.setId(prevHelp.getId());
			prevHelpNew.setTitle(prevHelp.getTitle());
			help.setPrevHelp(prevHelpNew);
		}
	}

	/**
	 * 彩种介绍
	 * 
	 * @param helpLotteryMap
	 * @param lotteryType
	 */
	private void toHelpLottery(Map<Short, OperateHelpLotteryBO> helpLotteryMap, OperateHelpLotteryTypeBO lotteryType) {
		OperateHelpEnum.LotteryCategory category = OperateHelpEnum.LotteryCategory.getCategory(lotteryType);
		if (category == null) {
			return;
		}
		OperateHelpLotteryBO helpLottery = helpLotteryMap.get(category.getValue());
		if (helpLottery == null) {
			helpLottery = new OperateHelpLotteryBO();
			helpLottery.setCategory(category.getValue());
			helpLottery.setCategoryName(category.getName());
			helpLottery.setLotteryList(new ArrayList<OperateHelpLotteryTypeBO>());
			helpLotteryMap.put(category.getValue(), helpLottery);
		}
		lotteryType.setLotteryCategory(null);
		lotteryType.setAdminCategory(null);
		lotteryType.setLotteryLogoUrl(beforeFileUrl + lotteryType.getLotteryLogoUrl());
		// 查找子栏目
		OperateHelpTypeVO queryVO = new OperateHelpTypeVO();
		queryVO.setParentTypeCode(lotteryType.getTypeCode());
		List<OperateHelpTypeBO> childTypeList = operateHelpTypeDaoMapper.findList(queryVO);
		lotteryType.setChildNode(childTypeList);
		//
		helpLottery.getLotteryList().add(lotteryType);
	}

}

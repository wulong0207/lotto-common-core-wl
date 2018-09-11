package com.hhly.commoncore.remote.operate.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.base.util.RedisUtil;
import com.hhly.commoncore.persistence.operate.dao.OperateArticleDaoMapper;
import com.hhly.commoncore.persistence.operate.dao.OperateArticleTypeDaoMapper;
import com.hhly.commoncore.remote.operate.service.IOperateArticleService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.common.ArticleEnum.AriticleEnum;
import com.hhly.skeleton.base.common.DicEnum.PlatFormEnum;
import com.hhly.skeleton.base.constants.ArticleConstants;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.constants.MessageCodeConstants;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.exception.ResultJsonException;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.StringUtil;
import com.hhly.skeleton.lotto.base.operate.bo.CacheClickBO;
import com.hhly.skeleton.lotto.base.operate.bo.ClickBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateArticleBaseBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateArticleLottBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateArticleTypeLottBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateArticleUserBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateArticleLottVO;
import com.hhly.skeleton.lotto.base.sys.bo.CmsUserBO;

/**
 * @desc 运营管理服务实现类
 * @author lidecheng
 * @date 2017年3月8日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service("operateArticleService")
public class OperateArticleServiceImpl  implements IOperateArticleService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OperateArticleServiceImpl.class);
	
	@Autowired
	private OperateArticleDaoMapper operateArticleDaoMapper;
	@Autowired
	private OperateArticleTypeDaoMapper operateArticleTypeDaoMapper;
	@Autowired
    private RedisUtil objectRedisUtil;
	
	
	@Value("${before_file_url}")
	protected String beforeFileUrl;
	

	/**
	 * 查询pc首页新闻资讯信息
	 * @return
	 */
	@Override
	@SuppressWarnings({ "rawtypes" })
	public ResultBO<Map<String,Object>> findPCHomeArticle() {
		Map<String,Object> retMap = new HashMap<String,Object>(); 
		retMap = new HashMap<String,Object>();	    		
		List<OperateArticleLottVO> list = AriticleEnum.homeList;
		List<OperateArticleBaseBO> boList = operateArticleDaoMapper.findArticleByTypeList(list);		
		if(boList==null||boList.isEmpty()){
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.DATA_NOT_FOUND_SYS));
		}			
		for(OperateArticleBaseBO bo:boList){
			bo.setArticleUrl(beforeFileUrl+bo.getArticleUrl());
		}
		//信息归类
		Map<String,List<OperateArticleBaseBO>> ariMap = groupArticleList(boList);
		Map<String,List<OperateArticleBaseBO>> numMap = new HashMap<String,List<OperateArticleBaseBO>>();
		Map<String,List<OperateArticleBaseBO>> sportMap = new HashMap<String,List<OperateArticleBaseBO>>();
		//将信息组装list中返回前台
		
		for(String key :ariMap.keySet()){
			if(AriticleEnum.isHomeNum(key)){
				numMap.put(key,ariMap.get(key));
			}else if(AriticleEnum.isHomeSport(key)){
				sportMap.put(key,ariMap.get(key));
			}else{
				retMap.put(key, (List)ariMap.get(key));
			}
		}	
		retMap.put("numList", numMap);
		retMap.put("sportList", sportMap);
		return ResultBO.ok(retMap);
	}	
	
	/**
	 * 根据栏目标识符进行信息归类
	 * @param boList
	 * @return
	 */
	public Map<String,List<OperateArticleBaseBO>>  groupArticleList(List<OperateArticleBaseBO> boList){
		Map<String,List<OperateArticleBaseBO>> map =new HashMap<String,List<OperateArticleBaseBO>>();
		for(OperateArticleBaseBO report : boList){			
			String typeCode = report.getIdentifiers().toUpperCase();
			if(map.containsKey(typeCode)){
				 map.get(typeCode).add(report);	
				 map.put(typeCode,map.get(typeCode));
			}else{
				 List<OperateArticleBaseBO> list = new ArrayList<OperateArticleBaseBO>();
				 list.add(report);
				 map.put(typeCode,list);
			}
            
        }
		return map;
	}
	
	/**
	 * 查询手机首页新闻资讯信息
	 * @return
	 */
	@Override
	public List<OperateArticleLottBO>  findMobailHomeArticle(short platFrom){
		//查询头条信息
		OperateArticleLottVO topVO = new OperateArticleLottVO();
		topVO.setTypeCode(AriticleEnum.TOP.getValue());
		topVO.setPlatform(platFrom);
		topVO.setStartRow(Constants.NUM_1);
		topVO.setPageSize(AriticleEnum.TOP.getRows());
		List<OperateArticleLottBO>  topList = operateArticleDaoMapper.findArticleByTop(topVO);	  
		//拼接全路径
		doArticleUrl(topList);
		return topList;
	}
	
	/**
	 * 根据多个栏目查询资讯信息
	 * @param voList
	 * @return
	 */
	@Override
	public ResultBO<Map<String,List<OperateArticleLottBO>>> findNewsByTypeList(List<OperateArticleLottVO> voList) {
		//信息归类
		Map<String,List<OperateArticleLottBO>> ariMap=null;
		List<OperateArticleLottBO> list = operateArticleDaoMapper.findNewsByTypeList(voList);
		if(list==null||list.isEmpty()){
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.DATA_NOT_FOUND_SYS));
		}
		ariMap = groupArticleByTypeCode(list);
		for(String mapkey:ariMap.keySet()){
			List<OperateArticleLottBO> artlist = ariMap.get(mapkey);
			doArticleUrl(artlist);
		}
		return new ResultBO<Map<String,List<OperateArticleLottBO>>>(ariMap);
	}
	
	
	/**
	 * 根据栏目编号进行信息归类
	 * @param boList
	 * @return
	 */
	public Map<String,List<OperateArticleLottBO>>  groupArticleByTypeCode(List<OperateArticleLottBO> boList){
		Map<String,List<OperateArticleLottBO>> map =new HashMap<String,List<OperateArticleLottBO>>();
		for(OperateArticleLottBO report : boList){			
			String typeCode = ArticleConstants.NEWS_NOW_ROW+report.getTypeCode();
			if(map.containsKey(typeCode)){
				 map.get(typeCode).add(report);	
				 map.put(typeCode,map.get(typeCode));
			}else{
				 List<OperateArticleLottBO> list = new ArrayList<OperateArticleLottBO>();
				 list.add(report);
				 map.put(typeCode,list);
			}
            
        }
		return map;
	}
	
	/**
	 * 根据多个栏目查询资讯信息
	 * @param voList
	 * @return
	 */
	@Override
	public ResultBO<Map<String,List<OperateArticleBaseBO>>> findArticleByTypeList(List<OperateArticleLottVO> voList) {
		//信息归类
		List<OperateArticleBaseBO> list = operateArticleDaoMapper.findArticleByTypeList(voList);
		if(list==null||list.isEmpty()){
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.DATA_NOT_FOUND_SYS));
		}
		for(OperateArticleBaseBO bo:list){
			bo.setArticleUrl(beforeFileUrl+bo.getArticleUrl());
		}
		Map<String,List<OperateArticleBaseBO>> ariMap = groupArticleList(list);
		return new ResultBO<Map<String,List<OperateArticleBaseBO>>>(ariMap);
	}
	
	
	/**
	 * 查询某个栏目的资讯信息
	 */
	@Override
	public ResultBO<List<OperateArticleLottBO>> findArticleByTop(OperateArticleLottVO articleVO) {				
		List<OperateArticleLottBO> list = null;
		if(articleVO.getTypeCode().equals(AriticleEnum.NEWS.getValue())){
			//为所有资讯
			if(!Objects.equals(PlatFormEnum.WEB.getValue(), articleVO.getPlatform())){				
				List<OperateArticleLottBO> lottBoList= findMobailHomeArticle(articleVO.getPlatform());
				if(lottBoList!=null&&lottBoList.size()>0){
					//非pc端过滤掉头条第一条重复数据
					articleVO.setId(lottBoList.get(0).getId());
				}
			}
			list =  operateArticleDaoMapper.findNewArticle(articleVO);
		}else{//为某个具体栏目资讯
			if(articleVO.getTypeCode().equals(AriticleEnum.LASTNEWS.getValue())){
				articleVO.setIsComple(Constants.YES);
				articleVO.setCompleCode(AriticleEnum.NEWS.getValue());
			}
			list = operateArticleDaoMapper.findArticleByTop(articleVO);
		}
		doArticleUrl(list);
		return new ResultBO<List<OperateArticleLottBO>>(list);
	}
	
	
	/**
	 * 查询专栏信息
	 */
	@Override
	public ResultBO<List<OperateArticleUserBO>> findExpertByCode(OperateArticleLottVO vo) {				
		List<OperateArticleUserBO> list =null;
		List<CmsUserBO> userList = operateArticleDaoMapper.findUserByArtType(vo);
		if(userList.isEmpty()){
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.DATA_NOT_FOUND_SYS));
		}
		List<OperateArticleLottVO> voList = new ArrayList<OperateArticleLottVO>();
		for(CmsUserBO userBo : userList){
			OperateArticleLottVO artVo = new OperateArticleLottVO();
			artVo.setUserId(userBo.getUserId());
			artVo.setTypeCode(vo.getTypeCode());
			artVo.setRownum(ArticleConstants.NEWS_EXPERT_NUM);
			artVo.setPlatform(vo.getPlatform());
			voList.add(artVo);
		}
		List<OperateArticleBaseBO>  artBolist  = operateArticleDaoMapper.findArticleByUserList(voList);
		list = groupArticleList(artBolist,userList);
		for(OperateArticleUserBO bo:list){
			bo.setHeadUrl(beforeFileUrl+bo.getHeadUrl());
			List<OperateArticleBaseBO> artlist = bo.getArtList();
			for(OperateArticleBaseBO artbo:artlist){
				artbo.setArticleUrl(beforeFileUrl+artbo.getArticleUrl());
			}
		}
		return ResultBO.ok(list);
	}
	
	/**
	 * 根据栏目进行信息归类
	 * @param boList
	 * @return
	 */
	public List<OperateArticleUserBO> groupArticleList(List<OperateArticleBaseBO> boList,List<CmsUserBO> userList){
		List<OperateArticleUserBO> retBo =new ArrayList<OperateArticleUserBO>();
		Map<Integer,OperateArticleUserBO> map= new LinkedHashMap<Integer,OperateArticleUserBO>();
		//信息归类
		for(OperateArticleBaseBO report : boList){				
			Integer userName = report.getUserId();
			for(CmsUserBO cmsBo : userList){
				if(Objects.equals(userName, cmsBo.getUserId())){
					if(map.containsKey(userName)){
						 map.get(userName).getArtList().add(report);	
					}else{
						OperateArticleUserBO userBO = new OperateArticleUserBO();
						List<OperateArticleBaseBO> artBaseList = new ArrayList<OperateArticleBaseBO>(); 
						artBaseList.add(report);
						userBO.setArtList(artBaseList);
						userBO.setHeadUrl(cmsBo.getHeadUrl());
						userBO.setUserName(cmsBo.getUserCname());
						userBO.setUserRemark(cmsBo.getRemark());
						map.put(userName, userBO);
					}
				}
			}            
        }
		//map->list
		for(Integer key : map.keySet()){
			retBo.add(map.get(key));
		}
		return retBo;
	}
	
		
	
	
	/**
	 * 查询标签相似的信息
	 */
	public ResultBO<List<OperateArticleLottBO>>  findMobailArticleLabel(OperateArticleLottVO artVo){
		List<OperateArticleLottBO> lottBOList  = null;
		artVo.setDays(ArticleConstants.NEWS_MOBAIL_DETAIL_DAYS);
		try {
			//查询存储过程
			 lottBOList =operateArticleDaoMapper.findArticleLabel(artVo);
			 doArticleUrl(lottBOList);
			 
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new ResultJsonException(ResultBO.err(MessageCodeConstants.DATA_NOT_FOUND_SYS));
		}	
		return ResultBO.ok(lottBOList);
	}

	
	
	/**
	 * 查询资讯所有栏目信息
	 */
	public ResultBO<List<OperateArticleBaseBO>> findNewsTypeList(){
		List<OperateArticleBaseBO> boList = operateArticleTypeDaoMapper.findNewsTypeList();
		return new ResultBO<List<OperateArticleBaseBO>>(boList);		
	}
		
	
	/**
	 * 根据父节点查询子节点信息
	 */
	@Override
	public ResultBO<List<OperateArticleTypeLottBO>>  findListByFaType(String typeCode){
		List<OperateArticleTypeLottBO>  list = operateArticleTypeDaoMapper.findListByFaType(typeCode);		
		return new ResultBO<List<OperateArticleTypeLottBO>>(list);	
	}
	
	/**
	 * 查询资讯栏目关系
	 */
	@Override	
	public ResultBO<List<OperateArticleTypeLottBO>> findNewTypeRelatList(OperateArticleLottVO artVo) {		
		List<OperateArticleLottBO> list = operateArticleTypeDaoMapper.findNewTypeRelatList(artVo);
		List<OperateArticleTypeLottBO> typelist = new ArrayList<OperateArticleTypeLottBO>();
		for(OperateArticleLottBO lottBO: list){
			if(lottBO.getTypeCode().equals(AriticleEnum.NEWS.getValue())){
				typelist.add(newsTreeMenuList(list,lottBO,null));
			}
		}				
		return ResultBO.ok(typelist);
	} 
	
	public OperateArticleTypeLottBO newsTreeMenuList(List<OperateArticleLottBO> lottList, OperateArticleLottBO lottBO,OperateArticleLottBO parentBO) {  
		OperateArticleTypeLottBO typeBo =new OperateArticleTypeLottBO();
			typeBo.setTypeCode(lottBO.getTypeCode());
	        typeBo.setTypeName(lottBO.getTypeName());	   
	        if(parentBO!=null){
	        	typeBo.setParentTypeName(parentBO.getTypeName());
	  	        typeBo.setParentTypeCode(parentBO.getTypeCode());
	        }
	       for (OperateArticleLottBO bo : lottList) {  
	           int pId = bo.getParent();  
	           if (lottBO.getId().intValue() == pId) {  	        	  
	        	   OperateArticleTypeLottBO c_node = newsTreeMenuList(lottList, bo,lottBO); 
	        	   typeBo.getChildNode().add(c_node);	        	   
	           }	           
	       }  
	       return typeBo;
	   }
	
	/**
	 * 查询侧边栏资讯头条信息 ，查询规则查当前节点和子节点下创建的信息
	 */
	@Override
	public ResultBO<List<OperateArticleLottBO>> findSidebarNews(OperateArticleLottVO vo){
		List<OperateArticleLottBO> boList  = operateArticleDaoMapper.findSidebarNews(vo);
		doArticleUrl(boList);
		return new ResultBO<List<OperateArticleLottBO>>(boList);
	}
	/**
	 * url拼接
	 * @param boList
	 */
	public void doArticleUrl(List<OperateArticleLottBO> boList){
		for(OperateArticleLottBO artbo:boList){
			artbo.setArticleUrl(beforeFileUrl+artbo.getArticleUrl());
			String artImgUrl = artbo.getArticleImg();
			if(!StringUtil.isBlank(artImgUrl)){
				String url[] = artImgUrl.split(SymbolConstants.COMMA);
				String retImgUrl = "";
				for(int i=0;i<url.length;i++){
					retImgUrl+=beforeFileUrl+url[i];
					if(url.length>0&&i<url.length-1){
						retImgUrl+=",";
					}
				}
				artbo.setArticleImg(retImgUrl);
			}
	    }
	}
	/**
	 * 更新资讯点击量信息
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResultBO updateAriClick(OperateArticleLottVO vo) {						
        return ResultBO.ok(doAriClick(vo));
	}
	
	int doAriClick(OperateArticleLottVO vo){
		/*缓存方案，对象保存缓存清理时间，初始化点击量，实际点击量
		     初始化：一个星期的文章信息
		     定时保存数据：保存所有发生变化的文章信息
		     定时清理：缓存时间结束后清理点击量未发生变化的文章信息，并将初始化点击量重新赋值	
		*/
		//注：此处出现的线程问题经过讨论可以不管
		int artClickNum = 1;
		String artId = vo.getArticleId();
		String cacheKey = CacheConstants.C_COMM_ARTICLE_UPDATE_CLICK;
		CacheClickBO newCacheClick  = (CacheClickBO)objectRedisUtil.getObj(cacheKey);
		if(newCacheClick==null){//初始化			
			newCacheClick = new CacheClickBO(DateUtil.getAfterDayHour(1,1),operateArticleDaoMapper.findClick());
		}		
		//更新数据进缓存
		if(newCacheClick.getClickBO().containsKey(artId)){
			ClickBO clickBO = newCacheClick.getClickBO().get(artId); 
			artClickNum = clickBO.getClick()+1;
			clickBO.setClick(artClickNum);
			newCacheClick.getClickBO().put(artId, clickBO);
		}else{
			OperateArticleLottBO artiBo = findArticle(vo).getData();
			if(artiBo==null)
				throw new ResultJsonException(ResultBO.err(MessageCodeConstants.DATA_NOT_FOUND_SYS));
			artClickNum = artiBo.getClick()+1;
			newCacheClick.getClickBO().put(artId, new ClickBO(artId,artClickNum,artiBo.getClick()));
		}	
		//定时保存清除数据
		if(DateUtil.isOver(new Timestamp(newCacheClick.getCountTime().getTime()))){
			List<ClickBO> addList = new ArrayList<ClickBO>(); 
			for (Iterator<Entry<String, ClickBO>> it = newCacheClick.getClickBO().entrySet().iterator(); it.hasNext();){
				Entry<String, ClickBO> item = it.next();
				ClickBO clickBo= item.getValue();
				if(clickBo.check()){
					clickBo.setInitClick(clickBo.getClick());
					addList.add(clickBo);
				}else{
					it.remove();
				}
			}
			if(addList.size()>0){
				operateArticleDaoMapper.updateClickList(addList);
			}
			newCacheClick.setCountTime(DateUtil.getAfterDayHour(1,1));//重新初始化时间
		}
		objectRedisUtil.addObj(cacheKey, newCacheClick, null);
		return artClickNum;
	}
	
	/**
	 * 查询某个资讯详细信息
	 */
	public ResultBO<OperateArticleLottBO> findArticle(OperateArticleLottVO articleVO) {
		String key = CacheConstants.C_COMM_ARTICLE_FIND_BY_CENTENT+articleVO.getId()+articleVO.getArticleId();	
		OperateArticleLottBO bo = (OperateArticleLottBO)objectRedisUtil.getObj(key);
		if(bo==null){
			bo = operateArticleDaoMapper.findArticle(articleVO);
			objectRedisUtil.addObj(key, bo, (long)Constants.DAY_1);
		}
		bo.setArticleUrl(beforeFileUrl+bo.getArticleUrl());
		String artImgUrl = bo.getArticleImg();
		if(!StringUtil.isBlank(artImgUrl)){
			String url[] = artImgUrl.split(SymbolConstants.COMMA);
			String retImgUrl = "";
			for(int i=0;i<url.length;i++){
				retImgUrl+=beforeFileUrl+url[i];
				if(url.length>0&&i<url.length-1){
					retImgUrl+=",";
				}
			}
			bo.setArticleImg(retImgUrl);
		}
		return new ResultBO<OperateArticleLottBO>(bo);
	}

	
}
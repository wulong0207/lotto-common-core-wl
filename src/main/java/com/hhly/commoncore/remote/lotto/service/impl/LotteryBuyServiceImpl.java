package com.hhly.commoncore.remote.lotto.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhly.commoncore.persistence.lottery.dao.LotteryTypeDaoMapper;
import com.hhly.commoncore.remote.cache.service.ILotteryIssueCacheService;
import com.hhly.commoncore.remote.lotto.service.ILotteryBuyService;
import com.hhly.commoncore.remote.operate.service.IOperateAdService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.common.LotteryEnum;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.constants.DrawLotteryConstant;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.constants.UserConstants;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.StringUtil;
import com.hhly.skeleton.lotto.base.buy.bo.LotteryBuyBO;
import com.hhly.skeleton.lotto.base.buy.bo.LotteryDrawBO;
import com.hhly.skeleton.lotto.base.issue.bo.CurrentAndPreIssueBO;
import com.hhly.skeleton.lotto.base.lottery.bo.LotteryDetailBO;
import com.hhly.skeleton.lotto.base.operate.bo.OperateAdLottoBO;
import com.hhly.skeleton.lotto.base.operate.vo.OperateAdVO;

/**
 * @author zhouyang
 * @version 1.0
 * @desc 接口实现类
 * @date 2017/11/7
 * @company 益彩网络科技公司
 */
@Service("lotteryBuyService")
public class LotteryBuyServiceImpl implements ILotteryBuyService {

    @Autowired
    private LotteryTypeDaoMapper lotteryTypeDaoMapper;

    @Autowired
    private ILotteryIssueCacheService lotteryIssueCacheService;

    @Autowired
    private IOperateAdService iOperateAdService;

    @Value("${before_file_url}")
    protected String beforeFileUrl;

    //开奖公告域名
    @Value("${lotto_draw_url}")
    private String lottoDrawUrl;

    @Override
    public ResultBO<?> findLotteryList(OperateAdVO vo) {
        LotteryBuyBO lotteryBuyBO = new LotteryBuyBO();
        vo.setDefaultAd(Integer.valueOf(UserConstants.IS_FALSE));
        List<LotteryDetailBO> lotteryList = new ArrayList<>();
         lotteryList = lotteryTypeDaoMapper.findLotteryList(vo);
         if (ObjectUtil.isBlank(lotteryList)) {
             lotteryList = lotteryTypeDaoMapper.findLotteryDefaultList(vo);
         }
        if (!ObjectUtil.isBlank(lotteryList)) {
            for (LotteryDetailBO lotteryDetailBO : lotteryList) {
                if (!ObjectUtil.isBlank(lotteryDetailBO.getLotteryLogoUrl())) {
                    lotteryDetailBO.setLotteryLogoUrl(beforeFileUrl + lotteryDetailBO.getLotteryLogoUrl());
                }
                if (!ObjectUtil.isBlank(lotteryDetailBO.getUrl())) {
                    lotteryDetailBO.setUrl(beforeFileUrl + lotteryDetailBO.getUrl());
                }
                if (lotteryDetailBO.getCategoryId().equals(LotteryEnum.LotteryCategory.HIGH.getValue())) {
                    String data = DateUtil.convertDateToStr(lotteryDetailBO.getLotteryTime(), DateUtil.DATE_FORMAT_NO_LINE);
                    if (!ObjectUtil.isBlank(DrawLotteryConstant.getLotteryKey(lotteryDetailBO.getLotteryCode()))) {
                        lotteryDetailBO.setDrawDetailUrl(lottoDrawUrl + DrawLotteryConstant.getLotteryKey(lotteryDetailBO.getLotteryCode()) + "/" + data + ".html");
                    }
                    int saleNum = getSaleNum(lotteryDetailBO.getSailDatCycle());
                    int len = lotteryDetailBO.getIssueCode().length();
                    String issueStr = "";
                    if (saleNum < 100) {
                        issueStr = lotteryDetailBO.getIssueCode().substring(len-2, len);
                    } else {
                        issueStr = lotteryDetailBO.getIssueCode().substring(len-3, len);
                    }
                    lotteryDetailBO.setSoldNum(Integer.valueOf(issueStr)-1);
                    lotteryDetailBO.setNoSalesNum(saleNum-lotteryDetailBO.getSoldNum());
                } else if (lotteryDetailBO.getCategoryId().equals(LotteryEnum.LotteryCategory.SPORT.getValue())) {
                    if (!ObjectUtil.isBlank(DrawLotteryConstant.getLotteryKey(lotteryDetailBO.getLotteryCode()))) {
                        lotteryDetailBO.setDrawDetailUrl(lottoDrawUrl + DrawLotteryConstant.getLotteryKey(lotteryDetailBO.getLotteryCode()) + "/");
                        if (lotteryDetailBO.getLotteryCode().equals(LotteryEnum.Lottery.SFC.getName()) ||
                                lotteryDetailBO.getLotteryCode().equals(LotteryEnum.Lottery.ZC_NINE.getName()) ||
                                lotteryDetailBO.getLotteryCode().equals(LotteryEnum.Lottery.JQ4.getName()) ||
                                lotteryDetailBO.getLotteryCode().equals(LotteryEnum.Lottery.ZC6.getName())) {
                            lotteryDetailBO.setDrawDetailUrl(lottoDrawUrl + DrawLotteryConstant.getLotteryKey(lotteryDetailBO.getLotteryCode()) + "/" + lotteryDetailBO.getIssueCode() + ".html");
                        }
                    }
                    lotteryDetailBO.setBetNum(lotteryTypeDaoMapper.findSportBetNum(lotteryDetailBO.getLotteryCode()));
                } else if (lotteryDetailBO.getCategoryId().equals(LotteryEnum.LotteryCategory.NUM.getValue())) {
                    if (!ObjectUtil.isBlank(DrawLotteryConstant.getLotteryKey(lotteryDetailBO.getLotteryCode()))) {
                        lotteryDetailBO.setDrawDetailUrl(lottoDrawUrl + DrawLotteryConstant.getLotteryKey(lotteryDetailBO.getLotteryCode()) + "/" + lotteryDetailBO.getIssueCode() + ".html");
                    }
                    LotteryDetailBO bo = lotteryTypeDaoMapper.findLotteryLastest(lotteryDetailBO.getLotteryCode());
                    if (!ObjectUtil.isBlank(bo)) {
                        lotteryDetailBO.setJackpotAmount(bo.getJackpotAmount());
                    }
                }
                /*if (Integer.valueOf(lotteryDetailBO.getLotteryCode()).equals(LotteryEnum.Lottery.FB.getName()) ||
                        Integer.valueOf(lotteryDetailBO.getLotteryCode()).equals(LotteryEnum.Lottery.BB.getName())) {

                }*/
                lotteryBuyBO.getLotteryList().add(lotteryDetailBO);
            }
        }
        vo.setDefaultAd(Integer.valueOf(UserConstants.IS_TRUE));
        List<LotteryDetailBO> commendLotteryList = new ArrayList<>();
        commendLotteryList = lotteryTypeDaoMapper.findLotteryList(vo);
        if (ObjectUtil.isBlank(commendLotteryList)) {
            commendLotteryList = lotteryTypeDaoMapper.findLotteryDefaultList(vo);
        }
        if (!ObjectUtil.isBlank(commendLotteryList)) {
            for (LotteryDetailBO lotteryDetailBO : commendLotteryList) {
                if (!ObjectUtil.isBlank(lotteryDetailBO.getLotteryLogoUrl())) {
                    lotteryDetailBO.setLotteryLogoUrl(beforeFileUrl + lotteryDetailBO.getLotteryLogoUrl());
                }
                if (!ObjectUtil.isBlank(lotteryDetailBO.getUrl())) {
                    lotteryDetailBO.setUrl(beforeFileUrl + lotteryDetailBO.getUrl());
                }
                if (lotteryDetailBO.getCategoryId().equals(LotteryEnum.LotteryCategory.HIGH.getValue())) {
                    String data = DateUtil.convertDateToStr(lotteryDetailBO.getLotteryTime(), DateUtil.DATE_FORMAT_NO_LINE);
                    if (!ObjectUtil.isBlank(DrawLotteryConstant.getLotteryKey(lotteryDetailBO.getLotteryCode()))) {
                        lotteryDetailBO.setDrawDetailUrl(lottoDrawUrl + DrawLotteryConstant.getLotteryKey(lotteryDetailBO.getLotteryCode()) + "/" + data + ".html");
                    }
                    int saleNum = getSaleNum(lotteryDetailBO.getSailDatCycle());
                    int len = lotteryDetailBO.getIssueCode().length();
                    String issueStr = "";
                    if (saleNum < 100) {
                        issueStr = lotteryDetailBO.getIssueCode().substring(len-2, len);
                    } else {
                        issueStr = lotteryDetailBO.getIssueCode().substring(len-3, len);
                    }
                    lotteryDetailBO.setSoldNum(Integer.valueOf(issueStr)-1);
                    lotteryDetailBO.setNoSalesNum(saleNum-lotteryDetailBO.getSoldNum());
                } else if (lotteryDetailBO.getCategoryId().equals(LotteryEnum.LotteryCategory.SPORT.getValue())) {
                    if (!ObjectUtil.isBlank(DrawLotteryConstant.getLotteryKey(lotteryDetailBO.getLotteryCode()))) {
                        lotteryDetailBO.setDrawDetailUrl(lottoDrawUrl + DrawLotteryConstant.getLotteryKey(lotteryDetailBO.getLotteryCode()) + "/");
                        if (lotteryDetailBO.getLotteryCode().equals(LotteryEnum.Lottery.SFC.getName()) ||
                                lotteryDetailBO.getLotteryCode().equals(LotteryEnum.Lottery.ZC_NINE.getName()) ||
                                lotteryDetailBO.getLotteryCode().equals(LotteryEnum.Lottery.JQ4.getName()) ||
                                lotteryDetailBO.getLotteryCode().equals(LotteryEnum.Lottery.ZC6.getName())) {
                            lotteryDetailBO.setDrawDetailUrl(lottoDrawUrl + DrawLotteryConstant.getLotteryKey(lotteryDetailBO.getLotteryCode()) + "/" + lotteryDetailBO.getIssueCode() + ".html");
                        }
                    }
                    lotteryDetailBO.setBetNum(lotteryTypeDaoMapper.findSportBetNum(lotteryDetailBO.getLotteryCode()));
                } else if (lotteryDetailBO.getCategoryId().equals(LotteryEnum.LotteryCategory.NUM.getValue())) {
                    if (!ObjectUtil.isBlank(DrawLotteryConstant.getLotteryKey(lotteryDetailBO.getLotteryCode()))) {
                        lotteryDetailBO.setDrawDetailUrl(lottoDrawUrl + DrawLotteryConstant.getLotteryKey(lotteryDetailBO.getLotteryCode()) + "/" + lotteryDetailBO.getIssueCode() + ".html");
                    }
                    LotteryDetailBO bo = lotteryTypeDaoMapper.findLotteryLastest(lotteryDetailBO.getLotteryCode());
                    if (!ObjectUtil.isBlank(bo)) {
                        lotteryDetailBO.setJackpotAmount(bo.getJackpotAmount());
                    }
                }
                lotteryBuyBO.getCommendLotteryList().add(lotteryDetailBO);
            }
        }

        ResultBO<?> resultBO = iOperateAdService.findHomeOperAd(vo);
        List<OperateAdLottoBO> adList = (List<OperateAdLottoBO>) resultBO.getData();
        lotteryBuyBO.setAdList(adList);
        return ResultBO.ok(lotteryBuyBO);
    }

    @Override
    public ResultBO<?> findDrawList() {
        LotteryDrawBO lotteryDrawBO = new LotteryDrawBO();
        List<LotteryDetailBO> lotteryDrawList = lotteryTypeDaoMapper.findLotteryDrawList();
        for (LotteryDetailBO lotteryDraw : lotteryDrawList) {
            if (!ObjectUtil.isBlank(lotteryDraw.getCategoryId())) {
                if (lotteryDraw.getCategoryId().equals(LotteryEnum.LotteryCategory.NUM.getValue())) {
                    if (!ObjectUtil.isBlank(DrawLotteryConstant.getLotteryKey(lotteryDraw.getLotteryCode()))) {
                        lotteryDraw.setDrawDetailUrl(lottoDrawUrl + DrawLotteryConstant.getLotteryKey(lotteryDraw.getLotteryCode()) + "/" + lotteryDraw.getPreIssueCode() + ".html");
                    }
                    lotteryDrawBO.getNumList().add(new LotteryDetailBO(lotteryDraw, LotteryEnum.ConIssue.LAST_CURRENT.getValue()));
                }
                if (lotteryDraw.getCategoryId().equals(LotteryEnum.LotteryCategory.HIGH.getValue())) {
                    String data = DateUtil.convertDateToStr(lotteryDraw.getLotteryTime(), DateUtil.DATE_FORMAT_NO_LINE);
                    if (!ObjectUtil.isBlank(DrawLotteryConstant.getLotteryKey(lotteryDraw.getLotteryCode()))) {
                        lotteryDraw.setDrawDetailUrl(lottoDrawUrl + DrawLotteryConstant.getLotteryKey(lotteryDraw.getLotteryCode()) + "/" + data + ".html");
                    }
                    lotteryDrawBO.getHighList().add(new LotteryDetailBO(lotteryDraw, LotteryEnum.ConIssue.LAST_CURRENT.getValue()));
                }
                if (lotteryDraw.getCategoryId().equals(LotteryEnum.LotteryCategory.SPORT.getValue())) {
                    if (!ObjectUtil.isBlank(DrawLotteryConstant.getLotteryKey(lotteryDraw.getLotteryCode()))) {
                        lotteryDraw.setDrawDetailUrl(lottoDrawUrl + DrawLotteryConstant.getLotteryKey(lotteryDraw.getLotteryCode()) + "/");
                        if (lotteryDraw.getLotteryCode().equals(LotteryEnum.Lottery.SFC.getName()) ||
                                lotteryDraw.getLotteryCode().equals(LotteryEnum.Lottery.ZC_NINE.getName()) ||
                                lotteryDraw.getLotteryCode().equals(LotteryEnum.Lottery.JQ4.getName()) ||
                                lotteryDraw.getLotteryCode().equals(LotteryEnum.Lottery.ZC6.getName())) {
                            lotteryDraw.setDrawDetailUrl(lottoDrawUrl + DrawLotteryConstant.getLotteryKey(lotteryDraw.getLotteryCode()) + "/" + lotteryDraw.getPreIssueCode() + ".html");
                        }
                    }
                    if(lotteryDraw.getLotteryCode()== LotteryEnum.Lottery.ZC_NINE.getName())continue;
                    if(lotteryDraw.getLotteryCode()== LotteryEnum.Lottery.SFC.getName()){
                        lotteryDraw.setLotteryName(LotteryEnum.Lottery.SFC.getDesc());
                        CurrentAndPreIssueBO rx9 = (CurrentAndPreIssueBO)lotteryIssueCacheService.getCurrentAndPreIssue(LotteryEnum.Lottery.ZC_NINE.getName()).getData();
                        if(rx9!=null&&Objects.equals(rx9.getPreIssue(),lotteryDraw.getPreIssueCode())){
                            String r9detail = rx9.getPreDrawDetail()==null?"":rx9.getPreDrawDetail();
                            r9detail =r9detail.replace(Constants.FIRST_PRIZE, Constants.RX9_PRIZE);
                            if(!StringUtil.isBlank(lotteryDraw.getPreDrawDetail())){
                                lotteryDraw.setPreDrawDetail(lotteryDraw.getPreDrawDetail()+SymbolConstants.COMMA+r9detail);
                            }else{
                                lotteryDraw.setPreDrawDetail(r9detail);
                            }
                        }
                    }
                    lotteryDrawBO.getSportList().add(new LotteryDetailBO(lotteryDraw, LotteryEnum.ConIssue.LAST_CURRENT.getValue()));
                }
                if (lotteryDraw.getLotteryCode().equals(LotteryEnum.Lottery.SSQ.getName()) ||
                        lotteryDraw.getLotteryCode().equals(LotteryEnum.Lottery.DLT.getName()) ||
                        lotteryDraw.getLotteryCode().equals(LotteryEnum.Lottery.SFC.getName())) {
                    lotteryDrawBO.getHotList().add(new LotteryDetailBO(lotteryDraw, LotteryEnum.ConIssue.LAST_CURRENT.getValue()));
                }
            }
        }
        return ResultBO.ok(lotteryDrawBO);
    }

    private Integer getSaleNum(String sailDayCycle) {
        Integer saleNum = 0;
        if (!sailDayCycle.contains(",")) {
            String [] arr = sailDayCycle.split("[-\\|]");
            saleNum = Integer.valueOf(arr[1]);
        } else {
            String [] cycleArr = sailDayCycle.split(",");
            for (int i=0; i<cycleArr.length; i++) {
                String [] arr = cycleArr[i].split("[-\\|]");
                int saleInt = Integer.valueOf(arr[1]);
                if (saleInt >= saleNum) {
                    saleNum = saleInt;
                }
            }
        }
        return saleNum;
    }
}

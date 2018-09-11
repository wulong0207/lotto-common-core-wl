
package com.hhly.commoncore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;

import com.hhly.commoncore.remote.cache.service.ILotteryIssueCacheService;
import com.hhly.commoncore.remote.cache.service.IUserInfoCacheService;
import com.hhly.commoncore.remote.cooperate.service.ICoOperateCdkeyService;
import com.hhly.commoncore.remote.exporter.LottoHessianServiceExporter;
import com.hhly.commoncore.remote.lotto.service.IJcDataCommService;
import com.hhly.commoncore.remote.lotto.service.ILotteryBuyService;
import com.hhly.commoncore.remote.operate.service.IOperateAdService;
import com.hhly.commoncore.remote.operate.service.IOperateArticleService;
import com.hhly.commoncore.remote.operate.service.IOperateFastService;
import com.hhly.commoncore.remote.operate.service.IOperateHelpService;
import com.hhly.commoncore.remote.operate.service.IOperateLotteryService;
import com.hhly.commoncore.remote.operate.service.IOperateMsgService;
import com.hhly.commoncore.remote.operate.service.IOperateService;
import com.hhly.commoncore.remote.sport.service.ISportMatchService;
import com.hhly.commoncore.remote.sys.service.IDicDataDetailService;

/**
 * @desc    
 * @author  cheng chen
 * @date    2018年8月3日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Configuration
public class HessionServerConfig {
	
	@Autowired
	IDicDataDetailService dicDataDetailService;
	
	@Autowired
	IOperateService operateService;
	
	@Autowired
	IOperateHelpService operateHelpService;
	
	@Autowired
	ILotteryBuyService lotteryBuyService;
	
	@Autowired
	ISportMatchService sportMatchService;
	
	@Autowired
	IOperateArticleService operateArticleService;
	
	@Autowired
	IOperateAdService operateAdService;
	
	@Autowired
	IOperateMsgService operateMsgService;
	
	@Autowired
	IOperateLotteryService operateLotteryService;
	
	@Autowired
	IOperateFastService operateFastService;
	
	@Autowired
	ILotteryIssueCacheService lotteryIssueCacheService;	
	
	@Autowired
	IUserInfoCacheService userInfoCacheService;
	
	@Autowired
	IJcDataCommService jcDataCommService;
	
	@Autowired
	ICoOperateCdkeyService coOperateCdkeyService;
	

    @Bean(name = "/remote/dicDataDetailService")
    public HessianServiceExporter dicDataDetailService() {
    	LottoHessianServiceExporter exporter = new LottoHessianServiceExporter();
        exporter.setService(dicDataDetailService);
        exporter.setServiceInterface(IDicDataDetailService.class);
        return exporter;
    }
    
    @Bean(name = "/remote/operateService")
    public HessianServiceExporter operateService() {
    	LottoHessianServiceExporter exporter = new LottoHessianServiceExporter();
        exporter.setService(operateService);
        exporter.setServiceInterface(IOperateService.class);
        return exporter;
    }
    
    @Bean(name = "/remote/operateHelpService")
    public HessianServiceExporter operateHelpService() {
    	LottoHessianServiceExporter exporter = new LottoHessianServiceExporter();
        exporter.setService(operateHelpService);
        exporter.setServiceInterface(IOperateHelpService.class);
        return exporter;
    }
    
    @Bean(name = "/remote/lotteryBuyService")
    public HessianServiceExporter lotteryBuyService() {
    	LottoHessianServiceExporter exporter = new LottoHessianServiceExporter();
        exporter.setService(lotteryBuyService);
        exporter.setServiceInterface(ILotteryBuyService.class);
        return exporter;
    }
    
    @Bean(name = "/remote/sportMatchService")
    public HessianServiceExporter sportMatchService() {
    	LottoHessianServiceExporter exporter = new LottoHessianServiceExporter();
        exporter.setService(sportMatchService);
        exporter.setServiceInterface(ISportMatchService.class);
        return exporter;
    }
    
    @Bean(name = "/remote/operateArticleService")
    public HessianServiceExporter operateArticleService() {
    	LottoHessianServiceExporter exporter = new LottoHessianServiceExporter();
        exporter.setService(operateArticleService);
        exporter.setServiceInterface(IOperateArticleService.class);
        return exporter;
    }
    
    @Bean(name = "/remote/operateAdService")
    public HessianServiceExporter operateAdService() {
    	LottoHessianServiceExporter exporter = new LottoHessianServiceExporter();
        exporter.setService(operateAdService);
        exporter.setServiceInterface(IOperateAdService.class);
        return exporter;
    }
    
    @Bean(name = "/remote/operateMsgService")
    public HessianServiceExporter operateMsgService() {
    	LottoHessianServiceExporter exporter = new LottoHessianServiceExporter();
        exporter.setService(operateMsgService);
        exporter.setServiceInterface(IOperateMsgService.class);
        return exporter;
    }
    
    @Bean(name = "/remote/operateLotteryService")
    public HessianServiceExporter operateLotteryService() {
    	LottoHessianServiceExporter exporter = new LottoHessianServiceExporter();
        exporter.setService(operateLotteryService);
        exporter.setServiceInterface(IOperateLotteryService.class);
        return exporter;
    }
    
    @Bean(name = "/remote/operateFastService")
    public HessianServiceExporter operateFastService() {
    	LottoHessianServiceExporter exporter = new LottoHessianServiceExporter();
        exporter.setService(operateFastService);
        exporter.setServiceInterface(IOperateFastService.class);
        return exporter;
    }
    
    @Bean(name = "/remote/lotteryIssueCacheService")
    public HessianServiceExporter lotteryIssueCacheService() {
    	LottoHessianServiceExporter exporter = new LottoHessianServiceExporter();
        exporter.setService(lotteryIssueCacheService);
        exporter.setServiceInterface(ILotteryIssueCacheService.class);
        return exporter;
    }
    
    @Bean(name = "/remote/userInfoCacheService")
    public HessianServiceExporter userInfoCacheService() {
    	LottoHessianServiceExporter exporter = new LottoHessianServiceExporter();
        exporter.setService(userInfoCacheService);
        exporter.setServiceInterface(IUserInfoCacheService.class);
        return exporter;
    }
    
    @Bean(name = "/remote/jcDataCommService")
    public HessianServiceExporter jcDataCommService() {
    	LottoHessianServiceExporter exporter = new LottoHessianServiceExporter();
        exporter.setService(jcDataCommService);
        exporter.setServiceInterface(IJcDataCommService.class);
        return exporter;
    }
    
    @Bean(name = "/remote/coOperateCdkeyService")
    public HessianServiceExporter coOperateCdkeyService() {
    	LottoHessianServiceExporter exporter = new LottoHessianServiceExporter();
        exporter.setService(coOperateCdkeyService);
        exporter.setServiceInterface(ICoOperateCdkeyService.class);
        return exporter;
    }    
    
    
}

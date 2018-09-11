package com.hhly.commoncore.base.util;

import com.caucho.hessian.client.HessianProxyFactory;

import java.net.MalformedURLException;

/**
 * @author Bruce Liu
 * @create on: 2016-5-11 下午05:24:53
 * @describe :
 */
public class HessianUtils {
	/**
	 * 连接超时
	 */
	private static final long CONNECT_TIME_OUT = 10000;
	/**
	 * 读取超时
	 */
	private static final long READ_TIME_OUT = 20000;
	
	/**
	 * @param <T>
	 * @param <T>
	 * @param c
	 * @param url
	 * @return 接口对象
	 * @throws MalformedURLException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getRemoteService(Class<T> c, String url) throws MalformedURLException {
		HessianProxyFactory factory = new HessianProxyFactory();
		factory.setConnectTimeout(CONNECT_TIME_OUT);
		factory.setReadTimeout(READ_TIME_OUT);
		factory.setUser("lotto_core");
		factory.setPassword("_ecai2017");
		return (T) factory.create(c, url);
	}

}

package com.hhly.commoncore.remote.cooperate.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hhly.commoncore.base.util.RedisUtil;
import com.hhly.commoncore.remote.cooperate.service.impl.CoOperateCdkeyServiceImpl;
import com.hhly.skeleton.lotto.base.cooperate.bo.CoOperateCdkeyQueueBO;
import com.hhly.skeleton.lotto.base.cooperate.vo.CoOperateCdkeyVO;

@RunWith(SpringJUnit4ClassRunner.class)    
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ICoOperateCdkeyServiceTest {

	private String key = "c_core_test_cdkey";
	@Autowired
	private CoOperateCdkeyServiceImpl coOperateCdkeyService;
	@Autowired
	private RedisTemplate<String, CoOperateCdkeyQueueBO> redisTemplate;
	@Autowired
	private RedisUtil redisUtil;

	@Before
	public void setUp() throws Exception {
		addTest();
	}

	@Test
	public void addTest() {
		redisUtil.delObj(key);
		int i = 0;
		List<CoOperateCdkeyQueueBO> cdkeyQueueBOList = new ArrayList<>();
		Date date = new Date();
		long time1 = System.currentTimeMillis();
		while (i++ < 100000) {
			CoOperateCdkeyQueueBO cdkeyBO = new CoOperateCdkeyQueueBO();
			cdkeyBO.setId(i);
			cdkeyQueueBOList.add(cdkeyBO);
		}
		long time2 = System.currentTimeMillis();
		System.out.println("*****************");
		System.out.println(time2 - time1);
		redisTemplate.opsForList().rightPushAll(key, cdkeyQueueBOList);
		long time3 = System.currentTimeMillis();
		System.out.println("*****************");
		System.out.println(time3 - time2);
		System.out.println(redisUtil.length(key));
	}

	@Test
	public void testLength() {
		List<CoOperateCdkeyQueueBO> cdkeyQueueBOList = new ArrayList<>();
		int i = 0;
		long time1 = System.currentTimeMillis();
		while (i++ < 10000) {
			cdkeyQueueBOList.add(redisTemplate.opsForList().leftPop(key));
		}
		long time2 = System.currentTimeMillis();
		System.out.println("*****************");
		System.out.println(time2 - time1);
		System.out.println("*****************");
		System.out.println(cdkeyQueueBOList.size());
	}

	@Test
	public void testLength2() {
		System.out.println("*****************");
		System.out.println(redisUtil.length(key));
		long time1 = System.currentTimeMillis();
		List<CoOperateCdkeyQueueBO> cdkeyQueueBOList = redisTemplate.opsForList().range(key, 0, 10000 - 1);
		redisTemplate.opsForList().trim(key, 10000, redisUtil.length(key));
		long time2 = System.currentTimeMillis();
		System.out.println("*****************");
		System.out.println(time2 - time1);
		System.out.println("*****************");
		System.out.println(cdkeyQueueBOList.size());
		System.out.println(redisUtil.length(key));
	}

	@Test
	public void test() {
		redisTemplate.opsForList().trim("c_core_test_cdkey", 0, 2);
	}

	@Test
	public void testLock() {
		List<Thread> threads = new ArrayList<>();
		int i = 0;
		final CoOperateCdkeyServiceImpl service = coOperateCdkeyService;
		while (i++ < 100) {
			threads.add(new Thread(new Runnable() {

				@Override
				public void run() {
					List<CoOperateCdkeyQueueBO> cdkeyList = service.findCdkeyList(100l);
					System.out.println(Thread.currentThread().getName() + ":" + cdkeyList.size());
				}

			}));
		}
		ListOperations<String, CoOperateCdkeyQueueBO> opsForList = redisTemplate.opsForList();
		Long length = opsForList.size(key);
		System.out.println("cdkeylength:" + length);
		long time1 = System.currentTimeMillis();
		threads.forEach(thread -> {
			thread.start();
		});
		long time2 = System.currentTimeMillis();
		System.out.println("*****************");
		System.out.println("线程启动完毕：" + (time2 - time1));
		try {
			Thread.sleep(60 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

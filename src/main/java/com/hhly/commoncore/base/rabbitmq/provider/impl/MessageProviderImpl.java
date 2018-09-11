package com.hhly.commoncore.base.rabbitmq.provider.impl;

import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hhly.commoncore.base.rabbitmq.provider.MessageProvider;

/**
 * 发送消息
 *
 * @author wulong
 * @create 2017/5/11 17:08
 */
@Service
public class MessageProviderImpl implements MessageProvider {
	private static final Logger logger = Logger.getLogger(MessageProviderImpl.class);
	@Autowired
	private AmqpTemplate amqpTemplate;

	/**
	 * 向消息队列中发送消息
	 * @param queueKey 队列名
	 * @param message  消息
	 */
	@Override
	public void sendMessage(String queueKey, Object message) {
		String jsonStr = "";
		if (message instanceof String) {
			jsonStr = (String) message;
		} else {
			jsonStr = JSON.toJSONString(message);
		}
		try {
			byte[] body = jsonStr.getBytes();
			MessageProperties properties = new MessageProperties();
			properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
			properties.setPriority(new Random().nextInt(10));
			Message message2 = new Message(body, properties);
			amqpTemplate.send(queueKey, message2);
		} catch (Exception e) {
			logger.error("发送消息异常！queueKey：" + queueKey + "，消息内容：" + jsonStr, e);
		}
		logger.info("发送消息成功！queueKey：" + queueKey + "，消息内容：" + jsonStr);
	}

}
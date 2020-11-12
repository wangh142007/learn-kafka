package com.hao.kafka.api.interceptor;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class CustomProducerInterceptor implements ProducerInterceptor<String, String> {

	private volatile long success = 0;
	
	private volatile long failure = 0;
	
	@Override
	public void configure(Map<String, ?> configs) {
	}

	//	发送消息之前的切面拦截
	@Override
	public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
		System.err.println("----------- 生产者发送消息前置拦截器 ----------");
		String modifyValue = "prefix-" + record.value();
		return new ProducerRecord<String, String>(record.topic(),
				record.partition(),
				record.timestamp(),
				record.key(), 
				modifyValue, 
				record.headers());
	}

	//	发送消息之后的切面拦截
	@Override
	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
		System.err.println("----------- 生产者发送消息后置拦截器 ----------");
		if(null == exception) {
			success ++;
		} else {
			failure ++;
		}
	}

	@Override
	public void close() {
		double successRatio = (double)success/(success + failure);
		System.err.println(String.format("生产者关闭，发送消息的成功率为：%s %%", successRatio * 100));
	}

}

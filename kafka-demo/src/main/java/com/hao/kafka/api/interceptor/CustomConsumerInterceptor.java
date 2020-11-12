package com.hao.kafka.api.interceptor;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

public class CustomConsumerInterceptor implements ConsumerInterceptor<String, String> {

	@Override
	public void configure(Map<String, ?> configs) {
		// TODO Auto-generated method stub
		
	}

	//	onConsume：消费者接到消息处理之前的拦截器
	@Override
	public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> records) {
		System.err.println("------  消费者前置处理器，接收消息   --------");
		return records;
	}

	@Override
	public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
		offsets.forEach((tp, offset) -> {
			System.err.println("消费者处理完成，" + "分区:" + tp + ", 偏移量：" + offset);
		});
	}

	@Override
	public void close() {
	}

}

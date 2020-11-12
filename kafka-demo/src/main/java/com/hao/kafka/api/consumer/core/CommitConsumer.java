package com.hao.kafka.api.consumer.core;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.hao.kafka.api.Const;

public class CommitConsumer {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.11.221:9092");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "core-group");
		
		//	使用手工方式提交
		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		
//		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
//		properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 5000);
		
		//	消费者默认每次拉取的位置：从什么位置开始拉取消息
		//	AUTO_OFFSET_RESET_CONFIG 有三种方式： "latest", "earliest", "none"
		//	none
		//	latest	从一个分区的最后提交的offset开始拉取消息
		//	earliset	从最开始的起始位置拉取消息 0
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "");
		
		
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
		
		//	对于consume消息的订阅 subscribe方法 ：可以订阅一个 或者  多个 topic
		consumer.subscribe(Collections.singletonList(Const.TOPIC_CORE));
		
		System.err.println("core consumer started...");
		
		try {
			while(true) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
				for(TopicPartition topicPartition : records.partitions()) {
					List<ConsumerRecord<String, String>> partitionRecords = records.records(topicPartition);
					String topic = topicPartition.topic();
					int size = partitionRecords.size();
					System.err.println(String.format("--- 获取topic: %s, 分区位置：%s, 消息总数： %s", topic, topicPartition.partition(), size));
					for(int i = 0; i < size; i++) {
						ConsumerRecord<String, String> consumerRecord = partitionRecords.get(i);
						String value = consumerRecord.value();
						long offset = consumerRecord.offset();
						long commitOffser = offset + 1;
						System.err.println(String.format("获取实际消息 value：%s, 消息offset: %s, 提交offset: %s", value, offset, commitOffser));
						//	在一个partition内部，每一条消息记录 进行一一提交方式
//						consumer.commitSync(Collections.singletonMap(topicPartition, new OffsetAndMetadata(commitOffser)));
						consumer.commitAsync(
								Collections.singletonMap(topicPartition, new OffsetAndMetadata(commitOffser)),
								new OffsetCommitCallback() {
									@Override
									public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets,
											Exception exception) {
										if (null != exception) {
											System.err.println("error . 处理");
										}
										System.err.println("异步提交成功：" + offsets);
									}
								});
					
					}
					//	一个partition做一次提交动作
				}
				
				/**
				//	整体提交：同步方式
//				consumer.commitSync();
				
				//	整体提交：异步方式
				consumer.commitAsync(new OffsetCommitCallback() {
					@Override
					public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
						if(null != exception) {
							System.err.println("error . 处理");
						}
						System.err.println("异步提交成功：" + offsets);
					}
				});
				*/
				
			}			
		} finally {
			consumer.close();
		}
	}
}

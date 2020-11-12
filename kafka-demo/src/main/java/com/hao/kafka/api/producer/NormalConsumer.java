package com.hao.kafka.api.producer;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.hao.kafka.api.Const;

public class NormalConsumer {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.11.221:9092");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "normal-group");
		properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10000);
		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 5000);
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
		consumer.subscribe(Collections.singletonList(Const.TOPIC_NORMAL));
		System.err.println("normal consumer started...");
		
		try {
			while(true) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
				for(TopicPartition topicPartition : records.partitions()) {
					List<ConsumerRecord<String, String>> partitionRecords = records.records(topicPartition);
					String topic = topicPartition.topic();
					int size = partitionRecords.size();
					System.err.println(String.format("--- 获取topic: %s, 分区位置：%s, 消息总数： %s", topic,topicPartition.partition(), size)); 
					for(int i = 0; i < size; i++) {
						ConsumerRecord<String, String> consumerRecord = partitionRecords.get(i);
						String value = consumerRecord.value();
						long offset = consumerRecord.offset();
						long commitOffser = offset + 1;
						System.err.println(String.format("获取实际消息 value：%s, 消息offset: %s, 提交offset: %s", value, offset, commitOffser));
					}
				}
			}			
		} finally {
			consumer.close();
		}
	}
}

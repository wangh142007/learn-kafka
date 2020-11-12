package com.hao.kafka.api.consumer.rebalance;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import com.hao.kafka.api.Const;

public class RebalanceConsumer2 {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.11.221:9092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "rebalance-group"); 
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true); 
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000"); 
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer"); 
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer"); 
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props); 
		//	订阅主题
		//	订阅主题
		consumer.subscribe(Collections.singletonList(Const.TOPIC_REBALANCE), new ConsumerRebalanceListener() {
			@Override
			public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
				System.err.println("Revoked Partitions:" + partitions);
			}
			
			@Override
			public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
				System.err.println("AssignedAssigned Partitions:" + partitions);
			}
		});
		
		System.err.println("rebalance consumer2 started.. ");
		try {
	        while (true) {
	            // 	拉取结果集
	        	ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
	            for (TopicPartition partition : records.partitions()) {
	                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
	                String topic = partition.topic();
	                int size = partitionRecords.size();
	                System.err.println(String.format("---- 获取topic: %s, 分区位置：%s, 消息数为：%s ----",topic, partition.partition(), size));
	                for (int i = 0; i< size; i++) {
	                    long offset = partitionRecords.get(i).offset() + 1;
		                System.err.println(String.format("获取value: %s, 提交的 offset: %s", 
		                		partitionRecords.get(i).value(), offset)); 
	                }
	            }
	        }			
		} finally {
			consumer.close();
		}
	}
}

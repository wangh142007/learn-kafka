package com.hao.kafka.api.consumer.multithread2;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import com.hao.kafka.api.Const;

public class Mt2Test {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.11.221:9092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "mt2-group"); 
		//	手工提交
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); 
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer"); 
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer"); 
		String topic = Const.TOPIC_MT2;
		
		
	}
}

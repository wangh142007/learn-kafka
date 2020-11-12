package com.hao.kafka.api.consumer.multithread2;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.alibaba.fastjson.JSON;
import com.hao.kafka.api.Const;
import com.hao.kafka.api.User;

public class Mt2Producer {

	public static void main(String[] args) {
		
		Properties props = new Properties(); 
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.11.221:9092"); 
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "mt2-producer"); 
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer"); 
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		KafkaProducer<String, String> producer = new KafkaProducer<>(props);	
		
		for(int i = 0 ; i < 10; i ++) {
			User user = new User();
			user.setId(i+"");
			user.setName("张三");
			producer.send(new ProducerRecord<>(Const.TOPIC_MT2, JSON.toJSONString(user)));	
		}
		
		producer.close();
		
	}

	
}

package com.hao.kafka.api.partition;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.alibaba.fastjson.JSON;
import com.hao.kafka.api.Const;
import com.hao.kafka.api.User;

public class PartitionProducer {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.11.221:9092");
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, "partition-producer");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		//	添加配置属性，自定义分区器
		properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());
		
		KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
		for(int i = 0; i <10; i ++) {
			User user = new User("00" + i, "张三");
			ProducerRecord<String, String> record = 
					new ProducerRecord<String, String>(Const.TOPIC_PARTITION,
							JSON.toJSONString(user));
			producer.send(record);			
		}
		producer.close();
	}
}

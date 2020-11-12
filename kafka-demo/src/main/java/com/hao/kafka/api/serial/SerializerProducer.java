package com.hao.kafka.api.serial;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.hao.kafka.api.Const;
import com.hao.kafka.api.User;

public class SerializerProducer {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.11.221:9092");
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, "serial-producer");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		//	添加序列化 value
//		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UserSerializer.class.getName());
		
		KafkaProducer<String, User> producer = new KafkaProducer<>(properties);
		for(int i = 0; i <10; i ++) {
			User user = new User("00" + i, "张三");
			ProducerRecord<String, User> record = 
					new ProducerRecord<String, User>(Const.TOPIC_SERIAL, user);
			producer.send(record);			
		}
		producer.close();
	}
}

package com.hao.kafka.api.quickstart;

import java.util.Properties;

import com.hao.kafka.api.Const;
import com.hao.kafka.api.User;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.alibaba.fastjson.JSON;

public class QuickStartProducer {

	public static void main(String[] args) {
		Properties properties = new Properties();
		//	1.配置生产者启动的关键属性参数
		
		//	1.1	BOOTSTRAP_SERVERS_CONFIG：连接kafka集群的服务列表，如果有多个，使用"逗号"进行分隔
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.228.130:9092");
		//	1.2	CLIENT_ID_CONFIG：这个属性的目的是标记kafkaclient的ID
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, "quickstart-producer");
		//	1.3 KEY_SERIALIZER_CLASS_CONFIG VALUE_SERIALIZER_CLASS_CONFIG
		//	Q: 对 kafka的 key 和 value 做序列化，为什么需要序列化？
		//	A: 因为KAFKA Broker 在接收消息的时候，必须要以二进制的方式接收，所以必须要对KEY和VALUE进行序列化
		//	字符串序列化类：org.apache.kafka.common.serialization.StringSerializer
		//	KEY: 是kafka用于做消息投递计算具体投递到对应的主题的哪一个partition而需要的
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		//	VALUE: 实际发送消息的内容
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		//	2.创建kafka生产者对象 传递properties属性参数集合
		KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
		
		for(int i = 0; i <10; i ++) {
			//	3.构造消息内容
			User user = new User("00" + i, "张三");
			ProducerRecord<String, String> record = 
					//	arg1：topic , arg2：实际的消息体内容
					new ProducerRecord<String, String>(Const.TOPIC_QUICKSTART,
							JSON.toJSONString(user));
			
			//	4.发送消息
			producer.send(record);			
		}

		
		//	5.关闭生产者
		producer.close();
		
	}
}

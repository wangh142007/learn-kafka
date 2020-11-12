package com.hao.kafka.api.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import com.alibaba.fastjson.JSON;
import com.hao.kafka.api.Const;
import com.hao.kafka.api.User;

public class NormalProducer {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.11.221:9092");
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, "normal-producer");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		//	kafka 消息的重试机制: RETRIES_CONFIG该参数默认是0： 
		properties.put(ProducerConfig.RETRIES_CONFIG, 3);
		
		//	可重试异常, 意思是执行指定的重试次数 如果到达重试次数上限还没有发送成功， 也会抛出异常信息
		//	NetworkException
		//	LeaderNotAvailableException
		
		//	不可重试异常
		//	RecordTooLargeException
		
		KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

		User user = new User("001", "xiao xiao");
		//	Const.TOPIC_NORMAL 是一个新的主题，kafka默认是可以在没有主题的情况下创建的
		//	自动创建主题的特性，在生产环境中一定是禁用的
		ProducerRecord<String, String> producerRecord = 
				new ProducerRecord<String, String>(Const.TOPIC_NORMAL, JSON.toJSONString(user));
		/**
		 * //一条消息 必须通过key 去计算出来实际的partition, 按照partition去存储的
		 * ProducerRecord(
		 * topic=topic_normal, 
		 * partition=null, 
		 * headers=RecordHeaders(headers = [], isReadOnly = false), 
		 * key=null, 
		 * value={"id":"001","name":"xiao xiao"}, 
		 * timestamp=null)
		 */
		System.err.println("新创建消息：" + producerRecord);
		
		
		//	一个参数的send方法 本质上也是异步的 返回的是一个future对象; 可以实现同步阻塞方式
		/**
		Future<RecordMetadata> future = producer.send(producerRecord);
		RecordMetadata recordMetadata = future.get();
		System.err.println(String.format("分区：%s, 偏移量: %s, 时间戳: %s",
				recordMetadata.partition(),
				recordMetadata.offset(),
				recordMetadata.timestamp()));
		*/
		
		//	带有两个参数的send方法 是完全异步化的。在回调Callback方法中得到发送消息的结果
		producer.send(producerRecord, new Callback() {
			@Override
			public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
				if(null != exception) {
					exception.printStackTrace();
					return;
				}
				System.err.println(String.format("分区：%s, 偏移量: %s, 时间戳: %s",
						recordMetadata.partition(),
						recordMetadata.offset(),
						recordMetadata.timestamp()));				
			}
		});
		
		producer.close();
		
	}
	
}

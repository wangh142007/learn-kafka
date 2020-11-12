package com.hao.kafka.api.partition;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

public class CustomPartitioner implements Partitioner {
	
	private AtomicInteger counter = new AtomicInteger(0);
	
	@Override
	public void configure(Map<String, ?> configs) {
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes,  
			Object value, byte[] valueBytes, Cluster cluster) {
		List<PartitionInfo> partitionList = cluster.partitionsForTopic(topic);
		int numOfPartition = partitionList.size();
		System.err.println("---- 进入自定义分区器，当前分区个数：" + numOfPartition);
		if(null == keyBytes) {
			return counter.getAndIncrement() % numOfPartition;
		} else {
			return Utils.toPositive(Utils.murmur2(keyBytes)) % numOfPartition;
		}
	}

	@Override
	public void close() {
	}

}

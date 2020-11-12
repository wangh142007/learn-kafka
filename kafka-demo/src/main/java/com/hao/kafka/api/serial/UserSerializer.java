package com.hao.kafka.api.serial;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.hao.kafka.api.User;

public class UserSerializer implements Serializer<User> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public byte[] serialize(String topic, User user) {
		if(null == user) {
			return null;
		}
		byte[] idBytes, nameBytes;
		try {
			String id = user.getId();
			String name = user.getName();
			if(id != null) {
				idBytes = id.getBytes("UTF-8");
			} else {
				idBytes = new byte[0];
			}
			if(name != null) {
				nameBytes = name.getBytes("UTF-8");
			} else {
				nameBytes = new byte[0];
			}	
			
			ByteBuffer buffer = ByteBuffer.allocate(4 + 4 + idBytes.length + nameBytes.length);
			//	4个字节 也就是一个 int类型 : putInt 盛放 idBytes的实际真实长度
			buffer.putInt(idBytes.length);
			//	put bytes[] 实际盛放的是idBytes真实的字节数组，也就是内容
			buffer.put(idBytes);
			buffer.putInt(nameBytes.length);
			buffer.put(nameBytes);
			return buffer.array();
			
		} catch (UnsupportedEncodingException e) {
			//	handle exption...
		}
		return new byte[0];
	}

	@Override
	public void close() {
	}

}

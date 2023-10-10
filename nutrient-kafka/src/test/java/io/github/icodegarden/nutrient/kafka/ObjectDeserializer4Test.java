package io.github.icodegarden.nutrient.kafka;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class ObjectDeserializer4Test implements Deserializer<Object> {
	@Override
	public Object deserialize(String topic, byte[] data) {
		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
				ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);) {
			return objectInputStream.readObject();
		} catch (Exception e) {
			throw new SerializationException(
					String.format("Error when deserializing byte[] to OrderDetail,topic:%s", topic));
		}
	}
}
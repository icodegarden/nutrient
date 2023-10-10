package io.github.icodegarden.nutrient.kafka;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class ObjectSerializer4Test implements Serializer<Serializable> {
	@Override
	public byte[] serialize(String topic, Serializable data) {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);) {

			objectOutputStream.writeObject(data);
			return byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			throw new SerializationException(
					String.format("Error when serializing %s to byte[],topic:%s", data.getClass().getName(), topic));
		}
	}
}
package io.github.icodegarden.nutrient.nio;

import io.github.icodegarden.nutrient.lang.serialization.Deserializer;
import io.github.icodegarden.nutrient.lang.serialization.Hessian2Deserializer;
import io.github.icodegarden.nutrient.lang.serialization.Hessian2Serializer;
import io.github.icodegarden.nutrient.lang.serialization.JavaDeserializer;
import io.github.icodegarden.nutrient.lang.serialization.JavaSerializer;
import io.github.icodegarden.nutrient.lang.serialization.KryoDeserializer;
import io.github.icodegarden.nutrient.lang.serialization.KryoSerializer;
import io.github.icodegarden.nutrient.lang.serialization.Serializer;
import lombok.Getter;

/**
 * 
 * @author Fangfang.Xu
 *
 */
@Getter
public enum SerializerType {

	Kryo((byte)0, new KryoSerializer(), new KryoDeserializer()), //
	Jdk((byte)1, new JavaSerializer(), new JavaDeserializer()),//
	Hessian2((byte)2, new Hessian2Serializer(), new Hessian2Deserializer()),//
//	Json((byte)3, new JsonSerializer(), new JsonDeserializer()), //
	;

	private final byte value;
	private final Serializer<Object> serializer;
	private final Deserializer<Object> deserializer;

	private SerializerType(byte value, Serializer<Object> serializer, Deserializer<Object> deserializer) {
		this.value = value;
		this.serializer = serializer;
		this.deserializer = deserializer;
	}

	public static SerializerType get(byte value) {
		SerializerType[] serializerTypes = SerializerType.values();
		for (SerializerType serializerType : serializerTypes) {
			if (serializerType.getValue() == value) {
				return serializerType;
			}
		}
		throw new IllegalArgumentException("SerializerType of value:" + value + " Not Support");
	}
}

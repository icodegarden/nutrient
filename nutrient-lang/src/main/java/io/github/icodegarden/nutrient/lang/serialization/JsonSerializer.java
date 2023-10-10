package io.github.icodegarden.nutrient.lang.serialization;

import io.github.icodegarden.nutrient.lang.util.JsonUtils;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class JsonSerializer implements Serializer<Object> {

	private final StringSerializer serializer = new StringSerializer();

	@Override
	public byte[] serialize(Object obj) throws SerializationException {
		String json = JsonUtils.serialize(obj);
		return serializer.serialize(json);
	}
}
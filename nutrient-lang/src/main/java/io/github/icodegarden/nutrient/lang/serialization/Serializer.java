package io.github.icodegarden.nutrient.lang.serialization;

/**
 * 
 * @author Fangfang.Xu
 *
 * @param <T>
 */
public interface Serializer<T> {

	/**
	 * Convert {@code data} into a byte array.
	 *
	 * @param data typed data
	 * @return serialized bytes
	 */
	byte[] serialize(T data) throws SerializationException;
}
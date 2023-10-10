package io.github.icodegarden.nutrient.lang.serialization;

import io.github.icodegarden.nutrient.lang.serialization.StringDeserializer;
import io.github.icodegarden.nutrient.lang.serialization.StringSerializer;

/**
 * 
 * @author Fangfang.Xu
 *
 */
//@RunWith(MockitoJUnitRunner.class)
public class StringSerializerTests extends SerializerTests{
	
	String s = "test string Serializer";
	
	@Override
	protected Object getData() {
		return s;
	}
	
	@Override
	protected StringSerializer getSerializer() {
		return new StringSerializer();
	}
	
	@Override
	protected StringDeserializer getDeserializer() {
		return new StringDeserializer();
	}
	
}

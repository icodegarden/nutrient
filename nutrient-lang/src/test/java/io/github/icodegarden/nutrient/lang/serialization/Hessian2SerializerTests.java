package io.github.icodegarden.nutrient.lang.serialization;

import io.github.icodegarden.nutrient.lang.serialization.Hessian2Deserializer;
import io.github.icodegarden.nutrient.lang.serialization.Hessian2Serializer;

/**
 * 
 * @author Fangfang.Xu
 *
 */
//@RunWith(MockitoJUnitRunner.class)
public class Hessian2SerializerTests extends SerializerTests {

	UserForTests d = new UserForTests("name", 18);

	@Override
	protected Object getData() {
		return d;
	}
	
	/**
	 * hessian2 必须对象实现Serializable，兼容性方面则很ok，可以增、删字段
	 */

	@Override
	protected Hessian2Serializer getSerializer() {
		return new Hessian2Serializer();
	}

	@Override
	protected Hessian2Deserializer getDeserializer() {
		return new Hessian2Deserializer();
	}

}

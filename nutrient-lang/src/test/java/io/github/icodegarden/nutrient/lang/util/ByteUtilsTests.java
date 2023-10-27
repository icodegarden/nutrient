package io.github.icodegarden.nutrient.lang.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author Fangfang.Xu
 *
 */
class ByteUtilsTests {

	@Test
	void booleanToBytes() throws Exception {
		byte[] bs = ByteUtils.booleanToBytes(true);
		assertThat(bs).isNotNull();
		assertThat(ByteUtils.bytesToBoolean(bs)).isEqualTo(true);

		bs = ByteUtils.booleanToBytes(false);
		assertThat(bs).isNotNull();
		assertThat(ByteUtils.bytesToBoolean(bs)).isEqualTo(false);
	}

	@Test
	void charToBytes() throws Exception {
		byte[] bs = ByteUtils.charToBytes(Character.MAX_VALUE);
		assertThat(bs).isNotNull();
		assertThat(ByteUtils.bytesToChar(bs)).isEqualTo(Character.MAX_VALUE);

		bs = ByteUtils.charToBytes(Character.MIN_VALUE);
		assertThat(bs).isNotNull();
		assertThat(ByteUtils.bytesToChar(bs)).isEqualTo(Character.MIN_VALUE);
	}
	
	@Test
	void shortToBytes() throws Exception {
		byte[] bs = ByteUtils.shortToBytes(Short.MAX_VALUE);
		assertThat(bs).isNotNull();
		assertThat(ByteUtils.bytesToShort(bs)).isEqualTo(Short.MAX_VALUE);

		bs = ByteUtils.shortToBytes(Short.MIN_VALUE);
		assertThat(bs).isNotNull();
		assertThat(ByteUtils.bytesToShort(bs)).isEqualTo(Short.MIN_VALUE);
	}

	@Test
	void intToBytes() throws Exception {
		byte[] bs = ByteUtils.intToBytes(Integer.MAX_VALUE);
		assertThat(bs).isNotNull();
		assertThat(ByteUtils.bytesToInt(bs)).isEqualTo(Integer.MAX_VALUE);

		bs = ByteUtils.intToBytes(Integer.MIN_VALUE);
		assertThat(bs).isNotNull();
		assertThat(ByteUtils.bytesToInt(bs)).isEqualTo(Integer.MIN_VALUE);
	}

	@Test
	void longToBytes() throws Exception {
		byte[] bs = ByteUtils.longToBytes(Long.MIN_VALUE);
		assertThat(bs).isNotNull();
		assertThat(ByteUtils.bytesToLong(bs)).isEqualTo(Long.MIN_VALUE);

		bs = ByteUtils.longToBytes(Long.MAX_VALUE);
		assertThat(bs).isNotNull();
		assertThat(ByteUtils.bytesToLong(bs)).isEqualTo(Long.MAX_VALUE);

		bs = ByteUtils.longToBytes(0);
		assertThat(bs).isNotNull();
		assertThat(ByteUtils.bytesToLong(bs)).isEqualTo(0);
	}

	@Test
	void floatToBytes() throws Exception {
		byte[] bs = ByteUtils.floatToBytes(Float.MIN_VALUE);
		assertThat(bs).isNotNull();
		assertThat(ByteUtils.bytesToFloat(bs)).isEqualTo(Float.MIN_VALUE);

		bs = ByteUtils.floatToBytes(Float.MAX_VALUE);
		assertThat(bs).isNotNull();
		assertThat(ByteUtils.bytesToFloat(bs)).isEqualTo(Float.MAX_VALUE);

		bs = ByteUtils.floatToBytes(0);
		assertThat(bs).isNotNull();
		assertThat(ByteUtils.bytesToFloat(bs)).isEqualTo(0);
	}

	@Test
	void doubleToBytes() throws Exception {
		byte[] bs = ByteUtils.doubleToBytes(Double.MIN_VALUE);
		assertThat(bs).isNotNull();
		assertThat(ByteUtils.bytesToDouble(bs)).isEqualTo(Double.MIN_VALUE);

		bs = ByteUtils.doubleToBytes(Double.MAX_VALUE);
		assertThat(bs).isNotNull();
		assertThat(ByteUtils.bytesToDouble(bs)).isEqualTo(Double.MAX_VALUE);

		bs = ByteUtils.doubleToBytes(0);
		assertThat(bs).isNotNull();
		assertThat(ByteUtils.bytesToDouble(bs)).isEqualTo(0);
	}
}

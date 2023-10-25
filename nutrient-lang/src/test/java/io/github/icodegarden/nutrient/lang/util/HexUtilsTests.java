package io.github.icodegarden.nutrient.lang.util;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author Fangfang.Xu
 *
 */
class HexUtilsTests {

	@Test
	void booleanToBytes() throws Exception {
		String d0d1 = "X02149018-5D943CEE";

		d0d1 = HexUtils.stringToHexStr(HexUtils.stringToHexStr(d0d1));
		System.out.println(d0d1);

		String d0d0 = "1130B";

		d0d0 = HexUtils.stringToHexStr(HexUtils.stringToHexStr(d0d0));
		System.out.println(d0d0);

		System.out.println(HexUtils.hexStrToBytes("0xFF")[0]);
		System.out.println(HexUtils.hexStrToBytes("0x1a")[0]);
	}
}

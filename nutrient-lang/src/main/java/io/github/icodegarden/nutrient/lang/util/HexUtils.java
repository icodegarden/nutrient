package io.github.icodegarden.nutrient.lang.util;

import org.springframework.util.ObjectUtils;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class HexUtils {

	/**
	 * 16进制的char （2个一组）转byte(int)转ascii char
	 */
	public static char[] hexStrToChar(String hex) {
		char[] hexCharArray = hex.toCharArray();
		char[] cs = new char[hexCharArray.length / 2];
		int j = 0;
		for (int i = 0; i < hexCharArray.length; i += 2) {
			char c = (char) Integer.parseInt(new String(new char[] { hexCharArray[i], hexCharArray[i + 1] }), 16);
			cs[j++] = c;
		}
		return cs;
	}

	/**
	 * @param i
	 * @return 15=0F 255=FF
	 */
	public static String intToHexStr(int i) {
		String upperCase = Integer.toHexString(i).toUpperCase();
		if (upperCase.length() % 2 != 0) {
			upperCase = "0" + upperCase;
		}
		return upperCase;
	}

	/**
	 * 
	 * @param s
	 * @return A=16进制数字 AA=16进制数字16进制数字
	 */
	public static String stringToHexStr(String s) {
		if (ObjectUtils.isEmpty(s)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		char[] charArray = s.toCharArray();
		for (char ch : charArray) {
			String hex = intToHexStr(ch);
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param s
	 * @return 202041=空格空格字母
	 */
	public static String hexStrToString(String s) {
		if (ObjectUtils.isEmpty(s)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length() / 2; i++) {
			sb.append((char) Integer.parseInt(s.substring(i * 2, (i + 1) * 2), 16));
		}
		return sb.toString();
	}

	public static byte[] hexStrToBytes(String hex) {
		if (hex.startsWith("0x")) {
			hex = hex.substring(2);
		}

		int m = 0, n = 0;
		int byteLen = hex.length() / 2;
		byte[] ret = new byte[byteLen];
		for (int i = 0; i < byteLen; i++) {
			m = i * 2 + 1;
			n = m + 1;
			int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
			ret[i] = Byte.valueOf((byte) intVal);
		}
		return ret;
	}

	public static String bytesToHexStr(byte[] bytes) {
		String strHex = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < bytes.length; n++) {
			strHex = Integer.toHexString(bytes[n] & 0xFF);
			sb.append((strHex.length() == 1) ? "0" + strHex : strHex);
		}
		return sb.toString().toUpperCase();
	}

}

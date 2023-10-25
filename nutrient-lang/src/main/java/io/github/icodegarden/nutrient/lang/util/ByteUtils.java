package io.github.icodegarden.nutrient.lang.util;

import java.nio.ByteBuffer;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class ByteUtils {

	public static byte[] booleanToBytes(boolean b) {
		return b ? new byte[] { 0x01 } : new byte[] { 0x00 };
	}

	public static boolean bytesToBoolean(byte[] bytes) {
		return bytes[0] == 0x01;
	}

	public static byte[] shortToBytes(short s) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(2);

		byteBuffer.putShort(s);

		return byteBuffer.array();
	}

	public static short bytesToShort(byte[] bytes) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(2);

		byteBuffer.put(bytes);

		byteBuffer.flip();
		return byteBuffer.getShort();
	}

	public static byte[] intToBytes(int i) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(4);

		byteBuffer.putInt(i);

		return byteBuffer.array();
	}

	public static int bytesToInt(byte[] bytes) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(4);

		byteBuffer.put(bytes);

		byteBuffer.flip();
		return byteBuffer.getInt();
	}

	public static byte[] longToBytes(long l) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(8);

		byteBuffer.putLong(l);

		return byteBuffer.array();
	}

	public static long bytesToLong(byte[] bytes) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(8);

		byteBuffer.put(bytes);

		byteBuffer.flip();
		return byteBuffer.getLong();
	}

	public static byte[] floatToBytes(float f) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(4);

		byteBuffer.putFloat(f);

		return byteBuffer.array();
	}

	public static float bytesToFloat(byte[] bytes) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(4);

		byteBuffer.put(bytes);

		byteBuffer.flip();
		return byteBuffer.getFloat();
	}

	public static byte[] doubleToBytes(double d) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(8);

		byteBuffer.putDouble(d);

		return byteBuffer.array();
	}

	public static double bytesToDouble(byte[] bytes) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(8);

		byteBuffer.put(bytes);

		byteBuffer.flip();
		return byteBuffer.getDouble();
	}
}

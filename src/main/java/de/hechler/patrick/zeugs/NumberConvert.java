package de.hechler.patrick.zeugs;

public class NumberConvert {
	
	private NumberConvert() {}
	
	
	
	public static byte[] intToByteArr(int val) {
		byte[] bytes = new byte[4];
		intToByteArr(val, bytes, 0);
		return bytes;
	}
	
	public static void intToByteArr(int val, byte[] bytes, int off) {
		bytes[off] = (byte) val;
		bytes[off + 1] = (byte) (val >> 8);
		bytes[off + 2] = (byte) (val >> 16);
		bytes[off + 3] = (byte) (val >> 24);
	}
	
	
	public static int byteArrToInt(byte[] bytes) {
		return byteArrToInt(bytes, 0);
	}
	
	public static int byteArrToInt(byte[] bytes, int off) {
		int val = 0xFF & (int) bytes[off];
		val |= (0xFF & (int) bytes[off + 1]) << 8;
		val |= (0xFF & (int) bytes[off + 2]) << 16;
		val |= (0xFF & (int) bytes[off + 3]) << 24;
		return val;
	}
	
	
	public static byte[] longToByteArr(long val) {
		byte[] bytes = new byte[8];
		longToByteArr(val, bytes, 0);
		return bytes;
	}
	
	public static void longToByteArr(long val, byte[] bytes, int off) {
		bytes[off] = (byte) val;
		bytes[off + 1] = (byte) (val >> 8);
		bytes[off + 2] = (byte) (val >> 16);
		bytes[off + 3] = (byte) (val >> 24);
		bytes[off + 4] = (byte) (val >> 32);
		bytes[off + 5] = (byte) (val >> 40);
		bytes[off + 6] = (byte) (val >> 48);
		bytes[off + 7] = (byte) (val >> 56);
	}
	
	
	
	public static long byteArrToLong(byte[] bytes) {
		return byteArrToLong(bytes, 0);
	}
	
	public static long byteArrToLong(byte[] bytes, int off) {
		long val = ((long) bytes[off]) & 0xFFL;
		val |= (0xFFL & (long) bytes[off + 1]) << 8;
		val |= (0xFFL & (long) bytes[off + 2]) << 16;
		val |= (0xFFL & (long) bytes[off + 3]) << 24;
		val |= (0xFFL & (long) bytes[off + 4]) << 32;
		val |= (0xFFL & (long) bytes[off + 5]) << 40;
		val |= (0xFFL & (long) bytes[off + 6]) << 48;
		val |= (0xFFL & (long) bytes[off + 7]) << 56;
		return val;
	}
	
}

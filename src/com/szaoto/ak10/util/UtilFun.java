/*
 * 文件名 UtilFun.java
 * 包含类名列表com.szaoto.ak10.util
 * 版本信息，版本号
 * 创建日期2013年12月30日下午7:03:49
 * 版权声明 liangdb-szaoto
 */
package com.szaoto.ak10.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import com.szaoto.ak10.common.RECT;
import android.graphics.Color;
import android.graphics.Rect;
import java.util.zip.CRC32;

/*
 * 类名UtilFun
 * 作者 liangdb
 * 主要功能
 * 创建日期2013年12月30日
 * 修改者，修改日期，修改内容
 */
public class UtilFun {

	/**
	 * 
	 */
	public UtilFun() {
		
	}

	public static byte[] CopyOfRange(byte[] buf, int from, int to) {
		int newLength = to - from;
		if (newLength < 0)
			throw new IllegalArgumentException(from + " > " + to);
		byte[] copy = new byte[newLength];
		System.arraycopy(buf, from, copy, 0,
				Math.min(buf.length - from, newLength));

		return copy;
	}

	public static byte[] getBytes(char[] chars) {
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);

		return bb.array();

	}

	/**
	 * 
	 */

	public static byte[] FromBytestringToByteArray(String strSource) {

		String[] strTmp = new String[6];

		strTmp = strSource.split("-");

		byte[] byteArr = new byte[6];

		for (int i = 0; i < 6; i++) {
			byteArr[i] = Byte.valueOf(strTmp[i]);
		}

		return byteArr;
	}

	public static byte[] CRC32(byte[] data , int length) {

		byte[] byteRet = new byte[4];
		int crc_data;
		int crc_crc;
		int i, j;
		int[] crc_table = new int[256];

		for (i = 0; i < 256; i++) {
			crc_data = i;
			crc_crc = 0;
			for (j = 0; j < 8; j++) {
				int nResult = (crc_data ^ (crc_crc >> 24)) & 0x80;
				if (nResult != 0) {
					crc_crc = (crc_crc << 1) ^ 0x04c11db7;
				} else {
					crc_crc <<= 1;
				}
				crc_data <<= 1;
			}
			crc_table[i] = crc_crc;
		}

		int crc_residue = 0xffffffff;

		for (i = 0; i < length; i++) {
			int tmp = crc_residue >> 24;
			int index = (data[i] ^ tmp) & 0xff;

			crc_residue = (crc_residue << 8) ^ crc_table[index];
		}

		int nTmp = crc_residue ^ 0xffffffff;

		byteRet[0] = (byte) ((nTmp & 0xff000000) >> 24);
		byteRet[1] = (byte) ((nTmp & 0xff0000) >> 16);
		byteRet[2] = (byte) ((nTmp & 0xff00) >> 8);
		byteRet[3] = (byte) (nTmp & 0xff);

		return byteRet;

	}

	public static byte[] GetCrc32Bytes(byte[] data) {
		byte[] byteRet = new byte[4];
		CRC32 crc32 = new CRC32();
		crc32.update(data);
		long lRet = crc32.getValue();

		byteRet[0] = (byte) ((lRet & 0xff000000) >> 24);
		byteRet[1] = (byte) ((lRet & 0xff0000) >> 16);
		byteRet[2] = (byte) ((lRet & 0xff00) >> 8);
		byteRet[3] = (byte) (lRet & 0xff);

		return byteRet;
	}

	/**
	 * Convert byte to byte
	 * 
	 * @param hexChars
	 *            byte
	 * @return byte
	 */
	public static String FormatRectToString(Rect rect) {// "left,right,top,bottom"
		String string = null;
		string = String.valueOf(rect.left) + "," + String.valueOf(rect.right)
				+ "," + String.valueOf(rect.top) + ","
				+ String.valueOf(rect.bottom);
		return string;
	}

	public static RECT FormatStringToRect(String str) {
		String[] strarray = str.split(",");

		int left = Integer.valueOf(strarray[0]);
		int top = Integer.valueOf(strarray[1]);
		int right = Integer.valueOf(strarray[2]);
		int bottom = Integer.valueOf(strarray[3]);

		return new RECT(left, top, right, bottom);
	}

	public static int f2i(float fValue) {

		return (int) ((fValue * 10 + 5) / 10);
	}

	public static int GetColorById(int id) {

		// 枚举28种颜色
		HashMap<String, Integer> tHashMap = new HashMap<String, Integer>();

		tHashMap.put("2001", Color.argb(200, 128, 50, 0));
		tHashMap.put("2002", Color.argb(200, 128, 0, 0));
		tHashMap.put("2003", Color.argb(200, 128, 100, 0));
		tHashMap.put("2004", Color.argb(200, 128, 200, 0));
		tHashMap.put("2005", Color.argb(200, 128, 210, 0));
		tHashMap.put("2006", Color.argb(200, 128, 220, 0));
		tHashMap.put("2007", Color.argb(200, 128, 230, 0));
		tHashMap.put("2008", Color.argb(200, 128, 240, 0));
		tHashMap.put("2009", Color.argb(200, 128, 250, 0));
		tHashMap.put("2010", Color.argb(200, 128, 260, 0));

		tHashMap.put("3001", Color.argb(127, 255, 0, 255));
		tHashMap.put("3002", Color.argb(200, 128, 0, 50));
		tHashMap.put("3003", Color.argb(200, 128, 0, 100));
		tHashMap.put("3004", Color.argb(200, 128, 0, 200));
		tHashMap.put("3005", Color.argb(200, 128, 0, 210));
		tHashMap.put("3006", Color.argb(200, 128, 0, 220));
		tHashMap.put("3007", Color.argb(200, 128, 0, 230));
		tHashMap.put("3008", Color.argb(200, 128, 0, 240));
		tHashMap.put("3009", Color.argb(200, 128, 0, 250));
		tHashMap.put("3010", Color.argb(200, 128, 0, 255));

		tHashMap.put("4001", Color.argb(200, 128, 0, 240));
		tHashMap.put("4002", Color.argb(200, 128, 50, 50));
		tHashMap.put("4003", Color.argb(200, 128, 100, 100));
		tHashMap.put("4004", Color.argb(200, 128, 200, 200));
		tHashMap.put("4005", Color.argb(200, 128, 210, 200));
		tHashMap.put("4006", Color.argb(200, 128, 220, 200));
		tHashMap.put("4007", Color.argb(200, 128, 230, 200));
		tHashMap.put("4008", Color.argb(200, 128, 240, 200));
		tHashMap.put("4009", Color.argb(200, 128, 200, 210));
		tHashMap.put("4010", Color.argb(200, 128, 200, 220));

		tHashMap.put("5001", Color.argb(200, 128, 240, 240));
		tHashMap.put("5002", Color.argb(200, 128, 50, 100));
		tHashMap.put("5003", Color.argb(200, 128, 50, 200));
		tHashMap.put("5004", Color.argb(200, 128, 50, 240));
		tHashMap.put("5005", Color.argb(200, 128, 60, 240));
		tHashMap.put("5006", Color.argb(200, 128, 70, 240));
		tHashMap.put("5007", Color.argb(200, 128, 80, 240));
		tHashMap.put("5008", Color.argb(200, 128, 90, 240));
		tHashMap.put("5009", Color.argb(200, 128, 100, 240));
		tHashMap.put("5010", Color.argb(200, 128, 200, 200));

		tHashMap.put("6001", Color.argb(200, 128, 100, 150));
		tHashMap.put("6002", Color.argb(200, 128, 100, 210));
		tHashMap.put("6003", Color.argb(200, 128, 100, 240));
		tHashMap.put("6004", Color.argb(200, 50, 100, 20));
		tHashMap.put("6005", Color.argb(200, 50, 200, 20));
		tHashMap.put("6006", Color.argb(200, 50, 210, 20));
		tHashMap.put("6007", Color.argb(200, 50, 220, 20));
		tHashMap.put("6008", Color.argb(200, 50, 230, 20));
		tHashMap.put("6009", Color.argb(200, 50, 100, 200));
		tHashMap.put("6010", Color.argb(200, 50, 100, 210));

		tHashMap.put("7001", Color.argb(200, 80, 100, 80));
		tHashMap.put("7002", Color.argb(200, 80, 100, 100));
		tHashMap.put("7003", Color.argb(200, 80, 100, 120));
		tHashMap.put("7004", Color.argb(200, 50, 0, 200));
		tHashMap.put("7005", Color.argb(200, 50, 0, 100));
		tHashMap.put("7006", Color.argb(200, 50, 100, 20));
		tHashMap.put("7007", Color.argb(200, 50, 110, 20));
		tHashMap.put("7008", Color.argb(200, 50, 120, 20));
		tHashMap.put("7009", Color.argb(200, 50, 130, 20));
		tHashMap.put("7010", Color.argb(200, 50, 140, 20));

		tHashMap.put("8001", Color.argb(200, 50, 0, 100));
		tHashMap.put("8002", Color.argb(200, 50, 0, 120));
		tHashMap.put("8003", Color.argb(200, 200, 20, 0));
		tHashMap.put("8004", Color.argb(200, 200, 120, 0));
		tHashMap.put("8005", Color.argb(200, 200, 120, 0));
		tHashMap.put("8006", Color.argb(200, 200, 120, 0));
		tHashMap.put("8007", Color.argb(200, 200, 120, 0));
		tHashMap.put("8008", Color.argb(200, 200, 120, 0));
		tHashMap.put("8009", Color.argb(200, 200, 120, 0));
		tHashMap.put("8010", Color.argb(200, 200, 120, 0));

		return tHashMap.get(String.valueOf(id));
	}

	public static int f2i(double fValue) {

		return (int) ((fValue * 10 + 5) / 10);
	}

	public static RECT FormatStringToRECT(String string) {
		String[] strgroup = string.split(",");
		RECT rect = new RECT();
		rect.setLeft(Integer.valueOf(strgroup[0]));
		rect.setTop(Integer.valueOf(strgroup[1]));
		rect.setRight(Integer.valueOf(strgroup[2]));
		rect.setBottom(Integer.valueOf(strgroup[3]));

		return rect;
	}

	public static byte[] subBytes(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		for (int i = begin; i < begin + count; i++)
			bs[i - begin] = src[i];
		return bs;
	}

	private static byte charToByte(char hexChars) {
		return (byte) "0123456789ABCDEF".indexOf(hexChars);
	}

	/*
	 * Convert byte[] to hex
	 * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * 
	 * @param src byte[] data
	 * 
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	// 将byte[]转化十六进制的字符串
	public static String bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}

//	// 将byte[]转化十六进制的字符串
//	public static String bytes2HexString_V1(byte[] b) {
//		String ret = "";
//		for (int i = 0; i < b.length; i++) {
//			String hex = Integer.toHexString(b[i] & 0xFF);
//			if (hex.length() == 1) {
//				hex = '0' + hex;
//			}
//			if (b[i] == '0') {
//				break;
//			}
//			ret += hex.toUpperCase();
//		}
//		return ret;
//	}
//	
	// 将byte[]转化十六进制的字符串
	public static String bytes2HexString(byte[] b, int nLength) {
		String ret = "";
		for (int i = 0; i < nLength; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}

	// 将byte[]转化十六进制的字符串（加分割）
	public static String bytes2HexString(byte[] b, int nLength, String split) {
		String ret = "";
		for (int i = 0; i < nLength; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			if (i != nLength - 1) {
				ret += hex.toUpperCase() + split;
			} else {
				ret += hex.toUpperCase();
			}
		}
		return ret;
	}

	// 将byte[]转化十六进制的字符串（加分割，无补0）
	public static String bytes2HexStringEx(byte[] b, int nLength, String split) {
		String ret = "";
		for (int i = 0; i < nLength; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (i != nLength - 1) {
				ret += hex.toUpperCase() + split;
			} else {
				ret += hex.toUpperCase();
			}
		}
		return ret;
	}

	// 将hexString 通过 split分割成 byte[]
	public static byte[] hexStringSplit2Bytes(String hexString, String split) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		if (split == null || split.equals("")) {
			return null;
		}
		hexString = hexString.replaceAll(split, "");
		hexString = hexString.toUpperCase();

		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	// 将逗号连接的gamma字符串转换为gamma表
	public static short[] ParseGamma(String sGamma, int nLen) {
		short[] sGammaTable = new short[nLen];
		String[] ssGammaTable = sGamma.split(",");
		// short a = (short)65535;
		for (int i = 0; i < ssGammaTable.length; i++) {
			if (nLen - 1 < i) {
				break;
			}
			if (Integer.parseInt(ssGammaTable[i]) <= 32767) {
				sGammaTable[i] = Short.parseShort(ssGammaTable[i]);
			} else {
				sGammaTable[i] = Short.parseShort(String.valueOf(Integer
						.parseInt(ssGammaTable[i]) - 65536));
			}
			/*
			 * int k = Integer.parseInt(ssGammaTable[i]) - 32768; sGammaTable[i]
			 * = (short)k;
			 */
		}

		return sGammaTable;
	}
	
//	public static String StringToAscii(String value)
//	{
//		StringBuffer sbu = new StringBuffer();
//		char[] chars = value.toCharArray(); 
//		for (int i = 0; i < chars.length; i++) {
//			if(i != chars.length - 1)
//			{
//				sbu.append((int)chars[i]).append(",");
//			}
//			else {
//				sbu.append((int)chars[i]);
//			}
//		}
//		return sbu.toString();
//	}
//	
//	public static String AsciiToString(String value)
//	{
//		StringBuffer sbu = new StringBuffer();
//		String[] chars = value.split(",");
//		for (int i = 0; i < chars.length; i++) {
//			sbu.append((char) Integer.parseInt(chars[i]));
//		}
//		return sbu.toString();
//	}
}

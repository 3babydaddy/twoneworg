/**
 * 
 */
package com.tf.base.common.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * 加密解密类
 */
public class Eryptogram {
	private static String Algorithm = "DES";
	private static String strkey = "CB7A92E3D3491975";
	// 定义 加密算法,可用 DES,DESede,Blowfish
	static boolean debug = false;

	/**
	 * 构造子注解.
	 */
	public Eryptogram() {

	}

	/**
	 * 生成密钥
	 * 
	 * @return byte[] 返回生成的密钥
	 * @throws exception
	 *             扔出异常.
	 */
	public static byte[] getSecretKey() throws Exception {
		KeyGenerator keygen = KeyGenerator.getInstance(Algorithm);
		SecretKey deskey = keygen.generateKey();
//		System.out.println("生成密钥:" + bytesToHexString(deskey.getEncoded()));
		if (debug)
			System.out.println("生成密钥:" + bytesToHexString(deskey.getEncoded()));
		return deskey.getEncoded();

	}

	/**
	 * 将指定的数据根据提供的密钥进行加密
	 * 
	 * @param input
	 *            需要加密的数据
	 * @param key
	 *            密钥
	 * @return byte[] 加密后的数据
	 * @throws Exception
	 */
	public static byte[] encryptData(byte[] input, byte[] key) throws Exception {
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
		if (debug) {
			System.out.println("加密前的二进串:" + byte2hex(input));
			System.out.println("加密前的字符串:" + new String(input));

		}
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] cipherByte = c1.doFinal(input);
		if (debug)
			System.out.println("加密后的二进串:" + byte2hex(cipherByte));
		return cipherByte;

	}

	/**
	 * 将给定的已加密的数据通过指定的密钥进行解密
	 * 
	 * @param input
	 *            待解密的数据
	 * @param key
	 *            密钥
	 * @return byte[] 解密后的数据
	 * @throws Exception
	 */
	public static byte[] decryptData(byte[] input, byte[] key) throws Exception {
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
		if (debug)
			System.out.println("解密前的信息:" + byte2hex(input));
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.DECRYPT_MODE, deskey);
		byte[] clearByte = c1.doFinal(input);
		if (debug) {
			System.out.println("解密后的二进串:" + byte2hex(clearByte));
			System.out.println("解密后的字符串:" + (new String(clearByte)));

		}
		return clearByte;

	}

	/**
	 * 字节码转换成16进制字符串
	 * 
	 * @param byte[] b 输入要转换的字节码
	 * @return String 返回转换后的16进制字符串
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";

		}
		return hs.toUpperCase();

	}

	/**
	 * 字符串转成字节数组.
	 * 
	 * @param hex
	 *            要转化的字符串.
	 * @return byte[] 返回转化后的字符串.
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * 字节数组转成字符串.
	 * 
	 * @param String
	 *            要转化的字符串.
	 * @return 返回转化后的字节数组.
	 */
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 从数据库中获取密钥.
	 * 
	 * @param deptid
	 *            企业id.
	 * @return 要返回的字节数组.
	 * @throws Exception
	 *             可能抛出的异常.
	 */
	public static byte[] getSecretKey(long deptid) throws Exception {
		byte[] key = null;
		String value = null;
		// CommDao dao=new CommDao();
		// List list=dao.getRecordList("from Key k where k.deptid="+deptid);
		// if(list.size()>0){
		// value=((com.csc.sale.bean.Key)list.get(0)).getKey();
		value = "CB7A92E3D3491975";
		key = hexStringToByte(value);
		// }
		if (debug)
			System.out.println("密钥:" + value);
		return key;
	}

	public static String encryptData2(String data) {
		String en = null;
		try {
			byte[] key = hexStringToByte(strkey);
			en = bytesToHexString(encryptData(data.getBytes(), key));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return en;
	}

	public static String decryptData2(String data) {
		String de = null;
		try {
			byte[] key = hexStringToByte(strkey);
			de = new String(decryptData(hexStringToByte(data), key));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return de;
	}
	public static void main(String args[]) {
//		Eryptogram eyp = new Eryptogram();
		String str = Eryptogram.encryptData2("SABR_F-1%2373287387%23LAX-YVR%2CYVR-PEK%3BAC%2CAC%3BAC1879%2CAC29%3B2015-01-10+07%3A45%3A00%2F2015-01-10+10%3A42%3A00%2C2015-01-10+12%3A20%3A00%2F2015-01-11+16%3A00%3A00%3BT%2CT%232759.0%5E1249.0%5E0.0%5E0.0%231231236%230%23utour");
		str = Eryptogram.encryptData2("L");
		System.out.println("str: " + str);
		str = "6605B932A4325F1B64DCCCD9926B02DB74355B2A4A172E8B78449EFC5FADB7AFD938A24AF4936AF0294776E4F9571740316A47DC6D34D25EF451369D1F42806849AD7BA134A6623D8B026D79E53AA1F7673F7FE5F6E74C04BE12DB3AA484D3B87A7A7CE3AE1A2A57F2932ACFC8980C8D980E17150062D43009110C93A1C57CF0";
		System.out.println("dec: " + Eryptogram.decryptData2(str));
		str = "C8881E83004367646DFFBFB9AD9CF3559CD7075E3428A27FEB91EE7B9BAB6D9ED660DB64604D501C4DA4C86B9385D169F164BCEB41CB63CD4F53B1506DEFCE310F1423EE4FA3CE2655B8F3B22241C505524C27E58C891C357E029A08418152350649C1BBAC92EC34E445049A8EFE0CC6783944CEF4CBE15C73DD9FEF476AAE09";
		System.out.println("dec: " + Eryptogram.decryptData2(str));
	}
}

package org.ccframe.commons.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class MD5Util {

	private MD5Util(){}
	
	public static String encode(String value) {
		return encode(value.getBytes());
	}

	public static String encode(byte[] bytes) {
		try {
			return Hex.encodeHexString(MessageDigest.getInstance("md5").digest(bytes));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}

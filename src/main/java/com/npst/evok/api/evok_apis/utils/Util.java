package com.npst.evok.api.evok_apis.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Util {

	public static String encryptRequest(String strToEncrypt, String encryptKey) {
		try {
//				setMerchantKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, setMerchantKey(encryptKey));
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static SecretKeySpec setMerchantKey(String myKey) {
		SecretKeySpec merchantSecretKey_ = null;
		try {
//		if (merchantSecretKey == null) {
			MessageDigest sha = null;
			byte[] key_ = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-256");
			key_ = sha.digest(key_);
			key_ = Arrays.copyOf(key_, 16);
			merchantSecretKey_ = new SecretKeySpec(key_, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return merchantSecretKey_;
	}

	public static String generateChecksumMerchant(String concatenatedString, String checksumkey) {
		String inputString = concatenatedString + checksumkey;
		StringBuffer sb = null;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(inputString.getBytes());
			byte byteData[] = md.digest();
			sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
//		System.out.println(" Checksum generated is :::" + sb.toString());
		return sb.toString();
	}

//	public static String decryptResponse(String responseString, String encryptKey) {
//		try {
//			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
//			cipher.init(Cipher.DECRYPT_MODE, setMerchantKey(encryptKey));
//			return new String(cipher.doFinal(Base64.getDecoder().decode(responseString)), "UTF-8");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

//	public static String decryptResponse(String responseString, String encryptKey) {
//	    try {
//	        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
//	        cipher.init(Cipher.DECRYPT_MODE, setMerchantKey(encryptKey));
//
//	        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(responseString));
//	        return new String(decryptedBytes, StandardCharsets.UTF_8);
//	    } catch (BadPaddingException e) {
//	        throw new RuntimeException("BadPaddingException: Possible incorrect encryption key or data corruption.", e);
//	    } catch (Exception e) {
//	        e.printStackTrace(); // Print the stack trace for debugging
//	        throw new RuntimeException("Error during decryption.", e);
//	    }
//	}
	public static String decryptResponse(String responseString, String encryptKey) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, setMerchantKey(encryptKey));
			return new String(cipher.doFinal(Base64.getDecoder().decode(responseString)), "UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getOnBoardingChecksum(String account, String bankName, String merckey, String ekey,
			String hKey, String checksumkey) {
		StringBuilder concatenatedString = new StringBuilder();
		concatenatedString.append(account).append(bankName).append(merckey).append(ekey).append(hKey);
		System.out.println(" Check sum string is ::" + concatenatedString);
		return Util.generateChecksumMerchant(concatenatedString.toString(), checksumkey);

	}
}

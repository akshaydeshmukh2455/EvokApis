package com.npst.evok.api.evok_apis.serviceimpl;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

@Service
public class AllDecryptionImpl {
//	public String decryptRequest(String strToDecrypt, String decryptKey) {
//		try {
//			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//			cipher.init(Cipher.DECRYPT_MODE, setMerchantKey(decryptKey));
//			byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
//			return new String(decryptedBytes);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
 
	String ENC_KEY="";
	
	private static SecretKeySpec setMerchantKey(String myKey) {
		SecretKeySpec merchantSecretKey_ = null;
		try {
			byte[] key_ = myKey.getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			key_ = sha.digest(key_);
			key_ = Arrays.copyOf(key_, 16); // For AES-128

			merchantSecretKey_ = new SecretKeySpec(key_, "AES");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return merchantSecretKey_;
	}
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

	public String decryptResponse(String dcrypt) {

		return "Decrypted request to cross verify" + decryptResponse(dcrypt, ENC_KEY);
	}
}

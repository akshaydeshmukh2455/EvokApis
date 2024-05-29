package com.npst.evok.api.evok_apis.controller;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.npst.evok.api.evok_apis.entity.Decryption;

@RestController
public class CallbackController {
//	@PostMapping("/callbackDecrypt")
//	public static String decryption(@RequestBody Decryption decryption) {
//		try {
//			String plainText = AESDecrypt(decryption.getCipherText(), decryption.getEncKey());
//	        return "Decrypted Text: " + plainText;
////	        System.out.println("Decrypted Text: " + plainText);
//		} catch (Exception e) {
//			System.out.println("Error: "+e.getMessage());
//			return "No Data Found";
//		}  
//    }
//
//    public static String AESDecrypt(String cipherText, String encKey) {
//        try {
//            String initializationVector = "NetworkPeopleVec";
//            SecretKeySpec keySpec = new SecretKeySpec(encKey.getBytes("UTF-8"), "AES");
//            IvParameterSpec ivSpec = new IvParameterSpec(initializationVector.getBytes("UTF-8"));
//
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
//
//            byte[] encryptedBytes = Base64.getDecoder().decode(cipherText);
//            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
//
//            // Convert the decrypted bytes to a string (assuming UTF-8 encoding)
//            String decryptedText = new String(decryptedBytes, "UTF-8");
//
//            // Remove the first 16 characters, which might be the initialization vector (IV)
//            if (decryptedText.length() > 15) {
//                decryptedText = decryptedText.substring(15);
//            }
//
//            return decryptedText;
//        } catch (Exception ex) {
//            return ex.getMessage();
//        }
//    }
//}
	@PostMapping("/callbackDecrypt")
	public static String decryption(@RequestBody Decryption decryption) {
		try {
			String plainText = AESDecrypt(decryption.getCipherText(), decryption.getEncKey());
			return "Decrypted Text: " + plainText;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			return "No Data Found";
		}
	}

	public static String AESDecrypt(String cipherText, String encKey) {
		try {
			// Convert the base64-encoded cipherText into bytes
			byte[] cipherTextBytes = Base64.getDecoder().decode(cipherText);

			// Extract the initialization vector (IV) from the first 16 bytes of the
			// cipherText
			byte[] ivBytes = new byte[16];
			System.arraycopy(cipherTextBytes, 0, ivBytes, 0, 16);
			IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

			// Extract the encrypted data from the rest of the cipherText
			byte[] encryptedBytes = new byte[cipherTextBytes.length - 16];
			System.arraycopy(cipherTextBytes, 16, encryptedBytes, 0, encryptedBytes.length);

			// Create the secret key from the provided encryption key
			SecretKeySpec keySpec = new SecretKeySpec(encKey.getBytes("UTF-8"), "AES");

			// Create and initialize the cipher in AES/CBC/PKCS5Padding mode
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

			// Decrypt the encrypted bytes
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

			// Convert the decrypted bytes to a string (assuming UTF-8 encoding)
			return new String(decryptedBytes, "UTF-8");
		} catch (Exception ex) {
			return "Error: " + ex.getMessage();
		}
	}	
}


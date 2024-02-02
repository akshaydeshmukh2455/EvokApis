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
	@PostMapping("/callbackDecrypt")
	public static String decryption(@RequestBody Decryption decryption) {
		try {
			String plainText = AESDecrypt(decryption.getCipherText(), decryption.getEncKey());
	        return "Decrypted Text: " + plainText;
//	        System.out.println("Decrypted Text: " + plainText);
		} catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
			return "No Data Found";
		}  
    }

    public static String AESDecrypt(String cipherText, String encKey) {
        try {
            String initializationVector = "NetworkPeopleVec";
            SecretKeySpec keySpec = new SecretKeySpec(encKey.getBytes("UTF-8"), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(initializationVector.getBytes("UTF-8"));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] encryptedBytes = Base64.getDecoder().decode(cipherText);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // Convert the decrypted bytes to a string (assuming UTF-8 encoding)
            String decryptedText = new String(decryptedBytes, "UTF-8");

            // Remove the first 16 characters, which might be the initialization vector (IV)
            if (decryptedText.length() > 16) {
                decryptedText = decryptedText.substring(16);
            }

            return decryptedText;
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
}

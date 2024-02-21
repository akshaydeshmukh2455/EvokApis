package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.constants.ConstantURL;
import com.npst.evok.api.evok_apis.entity.AccountVerificationThroughACandIFSC;
import com.npst.evok.api.evok_apis.service.AccountVerificationService;
import com.npst.evok.api.evok_apis.utils.HttpClient;
import com.npst.evok.api.evok_apis.utils.Util;

@Service
public class AccountVerificationServiceImpl implements AccountVerificationService {
	public String ENC_KEY = "";

	@Override
	public String accountVerification(AccountVerificationThroughACandIFSC accVerification) {
		JSONObject obj = new JSONObject();

		ENC_KEY = accVerification.getEncKey();
		obj.put("source", accVerification.getSource());
		obj.put("extTransactionId", accVerification.getSource() + Math.abs(new Random().nextInt()));
//        obj.put("extTransactionId", generateQR.getExtTransactionId());
		obj.put("sid", accVerification.getSid());
		obj.put("customerName", accVerification.getCustomerName());
		obj.put("customerAcc", accVerification.getCustomerAcc());
		obj.put("customerIFSC", accVerification.getCustomerIFSC());
		obj.put("customerMobileNo", accVerification.getCustomerMobileNo());

		System.out.println("Raw Request" + obj.toString());
		String checksum = generatQRChecksum(obj, accVerification.getChecksum());
		System.out.println("Checksum is " + checksum);
		obj.put("checksum", checksum);
		System.out.println("Final string to encrypt is " + obj.toString());
		String encryptedReq = Util.encryptRequest(obj.toString(), accVerification.getEncKey());
		System.out.println("Final encrypted request " + encryptedReq);
		// System.out.println("Decrypted request to cross verify " +
		// decryptResponse(encryptedReq, ENC_KEY));
		String des = null;
		String enqResponse = HttpClient.sendToSwitch(accVerification.getHeaderKey(), ConstantURL.ACC_VERI_ACC_IFSC,
				encryptedReq);
//      String enqResponse = "9LXEfPKniBexPt4fPYa7Us9rcWjmDLyFH/naqdxcoG4EXlT2c2az+fWPEYWJbt2VgFd+mDk8/Le2ND1DjFpC77rEN/NWyPnYhlu6RSvMd/q2pfd1ksJXTtytl5lFJMqxzlIsjAlx+E+EPdnNHaPk5LyeX5i8qE1poAeiJjhPYU70ZB89VqgN5crpyN72UMbLWOGBNKDjJzQZ0mBdyB55ECgDb/CkWJ+xqmm7ScvDwbPjR4fUblmiTObQ/K3De0bc2nfhNGJnslZlKsw6HGjK4fo2CW5nZjSZ9fjIdnnn+2d211vhWg4fGWUdrm4pirc51LZWWyKi0vXPYbpDSsEDre0d5RYif46Ib/Mt3HLriLk2JwBsWHvS9aSZcaBf0w/I3IRAzp6mk8N6uJonw4m0aKN52CXGX6CBsJWGHgr/WL3/ypmL4tyQhT11blzcVyovF0RERPxQMLoeWOVdT5qZyHMfOJ1LmmO0dPCdPE8Jpq7au19LT7U3HwdNv55UX7aoad0AZoTfi5KyVSMpt8PleIfBFvEXSwCISYf1GSR1QhL5g6ZLbyTkyIlizcBzt0yncqBGvtZqYPq9qj0d9lPDn088I0ENwW7/lP1s9j/V8Xn1Xub0IY48klPO1B3Qd82Blaf2PK9FktwO7Uk3Z7v68E+3pL6j50pUGzwqrU4TsGei2dhCYheX+It3JW+1ZUzvQMwKX9aad0yLdgopKB9pNOIL3W5PMVtkPaq0gR9zJgWwxzSo7nDfeYrtZynMytQVBCnsjAKQixyT53QUlsIogowNGVF0WRSJse59Q/tXRbHg3KZ7rU389VkI+4R3RHhf0GPXiIXdOHtoaVoW2+/QP1euI1/zvbZmGsD/oG7Jr/o=";
		// System.out.println(java.net.URLDecoder.decode(Util.decryptResponse(enqResponse,
		// ENC_KEY), StandardCharsets.UTF_8.name()));
//		System.out.println("Hello_________");
		try {
			des = java.net.URLDecoder.decode(Util.decryptResponse(enqResponse, ENC_KEY), StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return des;
	}

	private static String generatQRChecksum(JSONObject qrObject, String checkSumKey) {
		StringBuilder concatenatedString = new StringBuilder();
		try {
			concatenatedString.append(qrObject.get("source"));
			concatenatedString.append(qrObject.get("extTransactionId"));
			concatenatedString.append(qrObject.get("sid"));
			concatenatedString.append(qrObject.get("customerName"));
			concatenatedString.append(qrObject.get("customerAcc"));
			concatenatedString.append(qrObject.get("customerIFSC"));
			concatenatedString.append(qrObject.get("customerMobileNo"));

			System.out.println("String is " + concatenatedString.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Util.generateChecksumMerchant(concatenatedString.toString(), checkSumKey);
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
		return sb.toString();
	}

	private static String encryptRequest(String strToEncrypt, String encryptKey) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, setMerchantKey(encryptKey));
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static SecretKeySpec setMerchantKey(String myKey) {
		SecretKeySpec merchantSecretKey_ = null;
		try {
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

	@Override
	public String decryptResponse(String dcrypt) {
		return "Decrypted request to cross verify" + decryptResponse(dcrypt, ENC_KEY);
	}
}

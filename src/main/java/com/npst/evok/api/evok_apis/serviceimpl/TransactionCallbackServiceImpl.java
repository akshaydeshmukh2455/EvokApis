package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.entity.TransactionCallback;
import com.npst.evok.api.evok_apis.service.TransactionCallbackService;

@Service
public class TransactionCallbackServiceImpl implements TransactionCallbackService {

	public String ENC_KEY = "";

	@Override
	public String transactionCallback(TransactionCallback transactionCallback) {
		JSONObject obj = getJsonRequest();

		ENC_KEY = transactionCallback.getEncKey();

		obj.put("merchant", transactionCallback.getMerchant());
		obj.put("source", transactionCallback.getSource());
		obj.put("channel", transactionCallback.getChannel());
		obj.put("extTransactionId", transactionCallback.getSource() + Math.abs(new Random().nextInt()));
		obj.put("upiId", transactionCallback.getUpiId());
		obj.put("terminalId", transactionCallback.getTerminalId());
		obj.put("amount", transactionCallback.getAmount());
		obj.put("customerName", transactionCallback.getCustomerName());
		obj.put("respCode", transactionCallback.getRespCode());
		obj.put("respMessage", transactionCallback.getRespMessage());
		obj.put("upiTxnId", transactionCallback.getUpiTxnId());
		obj.put("status", transactionCallback.getStatus());
		obj.put("custRefNo", transactionCallback.getCustRefNo());

		System.out.println("Raw Request" + obj.toString());
		String checksum = generateVerifyVpaChecksum(obj, transactionCallback.getChecksum());
		System.out.println("Checksum is " + checksum);
		obj.put("checksum", checksum);
		System.out.println("Final string to encrypt is " + obj.toString());
		String encryptedReq = encryptRequest(obj.toString(), transactionCallback.getEncKey());
		System.out.println("Final encrypted request " + encryptedReq);

		// System.out.println("Decrypted request to cross verify " +
		// decryptResponse(encryptedReq, ENC_KEY));

		return encryptedReq;
	}

	private static JSONObject getJsonRequest() {
		JSONObject obj = new JSONObject();

		return obj;
	}

	private static String generateVerifyVpaChecksum(JSONObject qrObject, String checkSumKey) {
		StringBuilder concatenatedString = new StringBuilder();
		try {
			concatenatedString.append(qrObject.get("merchant"));
			concatenatedString.append(qrObject.get("source"));
			concatenatedString.append(qrObject.get("channel"));
			concatenatedString.append(qrObject.get("extTransactionId"));
			concatenatedString.append(qrObject.get("upiId"));
			concatenatedString.append(qrObject.get("terminalId"));
			concatenatedString.append(qrObject.get("customerName"));
			concatenatedString.append(qrObject.get("respCode"));
			concatenatedString.append(qrObject.get("respMessage"));
			concatenatedString.append(qrObject.get("upiTxnId"));
			concatenatedString.append(qrObject.get("status"));
			concatenatedString.append(qrObject.get("custRefNo"));

			System.out.println("String is " + concatenatedString.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return generateChecksumMerchant(concatenatedString.toString(), checkSumKey);
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

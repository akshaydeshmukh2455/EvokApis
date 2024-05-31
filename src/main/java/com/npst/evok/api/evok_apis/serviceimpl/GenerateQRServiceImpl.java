package com.npst.evok.api.evok_apis.serviceimpl;

//import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.npst.evok.api.evok_apis.entity.GenerateQR;
import com.npst.evok.api.evok_apis.repository.GenerateQrRepo;
import com.npst.evok.api.evok_apis.service.GenerateQRService;
import com.npst.evok.api.evok_apis.utils.HttpClient;
import com.npst.evok.api.evok_apis.utils.Util;

@Service
public class GenerateQRServiceImpl implements GenerateQRService {

	static Logger logger = LoggerFactory.getLogger(GenerateQRServiceImpl.class);

	@Autowired
	private GenerateQrRepo repo;

	@Override
	public String generateQR(GenerateQR generateQR) {
		JSONObject obj = new JSONObject();

		obj.put("source", generateQR.getSource());
		obj.put("channel", generateQR.getChannel());

		if (generateQR.getType().equals("D")) {
			String extTransactionId = generateQR.getSource()
					+ UUID.randomUUID().toString().substring(0, 20).replace("-", "").toUpperCase();
			obj.put("extTransactionId", extTransactionId);
			generateQR.setExtTransactionId(extTransactionId);
		} else {
			obj.put("extTransactionId", "");
			generateQR.setExtTransactionId("");
		}

		obj.put("sid", generateQR.getSid());
		obj.put("terminalId", generateQR.getTerminalId());
		obj.put("amount", generateQR.getAmount());
		obj.put("type", generateQR.getType());
		obj.put("remark", generateQR.getRemark());
		obj.put("requestTime", generateQR.getRequestTime());
		obj.put("minAmount", generateQR.getAmount());
		obj.put("receipt", generateQR.getReceipt());

		logger.info("Raw Request: " + obj.toString());
		String checksum = generatQRChecksum(obj, generateQR.getChecksum());
		logger.info("Checksum: " + checksum);
		obj.put("checksum", checksum);
		logger.info("Final string to encrypt: " + obj.toString());

		String encryptedReq = Util.encryptRequest(obj.toString(), generateQR.getEncKey());
		logger.info("Encrypted request generated: " + encryptedReq);

		// Send the encrypted request and log the raw response
		String enqResponse = HttpClient.sendToSwitch(generateQR.getHeaderKey(), generateQR.getUrl(), encryptedReq);
		logger.info("Response from switch: " + enqResponse);

		if (enqResponse == null || enqResponse.isEmpty()) {
			logger.error("Received empty response from switch.");
			return null;
		}

		try {
			String decryptedResponse = Util.decryptResponse(enqResponse, generateQR.getEncKey());
			logger.info("Decrypted response: " + decryptedResponse);

			String decodedResponse = java.net.URLDecoder.decode(decryptedResponse, StandardCharsets.UTF_8.name());
			logger.info("Decoded response: " + decodedResponse);

			if (!isValidJSON(decodedResponse)) {
				logger.error("Invalid JSON response: " + decodedResponse);
				return null;
			}

			JSONObject responseObj = new JSONObject(decodedResponse);

			// Use optString to safely retrieve values and provide default values for null
			// cases
			String extTransactionId = responseObj.optString("extTransactionId", null);
			String qrString = responseObj.optString("qrString", "");

			// Only set and save if qrString is not empty
			if (!qrString.isEmpty()) {
				generateQR.setQrString(qrString);
				// Set the expiry time to 5 minutes from now
				LocalDateTime expiryTime = LocalDateTime.now().plus(5, ChronoUnit.MINUTES);
				generateQR.setExpiryTime(expiryTime);
				repo.save(generateQR);
			} else {
				// Handle case where qrString is empty or null
				System.out.println("qrString is either empty or null");
			}

			JSONObject jsonResponse = new JSONObject();
			if (extTransactionId != null) {
				jsonResponse.put("extTransactionId", extTransactionId);
			}
			jsonResponse.put("qrString", qrString);

			logger.info("QR string: " + qrString);

			byte[] qr = generateQRCode(qrString, 300, 300);
			String filePath = "/home/akshaydeshmukh/Desktop/QR/qr.png";
			saveQRCodeAsImage(qr, filePath);
			logger.info("QR Code saved as image: " + filePath);

			return jsonResponse.toString();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error("UnsupportedEncodingException: ", e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("IOException: ", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
		}
		return null;
	}

	private static String generatQRChecksum(JSONObject qrObject, String checkSumKey) {
		StringBuilder concatenatedString = new StringBuilder();
		try {
			concatenatedString.append(qrObject.get("source"));
			concatenatedString.append(qrObject.get("channel"));
			concatenatedString.append(qrObject.get("extTransactionId"));
			concatenatedString.append(qrObject.get("sid"));
			concatenatedString.append(qrObject.get("terminalId"));
			concatenatedString.append(qrObject.get("amount"));
			concatenatedString.append(qrObject.get("type"));
			concatenatedString.append(qrObject.get("remark"));
			concatenatedString.append(qrObject.get("requestTime"));
			concatenatedString.append(qrObject.get("minAmount"));
			concatenatedString.append(qrObject.get("receipt"));

			logger.info("Concatenated String: " + concatenatedString.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while generating checksum: ", e);
		}
		return Util.generateChecksumMerchant(concatenatedString.toString(), checkSumKey);
	}

	public byte[] generateQRCode(String qrLink, int width, int height) {
		try {
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.MARGIN, 1);
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrLink, BarcodeFormat.QR_CODE, width, height, hints);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
			return outputStream.toByteArray();
		} catch (WriterException | IOException e) {
			e.printStackTrace();
			logger.error("Error while generating QR code: ", e);
			return null;
		}
	}

	public void saveQRCodeAsImage(byte[] qrCodeBytes, String filePath) throws IOException {
		try (OutputStream outputStream = new FileOutputStream(filePath)) {
			outputStream.write(qrCodeBytes);
		}
	}

	private boolean isValidJSON(String jsonString) {
		try {
			new JSONObject(jsonString);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}

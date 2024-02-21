package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.constants.ConstantURL;
import com.npst.evok.api.evok_apis.entity.GenerateQR;
import com.npst.evok.api.evok_apis.service.GenerateQRService;
import com.npst.evok.api.evok_apis.utils.HttpClient;
import com.npst.evok.api.evok_apis.utils.Util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
//import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class GenerateQRServiceImpl implements GenerateQRService {

	Logger logger = LoggerFactory.getLogger(GenerateQRServiceImpl.class);

	String URL_TO_HIT = "https://merchantprod.timepayonline.com/evok/qr/v1/dqr";
//	public String ENC_KEY = "";

	@Override
	public String generateQR(GenerateQR generateQR) {
		JSONObject obj = new JSONObject();

//		ENC_KEY = generateQR.getEncKey();
		obj.put("source", generateQR.getSource());
		obj.put("channel", generateQR.getChannel());

		if (generateQR.getType().equals("D")) {
//			obj.put("extTransactionId", generateQR.getSource() + Math.abs(new Random().nextInt()));
			obj.put("extTransactionId", generateQR.getSource()
					+ UUID.randomUUID().toString().substring(0, 20).replace("-", "").toUpperCase());
		} else {
			obj.put("extTransactionId", "");
		}
//        obj.put("extTransactionId", generateQR.getSource()+UUID.randomUUID().toString().substring(0,15));
//		obj.put("extTransactionId", generateQR.getExtTransactionId());
		obj.put("sid", generateQR.getSid());
		obj.put("terminalId", generateQR.getTerminalId());
		obj.put("amount", generateQR.getAmount());
		obj.put("type", generateQR.getType());
		obj.put("remark", generateQR.getRemark());
		obj.put("requestTime", generateQR.getRequestTime());
		obj.put("minAmount", generateQR.getAmount());
		obj.put("receipt", generateQR.getReceipt());

		System.out.println("Raw Request" + obj.toString());
		String checksum = generatQRChecksum(obj, generateQR.getChecksum());
		System.out.println("Checksum is " + checksum);
		obj.put("checksum", checksum);
		System.out.println("Final string to encrypt is " + obj.toString());
		String encryptedReq = Util.encryptRequest(obj.toString(), generateQR.getEncKey());
		logger.info("Encrypted request generated......");
		System.out.println("Final encrypted request " + encryptedReq);

		String des = null;
		String enqResponse = HttpClient.sendToSwitch(generateQR.getHeaderKey(), ConstantURL.GENERATE_QR, encryptedReq);

		try {
			des = java.net.URLDecoder.decode(Util.decryptResponse(enqResponse, generateQR.getEncKey()),
					StandardCharsets.UTF_8.name());
			JSONObject responseObj = new JSONObject(des);

			String extTransactionId = responseObj.getString("extTransactionId");
			String qrString = responseObj.getString("qrString");

			byte[] qr = generateQRCode(qrString, 200, 200);
//			String qrHex = byteArrayToHexString(qr);
//			System.out.println("This is QR string....." + qrHex);

			String filePath = "/home/akshaydeshmukh/Desktop/QR/qr.png"; // Specify the file path where you want to save
																		// the image
			saveQRCodeAsImage(qr, filePath);
			System.out.println("QR Code saved as image: " + filePath);

			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("extTransactionId", extTransactionId);
			jsonResponse.put("qrString", qrString);
			logger.info("This is decrypted response " + des);
			return jsonResponse.toString();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return des;
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

			System.out.println("String is " + concatenatedString.toString());
		} catch (Exception e) {
			e.printStackTrace();
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
			return null;
		}
	}

	public static String byteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	public static void saveQRCodeAsImage(byte[] qrCodeBytes, String filePath) throws IOException {
		try (OutputStream outputStream = new FileOutputStream(filePath)) {
			outputStream.write(qrCodeBytes);
		}
	}
}

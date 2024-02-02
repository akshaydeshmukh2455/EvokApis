package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.constants.ConstantURL;
import com.npst.evok.api.evok_apis.entity.GenerateQR;
import com.npst.evok.api.evok_apis.service.GenerateQRService;
import com.npst.evok.api.evok_apis.utils.HttpClient;
import com.npst.evok.api.evok_apis.utils.Util;

@Service
public class GenerateQRServiceImpl implements GenerateQRService {

	String URL_TO_HIT = "https://merchantprod.timepayonline.com/evok/qr/v1/dqr";
//	public String ENC_KEY = "";

	@Override
	public String generateQR(GenerateQR generateQR) {
		JSONObject obj = getJsonRequest();

//		ENC_KEY = generateQR.getEncKey();
		obj.put("source", generateQR.getSource());
		obj.put("channel", generateQR.getChannel());
		if (generateQR.getType().equals("D")) {
			obj.put("extTransactionId", generateQR.getSource() + Math.abs(new Random().nextInt()));
		} else {
			obj.put("extTransactionId", "");
		}
//        obj.put("extTransactionId", generateQR.getSource()+Math.abs(new Random().nextInt()));
//        obj.put("extTransactionId", generateQR.getExtTransactionId());
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
		System.out.println("Final encrypted request " + encryptedReq);

		String des = null;
		String enqResponse = HttpClient.sendToSwitch(generateQR.getHeaderKey(), ConstantURL.GENERATE_QR, encryptedReq);

		try {
			des = java.net.URLDecoder.decode(Util.decryptResponse(enqResponse, generateQR.getEncKey()),
					StandardCharsets.UTF_8.name());
			JSONObject responseObj = new JSONObject(des);

			String extTransactionId = responseObj.getString("extTransactionId");
			String qrString = responseObj.getString("qrString");

			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("extTransactionId", extTransactionId);
			jsonResponse.put("qrString", qrString);

			return jsonResponse.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return des;
	}

	private static JSONObject getJsonRequest() {
		JSONObject obj = new JSONObject();
		return obj;
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
}

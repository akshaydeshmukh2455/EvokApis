package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.constants.ConstantURL;
import com.npst.evok.api.evok_apis.entity.VerifyVpa;
import com.npst.evok.api.evok_apis.service.VerifyVPAService;
import com.npst.evok.api.evok_apis.utils.HttpClient;
import com.npst.evok.api.evok_apis.utils.Util;

@Service
public class VerifyVpaServiceImpl implements VerifyVPAService {

	Logger logger = LoggerFactory.getLogger(VerifyVpaServiceImpl.class);
	String URL_TO_HIT = "https://merchantuat.timepayonline.com/evok/cm/v2/verifyVPA";

	@Override
	public String verifyVpa(VerifyVpa verifyVpa) {
		JSONObject obj = new JSONObject();

		ENC_KEY = verifyVpa.getEncKey();
		obj.put("source", verifyVpa.getSource());
		obj.put("channel", verifyVpa.getChannel());
		obj.put("upiId", verifyVpa.getUpiId());
		obj.put("terminalId", verifyVpa.getTerminalId());
		obj.put("sid", verifyVpa.getSid());
		obj.put("extTransactionId", verifyVpa.getSource() + Math.abs(new Random().nextInt()));
//        obj.put("extTransactionId", verifyVpa.getExtTransactionId());

		System.out.println("Raw Request" + obj.toString());
		String checksum = generateVerifyVpaChecksum(obj, verifyVpa.getChecksum());
		System.out.println("Checksum is " + checksum);
		obj.put("checksum", checksum);
		System.out.println("Final string to encrypt is " + obj.toString());
		String encryptedReq = Util.encryptRequest(obj.toString(), verifyVpa.getEncKey());
		System.out.println("Final encrypted request " + encryptedReq);

//        return encryptedReq;

		String des = null;
		String enqResponse = HttpClient.sendToSwitch(verifyVpa.getHeaderKey(), ConstantURL.VERIFY_VPA, encryptedReq);

		try {
			des = java.net.URLDecoder.decode(Util.decryptResponse(enqResponse, ENC_KEY), StandardCharsets.UTF_8.name());
			logger.info("This is final decrypted string....." + des);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return des;
	}

	public String ENC_KEY = "";

//    private static JSONObject getJsonRequest() {
//        JSONObject obj = new JSONObject();
//        return obj;
//    }

	private static String generateVerifyVpaChecksum(JSONObject qrObject, String checkSumKey) {
		StringBuilder concatenatedString = new StringBuilder();
		try {
			concatenatedString.append(qrObject.get("source"));
			concatenatedString.append(qrObject.get("channel"));
			concatenatedString.append(qrObject.get("extTransactionId"));
			concatenatedString.append(qrObject.get("upiId"));
			concatenatedString.append(qrObject.get("terminalId"));
			concatenatedString.append(qrObject.get("sid"));

			System.out.println("String is " + concatenatedString.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Util.generateChecksumMerchant(concatenatedString.toString(), checkSumKey);
	}

}

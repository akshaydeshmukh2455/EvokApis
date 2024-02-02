package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.constants.ConstantURL;
import com.npst.evok.api.evok_apis.entity.AccTpvUPIID;
import com.npst.evok.api.evok_apis.service.AccTpvUPIIDService;
import com.npst.evok.api.evok_apis.utils.HttpClient;
import com.npst.evok.api.evok_apis.utils.Util;

@Service
public class AccTpvUPIIDServiceImpl implements AccTpvUPIIDService {
	public String ENC_KEY = "";

	@Override
	public String accTpvUPIID(AccTpvUPIID accTpvUPIID) {
//		JSONObject obj = getJsonRequest();
		JSONObject obj = new JSONObject();

		ENC_KEY = accTpvUPIID.getEncKey();
		obj.put("source", accTpvUPIID.getSource());
		obj.put("extTransactionId", accTpvUPIID.getSource() + Math.abs(new Random().nextInt()));
		obj.put("upiId", accTpvUPIID.getUpiId());
		obj.put("sid", accTpvUPIID.getSid());

		System.out.println("Raw Request" + obj.toString());
		String checksum = generatAccTpvUpiIdChecksum(obj, accTpvUPIID.getChecksum());
		System.out.println("Checksum is " + checksum);
		obj.put("checksum", checksum);
		System.out.println("Final string to encrypt is " + obj.toString());
		String encryptedReq = Util.encryptRequest(obj.toString(), accTpvUPIID.getEncKey());
		System.out.println("Final encrypted request " + encryptedReq);

		String des = null;
		String enqResponse = HttpClient.sendToSwitch(accTpvUPIID.getHeaderKey(), ConstantURL.TPV_UPIID, encryptedReq);

		try {
			des = java.net.URLDecoder.decode(Util.decryptResponse(enqResponse, ENC_KEY), StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return des;
	}

//	private static JSONObject getJsonRequest() {
//		JSONObject obj = new JSONObject();
//		return obj;
//	}

	private static String generatAccTpvUpiIdChecksum(JSONObject qrObject, String checkSumKey) {
		StringBuilder concatenatedString = new StringBuilder();
		try {
			concatenatedString.append(qrObject.get("source"));
			concatenatedString.append(qrObject.get("sid"));
			concatenatedString.append(qrObject.get("upiId"));
			concatenatedString.append(qrObject.get("extTransactionId"));

			System.out.println("String is " + concatenatedString.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Util.generateChecksumMerchant(concatenatedString.toString(), checkSumKey);
	}
}

package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.constants.ConstantURL;
import com.npst.evok.api.evok_apis.entity.QrStatusRRN;
import com.npst.evok.api.evok_apis.service.QrStatusRRNService;
import com.npst.evok.api.evok_apis.utils.HttpClient;
import com.npst.evok.api.evok_apis.utils.Util;

@Service
public class QrStatusRRNServiceImpl implements QrStatusRRNService {

	public String ENC_KEY = "";

	@Override
	public Object qrStatusRRN(QrStatusRRN qrStatusRRN) {
		JSONObject obj = getJsonRequest();

		ENC_KEY = qrStatusRRN.getEncKey();

		obj.put("source", qrStatusRRN.getSource());
		obj.put("channel", qrStatusRRN.getChannel());
		obj.put("terminalId", qrStatusRRN.getTerminalId());
		obj.put("extTransactionId", qrStatusRRN.getExtTransactionId());

		System.out.println("Raw Request" + obj.toString());
		String checksum = generateQrStatusRRNChecksum(obj, qrStatusRRN.getChecksum());
		System.out.println("Checksum is " + checksum);
		obj.put("checksum", checksum);
		System.out.println("Final string to encrypt is " + obj.toString());
		String encryptedReq = Util.encryptRequest(obj.toString(), qrStatusRRN.getEncKey());

		System.out.println("Final encrypted request " + encryptedReq);


		String des = null;
		String enqResponse = HttpClient.sendToSwitch(qrStatusRRN.getHeaderKey(), ConstantURL.QR_STATUS_RRN,
				encryptedReq);

		try {
			des = java.net.URLDecoder.decode(Util.decryptResponse(enqResponse, ENC_KEY), StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return des;
	}

	private static JSONObject getJsonRequest() {
		JSONObject obj = new JSONObject();
		return obj;
	}

	private static String generateQrStatusRRNChecksum(JSONObject qrObject, String checkSumKey) {
		StringBuilder concatenatedString = new StringBuilder();
		try {
			concatenatedString.append(qrObject.get("source"));
			concatenatedString.append(qrObject.get("channel"));
			concatenatedString.append(qrObject.get("terminalId"));
			concatenatedString.append(qrObject.get("extTransactionId"));

			System.out.println("String is " + concatenatedString.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Util.generateChecksumMerchant(concatenatedString.toString(), checkSumKey);
	}

}

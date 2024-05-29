package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.constants.ConstantURL;
import com.npst.evok.api.evok_apis.entity.QrStatusExtId;
import com.npst.evok.api.evok_apis.service.QrStatusExtIdService;
import com.npst.evok.api.evok_apis.utils.HttpClient;
import com.npst.evok.api.evok_apis.utils.Util;

@Service
public class QrStatusExtIdServiceImpl implements QrStatusExtIdService {
	Logger logger = LoggerFactory.getLogger(QrStatusExtIdServiceImpl.class);
	public String ENC_KEY = "";

	@Override
	public Object qrStatusExtId(QrStatusExtId qrStatusExtId) {
		JSONObject obj = new JSONObject();

		ENC_KEY = qrStatusExtId.getEncKey();

		obj.put("source", qrStatusExtId.getSource());
		obj.put("channel", qrStatusExtId.getChannel());
		obj.put("terminalId", qrStatusExtId.getTerminalId());
		obj.put("extTransactionId", qrStatusExtId.getExtTransactionId());

		System.out.println("Raw Request" + obj.toString());
		String checksum = generateQrStatusExtIdChecksum(obj, qrStatusExtId.getChecksum());
		System.out.println("Checksum is " + checksum);
		obj.put("checksum", checksum);
		System.out.println("Final string to encrypt is " + obj.toString());
		String encryptedReq = Util.encryptRequest(obj.toString(), qrStatusExtId.getEncKey());

		String des = null;
		String enqResponse = HttpClient.sendToSwitch(qrStatusExtId.getHeaderKey(), qrStatusExtId.getUrl(),
				encryptedReq);

		try {
			des = java.net.URLDecoder.decode(Util.decryptResponse(enqResponse, ENC_KEY), StandardCharsets.UTF_8.name());
			logger.info("This is decrypted response " + des);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(des);
		return des;
	}

	private static String generateQrStatusExtIdChecksum(JSONObject qrObject, String checkSumKey) {
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

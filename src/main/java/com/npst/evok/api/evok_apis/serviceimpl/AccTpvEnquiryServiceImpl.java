package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.npst.evok.api.evok_apis.constants.ConstantURL;
import com.npst.evok.api.evok_apis.entity.AccTpvEnquiry;
import com.npst.evok.api.evok_apis.service.AccTpvEnquiryService;
import com.npst.evok.api.evok_apis.utils.HttpClient;
import com.npst.evok.api.evok_apis.utils.Util;

@Service
public class AccTpvEnquiryServiceImpl implements AccTpvEnquiryService {
	public String ENC_KEY = "";

	@Override
	public String accTpvEnquiry(AccTpvEnquiry accTpvEnquiry) {
		JSONObject obj = new JSONObject();

		ENC_KEY = accTpvEnquiry.getEncKey();
		obj.put("source", accTpvEnquiry.getSource());
//		obj.put("extTransactionId", accTpvEnquiry.getSource() + Math.abs(new Random().nextInt()));
		obj.put("extTransactionId", accTpvEnquiry.getExtTransactionId());
		obj.put("sid", accTpvEnquiry.getSid());

		System.out.println("Raw Request" + obj.toString());
		String checksum = generatAccTpvEnquiryChecksum(obj, accTpvEnquiry.getChecksum());
		System.out.println("Checksum is " + checksum);
		obj.put("checksum", checksum);
		System.out.println("Final string to encrypt is " + obj.toString());
		String encryptedReq = Util.encryptRequest(obj.toString(), accTpvEnquiry.getEncKey());
		System.out.println("Final encrypted request " + encryptedReq);

		String des = null;
		String enqResponse = HttpClient.sendToSwitch(accTpvEnquiry.getHeaderKey(), ConstantURL.TPV_ENQUIRY,
				encryptedReq);

		try {
			des = java.net.URLDecoder.decode(Util.decryptResponse(enqResponse, ENC_KEY), StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return des;
	}

	private static String generatAccTpvEnquiryChecksum(JSONObject qrObject, String checkSumKey) {
		StringBuilder concatenatedString = new StringBuilder();
		try {
			concatenatedString.append(qrObject.get("source"));
//			concatenatedString.append(qrObject.get("extTransactionId"));
			concatenatedString.append(qrObject.get("sid"));
			concatenatedString.append(qrObject.get("extTransactionId"));

			System.out.println("String is " + concatenatedString.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Util.generateChecksumMerchant(concatenatedString.toString(), checkSumKey);
	}
}

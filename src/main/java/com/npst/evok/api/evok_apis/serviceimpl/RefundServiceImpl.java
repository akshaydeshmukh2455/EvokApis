package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.constants.ConstantURL;
import com.npst.evok.api.evok_apis.entity.Refund;
import com.npst.evok.api.evok_apis.service.RefundService;
import com.npst.evok.api.evok_apis.utils.HttpClient;
import com.npst.evok.api.evok_apis.utils.Util;
@Service
public class RefundServiceImpl implements RefundService{
	public String ENC_KEY = "";

	@Override
	public String refund(Refund refund) {
		JSONObject obj = getJsonRequest();

		ENC_KEY = refund.getEncKey();
		obj.put("source", refund.getSource());
//		obj.put("extTransactionId", accTpvEnquiry.getSource() + Math.abs(new Random().nextInt()));
		obj.put("extTransactionId", refund.getExtTransactionId());
		obj.put("orgTxnId", refund.getOrgTxnId());
		obj.put("orgRrn", refund.getOrgRrn());
		obj.put("payeeAddr", refund.getPayeeAddr());
		obj.put("amount", refund.getAmount());
		obj.put("remarks", refund.getRemarks());

		System.out.println("Raw Request" + obj.toString());
		String checksum = generatAccTpvEnquiryChecksum(obj, refund.getChecksum());
		System.out.println("Checksum is " + checksum);
		obj.put("checksum", checksum);
		System.out.println("Final string to encrypt is " + obj.toString());
		String encryptedReq = Util.encryptRequest(obj.toString(), refund.getEncKey());
		System.out.println("Final encrypted request " + encryptedReq);

		String des = null;
		String enqResponse = HttpClient.sendToSwitch(refund.getHeaderKey(), ConstantURL.REFUND,
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

	private static String generatAccTpvEnquiryChecksum(JSONObject qrObject, String checkSumKey) {
		StringBuilder concatenatedString = new StringBuilder();
		try {
			concatenatedString.append(qrObject.get("source"));
			concatenatedString.append(qrObject.get("sid"));
			concatenatedString.append(qrObject.get("orgTxnId"));
			concatenatedString.append(qrObject.get("extTransactionId"));
			concatenatedString.append(qrObject.get("orgRrn"));
			concatenatedString.append(qrObject.get("payeeAddr"));
			concatenatedString.append(qrObject.get("amount"));
			concatenatedString.append(qrObject.get("remarks"));

			System.out.println("String is " + concatenatedString.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Util.generateChecksumMerchant(concatenatedString.toString(), checkSumKey);
	}
}

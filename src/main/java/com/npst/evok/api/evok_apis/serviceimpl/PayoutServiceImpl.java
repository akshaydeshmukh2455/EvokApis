package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.constants.ConstantURL;
import com.npst.evok.api.evok_apis.entity.Payee;
import com.npst.evok.api.evok_apis.entity.Payout;
import com.npst.evok.api.evok_apis.service.PayoutService;
import com.npst.evok.api.evok_apis.utils.HttpClient;
import com.npst.evok.api.evok_apis.utils.Util;

@Service
public class PayoutServiceImpl implements PayoutService {

	public String ENC_KEY = "";

	@SuppressWarnings("unchecked")
	public String payoutRequest(Payout payout) {
//		JSONArray payees = new JSONArray();
		ENC_KEY = payout.getEncKey();

		List<Payee> payeeList = payout.getPayees();
		JSONArray payees = new JSONArray();

		for (Payee payee : payeeList) {
			JSONObject payeeObj = new JSONObject();
			payeeObj.put("extTransactionId", payout.getSource() + Math.abs(new Random().nextInt()));
			payeeObj.put("payeeName", payee.getPayeeName());
			payeeObj.put("payeeMobile", payee.getPayeeMobile());
			payeeObj.put("payeeAmount", payee.getPayeeAmount());
			payeeObj.put("remarks", payee.getRemarks());
			payeeObj.put("senderEmail", payee.getSenderEmail());
			payeeObj.put("paymentMethod", payee.getPaymentMethod());
			

			if (payee.getPayeeAcNum() != null && payee.getPayeeIfsc() != null) {
				payeeObj.put("payeeAcNum", payee.getPayeeAcNum());
				payeeObj.put("payeeIfsc", payee.getPayeeIfsc());
			}

			if (payee.getPayeeAddr() != null) {
				payeeObj.put("payeeAddr", payee.getPayeeAddr());
			}

			payees.add(payeeObj);
		}
		JSONObject mainObj = new JSONObject();
		mainObj.put("source", payout.getSource());
		mainObj.put("sid", payout.getSid());
//		mainObj.put("requestDate", payout.getRequestDate());
		mainObj.put("payees", payees);

		System.out.println("Final request before checksum " + mainObj.toString());

		String checksum = generatePayoutChecksum(mainObj, payout.getChecksum());
		mainObj.put("checksum", checksum);
		String enqReq = Util.encryptRequest(mainObj.toString(), payout.getEncKey());
		System.out.println("Final encrypted request " + enqReq);
		String des = null;
		String enqResponse = HttpClient.sendToSwitch(payout.getHeaderKey(), ConstantURL.PAYOUT, enqReq);
//		String urlDecodedResponse;
		try {
			des = URLDecoder.decode(enqResponse, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("URL Decoded Response: " + des);

//		 try {
//			des = java.net.URLDecoder.decode(Util.decryptResponse(enqResponse, payout.getEncKey()),
//					StandardCharsets.UTF_8.name());
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		};
//		System.out.println(des);

		return des;
//		return enqReq;
	}

//	static String generatePayoutChecksum(JSONObject payoutObj, String checkSumKey) {
//		System.out.println("This is payoutObj"+payoutObj);
//		StringBuilder concatenatedString = new StringBuilder();
//		try {
//			concatenatedString.append(payoutObj.get("extTransactionId"));
//			concatenatedString.append(payoutObj.get("payeeName"));
//			concatenatedString.append(payoutObj.get("payeeAcNum"));
//			concatenatedString.append(payoutObj.get("payeeIfsc"));
//			concatenatedString.append(payoutObj.get("payeeAddr"));
//			concatenatedString.append(payoutObj.get("payeeAmount"));
//			concatenatedString.append(payoutObj.get("senderEmail"));
//			concatenatedString.append(payoutObj.get("payeeMobile"));
//			concatenatedString.append(payoutObj.get("paymentMethod"));
//			concatenatedString.append(payoutObj.get("remarks"));
////			concatenatedString.append(payoutObj.get("extTransactionId"));
//
//			System.out.println("String is " + concatenatedString.toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return Util.generateChecksumMerchant(concatenatedString.toString(), checkSumKey);
//	}
	static String generatePayoutChecksum(JSONObject payoutObj, String checkSumKey) {
	    StringBuilder concatenatedString = new StringBuilder();
	    JSONArray payees = (JSONArray) payoutObj.get("payees");
	    
	    try {
	        for (Object payeeObj : payees) {
	            JSONObject payee = (JSONObject) payeeObj;
	            concatenatedString.append(payee.get("extTransactionId"));
	            concatenatedString.append(payee.get("payeeName"));
	            concatenatedString.append(payee.get("payeeAcNum"));
	            concatenatedString.append(payee.get("payeeIfsc"));
	            concatenatedString.append(payee.get("payeeAddr"));
	            concatenatedString.append(payee.get("payeeAmount"));
	            concatenatedString.append(payee.get("senderEmail"));
	            concatenatedString.append(payee.get("payeeMobile"));
	            concatenatedString.append(payee.get("paymentMethod"));
	            concatenatedString.append(payee.get("remarks"));
	        }

	        System.out.println("String is " + concatenatedString.toString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return Util.generateChecksumMerchant(concatenatedString.toString(), checkSumKey);
	}

}

package com.npst.evok.api.evok_apis.entity;

import java.util.Date;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.npst.evok.api.evok_apis.utils.HttpClient;
import com.npst.evok.api.evok_apis.utils.Util;

public class PayoutRequest {

	private static String CHECKSUM_KEY = "a28c6743bd4043be93dc03301ba68a45";
	private static String HEADER_KEY = "f9136861c4404e9ebdde8cec6eae8645";
	private static String ENC_KEY = "34e892909c494c46b3961c2084138345";
	private static String URL_TO_HIT = "https://merchantuat.timepayonline.com/evok/cm/merchantodr/v1/payout";

    public static String payoutRequest() {
    	String source = "HIGHWAYDELITE001";
		String sid = "HIGHWAYDELITE-001";
		String requestDate = new Date(new java.util.Date().getTime()).toString();
		JSONArray payees = new JSONArray();
		String extTransactionId = "HIGHWAYDELITE001" + UUID.randomUUID().toString().replaceAll("-", "");
		String extTransactionId1 = "HIGHWAYDELITE001" + UUID.randomUUID().toString().replaceAll("-", "");
		String payeeAcNum = "507101332423";
		String payeeIfsc = "CNRB0000445";
		String payeeName = "SAMBHAVPAY001 PAYMENT SOLUTIONS PRIVATE LIMITED";
		String payeeMobile = "917092221461";
		String payeeAmount = "20.00";
		String payeeAmountUPI = "290.00";
		String remarks = "flakpay  IFSC";
		String payeeAddr = "9730024610@cosb";
		String remarksUPI = "Payout via UPI ID";

        // Create payee objects
        JSONObject payee1 = createPayee(extTransactionId, payeeAddr, payeeName, "payeeMobile", "payeeAmount", "remarks");
//        JSONObject payee2 = createPayee("extTransactionId2", "payeeAddr2", "payeeName2", "payeeMobile2", "payeeAmount2", "remarks2");

        // Create an array and add payee objects
        JSONArray array = new JSONArray();
        array.put(payee1);
//        array.put(payee2);

        // Create main object and add fields
        JSONObject mainObj = new JSONObject();
        mainObj.put("source", source);
        mainObj.put("sid", sid);
        mainObj.put("requestDate", requestDate);
        mainObj.put("payees", array);

        System.out.println("Final request before checksum: " + mainObj.toString());

        // Generate checksum and add to the main object
        String checksum = generatePayoutChecksum(mainObj, CHECKSUM_KEY);
        mainObj.put("checksum", checksum);

        // Encrypt the request
        String enqReq = Util.encryptRequest(mainObj.toString(), ENC_KEY);

        System.out.println("Decrypt body for cross verify: " + Util.decryptResponse(enqReq, ENC_KEY));
        System.out.println("Final encrypted request: " + enqReq);

        return enqReq;
    }

    private static JSONObject createPayee(String extTransactionId, String payeeAddr, String payeeName,
                                          String payeeMobile, String payeeAmount, String remarks) {
        JSONObject payee = new JSONObject();
        payee.put("extTransactionId", extTransactionId);
        payee.put("payeeAddr", payeeAddr);
        payee.put("payeeName", payeeName);
        payee.put("payeeMobile", payeeMobile);
        payee.put("payeeAmount", payeeAmount);
        payee.put("remarks", remarks);
        return payee;
    }

    static String generatePayoutChecksum(JSONObject payoutObj, String checkSumKey) {
		StringBuilder concatenatedString = new StringBuilder();
		try {
			concatenatedString.append(payoutObj.get("source"));
			concatenatedString.append(payoutObj.get("sid"));
			concatenatedString.append("CH_$0Ma_K(yN@!D@n$T");

			System.out.println("String is " + concatenatedString.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Util.generateChecksumMerchant(concatenatedString.toString(), checkSumKey);
	}

    public static void main(String[] args) {
        try {
            String request = payoutRequest();
            String response = HttpClient.sendToSwitch(HEADER_KEY, URL_TO_HIT, request);
            System.out.println("Decrypted response: " + Util.decryptResponse(response, ENC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


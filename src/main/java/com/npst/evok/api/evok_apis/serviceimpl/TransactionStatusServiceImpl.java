package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.constants.ConstantURL;
import com.npst.evok.api.evok_apis.entity.TransactionStatus;
import com.npst.evok.api.evok_apis.service.TransactionStatusService;
import com.npst.evok.api.evok_apis.utils.HttpClient;
import com.npst.evok.api.evok_apis.utils.Util;

@Service
public class TransactionStatusServiceImpl implements TransactionStatusService{

	public String ENC_KEY = "";
    @Override
    public Object transactionStatus(TransactionStatus transactionStatus) {
        JSONObject obj = getJsonRequest();
        
        ENC_KEY=transactionStatus.getEncKey();
        
        obj.put("source", transactionStatus.getSource());
        obj.put("channel", transactionStatus.getChannel());
        obj.put("terminalId", transactionStatus.getTerminalId());
        obj.put("extTransactionId", transactionStatus.getExtTransactionId());
        
        System.out.println("Raw Request" + obj.toString());
        String checksum = generateTxnStatusChecksum(obj, transactionStatus.getChecksum());
        System.out.println("Checksum is " + checksum);
        obj.put("checksum", checksum);
        System.out.println("Final string to encrypt is " + obj.toString());
        String encryptedReq = Util.encryptRequest(obj.toString(), transactionStatus.getEncKey());

        System.out.println("Final encrypted request " + encryptedReq);

        String des = null;
		String enqResponse = HttpClient.sendToSwitch(transactionStatus.getHeaderKey(), transactionStatus.getUrl(), encryptedReq);

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

    private static String generateTxnStatusChecksum(JSONObject qrObject, String checkSumKey) {
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

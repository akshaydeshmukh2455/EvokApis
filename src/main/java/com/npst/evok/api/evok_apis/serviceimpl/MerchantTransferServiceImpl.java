package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.constants.ConstantURL;
import com.npst.evok.api.evok_apis.entity.MerchantTransfer;
import com.npst.evok.api.evok_apis.service.MerchantTransferService;
import com.npst.evok.api.evok_apis.utils.HttpClient;
import com.npst.evok.api.evok_apis.utils.Util;

@Service
public class MerchantTransferServiceImpl implements MerchantTransferService {

	Logger logger = LoggerFactory.getLogger(MerchantTransferServiceImpl.class);

	public String ENC_KEY = "";

	@Override
	public Object merchantTransfer(MerchantTransfer merchantTransfer) {
		JSONObject obj = new JSONObject();

		ENC_KEY = merchantTransfer.getEncKey();

		obj.put("source", merchantTransfer.getSource());
		obj.put("channel", merchantTransfer.getChannel());
//        obj.put("extTransactionId", merchantTransfer.getExtTransactionId());
		obj.put("extTransactionId", merchantTransfer.getSource() + Math.abs(new Random().nextInt()));
		obj.put("upiId", merchantTransfer.getUpiId());
		obj.put("terminalId", merchantTransfer.getTerminalId());
		obj.put("amount", merchantTransfer.getAmount());
		obj.put("customerName", merchantTransfer.getCustomerName());
		obj.put("statusKYC", merchantTransfer.getStatusKYC());
		obj.put("sid", merchantTransfer.getSid());

		System.out.println("Raw Request" + obj.toString());
		String checksum = generatemerchantTransferChecksum(obj, merchantTransfer.getChecksum());
		System.out.println("Checksum is " + checksum);
		obj.put("checksum", checksum);
		System.out.println("Final string to encrypt is " + obj.toString());
		String encryptedReq = Util.encryptRequest(obj.toString(), merchantTransfer.getEncKey());
		System.out.println("Final encrypted request " + encryptedReq);
//        return encryptedReq;
		String des = null;
		String enqResponse = HttpClient.sendToSwitch(merchantTransfer.getHeaderKey(), ConstantURL.MERCHANT_TRANSFER,
				encryptedReq);

		try {
			des = java.net.URLDecoder.decode(Util.decryptResponse(enqResponse, ENC_KEY), StandardCharsets.UTF_8.name());
			logger.info("This is decrypted response " + des);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return des;
	}

	private static String generatemerchantTransferChecksum(JSONObject qrObject, String checkSumKey) {
		StringBuilder concatenatedString = new StringBuilder();
		try {
			concatenatedString.append(qrObject.get("source"));
			concatenatedString.append(qrObject.get("channel"));
			concatenatedString.append(qrObject.get("extTransactionId"));
			concatenatedString.append(qrObject.get("upiId"));
			concatenatedString.append(qrObject.get("terminalId"));
			concatenatedString.append(qrObject.get("amount"));
			concatenatedString.append(qrObject.get("customerName"));
			concatenatedString.append(qrObject.get("statusKYC"));
			concatenatedString.append(qrObject.get("sid"));

			System.out.println("String is " + concatenatedString.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Util.generateChecksumMerchant(concatenatedString.toString(), checkSumKey);
	}

}

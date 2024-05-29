package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.constants.ConstantURL;
import com.npst.evok.api.evok_apis.entity.TransactionReport;
import com.npst.evok.api.evok_apis.service.TransactionReportService;
import com.npst.evok.api.evok_apis.utils.HttpClient;
import com.npst.evok.api.evok_apis.utils.Util;

@Service
public class TransactionReportServiceImpl implements TransactionReportService {

	public String ENC_KEY = "";

	@Override
	public String transactionReport(TransactionReport transactionReport) {
		JSONObject obj = getJsonRequest();

		ENC_KEY = transactionReport.getEncKey();

		obj.put("source", transactionReport.getSource());
		obj.put("channel", transactionReport.getChannel());
		obj.put("terminalId", transactionReport.getTerminalId());
		obj.put("startDate", transactionReport.getStartDate());
		obj.put("endDate", transactionReport.getEndDate());
		obj.put("pageSize", transactionReport.getPageSize());
		obj.put("pageNo", transactionReport.getPageNo());

		System.out.println("Raw Request" + obj.toString());
		String checksum = generateTxnReportChecksum(obj, transactionReport.getChecksum());
		System.out.println("Checksum is " + checksum);
		obj.put("checksum", checksum);
		System.out.println("Final string to encrypt is " + obj.toString());
		String encryptedReq = Util.encryptRequest(obj.toString(), transactionReport.getEncKey());
		System.out.println("Final encrypted request " + encryptedReq);

		String des = null;
		String enqResponse = HttpClient.sendToSwitch(transactionReport.getHeaderKey(), transactionReport.getUrl(),
				encryptedReq);

		try {
			des = java.net.URLDecoder.decode(Util.decryptResponse(enqResponse, ENC_KEY), StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return des;
	}

	private static JSONObject getJsonRequest() {
		JSONObject obj = new JSONObject();

		return obj;
	}

	private static String generateTxnReportChecksum(JSONObject qrObject, String checkSumKey) {
		StringBuilder concatenatedString = new StringBuilder();
		try {
			concatenatedString.append(qrObject.get("source"));
			concatenatedString.append(qrObject.get("channel"));
			concatenatedString.append(qrObject.get("terminalId"));
			concatenatedString.append(qrObject.get("startDate"));
			concatenatedString.append(qrObject.get("endDate"));
			concatenatedString.append(qrObject.get("pageNo"));
			concatenatedString.append(qrObject.get("pageSize"));

			System.out.println("String is " + concatenatedString.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Util.generateChecksumMerchant(concatenatedString.toString(), checkSumKey);
	}

}

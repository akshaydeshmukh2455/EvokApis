package com.npst.evok.api.evok_apis.serviceimpl;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.constants.ConstantURL;
import com.npst.evok.api.evok_apis.entity.QrReport;
import com.npst.evok.api.evok_apis.service.QrReportService;
import com.npst.evok.api.evok_apis.utils.HttpClient;
import com.npst.evok.api.evok_apis.utils.Util;

@Service
public class QrReportServiceImpl implements QrReportService {

	public String ENC_KEY = "";

	@Override
	public String qrReport(QrReport qrReport) {
		JSONObject obj = new JSONObject();

		ENC_KEY = qrReport.getEncKey();

		obj.put("source", qrReport.getSource());
		obj.put("channel", qrReport.getChannel());
		obj.put("terminalId", qrReport.getTerminalId());
		obj.put("startDate", qrReport.getStartDate());
		obj.put("endDate", qrReport.getEndDate());
		obj.put("sid", qrReport.getSid());
		obj.put("pageSize", qrReport.getPageSize());
		obj.put("pageNo", qrReport.getPageNo());

		System.out.println("Raw Request" + obj.toString());
		String checksum = generateQrReportChecksum(obj, qrReport.getChecksum());
		System.out.println("Checksum is " + checksum);
		obj.put("checksum", checksum);
		System.out.println(checksum);
		System.out.println("Final string to encrypt is " + obj.toString());
		String encryptedReq = Util.encryptRequest(obj.toString(), qrReport.getEncKey());
		System.out.println("Final encrypted request " + encryptedReq);

		String des = null;
		String enqResponse = HttpClient.sendToSwitch(qrReport.getHeaderKey(), qrReport.getUrl(), encryptedReq);

		des = Util.decryptResponse(enqResponse, ENC_KEY);
		return des;

	}

	private static String generateQrReportChecksum(JSONObject qrObject, String checkSumKey) {
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

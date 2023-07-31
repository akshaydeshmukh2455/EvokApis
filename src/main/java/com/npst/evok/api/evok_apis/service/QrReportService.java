package com.npst.evok.api.evok_apis.service;

import com.npst.evok.api.evok_apis.entity.QrReport;

public interface QrReportService {
	Object qrReport(QrReport qrReport);

    String decryptResponse(String dcrypt);
}

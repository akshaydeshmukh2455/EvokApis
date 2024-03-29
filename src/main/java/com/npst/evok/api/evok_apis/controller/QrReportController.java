package com.npst.evok.api.evok_apis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.entity.QrReport;
import com.npst.evok.api.evok_apis.service.QrReportService;

@RestController
public class QrReportController {

	private static final Logger LOG = LoggerFactory.getLogger(QrReportController.class);
	@Autowired
	private QrReportService qrReportService;

	@PostMapping("/qrReportEnc")
	public ResponseEntity<Object> qrReport(@RequestBody QrReport qrReport) {
		Object response = null;
		try {
			response = qrReportService.qrReport(qrReport);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

}

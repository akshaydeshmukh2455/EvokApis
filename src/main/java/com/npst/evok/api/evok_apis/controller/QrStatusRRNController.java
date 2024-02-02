package com.npst.evok.api.evok_apis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.entity.QrStatusRRN;
import com.npst.evok.api.evok_apis.entity.TransactionStatus;
import com.npst.evok.api.evok_apis.service.QrStatusRRNService;
import com.npst.evok.api.evok_apis.service.TransactionStatusService;

@RestController
public class QrStatusRRNController {
	private static final Logger LOG = LoggerFactory.getLogger(QrStatusRRNController.class);
	@Autowired
	private QrStatusRRNService qrStatusRRNService;

	@PostMapping("/rrnEnc")
	public ResponseEntity<Object> qrStatusRRN(@RequestBody QrStatusRRN qrStatusRRN) {
		Object response = null;
		try {
			response = qrStatusRRNService.qrStatusRRN(qrStatusRRN);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

}

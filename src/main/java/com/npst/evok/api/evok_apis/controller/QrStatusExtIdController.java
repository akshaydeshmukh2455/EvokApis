package com.npst.evok.api.evok_apis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.entity.QrStatusExtId;
import com.npst.evok.api.evok_apis.service.QrStatusExtIdService;

@RestController
public class QrStatusExtIdController {
	private static final Logger LOG = LoggerFactory.getLogger(QrStatusExtIdController.class);
	@Autowired
	private QrStatusExtIdService qrStatusExtIdService;

	@PostMapping("/qrExtEnc")
	public ResponseEntity<Object> qrStatusExtId(@RequestBody QrStatusExtId qrStatusExtId) {
		Object response = null;
		try {
			response = qrStatusExtIdService.qrStatusExtId(qrStatusExtId);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

}

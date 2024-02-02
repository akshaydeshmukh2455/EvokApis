package com.npst.evok.api.evok_apis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.entity.TransactionStatus;
import com.npst.evok.api.evok_apis.service.TransactionStatusService;

@RestController
public class TransactionStatusController {
	private static final Logger LOG = LoggerFactory.getLogger(TransactionStatusController.class);
	@Autowired
	private TransactionStatusService transactionStatusService;

	@PostMapping("/statusEnc")
	public ResponseEntity<Object> transactionStatus(@RequestBody TransactionStatus transactionStatus) {
		Object response = null;
		try {
			response = transactionStatusService.transactionStatus(transactionStatus);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

}

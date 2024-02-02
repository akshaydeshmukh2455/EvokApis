package com.npst.evok.api.evok_apis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.entity.Payout;
import com.npst.evok.api.evok_apis.service.PayoutService;


@RestController
public class PayoutController {
	@Autowired
	private PayoutService service;
	@PostMapping("/payoutEnc")
	public ResponseEntity<Object> qrReport(@RequestBody Payout payout) {
		Object response = null;
		try {
			response = service.payoutRequest(payout);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
}

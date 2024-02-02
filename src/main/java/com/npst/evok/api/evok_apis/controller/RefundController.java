package com.npst.evok.api.evok_apis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.entity.Refund;
import com.npst.evok.api.evok_apis.service.RefundService;

@RestController
public class RefundController {
	@Autowired
	private RefundService refundService;

	@PostMapping("/refund")
	public ResponseEntity<Object> refund(@RequestBody Refund refund) {
		Object response = null;
		try {
			response = refundService.refund(refund);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
}

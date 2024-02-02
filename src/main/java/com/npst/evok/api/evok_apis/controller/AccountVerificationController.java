package com.npst.evok.api.evok_apis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.entity.AccountVerificationThroughACandIFSC;
import com.npst.evok.api.evok_apis.service.AccountVerificationService;

@RestController
public class AccountVerificationController {
	
	@Autowired
	private AccountVerificationService accService;
	
	@PostMapping("/accVerificaton")
	public ResponseEntity<Object> qrReport(@RequestBody AccountVerificationThroughACandIFSC accVerification) {
		Object response = null;
		try {
			response = accService.accountVerification(accVerification);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
}

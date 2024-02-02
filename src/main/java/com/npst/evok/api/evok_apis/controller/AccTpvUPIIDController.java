package com.npst.evok.api.evok_apis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.entity.AccTpvUPIID;
import com.npst.evok.api.evok_apis.service.AccTpvUPIIDService;

@RestController
public class AccTpvUPIIDController {
	@Autowired
	private AccTpvUPIIDService accTpvUPIIDService;
	
	@PostMapping("/accTpvUpiId")
	public ResponseEntity<Object> qrReport(@RequestBody AccTpvUPIID accTpvUPIID) {
		Object response = null;
		try {
			response = accTpvUPIIDService.accTpvUPIID(accTpvUPIID);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
}

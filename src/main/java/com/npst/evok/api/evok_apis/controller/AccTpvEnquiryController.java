package com.npst.evok.api.evok_apis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.entity.AccTpvEnquiry;
import com.npst.evok.api.evok_apis.service.AccTpvEnquiryService;

@RestController
public class AccTpvEnquiryController {
	@Autowired
	private AccTpvEnquiryService accTpvEnquiryService;
	
	@PostMapping("/accTpvEnquiry")
	public ResponseEntity<Object> qrReport(@RequestBody AccTpvEnquiry accTpvEnquiry) {
		Object response = null;
		try {
			response = accTpvEnquiryService.accTpvEnquiry(accTpvEnquiry);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
}

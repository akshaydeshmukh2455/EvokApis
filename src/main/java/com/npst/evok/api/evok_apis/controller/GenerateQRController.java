package com.npst.evok.api.evok_apis.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;
import com.npst.evok.api.evok_apis.entity.GenerateQR;
import com.npst.evok.api.evok_apis.service.GenerateQRService;

@RestController
public class GenerateQRController {
	private static final Logger LOG = LoggerFactory.getLogger(GenerateQRController.class);
	@Autowired
	private GenerateQRService generateQRService;

	@PostMapping("/generateQREnc")
	public ResponseEntity<Object> generateQR(@RequestBody GenerateQR generateQR) {
		Object response = null;
		try {
			response = generateQRService.generateQR(generateQR);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/generateQR", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> generateQRCode() throws IOException, WriterException {
		String qrLink = "upi://pay?ver=01&mode=15&am=10.00&mam=10.00&cu=INR&pa=bharti.asent@timecosmos&pn=A S ENTERPRISES&mc=5611&tr=BHARTIPAY0015839B54E718444878&mid=BHARTIPAY001&msid=BHARTIPAY001-ASENT&mtid=BHARTIPAY001-ASENT"; // Replace with your QR link
		byte[] qrCodeBytes = generateQRService.generateQRCode(qrLink, 200, 200); // Adjust width and height as needed
		return ResponseEntity.ok().body(qrCodeBytes);
	}

}

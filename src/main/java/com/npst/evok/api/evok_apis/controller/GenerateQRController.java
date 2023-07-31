package com.npst.evok.api.evok_apis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.entity.GenerateQR;
import com.npst.evok.api.evok_apis.service.GenerateQRService;
@RestController
public class GenerateQRController {
	
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

	    @PostMapping("/generateQRDec")
	    public ResponseEntity<Object> decryptedData(@RequestBody String decrypted) {

	        String dcrypt = null;
	        try {

	            dcrypt = generateQRService.decryptResponse(decrypted);

	            return new ResponseEntity<Object>(dcrypt, HttpStatus.OK);

	        } catch (Exception e) {
	            e.printStackTrace();

	            return new ResponseEntity<Object>(dcrypt, HttpStatus.BAD_REQUEST);

	        }
	    }

}

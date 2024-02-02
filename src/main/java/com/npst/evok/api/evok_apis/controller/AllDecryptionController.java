package com.npst.evok.api.evok_apis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.serviceimpl.AllDecryptionImpl;

@RestController
public class AllDecryptionController {

	@Autowired
	private AllDecryptionImpl allDecryptionImpl;

//	@PostMapping("/allDecryption")
//	public String decryptRequest(@RequestBody String encryptedRequest) {
//		String decryptionKey = "0ff2db6ecc068377cdbdd9c812a36112"; // Replace with your decryption key
//		String decryptedData = allDecryptionImpl.decryptRequest(encryptedRequest, decryptionKey);
//		 if (decryptedData != null) {
//		        return decryptedData;
//		    } else {
//		        return "Error occurred during decryption.";
//		    }
//	}
	@PostMapping("/allDecryption")
    public ResponseEntity<Object> decryptedData(@RequestBody String decrypted) {
        String dcrypt = null;
        try {
            dcrypt = allDecryptionImpl.decryptResponse(decrypted);
            return new ResponseEntity<Object>(dcrypt, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(dcrypt, HttpStatus.BAD_REQUEST);
        }
    }
}

package com.npst.evok.api.evok_apis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.entity.TransactionCallback;
import com.npst.evok.api.evok_apis.service.TransactionCallbackService;
@RestController
public class TransactionCallbackController {
	
	private static final Logger LOG = LoggerFactory.getLogger(TransactionCallbackController.class);
    @Autowired
    private TransactionCallbackService transactionCallbackService;

    @PostMapping("/txnCallbackEnc")
    public ResponseEntity<Object> transactionReport(@RequestBody TransactionCallback transactionCallback) {
        Object response = null;
        try {
            response = transactionCallbackService.transactionCallback(transactionCallback);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/decrypt")
    public ResponseEntity<Object> decryptedData(@RequestBody String decrypted) {
        String dcrypt = null;
        try {
            dcrypt = transactionCallbackService.decryptResponse(decrypted);
            return new ResponseEntity<Object>(dcrypt, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(dcrypt, HttpStatus.BAD_REQUEST);
        }
    }

}

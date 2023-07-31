package com.npst.evok.api.evok_apis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.entity.VerifyVpa;
import com.npst.evok.api.evok_apis.service.VerifyVPAService;

@RestController
public class VerifyVPAController {

    private static final Logger LOG = LoggerFactory.getLogger(VerifyVPAController.class);
    @Autowired
    private VerifyVPAService verifyVPAService;

    @PostMapping("/verifyVpa")
    public ResponseEntity<Object> verifyVpa(@RequestBody VerifyVpa verifyVpa) {
        Object response = null;
        try {
            response = verifyVPAService.verifyVpa(verifyVpa);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/verifyVpaDec")
    public ResponseEntity<Object> decryptedData(@RequestBody String decrypted) {
        String dcrypt = null;
        try {
            dcrypt = verifyVPAService.decryptResponse(decrypted);
            return new ResponseEntity<Object>(dcrypt, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(dcrypt, HttpStatus.BAD_REQUEST);
        }
    }

}

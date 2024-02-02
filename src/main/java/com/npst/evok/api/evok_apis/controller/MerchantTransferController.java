package com.npst.evok.api.evok_apis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.npst.evok.api.evok_apis.entity.MerchantTransfer;
import com.npst.evok.api.evok_apis.service.MerchantTransferService;

@RestController
public class MerchantTransferController {

	private static final Logger LOG = LoggerFactory.getLogger(VerifyVPAController.class);

	@Autowired
	private MerchantTransferService merchantTransferService;

	@PostMapping("/merchantTransferEnc")
	public ResponseEntity<Object> merchantTransfer(@RequestBody MerchantTransfer merchantTransfer) {
		Object response = null;
		try {

			response = merchantTransferService.merchantTransfer(merchantTransfer);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

}

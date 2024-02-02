package com.npst.evok.api.evok_apis.entity;

import lombok.Getter;

@Getter
public class AccountVerificationThroughACandIFSC {
	
	private String source;
	private String extTransactionId;
	private String sid;
	private String customerName;
	private String customerAcc;
	private String customerIFSC;
	private String customerMobileNo;
	private String checksum;
	private String encKey;
	private String headerKey;
}

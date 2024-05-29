package com.npst.evok.api.evok_apis.entity;

import lombok.Getter;

@Getter
public class VerifyVpa {
	private String source;
	private String channel;
	private String extTransactionId;
	private String upiId;
	private String terminalId;
	private String sid;
	private String checksum;
	private String encKey;
	private String headerKey;
	private String url;
}

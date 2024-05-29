package com.npst.evok.api.evok_apis.entity;

import lombok.Getter;

@Getter
public class QrStatusRRN {
	
	private String source;
	private String channel;
	private String extTransactionId;
	private String terminalId;
	private String checksum;
	private String encKey;
	private String headerKey;
	private String url;
}

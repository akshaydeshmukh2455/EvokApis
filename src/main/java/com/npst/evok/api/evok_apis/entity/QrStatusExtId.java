package com.npst.evok.api.evok_apis.entity;

import lombok.Getter;

@Getter
public class QrStatusExtId {
	
	private String source;
	private String channel;
	private String terminalId;
	private String extTransactionId;
	private String checksum;
	private String encKey;
	private String headerKey;
}

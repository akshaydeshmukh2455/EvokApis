package com.npst.evok.api.evok_apis.entity;

import lombok.Getter;

@Getter
public class GenerateQR {
	
	private String source;
	private String channel;
	private String extTransactionId;
	private String sid;
	private String terminalId;
	private String amount;
	private String type;
	private String remark;
	private String requestTime;
	private String minAmount;
	private String receipt;
	private String checksum;
	private String encKey;
	private String headerKey;	
//	private String jwtToken;
}

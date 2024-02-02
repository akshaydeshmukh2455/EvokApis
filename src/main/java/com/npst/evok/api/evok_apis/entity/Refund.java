package com.npst.evok.api.evok_apis.entity;

import lombok.Getter;

@Getter
public class Refund {
	private String source;
	private String sid;
	private String extTransactionId;
	private String orgTxnId;
	private String orgRrn;
	private String payeeAddr;
	private String amount;
	private String remarks;
	private String checksum;
	private String encKey;
	private String headerKey;
}

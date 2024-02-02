package com.npst.evok.api.evok_apis.entity;

import lombok.Getter;

@Getter
public class TransactionCallback {
	private String merchant;
	private String source;
	private String channel;
	private String extTransactionId;
	private String upiId;
	private String terminalId;
	private String amount;
	private String customerName;
	private String respCode;
	private String respMessage;
	private String upiTxnId;
	private String status;
	private String custRefNo;
	private String checksum;
	private String encKey;
	
	
}

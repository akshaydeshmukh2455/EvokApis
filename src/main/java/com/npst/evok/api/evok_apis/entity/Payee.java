package com.npst.evok.api.evok_apis.entity;

import lombok.Getter;

@Getter
public class Payee {
	private String extTransactionId;
	private String payeeName;
	private String payeeAcNum;
	private String payeeIfsc;
	private String payeeAddr;
	private String payeeAmount;
	private String senderEmail;
	private String payeeMobile;
	private String paymentMethod;
	private String remarks;
	
}

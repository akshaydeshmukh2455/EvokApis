package com.npst.evok.api.evok_apis.entity;

import lombok.Getter;

@Getter
public class Payee {
	private String extTransactionId;
	private String payeeAcNum;
	private String payeeIfsc;
	private String payeeName;
	private String payeeMobile;
	private String payeeAmount;
	private String remarks;
	private String payeeAddr;
}

package com.npst.evok.api.evok_apis.entity;

import lombok.Getter;

@Getter
public class AccTpvEnquiry {
	private String source;
	private String sid;
	private String extTransactionId;
	private String checksum;
	private String encKey;
	private String headerKey;
}

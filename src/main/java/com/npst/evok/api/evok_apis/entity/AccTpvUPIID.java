package com.npst.evok.api.evok_apis.entity;

import lombok.Getter;

@Getter
public class AccTpvUPIID {
	private String source;
	private String sid;
	private String upiId;
	private String extTransactionId;
	private String checksum;
	private String encKey;
	private String headerKey;
}

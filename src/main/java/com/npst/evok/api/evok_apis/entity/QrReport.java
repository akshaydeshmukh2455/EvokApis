package com.npst.evok.api.evok_apis.entity;

import lombok.Getter;

@Getter
public class QrReport {
	private String source;
	private String channel;
	private String terminalId;
	private String startDate;
	private String endDate;
	private String pageSize;
	private String pageNo;
	private String sid;
	private String checksum;
	private String encKey;
	private String headerKey;
}

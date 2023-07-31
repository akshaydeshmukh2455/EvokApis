package com.npst.evok.api.evok_apis.entity;

import lombok.Getter;

@Getter
public class TransactionReport {
	private String source;
	private String channel;
	private String terminalId;
	private String startDate;
	private String endDate;
	private String pageSize;
	private String pageNo;
	
}

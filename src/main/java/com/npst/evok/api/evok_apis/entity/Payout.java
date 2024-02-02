package com.npst.evok.api.evok_apis.entity;

import java.util.List;

import lombok.Getter;
@Getter
public class Payout {
	private String source;
	private String sid;
	private String requestDate;
	private String checksum;
	private String encKey;
	private String headerKey;
	private List<Payee> payees;
}

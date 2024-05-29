package com.npst.evok.api.evok_apis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GenerateQR {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int qrId;
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
	private String qrString;
	@Transient
	private String url;
//	private String jwtToken;
}

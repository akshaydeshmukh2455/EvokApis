package com.npst.evok.api.evok_apis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MerchantTransfer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String source;
	private String channel;
	private String extTransactionId;
	private String upiId;
	private String terminalId;
	private String amount;
	private String statusKYC;
	private String customerName;
	private String sid;
	private String infoKYC;
	private String Remark;
	private String checksum;
	private String encKey;
	private String headerKey;
	@Transient
	private String url;
	
}

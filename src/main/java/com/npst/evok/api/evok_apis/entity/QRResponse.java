package com.npst.evok.api.evok_apis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QRResponse {
	private String extTransactionId;
	private String qrString;
}

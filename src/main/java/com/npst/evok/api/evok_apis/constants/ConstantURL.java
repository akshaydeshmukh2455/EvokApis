package com.npst.evok.api.evok_apis.constants;

public interface ConstantURL {

	String VERIFY_VPA="https://merchantuat.timepayonline.com/evok/cm/v2/verifyVPA";
	String GENERATE_QR="https://merchantuat.timepayonline.com/evok/qr/v1/dqr";
	String MERCHANT_TRANSFER="https://merchantuat.timepayonline.com/evok/cm/v2/transfer";
	String QR_REPORT="https://merchantuat.timepayonline.com/evok/qr/v1/qrreport";
	String QR_STATUS_RRN="https://merchantuat.timepayonline.com/evok/qr/v1/qrStatusRRN";
	String QR_STATUS_EXTID="https://merchantprod.timepayonline.com/evok/qr/v1/qrStatus";
	String TRANSACTION_REPORT="https://merchantuat.timepayonline.com/evok/cm/v2/report";
	String TRANSACTION_STATUS="https://merchantuat.timepayonline.com/evok/cm/v2/status";
	
	
	String PAYOUT="https://merchantuat.timepayonline.com/evok/cm/merchantodr/v1/payout";
	String ACC_VERI_ACC_IFSC="https://merchantuat.timepayonline.com/evok/cm/v1/tpv";
	String TPV_ENQUIRY="https://merchantuat.timepayonline.com/evok/cm/v1/tpvEnquiry";
	String TPV_UPIID="https://merchantuat.timepayonline.com/evok/cm/v1/tpvUPIID";
	String REFUND = "https://merchantuat.timepayonline.com/evok/cm/merchantodr/v1/refund";
}

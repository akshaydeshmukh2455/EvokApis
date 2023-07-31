package com.npst.evok.api.evok_apis.service;

import com.npst.evok.api.evok_apis.entity.QrStatusRRN;

public interface QrStatusRRNService {
	Object qrStatusRRN(QrStatusRRN qrStatusRRN);

    String decryptResponse(String dcrypt);
}

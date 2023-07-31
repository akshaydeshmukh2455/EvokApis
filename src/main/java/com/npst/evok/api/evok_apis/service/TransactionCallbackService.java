package com.npst.evok.api.evok_apis.service;

import com.npst.evok.api.evok_apis.entity.TransactionCallback;

public interface TransactionCallbackService {
	Object transactionCallback(TransactionCallback transactionCallback);
	String decryptResponse(String dcrypt);
}

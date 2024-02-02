package com.npst.evok.api.evok_apis.service;

import com.npst.evok.api.evok_apis.entity.AccountVerificationThroughACandIFSC;

public interface AccountVerificationService {
	String accountVerification(AccountVerificationThroughACandIFSC accVerification);

    String decryptResponse(String dcrypt);
}

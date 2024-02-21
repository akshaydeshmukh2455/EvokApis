package com.npst.evok.api.evok_apis.service;

import com.npst.evok.api.evok_apis.entity.GenerateQR;

public interface GenerateQRService {
	String generateQR(GenerateQR generateQR);
	
	public byte[] generateQRCode(String qrLink, int width, int height);

}

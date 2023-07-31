package com.npst.evok.api.evok_apis.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.npst.evok.api.evok_apis.entity.VerifyVpa;
import com.npst.evok.api.evok_apis.service.VerifyVPAService;

@Service
public class VerifyVpaServiceImpl implements VerifyVPAService {

    private static final String CHECKSUM_KEY = "46efbba174d340d791ba66fa8f6606c1";
    public static final String ENC_KEY = "2b273ac2cc334f05812b34a04310360a";

//    private static final String SOURCE = "SPAYFIN001";
//    private static final String CHANNEL = "api";
//    private static final String TERMINAL_ID = "SPAYFIN001-001";
//    // private static final String END_DATE = "2022-11-09 23:59:59";
//    // private static final String START_DATE = "2022-11-09 00:00:00";
//    // private static final String PAGE_SIZE = "40";
//    // private static final String PAGE_NO = "0";
//    private static final String EXTERNAL_TRANS_ID = "SPAYFIN001JGHNM"; // unique
//    private static final String UPI_ID = "spayfin.pay@cosb";
//    private static final String SID = "SPAYFIN001-001";
    
    

    @Override
    public String verifyVpa(VerifyVpa verifyVpa) {
        JSONObject obj = getJsonRequest();
//        String src=;
//        String chnl=verifyVpa.getChannel();
//        String trnId=verifyVpa.getExtTransactionId();
//        String upiId=verifyVpa.getUpiId();
//        String terId=verifyVpa.getTerminalId();
//        String s=verifyVpa.getSid();
        
        obj.put("source", verifyVpa.getSource());
        obj.put("channel", verifyVpa.getChannel());
        obj.put("upiId", verifyVpa.getUpiId());
        obj.put("terminalId", verifyVpa.getTerminalId());
        obj.put("sid", verifyVpa.getSid());
        obj.put("extTransactionId", verifyVpa.getExtTransactionId());
        
        System.out.println("Raw Request" + obj.toString());
        String checksum = generateVerifyVpaChecksum(obj, CHECKSUM_KEY);
        System.out.println("Checksum is " + checksum);
        obj.put("checksum", checksum);
        System.out.println("Final string to encrypt is " + obj.toString());
        String encryptedReq = encryptRequest(obj.toString(), ENC_KEY);
        System.out.println("Final encrypted request " + encryptedReq);

        // System.out.println("Decrypted request to cross verify " +
        // decryptResponse(encryptedReq, ENC_KEY));

        return encryptedReq;
    }

    private static JSONObject getJsonRequest() {
        JSONObject obj = new JSONObject();
       

        return obj;
    }

    private static String generateVerifyVpaChecksum(JSONObject qrObject, String checkSumKey) {
        StringBuilder concatenatedString = new StringBuilder();
        try {
            concatenatedString.append(qrObject.get("source"));
            concatenatedString.append(qrObject.get("channel"));
            concatenatedString.append(qrObject.get("extTransactionId"));
            concatenatedString.append(qrObject.get("upiId"));
            concatenatedString.append(qrObject.get("terminalId"));
            concatenatedString.append(qrObject.get("sid"));
            
            
            System.out.println("String is " + concatenatedString.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generateChecksumMerchant(concatenatedString.toString(), checkSumKey);
    }

    public static String generateChecksumMerchant(String concatenatedString, String checksumkey) {
        String inputString = concatenatedString + checksumkey;
        StringBuffer sb = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(inputString.getBytes());
            byte byteData[] = md.digest();
            sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static String encryptRequest(String strToEncrypt, String encryptKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, setMerchantKey(encryptKey));
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static SecretKeySpec setMerchantKey(String myKey) {
        SecretKeySpec merchantSecretKey_ = null;
        try {
            MessageDigest sha = null;
            byte[] key_ = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-256");
            key_ = sha.digest(key_);
            key_ = Arrays.copyOf(key_, 16);
            merchantSecretKey_ = new SecretKeySpec(key_, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return merchantSecretKey_;
    }

    public static String decryptResponse(String responseString, String encryptKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, setMerchantKey(encryptKey));
            return new String(cipher.doFinal(Base64.getDecoder().decode(responseString)), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // public static void main(String[] args) {
    // VerifyVpaServiceImpl verifyVpaServiceImpl = new VerifyVpaServiceImpl();

    // String enqReq = verifyVpaServiceImpl.verifyVpa();
    // System.out.println("Encrypted request to check " + enqReq);

    // System.out.println("Decrypted request to cross verify " + decryptResponse(
    // "36yDqzRDKHL7m1LNcxW9mQHuvcNoQKk2gblK1IanXJeJ10uIlG6GvzpGAL+pecaRgKkuf26yJu3ZbECs6he3gz7sis3HM/WK+9VnmwIX4P6sYHP75h1FzFIp4b3XXkgGK7Et5GcYLHib4HS9qfPAgqwqaxzXm3aKIvuogYp/ba0iSuxzfRRokZVkftOUi8fBPDcNq6OeFUGlL1QpUknWcdcQmBezxPL2rZvc0oxf83UQ3h/FrrphKZq0r4knPFnhDZnbOWL6Jjn+tUGXF0Ky4fHzvtcFP5C4NqmGsr9X9A/dyWvrvZC2ItKUHuPYWlPMVeJOPY3D/ivkTjUJ+l4Vffg8UZ2G3BsDpVCiyDbpJ6lq6DRY7H1SNq6R9ZdBZvDbJcDtlD6Vtzkn5hhUVnNOWljorr4hBZRQmY4x5V4DHpewrwxxpOdslTq2T3ADKGOTt5fG0uRxUyZ558NhHVFl5szrjrd82f8Xzb6bFSenSfE=",
    // ENC_KEY));
    // }
    @Override
    public String decryptResponse(String dcrypt) {
        return "Decrypted request to cross verify" + decryptResponse(dcrypt, ENC_KEY);
    }
}

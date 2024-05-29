package com.npst.evok.api.evok_apis.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;


public class HttpClient {

	 private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

	    public static String sendToSwitch(String cid, String url, String request) {
	        StringBuffer outputSB = new StringBuffer();
	        long ts = 0;

	        try {
	            SSLContext sc = SSLContext.getInstance("SSL");
	            sc.init(null, getTrustManagers(), new SecureRandom());
	            CloseableHttpClient httpClient = HttpClients.custom()
	                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
	                    .setSSLContext(sc)
	                    .build();

	            HttpPost post = new HttpPost(url);
	            post.setHeader("Content-Type", "text/plain");
	            post.setHeader("cid", cid);
	            StringEntity params = new StringEntity(request);
	            post.setEntity(params);

	            ts = System.currentTimeMillis();
	            logger.info("Sending request to URL: " + url);
	            logger.info("Request Headers: cid=" + cid);
	            logger.info("Request Body: " + request);

	            try (CloseableHttpResponse response = httpClient.execute(post)) {
	                int responseCode = response.getStatusLine().getStatusCode();
	                logger.info("Response code from remote service: " + responseCode);
	                try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
	                    String line;
	                    while ((line = rd.readLine()) != null) {
	                        outputSB.append(line);
	                    }
	                }
	                ts = System.currentTimeMillis() - ts;
	                logger.info("Time taken: {} ms, remote response: {}", ts, outputSB.toString());
	            }
	        } catch (Exception e) {
	            logger.error("Exception during sendToSwitch: ", e);
	        }

	        return outputSB.toString();
	    }

	    public static String sendPlainReqToSwitch(String cid, String url, String request) {
	        StringBuffer outputSB = new StringBuffer();
	        long ts = 0;

	        try {
	            SSLContext sc = SSLContext.getInstance("SSL");
	            sc.init(null, getTrustManagers(), new SecureRandom());
	            CloseableHttpClient httpClient = HttpClients.custom()
	                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
	                    .setSSLContext(sc)
	                    .build();

	            HttpPost post = new HttpPost(url);
	            post.setHeader("Content-Type", "application/json");
	            post.setHeader("cid", cid);
	            StringEntity params = new StringEntity(request);
	            post.setEntity(params);

	            ts = System.currentTimeMillis();
	            logger.info("Sending request to URL: " + url);
	            logger.info("Request Headers: cid=" + cid);
	            logger.info("Request Body: " + request);

	            try (CloseableHttpResponse response = httpClient.execute(post)) {
	                int responseCode = response.getStatusLine().getStatusCode();
	                logger.info("Response code from remote service: " + responseCode);
	                try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
	                    String line;
	                    while ((line = rd.readLine()) != null) {
	                        outputSB.append(line);
	                    }
	                }
	                ts = System.currentTimeMillis() - ts;
	                logger.info("Time taken: {} ms, remote response: {}", ts, outputSB.toString());
	            }
	        } catch (Exception e) {
	            logger.error("Exception during sendPlainReqToSwitch: ", e);
	        }

	        return outputSB.toString();
	    }

	    private static TrustManager[] getTrustManagers() {
	        return new TrustManager[]{new X509ExtendedTrustManager() {
	            @Override
	            public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
	            }

	            @Override
	            public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
	            }

	            @Override
	            public X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }

	            @Override
	            public void checkClientTrusted(X509Certificate[] arg0, String arg1, Socket arg2) {
	            }

	            @Override
	            public void checkClientTrusted(X509Certificate[] arg0, String arg1, SSLEngine arg2) {
	            }

	            @Override
	            public void checkServerTrusted(X509Certificate[] arg0, String arg1, Socket arg2) {
	            }

	            @Override
	            public void checkServerTrusted(X509Certificate[] arg0, String arg1, SSLEngine arg2) {
	            }
	        }};
	    }
	}

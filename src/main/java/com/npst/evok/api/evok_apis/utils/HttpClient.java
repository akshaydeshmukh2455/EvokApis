package com.npst.evok.api.evok_apis.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpHeaders;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.SecureRandom;


public class HttpClient {

	 public static String sendToSwitch(String cid, String url, String request) {
	        StringBuffer outputSB = new StringBuffer();
	        long ts = 0;
	        boolean f = false;
	        try {
	            SSLContext sc = SSLContext.getInstance("SSL");
	            sc.init(null, get_trust_mgr(), new SecureRandom());
	            CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setSSLContext(sc).build();

	            HttpPost post = new HttpPost(url);
	            HttpHeaders headers = new HttpHeaders();
	            post.setHeader("Content-Type", "text/plain");
	            post.setHeader("cid", cid);
	            StringEntity params = new StringEntity(request);
	            post.setEntity(params);

	            ts = System.currentTimeMillis();
	            try (CloseableHttpResponse response = httpClient.execute(post);) {
	                int responseCode = response.getStatusLine().getStatusCode();
	                System.out.println("Response code from remote service " + responseCode);
	                try (BufferedReader rd = new BufferedReader(
	                        new InputStreamReader(response.getEntity().getContent()));) {
	                    String line = "";
	                    while ((line = rd.readLine()) != null) {
	                        outputSB.append(line);
	                    }
	                }
	                ts = System.currentTimeMillis() - ts;
	                System.out.println("ts={} ms ,remote response : {}" + ts + " " + outputSB.toString());
	                f = true;
	            }
	        } catch (org.apache.http.conn.ConnectionPoolTimeoutException e) {
	            e.printStackTrace();
	        } catch (org.apache.http.conn.HttpHostConnectException e) {
	            e.printStackTrace();
	        } catch (java.net.SocketTimeoutException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return outputSB.toString();
	    }

	    public static String sendPlainReqToSwitch(String cid, String url, String request) {
	        StringBuffer outputSB = new StringBuffer();
	        long ts = 0;
	        boolean f = false;
	        try {
	            SSLContext sc = SSLContext.getInstance("SSL");
	            sc.init(null, get_trust_mgr(), new SecureRandom());
	            CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setSSLContext(sc).build();

	            HttpPost post = new HttpPost(url);
	            HttpHeaders headers = new HttpHeaders();
	            post.setHeader("Content-Type", "application/json");
	            post.setHeader("cid", cid);
	            StringEntity params = new StringEntity(request);
	            post.setEntity(params);

	            ts = System.currentTimeMillis();
	            try (CloseableHttpResponse response = httpClient.execute(post);) {
	                int responseCode = response.getStatusLine().getStatusCode();
	                System.out.println("Response code from remote service " + responseCode);
	                try (BufferedReader rd = new BufferedReader(
	                        new InputStreamReader(response.getEntity().getContent()));) {
	                    String line = "";
	                    while ((line = rd.readLine()) != null) {
	                        outputSB.append(line);
	                    }
	                }
	                ts = System.currentTimeMillis() - ts;
	                System.out.println("ts={} ms ,remote response : {}" + ts + " " + outputSB.toString());
	                f = true;
	            }
	        } catch (org.apache.http.conn.ConnectionPoolTimeoutException e) {
	            e.printStackTrace();
	        } catch (org.apache.http.conn.HttpHostConnectException e) {
	            e.printStackTrace();
	        } catch (java.net.SocketTimeoutException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return outputSB.toString();
	    }


	    private static TrustManager[] get_trust_mgr() {
	        TrustManager[] certs = new TrustManager[]{new X509ExtendedTrustManager() {
	            @Override
	            public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
	                    throws java.security.cert.CertificateException {
	                // TODO Auto-generated method stub

	            }

	            @Override
	            public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
	                    throws java.security.cert.CertificateException {
	                // TODO Auto-generated method stub

	            }

	            @Override
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                // TODO Auto-generated method stub
	                return null;
	            }

	            @Override
	            public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1, Socket arg2)
	                    throws java.security.cert.CertificateException {
	                // TODO Auto-generated method stub

	            }

	            @Override
	            public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1, SSLEngine arg2)
	                    throws java.security.cert.CertificateException {
	                // TODO Auto-generated method stub

	            }

	            @Override
	            public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1, Socket arg2)
	                    throws java.security.cert.CertificateException {
	                // TODO Auto-generated method stub

	            }

	            @Override
	            public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1, SSLEngine arg2)
	                    throws java.security.cert.CertificateException {
	                // TODO Auto-generated method stub

	            }

	        }};
	        return certs;
	    }

}

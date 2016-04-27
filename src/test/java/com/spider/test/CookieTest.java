package com.spider.test;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class CookieTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CloseableHttpClient client = HttpClients.custom().build();
		CookieStore cookieStore = null;
		HttpClientContext context = null;
		
		HttpGet get = new HttpGet("http://weixin.sogou.com");
		
		CloseableHttpResponse response;
		try {
			response = client.execute(get, context);
			
			String setCookie = response.getFirstHeader("Set-Cookie").getValue();
			System.out.println(setCookie);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

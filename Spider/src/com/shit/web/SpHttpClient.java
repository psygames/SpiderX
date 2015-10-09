package com.shit.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class SpHttpClient
{
	private CloseableHttpClient httpclient = null;
	private long totalDownloadSize = 0;

	public SpHttpClient()
	{
		httpclient = HttpClients.createDefault();
	}

	public long getTotalDownloadSize()
	{
		return this.totalDownloadSize;
	}

	public String getContentFormUrl(String strURL, String decode)
	{
		String contentBuf = getContentFormUrl2(strURL, decode, "utf-8", 10);
		return contentBuf;
	}

	public String getContentFormUrl2(String url, String decode, String encode, int retryCount)
	{
		boolean isTimeOut = false;
		String content = null;
		HttpGet httpget = null;
		CloseableHttpResponse response = null;
		try
		{
			httpget = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();// 设置请求和传输超时时间
			httpget.setConfig(requestConfig);
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null)
			{
				content = EntityUtils.toString(entity);
			}
		} catch (ParseException | IOException e)
		{
			if (retryCount > 0)
			{
				isTimeOut = true;
				// System.out.println("Exception:" + e.getMessage() + "\nRetry URL(" + retryCount + "): " + url);
			}
			else
			{
				System.out.println("Exception:" + e.getMessage() + "\nRetry Failed URL: " + url);
			}
		} finally
		{
			try
			{
				if (response != null)
					response.close();
				if (httpget != null)
					httpget.releaseConnection();
				// if (httpclient != null)
				// httpclient.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		if (isTimeOut)
			content = getContentFormUrl2(url, decode, encode, retryCount - 1);
		else if (content != null)
		{
			try
			{
				byte[] bytes = content.getBytes(decode);
				content = new String(bytes, encode);
				totalDownloadSize += bytes.length;
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}

		return content;
	}
}

package com.jeeplus.modules.sys.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * <p>
 * 获取Http请求内容
 * </p>
 * 
 * @author luoxuelin
 */
public class HttpUtil {
	/**
	 * <p>
	 * get方式获取请求内容
	 * </p>
	 * 
	 * @param url
	 *            访问链接地址
	 * @return 数据请求内容byte[]形式
	 * @throws Exception
	 */
	public static byte[] get4Byte(String url) throws Exception {
		byte[] content = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response = httpclient.execute(httpget);
		try {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				content = EntityUtils.toByteArray(entity);
			}
		} finally {
			response.close();
		}
		return content;
	}

	/**
	 * <p>
	 * get获取请求内容
	 * </p>
	 * 
	 * @param url
	 *            访问链接地址
	 * @return 数据请求内容JSON形式
	 * @throws Exception
	 */
	public static JSONObject get(String url) throws Exception {
		JSONObject content = null;
		byte[] bytes = get4Byte(url);
		if (bytes != null) {
			String ss = new String(bytes);
			try {
				content = JSONObject.fromObject(ss);
			} catch (Exception e) {
				// TODO
				content = new JSONObject();
				content.put("htmlContext", ss);
			}
		}
		return content;
	}

	/**
	 * <p>
	 * post方式获取请求内容
	 * </p>
	 * 
	 * @param url
	 *            访问链接地址
	 * @param param
	 *            参数值对
	 * @return 数据请求内容byte[]形式
	 * @throws Exception
	 */
	public static byte[] post4Byte(String url, Map<String, String> param)
			throws Exception {

		byte[] content = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> postpParam = new ArrayList<NameValuePair>();

		if (param != null && !param.isEmpty()) {
			Set<String> keySet = param.keySet();
			for (String key : keySet) {
				postpParam.add(new BasicNameValuePair(key, param.get(key)));
			}
		}
		post.setEntity(new UrlEncodedFormEntity(postpParam,"UTF-8"));
		CloseableHttpResponse response = httpclient.execute(post);
		try {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				content = EntityUtils.toByteArray(entity);
			}
		} finally {
			response.close();
		}

		return content;
	}

	/**
	 * <p>
	 * post方式获取请求内容
	 * </p>
	 * 
	 * @param url
	 *            访问链接地址
	 * @param str
	 *            字符串内容
	 * @return 数据请求内容byte[]形式
	 * @throws Exception
	 */
	public static byte[] post4Byte(String url, String str) throws Exception {

		byte[] content = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);

		post.setEntity(new StringEntity(str,"UTF-8"));

		CloseableHttpResponse response = httpclient.execute(post);
		try {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				content = EntityUtils.toByteArray(entity);
			}
		} finally {
			response.close();
		}

		return content;
	}

	/**
	 * <p>
	 * post获取请求内容
	 * </p>
	 * 
	 * @param url
	 *            访问链接地址
	 * @param param
	 *            参数值对
	 * @return 数据请求内容JSON形式
	 * @throws Exception
	 */
	public static JSONObject postParam(String url, Map<String, String> param)
			throws Exception {
		JSONObject content = null;
		byte[] bytes = post4Byte(url, param);
		if (bytes != null) {
			String ss = new String(bytes);
			try {
				content = JSONObject.fromObject(ss);
			} catch (Exception e) {
				// TODO
				content = new JSONObject();
				content.put("htmlContext", ss);
			}
		}
		return content;
	}

	/**
	 * <p>
	 * post获取请求内容
	 * </p>
	 * 
	 * @param url
	 *            访问链接地址
	 * @param post4Byte
	 *            字符串内容
	 * @return 数据请求内容JSON形式
	 * @throws Exception
	 */
	public static JSONObject postString(String url, String str)
			throws Exception {
		JSONObject content = null;
		byte[] bytes = post4Byte(url, str);
		if (bytes != null) {
			String ss = new String(bytes, "utf-8");
			try {
				content = JSONObject.fromObject(ss);
			} catch (Exception e) {
				// TODO
				content = new JSONObject();
				content.put("htmlContext", ss);
			}
		}
		return content;
	}
}

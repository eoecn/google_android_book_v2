/**
 * 
 */
package cn.eoe.wiki.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionManagerFactory;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.text.TextUtils;
import cn.eoe.wiki.http.HttpResponseException;

/**
 * http util. provide get or post method to connect to the http server
 * 
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @version 1.0.0
 */
public class HttpUtil {
	private static final HttpClient httpClient;

	static {
		httpClient = new DefaultHttpClient();
		HttpParams params = httpClient.getParams();

		// set the time out of the connection/ socket. and the cache size
		HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
		HttpConnectionParams.setSoTimeout(params, 30 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);

		// set the connection manager factory
		params.setParameter(ClientPNames.CONNECTION_MANAGER_FACTORY,
				new ClientConnectionManagerFactory() {
					public ClientConnectionManager newInstance(
							HttpParams params, SchemeRegistry schemeRegistry) {
						return new ThreadSafeClientConnManager(params,
								schemeRegistry);
					}
				});

		// set the redirect, default is true
		HttpClientParams.setRedirecting(params, true);

		// set the client verion
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		// set user-agent
		HttpProtocolParams.setUserAgent(params, "eoeWIKI_Android/0.9");
		// set the charset
		HttpProtocolParams.setHttpElementCharset(params, HTTP.UTF_8);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		// set not activate Expect:100-Continue
		HttpProtocolParams.setUseExpectContinue(params, false);

		// set the version of the cookie
		params.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);
	}

	/**
	 * get the response from the url
	 * 
	 * @param url
	 *            server url
	 * @return return the response <String>
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws IllegalStateException
	 * @throws HttpResponseException
	 */
	public static String get(String url) throws IllegalStateException,
			ClientProtocolException, HttpResponseException, IOException {
		return get(url, null);
	}

	/**
	 * get the response data from the url
	 * 
	 * @param url
	 *            target url
	 * @param data
	 *            request data, they will be send to the server
	 * @return
	 * @throws ClientProtocolException
	 * @throws IllegalStateException
	 * @throws HttpResponseException
	 */
	public static String get(String url, Map<String, String> data)
			throws IllegalStateException, ClientProtocolException,
			HttpResponseException, IOException {
		HttpGet request = null;
		L.d("request url:" + url);
		if (data == null || data.isEmpty()) {
			request = new HttpGet(url);
		} else {
			String paramStr = "";
			for (Map.Entry<String, String> entry : data.entrySet()) {
				paramStr += "&" + entry.getKey() + "=" + entry.getValue();
			}

			if (url.indexOf("?") == -1) {
				url += paramStr.replaceFirst("&", "?");
			} else {
				url += paramStr;
			}
			url += "&";
			url = encodeSpecCharacters(url);
			L.e("Request URL:" + url);
			request = new HttpGet(url);
			// request.addHeader("Content-Type",
			// "application/x-www-form-urlencoded");
		}

		// support Gzip
		supportGzip(request);
		return processResponse(httpClient.execute(request));
	}

	/**
	 * 
	 * @param url
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws HttpResponseException
	 */
	public static String post(String url, Map<String, String> data)
			throws IllegalStateException, HttpResponseException, IOException {
		HttpPost request = new HttpPost(url);

		L.e("Request URL:" + url);
		// add the support of Gzip
		supportGzip(request);

		List<NameValuePair> parameters = new LinkedList<NameValuePair>();
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : data.entrySet()) {
			if (entry.getValue() == null)
				continue;
			sb.append(entry.getKey() + "=" + entry.getValue() + "&");
			parameters.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		L.e(sb.toString());
		try {
			UrlEncodedFormEntity form = new UrlEncodedFormEntity(parameters,
					HTTP.UTF_8);
			request.addHeader("Content-Type",
					"application/x-www-form-urlencoded");

			request.setEntity(form);

			return processResponse(httpClient.execute(request));
		} catch (UnsupportedEncodingException e) {
			throw new ParseException(e.getMessage());
		}
	}

	public static String put(String url, Map<String, String> data)
			throws IllegalStateException, ClientProtocolException,
			HttpResponseException, IOException {

		HttpPut request = new HttpPut(url);

		L.e("Request URL:" + url);
		// add the support of Gzip
		supportGzip(request);

		List<NameValuePair> parameters = new LinkedList<NameValuePair>();
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : data.entrySet()) {
			if (entry.getValue() == null)
				continue;
			sb.append(entry.getKey() + "=" + entry.getValue() + "&");
			parameters.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		L.e(sb.toString());
		try {
			UrlEncodedFormEntity form = new UrlEncodedFormEntity(parameters,
					HTTP.UTF_8);
			request.addHeader("Content-Type",
					"application/x-www-form-urlencoded");
			request.setEntity(form);

			return processResponse(httpClient.execute(request));
		} catch (UnsupportedEncodingException e) {
			throw new ParseException(e.getMessage());
		}
	}

	public static String delete(String url, Map<String, String> data)
			throws IllegalStateException, ClientProtocolException,
			HttpResponseException, IOException {
		HttpDelete request = null;

		if (data == null || data.isEmpty()) {
			request = new HttpDelete(url);
		} else {
			String paramStr = "";
			for (Map.Entry<String, String> entry : data.entrySet()) {
				paramStr += "&" + entry.getKey() + "=" + entry.getValue();
			}

			if (url.indexOf("?") == -1) {
				url += paramStr.replaceFirst("&", "?");
			} else {
				url += paramStr;
			}
			url += "&";
			url = encodeSpecCharacters(url);
			L.e(url);
			request = new HttpDelete(url);
		}
		request.addHeader("Content-Type", "application/x-www-form-urlencoded");
		// support Gzip
		supportGzip(request);
		return processResponse(httpClient.execute(request));
	}

	/**
	 * add the Gzip support
	 * 
	 * @param request
	 */
	private static void supportGzip(HttpRequest request) {
		// support zgip
		request.addHeader("Accept-Encoding", "gzip");
	}

	/**
	 * judge the server is return gzip
	 * 
	 * @param response
	 * @return
	 */
	private static boolean isSupportGzip(HttpResponse response) {
		Header contentEncoding = response.getFirstHeader("Content-Encoding");

		return contentEncoding != null
				&& contentEncoding.getValue().equalsIgnoreCase("gzip");
	}

	/**
	 * deal with the response
	 * 
	 * @param response
	 * @param clazz
	 * @return
	 * @throws IllegalStateException
	 * @throws ParseException
	 * @throws IOException
	 */
	private static String processResponse(HttpResponse response)
			throws HttpResponseException, IllegalStateException, IOException {
		int statusCode = response.getStatusLine().getStatusCode();
		L.e("status--->code-->" + statusCode);
		Header[] headers = response.getAllHeaders();
		// if the return code is between 200 and 300. the request is success
		if (HttpStatus.SC_OK <= statusCode
				&& statusCode < HttpStatus.SC_MULTIPLE_CHOICES) {
			return processEntity(response);
		} else {
			throw new HttpResponseException(statusCode,
					processEntity(response), headers);
		}
	}

	private static String processEntity(HttpResponse response)
			throws IllegalStateException, IOException {
		HttpEntity entity = response.getEntity();
		if (entity == null) {
			return null;
		}
		String result = null;
		if (isSupportGzip(response)) {
			InputStream is = null;
			GZIPInputStream gis = null;
			try {
				is = entity.getContent();
				gis = new GZIPInputStream(is);
				long contentLen = +entity.getContentLength();
				StringBuffer stringBuffer;
				if (contentLen < 0) {
					stringBuffer = new StringBuffer();
				} else {
					stringBuffer = new StringBuffer((int) contentLen);
				}
				byte[] tmp = new byte[4096];
				int l;
				while ((l = gis.read(tmp)) != -1) {
					stringBuffer.append(new String(tmp, 0, l));
				}
				result = stringBuffer.toString();
			} catch (Exception e) {
				L.e("read input stream exception", e);
			} finally {
				if (gis != null) {
					gis.close();
				}
				if (is != null) {
					is.close();
				}
			}
		} else {
			result = EntityUtils.toString(entity);
		}
		entity.consumeContent(); // consume or destroy the entity content
		return result;
	}

	private static String encodeSpecCharacters(String url) {
		if (TextUtils.isEmpty(url)) {
			return url;
		}
		Iterator<String> iterator = specCharactersMap.keySet().iterator();
		String key = null;
		while (iterator.hasNext()) {
			key = iterator.next();
			int index = url.indexOf(key);
			L.p("Key:" + key + "#index:" + index);
			url = url.replace(key, specCharactersMap.get(key));
		}
		return url;

	}

	private static Map<String, String> specCharactersMap = new HashMap<String, String>();
	static {
		specCharactersMap.put("+", "%2B");
		specCharactersMap.put(" ", "%20");
		specCharactersMap.put("[", "%5B");
		specCharactersMap.put("]", "%5D");
	}

}

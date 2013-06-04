package com.eoeandroid.wikirecent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
/**
 * 该类连接网络，获取最新更新的wiki信息
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @date 2012-10-20
 * @version 1.0.0
 *
 */
public class WikiHelper {
	private static final String TAG = "WikiHelper";
	private static final String WIKTIONARY_PAGE = "http://wiki.eoeandroid.com/api.php?action=query&list=recentchanges&rclimit=1&format=json&rcprop=title%7Cuser";

	/**
	 * api请求无误的状态码
	 */
	private static final int HTTP_STATUS_OK = 200;

	/**
	 * 读取结果的缓存 
	 */
	private static byte[] sBuffer = new byte[2048];

	/**
	 * 请求接口，将返回的json数据解析成{@link WikiInfo}
	 * @return 返回{@link WikiInfo}
	 * @throws ApiException
	 * 			getUrlContent()方法抛出的异常
	 * @throws ParseException
	 * 			如果在解析返回结果时出现了异常
	 */
	public static WikiInfo getRecentWiki() throws ApiException, ParseException {
		// 通过接口获取到数据
		String content = getUrlContent(WIKTIONARY_PAGE);
		Log.e(TAG, "content:" + content);
		WikiInfo wikiInfo = null;
		try {
			// 解析接口返回来的json数据
			JSONObject response = new JSONObject(content);
			JSONObject query = response.getJSONObject("query");
			JSONArray recentchanges = query.getJSONArray("recentchanges");
			JSONObject recentchange = recentchanges.getJSONObject(0);
			wikiInfo = new WikiInfo();
			String title = recentchange.getString("title");
			String user =recentchange.getString("user");
			wikiInfo.setTitle(title);
			wikiInfo.setUser(user);
		} catch (JSONException e) {
			throw new ParseException("Problem parsing API response", e);
		}
		return wikiInfo;
	}

	/**
	 * 通过url去获取网络返回结果
	 * @param url 接口地址
	 * @return 返回json数据
	 * @throws ApiException
	 * 			如果发生了网络异常
	 */
	protected static synchronized String getUrlContent(String url)
			throws ApiException {
		Log.e(TAG, "url:" + url);
		// 创建HttpClient
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		try {
			HttpResponse response = client.execute(request);

			// 检查服务器返回的状态码
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HTTP_STATUS_OK) {
				throw new ApiException("Invalid response from server: "
						+ status.toString());
			}

			// 从数据流中将结果转化成String
			HttpEntity entity = response.getEntity();
			InputStream inputStream = entity.getContent();
			ByteArrayOutputStream content = new ByteArrayOutputStream();

			int readBytes = 0;
			while ((readBytes = inputStream.read(sBuffer)) != -1) {
				content.write(sBuffer, 0, readBytes);
			}

			return new String(content.toByteArray());
		} catch (IOException e) {
			throw new ApiException("Problem communicating with API", e);
		}
	}
	/**
	 * 用于保存wiki的信息
	 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
	 * @date 2012-10-20
	 * @version 1.0.0
	 *
	 */
	public static class WikiInfo
	{
		private String title;
		private String user;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getUser() {
			return user;
		}
		public void setUser(String user) {
			this.user = user;
		}
		public String toString()
		{
			return "WikiInfo"+
					"#title:"+title+
					"#user:"+user;
		}
	}
	/**
	 * Thrown when there were problems contacting the remote API server, either
	 * because of a network error, or the server returned a bad status code.
	 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
	 * @date 2012-10-20
	 * @version 1.0.0
	 */
	public static class ApiException extends Exception {
		private static final long serialVersionUID = 1L;

		public ApiException(String detailMessage, Throwable throwable) {
			super(detailMessage, throwable);
		}

		public ApiException(String detailMessage) {
			super(detailMessage);
		}
	}

	/**
	 * Thrown when there were problems parsing the response to an API call,
	 * either because the response was empty, or it was malformed.
	 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
	 * @date 2012-10-20
	 * @version 1.0.0
	 */
	public static class ParseException extends Exception {
		private static final long serialVersionUID = 1L;

		public ParseException(String detailMessage, Throwable throwable) {
			super(detailMessage, throwable);
		}
	}
}
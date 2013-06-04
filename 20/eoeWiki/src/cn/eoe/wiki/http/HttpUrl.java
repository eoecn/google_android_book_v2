package cn.eoe.wiki.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import cn.eoe.wiki.Constants;


public class HttpUrl {
	private String	host;
	private String	path;
	private Map<String,String> params;
	
	public HttpUrl(String path) {
		this(path, null);
	}
	public HttpUrl(String path, Map<String,String> params) {
		host = Constants.API_HOST;
		this.path = path;
		this.params = params;
	}
	public Map<String,String> getParams()
	{
		if(params==null)
		{
			params = new HashMap<String, String>();
		}
		return params;
	}
	public void setAction(String action)
	{
		getParams().put("action", action);
	}
	public void addParam(String key,String value)
	{
		getParams().put(key, value);
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String generateUrl()
	{
		if(TextUtils.isEmpty(host))
			return null;
		return host+"/"+path;
	}
}

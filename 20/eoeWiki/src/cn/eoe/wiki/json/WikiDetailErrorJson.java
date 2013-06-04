package cn.eoe.wiki.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class WikiDetailErrorJson extends WikiDetailParent{

	@JsonProperty("servedby")
	private String servedby;
	
	@JsonProperty("error")
	private WikiDetailErrorChild error;

	public String getServedby() {
		return servedby;
	}

	public void setServedby(String servedby) {
		this.servedby = servedby;
	}

	public WikiDetailErrorChild getError() {
		return error;
	}

	public void setError(WikiDetailErrorChild error) {
		this.error = error;
	}
	
}

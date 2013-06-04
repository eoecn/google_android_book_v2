package cn.eoe.wiki.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RecentlyUpdatedJson {
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("pageid")
	private int pageid;

	public RecentlyUpdatedJson() {}
	
	public String getType(){
		return type;
	}
	
	public String getTitle(){
		return title;
	}
	
	public int getPageid(){
		return pageid;
	}

}

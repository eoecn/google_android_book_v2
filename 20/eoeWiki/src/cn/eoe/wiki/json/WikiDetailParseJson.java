package cn.eoe.wiki.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class WikiDetailParseJson {
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("revid")
	private String revid;
	
	@JsonProperty("displaytitle")
	private String displayTitle;
	
	@JsonProperty("text")
	private WikiDetailTextJson text;
	
	public WikiDetailParseJson(){}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRevid() {
		return revid;
	}

	public void setRevid(String revid) {
		this.revid = revid;
	}

	public String getDisplayTitle() {
		return displayTitle;
	}

	public void setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
	}

	public WikiDetailTextJson getText() {
		return text;
	}

	public void setText(WikiDetailTextJson text) {
		this.text = text;
	}
}

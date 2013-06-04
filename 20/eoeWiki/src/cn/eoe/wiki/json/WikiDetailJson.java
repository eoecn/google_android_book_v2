package cn.eoe.wiki.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class WikiDetailJson extends WikiDetailParent{

	@JsonProperty("parse")
	private WikiDetailParseJson parse;

	public WikiDetailParseJson getParse() {
		return parse;
	}

	public void setParse(WikiDetailParseJson parse) {
		this.parse = parse;
	}
	
}

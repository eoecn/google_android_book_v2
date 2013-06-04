package cn.eoe.wiki.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchOffsetJson {
	@JsonProperty("sroffset")
	private int sroffset;

	public int getSroffset() {
		return sroffset;
	}

	public void setSroffset(int sroffset) {
		this.sroffset = sroffset;
	}
}

package cn.eoe.wiki.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchQueryContinusJson {
	@JsonProperty("search")
	private SearchOffsetJson search;

	public SearchOffsetJson getSearch() {
		return search;
	}

	public void setSearch(SearchOffsetJson search) {
		this.search = search;
	}

}

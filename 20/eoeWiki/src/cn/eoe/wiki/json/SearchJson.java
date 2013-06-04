package cn.eoe.wiki.json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchJson {
	@JsonProperty("search")
	private List<SearchItemJson> search;

	public List<SearchItemJson> getSearch() {
		return search;
	}

	public void setSearch(List<SearchItemJson> search) {
		this.search = search;
	}

}

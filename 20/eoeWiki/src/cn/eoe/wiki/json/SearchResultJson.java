package cn.eoe.wiki.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResultJson {
	
	@JsonProperty("query")
	private SearchJson query;

	@JsonProperty("query-continue")
	private SearchQueryContinusJson queryContinue;

	public SearchJson getQuery() {
		return query;
	}

	public SearchQueryContinusJson getQueryContinue() {
		return queryContinue;
	}



	public void setQueryContinue(SearchQueryContinusJson queryContinue) {
		this.queryContinue = queryContinue;
	}



	public void setQuery(SearchJson query) {
		this.query = query;
	}

}

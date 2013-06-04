package cn.eoe.wiki.json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecentChilds {
	@JsonProperty("recentchanges")
	private List<RecentlyUpdatedJson> recentchanges;

	public List<RecentlyUpdatedJson> getQuery() {
		return recentchanges;
	}

	public void setQuery(List<RecentlyUpdatedJson> query) {
		this.recentchanges = query;
	}
	
	
}

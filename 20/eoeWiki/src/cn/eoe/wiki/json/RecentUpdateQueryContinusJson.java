package cn.eoe.wiki.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecentUpdateQueryContinusJson {
	@JsonProperty("recentchanges")
	private UpdateRcstartJson recentchanges;

	public UpdateRcstartJson getRecentchanges() {
		return recentchanges;
	}

	public void setRecentchanges(UpdateRcstartJson recentchanges) {
		this.recentchanges = recentchanges;
	}
	
}

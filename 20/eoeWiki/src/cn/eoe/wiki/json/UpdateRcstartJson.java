package cn.eoe.wiki.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateRcstartJson {
	@JsonProperty("rcstart")
	private String rcstart;

	public String getRcstart() {
		return rcstart;
	}

	public void setRcstart(String rcstart) {
		this.rcstart = rcstart;
	}

}

package cn.eoe.wiki.json;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import android.os.Parcel;
import android.os.Parcelable;

@JsonIgnoreProperties(ignoreUnknown=true)
public class CategoryChild implements Parcelable{
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("desc")
	private String description;
	
	@JsonProperty("uri")
	private String uri;
	
	@JsonProperty("pageid")
	private String pageID;
	
	@JsonProperty("children")
	private	List<CategoryChild> children;
	
	public CategoryChild()
	{
		
	}
	
	public CategoryChild(Parcel source)
	{
		title = source.readString();
		name = source.readString();
		description = source.readString();
		uri = source.readString();
		pageID = source.readString();
		children = new ArrayList<CategoryChild>();
		source.readTypedList(children, CategoryChild.CREATOR);
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(name);
		dest.writeString(description);
		dest.writeString(uri);
		dest.writeString(pageID);
		dest.writeTypedList(children);
	}
	public static final Parcelable.Creator<CategoryChild> CREATOR = new Creator<CategoryChild>() {

		@Override
		public CategoryChild createFromParcel(Parcel source) {
			return new CategoryChild(source);
		}

		@Override
		public CategoryChild[] newArray(int size) {
			return new CategoryChild[size];
		}
	};

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	
	public String getPageID() {
		return pageID;
	}

	public void setPageID(String pageID) {
		this.pageID = pageID;
	}

	public List<CategoryChild> getChildren() {
		return children;
	}

	public void setChildren(List<CategoryChild> children) {
		this.children = children;
	}
}

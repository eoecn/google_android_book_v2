package cn.eoe.wiki.activity;

import android.os.Parcel;
import android.os.Parcelable;

public class ParamsEntity implements Parcelable{

	private String firstTitle;
	private String secondTitle;
	private String uri;
	
	public String getFirstTitle() {
		return firstTitle;
	}

	public void setFirstTitle(String firstTitle) {
		this.firstTitle = firstTitle;
	}

	public String getSecondTitle() {
		return secondTitle;
	}

	public void setSecondTitle(String secondTitle) {
		this.secondTitle = secondTitle;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

}

package cn.eoe.wiki.listener;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import cn.eoe.wiki.activity.ParamsEntity;
import cn.eoe.wiki.activity.RecentlyUpdatedActivity;
import cn.eoe.wiki.activity.WikiContentActivity;

public class RecentlyUpdatedListener implements OnClickListener {
	private String mUri;
	private String mFirstTitle;
	private String mSecondTitle;
	private RecentlyUpdatedActivity mContext;
	
	public RecentlyUpdatedListener(String pFirstTitle,String pSecondTitle,String pUri,RecentlyUpdatedActivity pContext){
		mUri = pUri;
		mFirstTitle = pFirstTitle;
		mSecondTitle = pSecondTitle;
		mContext = pContext;
	}
	
	@Override
	public void onClick(View v) {
		if(mContext.getmMainActivity().getSliderLayer().isAnimationing())
		{
			return;
		}
		Intent intent = new Intent(mContext, WikiContentActivity.class);
		ParamsEntity pe = new ParamsEntity();
		pe.setFirstTitle(mFirstTitle);
		pe.setSecondTitle(mSecondTitle);
		pe.setUri(mUri);
		intent.putExtra(WikiContentActivity.WIKI_CONTENT, pe);
		mContext.getmMainActivity().showView(2, intent);
	}
}

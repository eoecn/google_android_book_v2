package cn.eoe.wiki.listener;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import cn.eoe.wiki.activity.FavoriteActivity;
import cn.eoe.wiki.activity.ParamsEntity;
import cn.eoe.wiki.activity.WikiContentActivity;

/**
 * 点击收藏夹的第一项，跳转到detail页面
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-2
 * @version 1.0.0
 */
public class FavoriteItemListener implements OnClickListener{

	private String 				mUri;
	private FavoriteActivity 	mContext;
	private String 				mFristParentName;
	private String 				mSecondParentName;
	
	public FavoriteItemListener(String uri,FavoriteActivity context,String firstParentName,String sencondParentName){
		mUri = uri;
		mContext = context;
		mFristParentName = firstParentName;
		mSecondParentName = sencondParentName;
	}
	
	@Override
	public void onClick(View v) {
		if(mContext.getmMainActivity().getSliderLayer().isAnimationing())
		{
			return;
		}
		Intent intent = new Intent (mContext,WikiContentActivity.class);
		ParamsEntity pe = new ParamsEntity();
		pe.setFirstTitle(mFristParentName);
		pe.setSecondTitle(mSecondParentName);
		pe.setUri(mUri);
		intent.putExtra(WikiContentActivity.WIKI_CONTENT, pe);
		mContext.getmMainActivity().showView(2, intent);
	}

}

package cn.eoe.wiki.listener;

import com.umeng.analytics.MobclickAgent;

import cn.eoe.wiki.activity.ParamsEntity;
import cn.eoe.wiki.activity.SubCategoryActivity;
import cn.eoe.wiki.activity.WikiContentActivity;
import cn.eoe.wiki.utils.L;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SubCategoryListener implements OnClickListener{

	private String mUri;
	private String mFirstTitle;
	private String mSecondTitle;
	private SubCategoryActivity mContext;
	private String mName;
	
	public SubCategoryListener(String mName,String pFirstTitle,String pSecondTitle,String pUri,SubCategoryActivity pContext){
		this.mName = mName;
		mUri = pUri;
		mFirstTitle = pFirstTitle;
		mSecondTitle = pSecondTitle;
		mContext = pContext;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(mContext.getmMainActivity().getSliderLayer().isAnimationing())
		{
			return;
		}
		L.e(mUri);
		MobclickAgent.onEvent(mContext, "sub-category", "click-"+mName);
		Intent intent = new Intent (mContext,WikiContentActivity.class);
		ParamsEntity pe = new ParamsEntity();
		pe.setFirstTitle(mFirstTitle);
		pe.setSecondTitle(mSecondTitle);
		pe.setUri(mUri);
		intent.putExtra(WikiContentActivity.WIKI_CONTENT, pe);
		mContext.getmMainActivity().showView(2, intent);
	}
	
}

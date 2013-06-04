package cn.eoe.wiki.listener;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cn.eoe.wiki.R;
import cn.eoe.wiki.activity.SubCategoryActivity;

public class SubCategoryNoFinishListener implements OnClickListener{

	private SubCategoryActivity mContext;
	
	public SubCategoryNoFinishListener(SubCategoryActivity context){
		mContext = context;
	}
	
	@Override
	public void onClick(View v) {
		Toast.makeText(mContext, mContext.getString(R.string.tip_category_not_finish), Toast.LENGTH_LONG).show();
	}
	
}

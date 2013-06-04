package cn.eoe.wiki.listener;

import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import cn.eoe.wiki.activity.CategoryActivity;
import cn.eoe.wiki.activity.MainActivity;
import cn.eoe.wiki.activity.SubCategoryActivity;
import cn.eoe.wiki.json.CategoryChild;
import cn.eoe.wiki.utils.L;

/**
 * listener for the category
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-5
 * @version 1.0.0
 */
public class CategoryListener implements OnClickListener {

	private CategoryActivity 	context;
	private CategoryChild 		category;
	private String				parentName;
	
	public CategoryListener(CategoryActivity context,CategoryChild category,String parentName)
	{
		
		this.context = context;
		this.category = category;
		this.parentName = parentName;
	}

	@Override
	public void onClick(View v) {
		if(context.getmMainActivity().getSliderLayer().isAnimationing())
		{
			return;
		}
		L.e("category click:"+category.getUri());
		MobclickAgent.onEvent(context, "home", "click-"+category.getName());
		Intent intent = new Intent (context,SubCategoryActivity.class);
		intent.putExtra(SubCategoryActivity.KEY_CATEGORY, category);
		intent.putExtra(SubCategoryActivity.KEY_PARENT_TITLE, parentName);
		context.getmMainActivity().showView(1, intent);
	}
	
}

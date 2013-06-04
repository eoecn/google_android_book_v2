package cn.eoe.wiki.listener;

import com.umeng.analytics.MobclickAgent;

import android.view.View;
import android.view.View.OnClickListener;
import cn.eoe.wiki.activity.CategoryActivity;
import cn.eoe.wiki.activity.MainCategoryActivity;
import cn.eoe.wiki.json.CategoryChild;

/**
 * listener for the category title
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-5
 * @version 1.0.0
 */
public class CategoryTitleListener implements OnClickListener {

	private MainCategoryActivity context;
	private CategoryChild category;
	
	public CategoryTitleListener(MainCategoryActivity context,CategoryChild category)
	{
		
		this.context = context;
		this.category = category;
	}

	@Override
	public void onClick(View v) {
		context.refreshCategory(category);
	}
	
}

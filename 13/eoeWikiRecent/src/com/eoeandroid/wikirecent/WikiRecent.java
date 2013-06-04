package com.eoeandroid.wikirecent;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
/**
 * {@link AppWidgetProvider} ，该应用只需要在onUpdate()方法中启动一个{@link AsyncTask} 去获取数据
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @date 2012-10-20
 * @version 1.0.0
 *
 */
public class WikiRecent extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		new RecentAsyncTask(context).execute();
	}

}

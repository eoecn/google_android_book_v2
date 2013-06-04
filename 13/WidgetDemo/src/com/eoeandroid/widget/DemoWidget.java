package com.eoeandroid.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
/**
 * AppWidgetProvider
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @date 2012-10-21
 * @version 1.0.0
 *
 */
public class DemoWidget extends AppWidgetProvider {
	private static final String tag = "DemoWidget";

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		Log.e(tag, "onDeleted");
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		Log.e(tag, "onDisabled");
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Log.e(tag, "onEnabled");
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		Log.e(tag, "onReceive:"+intent.getAction());
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.e(tag, "onUpdate");
		//遍历需要更新有appWidgetIds
		for(int widgetId: appWidgetIds)
		{
			update(context, appWidgetManager, widgetId);
		}
	}

	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);
		Log.e(tag, "onAppWidgetOptionsChanged");
	}
	
	public static void update(Context context, AppWidgetManager appWidgetManager,
			int widgetId)
	{
		//从Preferences根据WidgetID取出内容
		String title = context.getSharedPreferences(WidgetConfig.PREFS_NAME, 0).getString(WidgetConfig.PREF_TITLE_KEY + widgetId, "");
		//new一个新的RemoteViews,并赋值
		RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget);
		views.setTextViewText(R.id.textView, title);
		Log.e(tag, "textView:"+title);
		//通知系统更新
		appWidgetManager.updateAppWidget(widgetId, views);
	}
}

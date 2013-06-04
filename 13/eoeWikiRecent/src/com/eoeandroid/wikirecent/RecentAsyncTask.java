package com.eoeandroid.wikirecent;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.eoeandroid.wikirecent.WikiHelper.ApiException;
import com.eoeandroid.wikirecent.WikiHelper.ParseException;
import com.eoeandroid.wikirecent.WikiHelper.WikiInfo;

/**
 * 用来完成获取最近更新的后台任务
 * 
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @date 2012-10-20
 * @version 1.0.0
 * 
 */
public class RecentAsyncTask extends AsyncTask<Object, Integer, Boolean> {
	private static final String TAG = "WikiHelper";
	private Context mContext;

	public RecentAsyncTask(Context context) {
		mContext = context;
	}

	@Override
	protected Boolean doInBackground(Object... params) {
		// 获取到需要显示的widget布局，并初始化
		RemoteViews updateViews = buildUpdate();

		// 通知 AppWidgetManager 更新所有的widget
		ComponentName thisWidget = new ComponentName(mContext, WikiRecent.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(mContext);
		manager.updateAppWidget(thisWidget, updateViews);
		return true;
	}

	public RemoteViews buildUpdate() {
		RemoteViews updateViews = null;
		WikiInfo pageContent = null;

		try {
			// 获取最新更新的wiki
			pageContent = WikiHelper.getRecentWiki();
			Log.e(TAG, "pageContent:" + pageContent);
			updateViews = new RemoteViews(mContext.getPackageName(), R.layout.layout);

			//给widget页面元素赋值
			String title = pageContent.getTitle();
			String name = mContext.getString(R.string.widget_user, pageContent.getUser());
			updateViews.setTextViewText(R.id.tv_title, title);
			updateViews.setTextViewText(R.id.tv_user, name);

			// 当用户点击widget的时候，跳转到widget详细页面
			String url = title.replace(" ", "_");
			Intent defineIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://wiki.eoeandroid.com/" + url));
			PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0 , defineIntent, 0 );
			updateViews.setOnClickPendingIntent(R.id.widget, pendingIntent);
		} catch (ApiException e) {
			Log.e(TAG, "Couldn't contact API", e);
			CharSequence errorMessage = mContext.getText(R.string.widget_api_error);
			updateViews = dealWithExcaption(errorMessage);
		}catch (ParseException e) {
			Log.e(TAG, "Couldn't contact API", e);
			CharSequence errorMessage = mContext.getText(R.string.widget_parse_error);
			updateViews = dealWithExcaption(errorMessage);
		}
		return updateViews;
	}
	private RemoteViews dealWithExcaption(CharSequence error)
	{
		//如果出现了错误 ，则在页面提醒用户
		RemoteViews updateViews = new RemoteViews(mContext.getPackageName(), R.layout.layout_message);
		updateViews.setTextViewText(R.id.tv_message, error);
		updateViews.setTextColor(R.id.tv_message, Color.RED);
		return updateViews;
	}

}

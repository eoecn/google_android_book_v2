package com.eoeandroid.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
/**
 * widget的配置界面 
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @date 2012-10-21
 * @version 1.0.0
 *
 */
public class WidgetConfig extends Activity{
	private static final String 	tag = "WidgetConfig";
	public static final String 		PREFS_NAME = "WidgetDemo";
	public static final String 		PREF_TITLE_KEY = "title_";

	private EditText		etInput;
	private Button 			btnOK;
	private int 			mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//当用户在配置过程中，点击了返回键，也可以通知宿主当前  Widget已经被取消了
		setResult(RESULT_CANCELED);
		setContentView(R.layout.config);
		etInput = (EditText)findViewById(R.id.editText);
		btnOK = (Button)findViewById(R.id.button);
		
		float margin = getResources().getDimension(R.dimen.widget_margin);
		Log.e(tag, "margin:"+margin);
		System.out.println("margin:"+margin);
		//从Intent中找到Widget的ID
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        Log.e(tag, "mAppWidgetId:"+mAppWidgetId);
        
        // 如果给出了一个不可用的Widget ID ,则关闭当前页面
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
		
		etInput.setText(loadTitlePref(mAppWidgetId));
		btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//当OK按钮按下后，我们保存 输入的文字到Preference
	            String title = etInput.getText().toString();
	            saveTitlePref(mAppWidgetId, title);

	            // 设置返回值与Widget的ID
	            Intent resultValue = new Intent();
	            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
	            setResult(RESULT_OK, resultValue);

	            //更新Widget
				Context context = WidgetConfig.this;
	            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	            DemoWidget.update(context, appWidgetManager, mAppWidgetId);
	            finish();
			}
		});
	}

	//保存text到Preference文件中去
	private void saveTitlePref(int appWidgetId, String text) {
        SharedPreferences.Editor prefs = getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_TITLE_KEY + appWidgetId, text);
        prefs.commit();
    }

	//从Preference文件中读取出值
    private String loadTitlePref(int appWidgetId) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        String prefix = prefs.getString(PREF_TITLE_KEY + appWidgetId, null);
        if (prefix != null) {
            return prefix;
        } else {
            return "Default Text";
        }
    }
}

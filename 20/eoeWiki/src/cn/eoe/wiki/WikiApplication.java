package cn.eoe.wiki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Application;
import android.text.TextUtils;
import cn.eoe.wiki.activity.MainActivity;
import cn.eoe.wiki.utils.L;
/**
 * 定义我们整个应用程序的Application对象，在此类中可以实例化一些与整个应用周期有关的变量与属性。<br>
 * 可以作一些简单的缓存，但是不建议做太大的缓存保存
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-2
 * @version 1.0.0
 */
public class WikiApplication extends Application {
	public static 	WikiApplication		application;
	public static 	StringBuilder			wikiHtml;
	public 	 		MainActivity		mainActivity;
	
	public static WikiApplication getApplication() {
		if(application==null) {
			throw new NullPointerException("The application may be not running .");
		}
		return application;
	}
	
	public MainActivity getMainActivity() {
		return mainActivity;
	}

	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		//ini the application config
		WikiConfig.initConfig(application);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		L.e("onTerminate");
	}

	public static StringBuilder getWikiHtml()
	{
		if(wikiHtml == null) {
			try {
				InputStream inputStream =  getApplication().getResources().openRawResource(R.raw.html);
				BufferedReader r = new BufferedReader(new InputStreamReader(inputStream)); 
				wikiHtml = new StringBuilder(); 
				String line; 
				while ((line = r.readLine()) != null) { 
					wikiHtml.append(line); 
				}
			} catch (Exception e) {
				L.e("read html file exception:"+e.getMessage());
				throw new NullPointerException("can not read the html file");
			}
		}
		return wikiHtml;
	}
}

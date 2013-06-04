package com.baidumap.demo;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.*;
import com.baidumap.demo.R;

public class BMapDemoApp extends Application {
	static BMapDemoApp mDemoApp;
	BMapManager mBMapMan = null;
	public String mStrKey = "2C70D4F3E0810C7BF615DFFCDF9244D6AC2C6CCC";
	boolean m_bKeyRight = true;
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	static class MyGeneralListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
			Log.d("MyGeneralListener", "onGetNetworkState error is "+ iError);
			Toast.makeText(BMapDemoApp.mDemoApp.getApplicationContext(), "您的网络出错啦！",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onGetPermissionState(int iError) {
			Log.d("MyGeneralListener", "onGetPermissionState error is "+ iError);
			if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Toast.makeText(BMapDemoApp.mDemoApp.getApplicationContext(), 
						"请输入正确的授权Key！",
						Toast.LENGTH_LONG).show();
				BMapDemoApp.mDemoApp.m_bKeyRight = false;
			}
		}
	}

	@Override
    public void onCreate() {
		Log.v("BMapApiDemoApp", "onCreate");
		mDemoApp = this;
		mBMapMan = new BMapManager(this);
		mBMapMan.init(this.mStrKey, new MyGeneralListener());
		mBMapMan.getLocationManager().setNotifyInternal(10, 5);
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onTerminate();
	}

}

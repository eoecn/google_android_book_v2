package com.eoeAndroid.helloService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HelloService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		System.out.println("∆Ù∂ØService£¨–›√ﬂ10√Î");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}

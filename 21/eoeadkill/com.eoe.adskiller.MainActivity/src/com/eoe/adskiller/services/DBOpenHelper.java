package com.eoe.adskiller.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {

	public DBOpenHelper(Context context) { 
		super(context, "adskiller", null, 1);
		Log.d("db", "db is created----------------");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE ads (adId integer primary key autoincrement, receivername varchar(200))");
//		db.execSQL("insert into ads (1,com.adwo.)");
//		db.execSQL("insert into ads (2,com.mt.airad.MultiAD)");
//		db.execSQL("insert into ads (3,com.waps.OffersWebView)");
//		db.execSQL("insert into ads (4,cn.domob.android.ads.DomobActivity)");
//		db.execSQL("insert into ads (5,net.youmi.)");
		db.execSQL("insert into ads(receivername) values('com.adwo.')");
		db.execSQL("insert into ads(receivername) values('com.mt.airad.MultiAD')");
		db.execSQL("insert into ads(receivername) values('com.waps.OffersWebView')");
		db.execSQL("insert into ads(receivername) values('cn.domob.android.ads.DomobActivity')");
		db.execSQL("insert into ads(receivername) values('com.google.ads.AdActivity')");
		
		
		
		db.execSQL("insert into ads(receivername) values('net.youmi.android.AdActivity')");
		db.execSQL("insert into ads(receivername) values('com.adwo.adsdk.AdwoAdBrowserActivity')");
		db.execSQL("insert into ads(receivername) values('com.wooboo.adlib_android.AdActivity')");
		db.execSQL("insert into ads(receivername) values('com.wooboo.adlib_android.FullActivity')");
		db.execSQL("insert into ads(receivername) values('com.vpon.adon.android.WebInApp')");
		
		
		db.execSQL("insert into ads(receivername) values('com.legend.hot.free.app.share.ShareActivity')");
		db.execSQL("insert into ads(receivername) values('com.adchina.android.ads.views.AdBrowserView')");
		db.execSQL("insert into ads(receivername) values('com.waps.OffersWebView')");
		db.execSQL("insert into ads(receivername) values('com.mt.airad.MultiAD')");
		db.execSQL("insert into ads(receivername) values('com.dianru.sdk.AdActivity')");
		
		
		
		db.execSQL("insert into ads(receivername) values('com.nd.dianjin.activity.OfferAppActivity')");
		db.execSQL("insert into ads(receivername) values('com.dianru.sdk.AdActivity')");
		db.execSQL("insert into ads(receivername) values('com.dianru.sdk.NetWorkChanged')");
		db.execSQL("insert into ads(receivername) values('com.lmmob.ad.sdk.LmMobAdWebView')");
		db.execSQL("insert into ads(receivername) values('com.lmmob.ad.sdk.LmMobFullImageActivity')");
		
		db.execSQL("insert into ads(receivername) values('com.mobisage.android.MobiSageActivity')");
		db.execSQL("insert into ads(receivername) values('com.mobisage.android.MobiSageApkService')");
		db.execSQL("insert into ads(receivername) values('net.youmi.android.appoffers.YoumiOffersActivity')");
		db.execSQL("insert into ads(receivername) values('com.nd.dianjin.activity.OfferAppActivity')");
		db.execSQL("insert into ads(receivername) values('com.nd.dianjin.service.PackageChangedService')");
		
		
		db.execSQL("insert into ads(receivername) values('net.youmi.toolkit.android.TKActivity')");
		db.execSQL("insert into ads(receivername) values('net.youmi.toolkit.android.PushService')");
		db.execSQL("insert into ads(receivername) values('com.kuguo.pushads.PushAdsActivity')");
		db.execSQL("insert into ads(receivername) values('com.dianru.push.NotifyActivity')");
		db.execSQL("insert into ads(receivername) values('com.dianru.push.NetWorkChanged')");
		
		
		
		
		
		Log.d("db", "tablse ads is created----------------");
		
	}
	
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}

package com.eoe.adskiller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import com.eoe.adskiller.services.DBOpenHelper;



import android.content.pm.ApplicationInfo;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

public class ScanTask extends Thread {
	
	
	private  boolean end;
	Context  mycontext;
	int counter=0;
	AssetManager am = null;
	List<PackageInfo>  packages;
	Handler myscanHandler = null;
	XmlResourceParser xml ;
	
	
	public ScanTask(Context context,Handler theHandler)
	{
		mycontext=context;
		myscanHandler=theHandler;
	}
	
	
	
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		super.run();
		packages = mycontext.getPackageManager().getInstalledPackages(0); 

//		Log.d("appdisply num", "num: "+packages.size());
	
		
		String []adsString = null;
		int adsnum=0;
		DBOpenHelper dbOpenHelper=new DBOpenHelper(mycontext);
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
//		Cursor cursor = db.rawQuery("select * from ads where adId=?",new String[]{""});
		Cursor cursor = db.query("ads", new String[] { "receivername" }, null,
				null, null, null, null, null);

		while(cursor.moveToNext()){
//			int adid = cursor.getInt(cursor.getColumnIndex("adId"));
			String name = cursor.getString(0);
			if (null == adsString){
				adsString = new String[cursor.getCount()];
			}
			adsString[adsnum]=name;
			Log.d("ads string", adsString[adsnum]);
			adsnum++;	
		}
		
		
		
		
		for(int i=0;i<packages.size();i++)
		{
			
			int deciedecount=0;
			String []adresultString;
			PackageInfo packageInfo=packages.get(i);
		 
			if((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0)
			{
			
			
			try {
				// String installLocation = "";
					am =  mycontext.createPackageContext(packageInfo.packageName, 0)
							.getAssets();
					XmlResourceParser xml = am
							.openXmlResourceParser("AndroidManifest.xml");
					int eventType = xml.getEventType();
					while (eventType != XmlPullParser.END_DOCUMENT) {
						switch (eventType) {
						case XmlPullParser.START_TAG:
						//	Log.i("xml tag", xml.getName());
							if (xml.getName().compareToIgnoreCase("receiver") == 0) {

								int countAttrs = xml.getAttributeCount();
								for (int j = 0; j < countAttrs; j++) 
								{							
								}
								for(int adscount=0;adscount<adsnum;adscount++)
								{
									if((xml.getAttributeValue(0).contains(adsString[adscount])==true))
									{
										deciedecount++;	
									}
								}
							
							}
							if (xml.getName().compareToIgnoreCase("activity") == 0) {
								
								
								for(int adscount=0;adscount<adsnum;adscount++)
								{
									if((xml.getAttributeValue(0).contains(adsString[adscount])==true))
									{
										deciedecount++;	
									}
								}
							
								
							}
						}
						eventType = xml.next();
					}


			} catch (NameNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}
			
			
			
			
			
			
			


			Message msg = new Message();    
            AppInfo tmpInfo = new AppInfo(); 
 	        tmpInfo.appName = packageInfo.applicationInfo.loadLabel(mycontext.getPackageManager()).toString(); 
 	        tmpInfo.packageName = packageInfo.packageName; 
 	        tmpInfo.versionName = packageInfo.versionName; 
 	        tmpInfo.versionCode = packageInfo.versionCode; 
 	        tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(mycontext.getPackageManager());
 	        
 	        msg.obj=tmpInfo;
 	        msg.what=0;
 	        

            if((deciedecount>0))
	        {
            	try {
					sleep(200);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
            	this.myscanHandler.sendMessage(msg);
	        }
        	
			}
		}
		Message endmsg=new Message();
		endmsg.what=1;
		this.myscanHandler.sendMessage(endmsg);
	
	}
}




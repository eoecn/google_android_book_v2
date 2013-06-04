package com.baidumap.demo;

import java.util.ArrayList;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKOLSearchRecord;
import com.baidu.mapapi.MKOLUpdateElement;
import com.baidu.mapapi.MKOfflineMap;
import com.baidu.mapapi.MKOfflineMapListener;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;
import com.baidumap.demo.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OfflineDemo extends MapActivity implements MKOfflineMapListener {
	
	private MapView mMapView = null;
	private MKOfflineMap mOffline = null;
	private EditText mEditCityName;
	private EditText mEditCityId;
	private TextView mText;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offline);
		BMapDemoApp app = (BMapDemoApp)this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new BMapDemoApp.MyGeneralListener());
		}
		app.mBMapMan.start();
        super.initMapActivity(app.mBMapMan);
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.setBuiltInZoomControls(true);
        mOffline = new MKOfflineMap();
        mOffline.init(app.mBMapMan, this);
        ArrayList<MKOLUpdateElement> info = mOffline.getAllUpdateInfo();
        if (info != null) {
        	Log.d("OfflineDemo", String.format("has %d city info", info.size()));
        	if (info.get(0).status == MKOLUpdateElement.FINISHED) {
        	}
        }
        ArrayList<MKOLSearchRecord> records = mOffline.getHotCityList();
        if (records != null) {
        	Log.d("OfflineDemo", String.format("has %d hot city", records.size()));
        }
        mEditCityName = (EditText)findViewById(R.id.city);
        mEditCityId = (EditText)findViewById(R.id.cityid);
        mText = (TextView)findViewById(R.id.text);
        Button btn = (Button)findViewById(R.id.start);
        btn.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				int cityid = -1;
				try {
					cityid = Integer.parseInt(mEditCityId.getText().toString());
				} catch (Exception e) {					
				}
		        if (mOffline.start(cityid)) {
		        	Log.d("OfflineDemo", String.format("start cityid:%d", cityid));
		        } else {
		        	Log.d("OfflineDemo", String.format("not start cityid:%d", cityid));
		        }
			}
		});
        btn = (Button)findViewById(R.id.stop);
        btn.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				int cityid = -1;
				try {
					cityid = Integer.parseInt(mEditCityId.getText().toString());
				} catch (Exception e) {		
				}
		        if (mOffline.pause(cityid)) {
		        	Log.d("OfflineDemo", String.format("stop cityid:%d", cityid));
		        } else {
		        	Log.d("OfflineDemo", String.format("not pause cityid:%d", cityid));
		        }
			}
		});        
        btn = (Button)findViewById(R.id.search);
        btn.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				ArrayList<MKOLSearchRecord> records = mOffline.searchCity(mEditCityName.getText().toString());
				if (records == null || records.size() != 1)
					return;
				mEditCityId.setText(String.valueOf(records.get(0).cityID));
			}
		});       
        btn = (Button)findViewById(R.id.del);
        btn.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				int cityid = -1;
				try {
					cityid = Integer.parseInt(mEditCityId.getText().toString());
				} catch (Exception e) {				
				}
		        if (mOffline.remove(cityid)) {
		        	Log.d("OfflineDemo", String.format("del cityid:%d", cityid));
		        } else {
		        	Log.d("OfflineDemo", String.format("not del cityid:%d", cityid));
		        }
			}
		});        
        btn = (Button)findViewById(R.id.scan);
        btn.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				int num = mOffline.scan();
				if (num != 0)
					mText.setText(String.format("已安装%d个离线包", num));
				Log.d("OfflineDemo", String.format("scan offlinemap num:%d", num));
			}
		});        
        btn = (Button)findViewById(R.id.get);
        btn.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				int cityid = -1;
				try {
					cityid = Integer.parseInt(mEditCityId.getText().toString());
				} catch (Exception e) {				
				}
				MKOLUpdateElement element = mOffline.getUpdateInfo(cityid);
				if (element != null) {
					new AlertDialog.Builder(OfflineDemo.this)
					.setTitle(element.cityName)
					.setMessage(String.format("大小:%.2fMB 已下载%d%%", ((double)element.size)/1000000, element.ratio))
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {						
						}
					}).show();
				}
			}
		}); 
	}	
	@Override
	protected void onPause() {
		BMapDemoApp app = (BMapDemoApp)this.getApplication();
		app.mBMapMan.stop();
		super.onPause();
	}
	@Override
	protected void onResume() {
		BMapDemoApp app = (BMapDemoApp)this.getApplication();
		app.mBMapMan.start();
		super.onResume();
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onGetOfflineMapState(int type, int state) {
		switch (type) {
		case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
			{
				Log.d("OfflineDemo", String.format("cityid:%d update", state));
				MKOLUpdateElement update = mOffline.getUpdateInfo(state);
				mText.setText(String.format("%s : %d%%", update.cityName, update.ratio));
			}
			break;
		case MKOfflineMap.TYPE_NEW_OFFLINE:
			Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
			break;
		case MKOfflineMap.TYPE_VER_UPDATE:
			Log.d("OfflineDemo", String.format("new offlinemap ver"));
			break;
		}		 
	}
}
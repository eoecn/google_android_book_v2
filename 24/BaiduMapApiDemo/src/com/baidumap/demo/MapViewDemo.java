package com.baidumap.demo;


import java.util.ArrayList;
import java.util.List;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.PoiOverlay;
import com.baidu.mapapi.RouteOverlay;
import com.baidu.mapapi.TransitOverlay;
import com.baidumap.demo.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class MapViewDemo extends MapActivity {
	public static final int MYLOC = Menu.FIRST;
	public static final int ZHOUBIAN = Menu.FIRST + 1;
	public static final int GONGJIAO = Menu.FIRST + 2;
	public static final int CHUXING = Menu.FIRST + 3;
	public static final int GEO = Menu.FIRST + 4;
	public static final int OFFLINE = Menu.FIRST + 5;
	private LinearLayout SearchInCity,SuggestionSearch,SuggestionList,busSearch,routeli;
	private GeoPoint mypt;
	private MapView mapView ;
	private LocationListener mLocationListener = null;//onResume时注册此listener，onPause时需要Remove
	private MyLocationOverlay mLocationOverlay = null;	//定位图层
	private Button mBtnSearch ,busbtn;	// 搜索按钮
	private Button mSuggestionSearch = null;  //suggestion搜索
	private ListView mSuggestionList = null;
	public  static String mStrSuggestions[] = {};
	private MKSearch mSearch = null;	// 搜索模块，也可去掉地图模块独立使用
	private BMapDemoApp app;
	private String mCityName;
	private Boolean isbus=false,issearch=false;
	private Button mBtnDrive = null;	// 驾车搜索
	private Button mBtnTransit = null;	// 公交搜索
	private Button mBtnWalk = null;	// 步行搜索
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.mapviewdemo);
        
		app = (BMapDemoApp)this.getApplication();
		 Log.v("baidumap",""+(app.mBMapMan==null));
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new BMapDemoApp.MyGeneralListener());
		}
		if (!app.m_bKeyRight) {
			Toast.makeText(this, "授权Key错误！", Toast.LENGTH_LONG);
		}
		app.mBMapMan.start();
        // 如果使用地图SDK，请初始化地图Activity
        super.initMapActivity(app.mBMapMan);
        mapView = (MapView)findViewById(R.id.bmapView);
        mapView.setBuiltInZoomControls(true);
        //设置在缩放动画过程中也显示overlay,默认为不绘制
        mapView.setDrawOverlayWhenZooming(true);
        mapView.setDoubleClickZooming(true);
        //mapView.setTraffic(true);
        
     // myloc定位图层
        mLocationOverlay = new MyLocationOverlay(this, mapView);
     // 注册定位事件
        mLocationListener = new LocationListener(){
			@Override
			public void onLocationChanged(Location location) {
				if (location != null){
					//获取到自己的位置并保存到mypt
					mypt = new GeoPoint((int)(location.getLatitude()*1e6),
							(int)(location.getLongitude()*1e6));
				}
			}
        };
        findview();
	}
	private void findview()
	{
		SearchInCity=(LinearLayout)this.findViewById(R.id.SearchInCity);
		SuggestionSearch=(LinearLayout)this.findViewById(R.id.SuggestionSearch);
		SuggestionList=(LinearLayout)this.findViewById(R.id.SuggestionList);
		busSearch=(LinearLayout)this.findViewById(R.id.busSearch);
		routeli=(LinearLayout)this.findViewById(R.id.routeli);
		SearchInCity.setVisibility(View.INVISIBLE);
		SuggestionSearch.setVisibility(View.INVISIBLE);
		SuggestionList.setVisibility(View.INVISIBLE);
		busSearch.setVisibility(View.INVISIBLE);
		routeli.setVisibility(View.INVISIBLE);
		mSuggestionList = (ListView) findViewById(R.id.listView1);
	        // 设定搜索按钮的响应
	    mBtnSearch = (Button)findViewById(R.id.search);
	    mBtnSearch.setOnClickListener(clickListener); 
	           // 设定suggestion响应
	    mSuggestionSearch = (Button)findViewById(R.id.suggestionsearch);
		mSuggestionSearch.setOnClickListener(clickListener); 
			//bus search
		busbtn = (Button)findViewById(R.id.busbtn);
		busbtn.setOnClickListener(clickListener); 
			// 设定出行搜索按钮的响应
		mBtnDrive = (Button)findViewById(R.id.drive);
		mBtnTransit = (Button)findViewById(R.id.transit);
		mBtnWalk = (Button)findViewById(R.id.walk);
		mBtnDrive.setOnClickListener(clickListener); 
		mBtnTransit.setOnClickListener(clickListener); 
		mBtnWalk.setOnClickListener(clickListener);
			 // 初始化搜索模块，注册事件监听
	    mSearch = new MKSearch();
	    mSearch.init(app.mBMapMan, new MKSearchListener(){
	    public void onGetPoiResult(MKPoiResult res, int type, int error) {
					// 错误号可参考MKEvent中的定义
					if (error != 0 || res == null) {
						Toast.makeText(MapViewDemo.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
						return;
					}	
				if(isbus)	{
						// 找到公交路线poi node
		                MKPoiInfo curPoi = null;
		                int totalPoiNum  = res.getNumPois();
						for( int idx = 0; idx < totalPoiNum; idx++ ) {
							Log.d("busline", "the busline is " + idx);
		                    curPoi = res.getPoi(idx);
		                    if ( 2 == curPoi.ePoiType ) {
		                    	break;
		                    }
						}
						mSearch.busLineSearch(mCityName, curPoi.uid);						
					}
				else if(issearch){
						   // 将地图移动到第一个POI中心点
					    if (res.getCurrentNumPois() > 0) {
						    // 将poi结果显示到地图上
							PoiOverlay poiOverlay = new PoiOverlay(MapViewDemo.this, mapView);
							poiOverlay.setData(res.getAllPoi());
							mapView.getOverlays().clear();
							mapView.getOverlays().add(poiOverlay);
							mapView.invalidate();
							mapView.getController().animateTo(res.getPoi(0).pt);
							mapView.getController().setZoom(16);
					    } else if (res.getCityListNum() > 0) {
					    	String strInfo = "在";
					    	for (int i = 0; i < res.getCityListNum(); i++) {
					    		strInfo += res.getCityListInfo(i).city;
					    		strInfo += ",";
					    	}
					    	strInfo += "找到结果";
							Toast.makeText(MapViewDemo.this, strInfo, Toast.LENGTH_LONG).show();
					    }
					    
					}

				 
				}
				public void onGetDrivingRouteResult(MKDrivingRouteResult res,
						int error) {
					// 错误号可参考MKEvent中的定义
					if (error != 0 || res == null) {
						Toast.makeText(MapViewDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
						return;
					}
					RouteOverlay routeOverlay = new RouteOverlay(MapViewDemo.this, mapView);
				    routeOverlay.setData(res.getPlan(0).getRoute(0));
				    mapView.getOverlays().clear();
				    mapView.getOverlays().add(routeOverlay);
				    mapView.invalidate();
				    mapView.getController().setZoom(14);
				    mapView.getController().animateTo(res.getStart().pt);
				}
				public void onGetTransitRouteResult(MKTransitRouteResult res,
						int error) {
					if (error != 0 || res == null) {
						Toast.makeText(MapViewDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
						return;
					}
					TransitOverlay  routeOverlay = new TransitOverlay (MapViewDemo.this, mapView);
				    routeOverlay.setData(res.getPlan(0));
				    mapView.getOverlays().clear();
				    mapView.getOverlays().add(routeOverlay);
				    mapView.invalidate();
				    mapView.getController().setZoom(14);
				    mapView.getController().animateTo(res.getStart().pt);
				}
				public void onGetWalkingRouteResult(MKWalkingRouteResult res,
						int error) {
					if (error != 0 || res == null) {
						Toast.makeText(MapViewDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
						return;
					}
					RouteOverlay routeOverlay = new RouteOverlay(MapViewDemo.this, mapView);
				    routeOverlay.setData(res.getPlan(0).getRoute(0));
				    mapView.getOverlays().clear();
				    mapView.getOverlays().add(routeOverlay);
				    mapView.invalidate();
				    mapView.getController().setZoom(14);
				    mapView.getController().animateTo(res.getStart().pt);
				}
				public void onGetAddrResult(MKAddrInfo res, int error) {
					if (error != 0) {
						String str = String.format("错误号：%d", error);
						Toast.makeText(MapViewDemo.this, str, Toast.LENGTH_LONG).show();
						return;
					}
				//	mapView.getController().animateTo(res.geoPt);		
					String strInfo = String.format("纬度：%f 经度：%f\r\n", res.geoPt.getLatitudeE6()/1e6, 
					res.geoPt.getLongitudeE6()/1e6)+res.strAddr;
					Toast.makeText(MapViewDemo.this, strInfo, Toast.LENGTH_LONG).show();
				//	Drawable marker = getResources().getDrawable(R.drawable.iconmarka);  //得到需要标在地图上的资源
				//	marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());   //为maker定义位置和边界
				//	mapView.getOverlays().clear();
				//	mapView.getController().setZoom(16);
				//	mapView.getOverlays().add(new OverItemT(marker, MapViewDemo.this, res.geoPt, res.strAddr));
				}
				public void onGetBusDetailResult(MKBusLineResult result, int iError) {
					if (iError != 0 || result == null) {
						Toast.makeText(MapViewDemo.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
						return;
			        }

					RouteOverlay routeOverlay = new RouteOverlay(MapViewDemo.this, mapView);
				    routeOverlay.setData(result.getBusRoute());
				    mapView.getOverlays().clear();
				    mapView.getOverlays().add(routeOverlay);
				    mapView.invalidate();
				    mapView.getController().setZoom(16);
				    mapView.getController().animateTo(result.getBusRoute().getStart());
				
				}
				@Override
				public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
					// TODO Auto-generated method stub
					if (arg1 != 0 || res == null) {
						Toast.makeText(MapViewDemo.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
						return;
					}
					int nSize = res.getSuggestionNum();
					mStrSuggestions = new String[nSize];
					for (int i = 0; i < nSize; i++) {
						mStrSuggestions[i] = res.getSuggestion(i).city + res.getSuggestion(i).key;
					}
					ArrayAdapter<String> suggestionString = new ArrayAdapter<String>(MapViewDemo.this, android.R.layout.simple_list_item_1,mStrSuggestions);
					mSuggestionList.setAdapter(suggestionString);
					Toast.makeText(MapViewDemo.this, "suggestion callback", Toast.LENGTH_LONG).show();
				}		
	        });
	}
	@Override
	protected void onPause() {
		app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
		mLocationOverlay.disableMyLocation();
        mLocationOverlay.disableCompass(); // 关闭指南针
		app.mBMapMan.stop();
		super.onPause();
	}
	@Override
	protected void onResume() {
		
		 app.mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
	     mLocationOverlay.enableMyLocation();
	     mLocationOverlay.enableCompass(); // 打开指南针
		app.mBMapMan.start();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	   @Override
	    protected void onDestroy() {
		
		super.onDestroy();}
	private void unshowall()
	{
		SearchInCity.setVisibility(View.INVISIBLE);
		SuggestionSearch.setVisibility(View.INVISIBLE);
		SuggestionList	.setVisibility(View.INVISIBLE);
		busSearch.setVisibility(View.INVISIBLE);
		routeli.setVisibility(View.INVISIBLE);;
		
	}
	/**
	 * 创建菜单(android框架回调,super.onCreate()中)
	 */
	public boolean onCreateOptionsMenu(Menu aMenu) {
		super.onCreateOptionsMenu(aMenu);
		aMenu.add(0, MYLOC, 0, R.string.MYLOC);
		aMenu.add(0, ZHOUBIAN, 0, R.string.ZHOUBIAN);
		aMenu.add(0, GONGJIAO, 0, R.string.GONGJIAO);
		aMenu.add(0, CHUXING, 0, R.string.CHUXING);
		aMenu.add(0, GEO, 0, R.string.GEO);
		aMenu.add(0, OFFLINE, 0, R.string.OFFLINE);
		return true;
	}
	/**
	 * 菜单项点击事件处理(android框架回调)
	 */
	public boolean onOptionsItemSelected(MenuItem aMenuItem) {
		Intent intent = new Intent();
		switch (aMenuItem.getItemId()) {
		case MYLOC:
			if(!mapView.getOverlays().contains(mLocationOverlay))
			{
				mapView.getOverlays().add(mLocationOverlay);
			}
			GeoPoint ptCenter = mypt;
			mSearch.reverseGeocode(ptCenter);
			mapView.getController().setZoom(16);
			mapView.getController().animateTo(mypt);
			break;
		case ZHOUBIAN:
			if(SearchInCity.isShown()){
			SearchInCity.setVisibility(View.INVISIBLE);
			issearch=false;
			}else
			{
				unshowall();
			SearchInCity.setVisibility(View.VISIBLE);
			issearch=true;
			}
			
			break;
		case GONGJIAO:
			if(busSearch.isShown())
			{
			busSearch.setVisibility(View.INVISIBLE);
			isbus=false;
			}else
			{
				unshowall();
			busSearch.setVisibility(View.VISIBLE);
			isbus=true;
			}
			break;
		case CHUXING:
			if(routeli.isShown())
			{
			routeli.setVisibility(View.INVISIBLE);
			}else
			{unshowall();
			routeli.setVisibility(View.VISIBLE);
			}
			break;
		case GEO:
			if(!mapView.isSatellite())
			{
				mapView.setSatellite(true);
			}else
			{
				mapView.setSatellite(false);
			}
			
			break;
	case OFFLINE:
		intent.setClass(this, OfflineDemo.class);
		this.startActivity(intent);
		break;
		}
		return super.onOptionsItemSelected(aMenuItem);
	}
	
	 OnClickListener clickListener = new OnClickListener(){
			public void onClick(View v) {
				switch (v.getId())
				{
				case R.id.search:
				SearchButtonProcess(v);
				break;
				case R.id.suggestionsearch:
				SuggestionSearchButtonProcess(v);
				case R.id.busbtn:
				busSearchButtonProcess(v);
				break;
				case R.id.drive:
				case R.id.transit:
				case R.id.walk:
					routeSearchButtonProcess(v);
				break;
				}
			}
     };
     
     void busSearchButtonProcess(View v) 
 	{
 			EditText editCity = (EditText)findViewById(R.id.buscity);
 			EditText editSearchKey = (EditText)findViewById(R.id.bussearchkey);
 			mCityName = editCity.getText().toString(); 
 			mSearch.poiSearchInCity(mCityName, editSearchKey.getText().toString());
 	}
     void routeSearchButtonProcess(View v) {
 		// 处理搜索按钮响应
    	 EditText routecity = (EditText)findViewById(R.id.routecity);
 		EditText editSt = (EditText)findViewById(R.id.start);
 		EditText editEn = (EditText)findViewById(R.id.end);
 		
 		// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
 		MKPlanNode stNode = new MKPlanNode();
 		stNode.name = editSt.getText().toString();
 		MKPlanNode enNode = new MKPlanNode();
 		enNode.name = editEn.getText().toString();
 		// 实际使用中请对起点终点城市进行正确的设定
 		if (mBtnDrive.equals(v)) {
 			mSearch.drivingSearch(routecity.getText().toString(), stNode, routecity.getText().toString(), enNode);
 		} else if (mBtnTransit.equals(v)) {
 			mSearch.transitSearch(routecity.getText().toString(), stNode, enNode);
 		} else if (mBtnWalk.equals(v)) {
 			mSearch.walkingSearch(routecity.getText().toString(), stNode, routecity.getText().toString(), enNode);
 		}
 	}

	void SearchButtonProcess(View v) {
		if (mBtnSearch.equals(v)) {
			EditText editCity = (EditText)findViewById(R.id.city);
			EditText editSearchKey = (EditText)findViewById(R.id.searchkey);
			mSearch.poiSearchInCity(editCity.getText().toString(), 
			editSearchKey.getText().toString());
		}
	}

	void SuggestionSearchButtonProcess(View v) {
		EditText editSearchKey = (EditText)findViewById(R.id.suggestionkey);
		mSearch.suggestionSearch(editSearchKey.getText().toString());
	}
	
	class OverItemT extends ItemizedOverlay<OverlayItem>{
		private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
		public OverItemT(Drawable marker, Context context, GeoPoint pt, String title) {
			super(boundCenterBottom(marker));
			mGeoList.add(new OverlayItem(pt, title, null));
			populate();
		}
		@Override
		protected OverlayItem createItem(int i) {
			return mGeoList.get(i);
		}
		@Override
		public int size() {
			return mGeoList.size();
		}
		@Override
		public boolean onSnapToItem(int i, int j, Point point, MapView mapview) {
			Log.e("ItemizedOverlayDemo","enter onSnapToItem()!");
			return false;
		}
	}
}

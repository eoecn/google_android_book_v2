package com.tudi.cn;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.SyncStateContract.Helpers;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.CacheManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class main extends Activity implements OnTouchListener,
		OnGestureListener {
    //浮动输入栏的总布局
	private RelativeLayout  skinLayout;
	
	//第一层输入布局
	private LinearLayout myenter01;
	private EditText enterurl01;
    //第二层输入布局
	private LinearLayout myenter02;
	private EditText enterurl02;
	private ImageView okImageView;
    //手势部分
	private WebView myWebView;
	private GestureDetector gestureDetector;
	private Boolean flag = true;
	private RelativeLayout myRelativeLayout;
    //网址部分
	private String strURL;
	private String strindex;
	private String strurlhttp = "";

	//标题、进度条、图标
	private TextView    mytitle;
	private ProgressBar myProgressBar;
	private ImageView   myicon;
	
	
	private WebSettings webSettings;
	
	//底部菜单
	private GridView    myGridView;
	private SimpleAdapter adapter;
	
	//历史的数据库
	private MySQLiteHelper mySQLiteHelper;
	private SQLiteDatabase db;
	private String         dbString;
	
	//设置对话框的网格视图
	private GridView      setGridView;
	
	//主页设置
	private EditText      zhuyenameEditText;
	private EditText      zhuyeurlEditText;
	private SharedPreferences zhuyesp;
	private SharedPreferences.Editor zhuyeEditor;
	private String        queryurlString;
	//JavaScript设置
	private SharedPreferences jssp;
	private SharedPreferences.Editor jsEditor;
	//缓存设置
	private SharedPreferences cachesp;
	private SharedPreferences.Editor  cacheEditor;
	//皮肤设置
	private SharedPreferences skinsp;
	private SharedPreferences.Editor  skinEditor;
	//壁纸设置
	private SharedPreferences bizhisp;
	private SharedPreferences.Editor  bizhiEditor;
	//删除缓存的标志板
	private boolean           cleancachebln;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 无状态栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);
         
		//引入历史数据库的实例
		mySQLiteHelper=new MySQLiteHelper(main.this, "mydb.db", null, 1);
	    
		//浮动输入栏的总的布局(皮肤)
		skinLayout=(RelativeLayout) findViewById(R.id.skin01);
		
		// 引入输入框的实例
		myenter01 = (LinearLayout) findViewById(R.id.enter01);
		enterurl01 = (EditText) findViewById(R.id.enterurl01);

		myenter02 = (LinearLayout) findViewById(R.id.enter02);
		enterurl02 = (EditText) findViewById(R.id.enterurl02);
		okImageView = (ImageView) findViewById(R.id.ImageView01);

		//标题、进度条、图标的实例
		mytitle=(TextView) findViewById(R.id.title01);
		myProgressBar=(ProgressBar) findViewById(R.id.progress_horizontal01);
		myicon=(ImageView) findViewById(R.id.icon);
		//欲取得网站的图标，必须设置网站数据库的实例
		final WebIconDatabase mydb=WebIconDatabase.getInstance();
		mydb.open(getDir("icons", MODE_PRIVATE).getPath());
		
		//-----------------------------------
		//SharedPreferences的实例部分：开始
		//-----------------------------------
		
		//主页设置		
		zhuyesp=main.this.getSharedPreferences("zhuyeset", MODE_PRIVATE);
		zhuyeEditor=zhuyesp.edit();
		//JavaScript设置
		jssp=main.this.getSharedPreferences("jsset", MODE_PRIVATE);
		jsEditor=jssp.edit();
		//缓存设置
		cachesp=main.this.getSharedPreferences("cacheset", MODE_PRIVATE);
		cacheEditor=cachesp.edit();
		//皮肤设置
		skinsp=main.this.getSharedPreferences("skinset", MODE_PRIVATE);
		skinEditor=skinsp.edit();
		//壁纸设置
		bizhisp=main.this.getSharedPreferences("bizhiset", MODE_PRIVATE);
		bizhiEditor=bizhisp.edit();
		//------------------------------------
		//SharedPreferences的实例部分：结束
		//------------------------------------
		//---------begin:查询数据，设置皮肤-------------------------------
		int skinvalue=skinsp.getInt("skin", 0);
		switch(skinvalue){
		case 0:
			skinLayout.setBackgroundColor(Color.parseColor("#ffd7d7d7"));
			break;
		case 1:
			skinLayout.setBackgroundResource(R.drawable.skin_title_jingdian);
			break;
		case 2:
			skinLayout.setBackgroundResource(R.drawable.skin_title_chitang);
			break;
		case 3:
			skinLayout.setBackgroundResource(R.drawable.skin_title_jijingshouwang);
			break;
		case 4:
			skinLayout.setBackgroundResource(R.drawable.skin_title_kewangfeixiang);
			break;
		case 5:
			skinLayout.setBackgroundResource(R.drawable.skin_title_mengdong);
			break;
		case 6:
			skinLayout.setBackgroundResource(R.drawable.skin_title_xuanlan);
			break;
		case 7:
			skinLayout.setBackgroundResource(R.drawable.skin_title_hetang);
			break;
		case 8:
			skinLayout.setBackgroundResource(R.drawable.skin_title_zhanlantiankong);
			break;
		case 9:
			skinLayout.setBackgroundResource(R.drawable.skin_title_qingchasiyu);
			break;
		}
		
		
		//--------end:查询数据，设置皮肤-----------------------------------------------------------
		// -----begin网页部分---------------------------------------------------------
		// 网页视图的实例
		myWebView = (WebView) findViewById(R.id.WebView01);
		//网页视图的壁纸设置
		int bizhivalue=bizhisp.getInt("bizhi", 0);
		switch(bizhivalue){
		case 0:
			myWebView.setBackgroundColor(0);
			myWebView.setBackgroundResource(R.drawable.white);
			break;
		case 1:
			myWebView.setBackgroundColor(0);
			myWebView.setBackgroundResource(R.drawable.chuntian);
			break;
		case 2:
			myWebView.setBackgroundColor(0);
			myWebView.setBackgroundResource(R.drawable.xiatian);
			break;
		case 3:
			myWebView.setBackgroundColor(0);
			myWebView.setBackgroundResource(R.drawable.xianhuaduoduo);
			break;
		case 4:
			myWebView.setBackgroundColor(0);
			myWebView.setBackgroundResource(R.drawable.huayu);
			break;
		}
		
		myWebView.setOnTouchListener(main.this);
		gestureDetector = new GestureDetector(main.this);
		// 网页的覆盖视图的实例
		myRelativeLayout = (RelativeLayout) findViewById(R.id.skin01);

		// 设置网页客户端
		myWebView.setWebViewClient(new WebViewClient() {

		});
        //设置网页chrome客户端
		myWebView.setWebChromeClient(new WebChromeClient(){
		    //设置标题
			public void onReceivedTitle(WebView view,String title){
				super.onReceivedTitle(view, title);
				title=myWebView.getTitle();
				dbString=title;
				mytitle.setText(title);
			}
			//设置进度条
			public void onProgressChanged(WebView view,int progress){
				myProgressBar.setProgress(progress);
				if(progress==100){
					myProgressBar.setProgress(0);
				}
			}
			
			//设置图标
			public void onReceivedIcon(WebView view,Bitmap icon){
				super.onReceivedIcon(view, icon);
				icon=myWebView.getFavicon();
				myicon.setImageBitmap(icon);
			}
			
		
			
			
		});
		
		
		
		
		// ------end网页部分---------------------------------------------------------------------

		// 输入框的点击事件
		enterurl01.setOnClickListener(new LinearLayout.OnClickListener() {

			public void onClick(View v) {
				myenter01.setVisibility(View.GONE);
				myenter02.setVisibility(View.VISIBLE);
			}

		});
		// 图片按钮的点击事件
		okImageView.setOnClickListener(new ImageView.OnClickListener() {

			public void onClick(View v) {
				myenter02.setVisibility(View.GONE);
				myenter01.setVisibility(View.VISIBLE);
				// 取得网址
				strURL = enterurl02.getText().toString();
				int length = strURL.length();
				if (length == 0) {
					Toast.makeText(main.this, "请输入网址！", Toast.LENGTH_SHORT)
							.show();
				} else {
					strindex = strURL.substring(0, 7);
					boolean bln = strindex.equalsIgnoreCase("http://");
					if (bln == true) {

						if (URLUtil.isNetworkUrl(strURL)) {// 进行网址的合法性判断

							myWebView.loadUrl(strURL);
                            
							db=mySQLiteHelper.getReadableDatabase();
							//将数据保存到历史数据库中
							ContentValues cv=new ContentValues();							
							cv.put("title", dbString);
							cv.put("url", strURL);
							db.insert("history", null, cv);
						
							
						} else {
							Toast.makeText(main.this, "请输入合法的网址，谢谢！",
									Toast.LENGTH_SHORT).show();
						}
					} else if (bln == false) {
						strURL = ("http://" + strURL);
						if (URLUtil.isNetworkUrl(strURL)) {
							myWebView.loadUrl(strURL);
							
							db=mySQLiteHelper.getReadableDatabase();
							//将数据保存到历史数据库中
							ContentValues cv=new ContentValues();						
							cv.put("title", dbString);
							cv.put("url", strURL);
							db.insert("history", null, cv);
						   //mydb.execSQL("insert into historytable(_id,title,url) values(1,dbString,strURL);"); 
						} else {
							Toast.makeText(main.this, "请输入合法的网址，谢谢！",
									Toast.LENGTH_SHORT).show();
						}
					}

				}

				enterurl02.setText("");

			}

		});

		//------begin网页的各项设置-------------------------------------
		webSettings=myWebView.getSettings();
		//JavaScript的设置
		boolean jsbln=jssp.getBoolean("jsbln", false);
		if(jsbln==true){
		webSettings.setJavaScriptEnabled(true);	
		}else if(jsbln==false){
		webSettings.setJavaScriptEnabled(false);
		//缓存
		int cachevalue=cachesp.getInt("cache", 1);
		if(cachevalue==0){
		webSettings.setAppCacheEnabled(true);	
		}else if(cachevalue==1){
		webSettings.setAppCacheEnabled(false);	
		}
		
		}
		
		
		
		//------end网页的各项设置-------------------------------------------------------
		
	    
	    
	}

	// ---------begin触摸屏监听和手势监听的方法-------------------------------------------------------

	public boolean onTouch(View v, MotionEvent event) {
		gestureDetector.onTouchEvent(event);// 将触摸屏事件传入手势事件
        
		//请求焦点
		myWebView.requestFocus();

		
		
		return false;
	}

	public boolean onDown(MotionEvent e) {
		if (flag == true) {
			int height = myRelativeLayout.getHeight();
			Animation myTranslateAnimation = new TranslateAnimation(0, 0, 0,
					-height);
			myTranslateAnimation.setDuration(500);
			myRelativeLayout.setAnimation(myTranslateAnimation);
			myRelativeLayout.setVisibility(View.GONE);
			flag = false;
		}

		return true;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float vx = Math.abs(velocityX);// 取其绝对值
		float vy = Math.abs(velocityY);
		//----begin设置浮动框的弹出和收回--------------------
		if (vy > vx) {
			if (velocityY > 0) {//设置弹出
				if (flag == false) {

					int height = myRelativeLayout.getHeight();
					Animation myTranslateAnimation = new TranslateAnimation(0,
							0, -height, 0);
					myTranslateAnimation.setDuration(500);
					myRelativeLayout.setAnimation(myTranslateAnimation);
					myRelativeLayout.setVisibility(View.VISIBLE);

					flag = true;
				}

			} else if (velocityY < 0) {//设置收回

				if (flag == true) {
					int height = myRelativeLayout.getHeight();
					Animation myTranslateAnimation = new TranslateAnimation(0,
							0, 0, -height);
					myTranslateAnimation.setDuration(500);
					myRelativeLayout.setAnimation(myTranslateAnimation);
					myRelativeLayout.setVisibility(View.GONE);
					flag = false;
				}

			}

		}else
		//-----end设置浮动框的弹出和收回---------------------------------	
		//-----begin设置左右滑动翻页-----------------------	
		if(vx>vy){
		if(velocityX>0){//前一页
		   if(myWebView.canGoBack()){
			  
			   myWebView.goBack(); 
			   
			   int width=myWebView.getWidth();
			   Animation myTranslateAnimation=new TranslateAnimation(0,width,0,0);
			   myTranslateAnimation.setDuration(400);
			   myWebView.setAnimation(myTranslateAnimation);
			   
			  
		   }
		}else if(velocityX<0){//后一页
			if(myWebView.canGoForward()){
			  
				myWebView.goForward();
				
				int width=myWebView.getWidth();
				Animation myTranslateAnimation=new TranslateAnimation(0,-width,0,0);
				myTranslateAnimation.setDuration(400);
				myWebView.setAnimation(myTranslateAnimation);
				
				
			}
		}
		}
		//-----end设置左右滑动翻页------------------------
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	// --------end触摸屏监听和手势监听的方法----------------------------------------------

	//__________________________________________________________________________________
	//-------------(begin)按键的点击事件------------------------------------------------------
	//___________________________________________________________________________________
	
	public boolean onKeyDown(int keyCode,KeyEvent event){
		
		switch(keyCode){
		case KeyEvent.KEYCODE_MENU:
			//引入方法
			if(myGridView==null){
			
			loadBottomMenu();
			
			}
			if(myGridView.getVisibility()==View.GONE){
				myGridView.setVisibility(View.VISIBLE);
			}else{
				myGridView.setVisibility(View.GONE);
			}
			break;
		
		//返回键的事件
		case KeyEvent.KEYCODE_BACK:
			
			Builder exitBuilder=new Builder(main.this);
			exitBuilder.setIcon(R.drawable.siyecao30);
			exitBuilder.setTitle("退出提示");
			exitBuilder.setMultiChoiceItems(new String[]{"删除缓存"}, new boolean[]{false}, new OnMultiChoiceClickListener(){

				public void onClick(DialogInterface dialog, int which,
						boolean isChecked) {
				switch(which){
				case 0:
					if(isChecked==true){
					cleancachebln=true;	
					}
					break;
				}
					
				}

				});
			exitBuilder.setPositiveButton("确定", new OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					if(cleancachebln==true){
					File file=CacheManager.getCacheFileBaseDir();
					if(file!=null&&file.exists()&&file.isDirectory()){
					
						for(File item:file.listFiles()){
							item.delete();
						}
						file.delete();
					}
					main.this.deleteDatabase("webview.db");
					main.this.deleteDatabase("webviewCache.db");
					Toast.makeText(main.this,"缓存删除成功！", Toast.LENGTH_LONG).show();
					}
					
					main.this.finish();
				}
				
			});
			exitBuilder.setNegativeButton("取消", new OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					
					
				}
				
			});
			exitBuilder.show();
			break;
		
		
		
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	//__________________________________________________________________________________
	//-------------(end)按键的点击事件------------------------------------------------------
	//___________________________________________________________________________________
	
	//-------------begin载入底部菜单的方法---------------------------------------------------------------------
	public void loadBottomMenu(){
		myGridView=(GridView) findViewById(R.id.GridView01);
		//设置菜单项
		myGridView.setNumColumns(5);
		myGridView.setGravity(Gravity.CENTER);
		myGridView.setVerticalSpacing(10);
		myGridView.setHorizontalSpacing(10);
		
		ArrayList arrayList=new ArrayList();
		HashMap hashMap=new HashMap();
		hashMap.put("itemimage", R.drawable.fangda);
		arrayList.add(hashMap);
		
		hashMap=new HashMap();
		hashMap.put("itemimage", R.drawable.suoxiao);
		arrayList.add(hashMap);
		
		hashMap=new HashMap();
		hashMap.put("itemimage", R.drawable.home);
		arrayList.add(hashMap);
		
		hashMap=new HashMap();
		hashMap.put("itemimage", R.drawable.history);
		arrayList.add(hashMap);
		
		hashMap=new HashMap();
		hashMap.put("itemimage", R.drawable.set);
		arrayList.add(hashMap);
		
		adapter=new SimpleAdapter(this, arrayList, R.layout.bottommenu, new String[]{"itemimage"}, new int[]{R.id.item});
		
		myGridView.setAdapter(adapter);
		
		myGridView.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int which,
					long arg3) {
			switch(which){
			    case 0:
				webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
				    break;
			    case 1:
			    webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);	
			    	break;
			    case 2:
			    //查询主页设置中的值
			    	 queryurlString=zhuyesp.getString("url", "http://www.baidu.cn");
			    	//判断网址的前缀是否包含http://
			    	String strindex=queryurlString.substring(0,7);
			    	boolean bln=strindex.equalsIgnoreCase("http://");
			    	if(bln==true){
			    	
			    		if(URLUtil.isNetworkUrl(queryurlString)){
			    			myWebView.loadUrl(queryurlString);		
			    		}else{
			    			Toast.makeText(main.this, "网址不合法！", Toast.LENGTH_LONG).show();
			    		}
			    	}else if(bln==false){
			    		queryurlString="http://"+queryurlString;
			    		if(URLUtil.isNetworkUrl(queryurlString)){
			    			myWebView.loadUrl(queryurlString);		
			    		}else{
			    			Toast.makeText(main.this, "网址不合法！", Toast.LENGTH_LONG).show();
			    		}
			    	}
			    	
			    	
			    	break;
			    case 3:
			    	Intent intent=new Intent();
			    	intent.setClass(main.this, bookmarkandhistory.class);
			    	startActivity(intent);
			    	main.this.finish();			    	
			    	break;
			    case 4:
			    	//打开设置对话框
			    	setdialoge();
			    				    	
			    	break;
				
			}
				
			}
			
		});
	}
	
	
	//-------------end载入底部菜单的方法----------------------------------------------------------------------
	//-------------begin:设置对话框-----------------------------------------------------------------------
	public void setdialoge(){
	
		//建立设置对话框
    	final AlertDialog setBuilder=new Builder(main.this).create();

    	//载入设置对话框的视图
    	LayoutInflater inflater=LayoutInflater.from(main.this);
    	View view=inflater.inflate(R.layout.setview, null);
    	setGridView=(GridView) view.findViewById(R.id.GridView01);
    	
    	ArrayList  arrayList=new ArrayList();
    	
    	HashMap map=new HashMap();
    	map.put("imageitem", R.drawable.skin);
    	map.put("textitem", "皮肤");
    	arrayList.add(map);
    	
    	map=new HashMap();
    	map.put("imageitem", R.drawable.bizhi);
    	map.put("textitem", "壁纸");
    	arrayList.add(map);
    	
    	map=new HashMap();
    	map.put("imageitem", R.drawable.zhuye);
    	map.put("textitem", "主页");
    	arrayList.add(map);
    	
    	map=new HashMap();
    	map.put("imageitem", R.drawable.js);
    	map.put("textitem", "JavaScript");
    	arrayList.add(map);
    	
    	map=new HashMap();
    	map.put("imageitem", R.drawable.huancun);
    	map.put("textitem", "缓存");
    	arrayList.add(map);
    	
    	map=new HashMap();
    	map.put("imageitem", R.drawable.help);
    	map.put("textitem", "帮助");
    	arrayList.add(map);
    	
    	map=new HashMap();
    	map.put("imageitem", R.drawable.about);
    	map.put("textitem", "关于");
    	arrayList.add(map);
    	
    	map=new HashMap();
    	map.put("imageitem", R.drawable.menu_return);
    	map.put("textitem", "返回");
    	arrayList.add(map);
    	
    	SimpleAdapter setsa=new SimpleAdapter(main.this, arrayList, R.layout.setviewitem, new String[]{"imageitem","textitem"}, new int[]{R.id.ImageView01,R.id.TextView01});
    	setGridView.setAdapter(setsa);
    	
    	
    	//添加视图
    	setBuilder.setView(view);
    	
    	setBuilder.show();
    	//--------begin:网格视图的点击事件------------------------------------------------------
    	setGridView.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int which,
					long arg3) {
			switch(which){
			
			case 0:
				//皮肤设置
				Builder builder=new Builder(main.this);
				builder.setIcon(R.drawable.siyecao30);
				builder.setTitle("皮肤设置");
				builder.setSingleChoiceItems(new String[]{"默认皮肤","极致幻动","水墨池塘","寂静守望","渴望飞翔","萌动春色","绚烂飘零","荷塘月色","湛蓝天空","清茶丝雨"}, -1, new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
					switch(which){
					
					case 0:
						skinLayout.setBackgroundColor(Color.parseColor("#ffd7d7d7"));
						Toast.makeText(main.this, "默认设置", Toast.LENGTH_LONG).show();
						//保存设置的数据
						skinEditor.putInt("skin", 0);
						skinEditor.commit();
						
						break;
					case 1:
						skinLayout.setBackgroundResource(R.drawable.skin_title_jingdian);
						Toast.makeText(main.this, "极致幻动", Toast.LENGTH_LONG).show();
						//保存设置的数据
						skinEditor.putInt("skin", 1);
						skinEditor.commit();
						break;
						
					case 2:
						skinLayout.setBackgroundResource(R.drawable.skin_title_chitang);
						Toast.makeText(main.this, "水墨池塘", Toast.LENGTH_LONG).show();
						//保存设置的数据
						skinEditor.putInt("skin", 2);
						skinEditor.commit();
						
						break;
						
					case 3:
						skinLayout.setBackgroundResource(R.drawable.skin_title_jijingshouwang);
						Toast.makeText(main.this, "寂静守望", Toast.LENGTH_LONG).show();
						//保存设置的数据
						skinEditor.putInt("skin", 3);
						skinEditor.commit();
						break;
						
					case 4:
						skinLayout.setBackgroundResource(R.drawable.skin_title_kewangfeixiang);
						Toast.makeText(main.this, "渴望飞翔", Toast.LENGTH_LONG).show();
						//保存设置的数据
						skinEditor.putInt("skin", 4);
						skinEditor.commit();
						break;
						
					case 5:
						skinLayout.setBackgroundResource(R.drawable.skin_title_mengdong);
						Toast.makeText(main.this, "萌动春色", Toast.LENGTH_LONG).show();
						//保存设置的数据
						skinEditor.putInt("skin", 5);
						skinEditor.commit();
						break;
						
					case 6:
						skinLayout.setBackgroundResource(R.drawable.skin_title_xuanlan);
						Toast.makeText(main.this, "绚烂飘零", Toast.LENGTH_LONG).show();
						//保存设置的数据
						skinEditor.putInt("skin", 6);
						skinEditor.commit();
						break;
						
					case 7:
						skinLayout.setBackgroundResource(R.drawable.skin_title_hetang);
						Toast.makeText(main.this, "荷塘月色", Toast.LENGTH_LONG).show();
						//保存设置的数据
						skinEditor.putInt("skin", 7);
						skinEditor.commit();
						break;
						
					case 8:
						skinLayout.setBackgroundResource(R.drawable.skin_title_zhanlantiankong);
						Toast.makeText(main.this, "湛蓝天空", Toast.LENGTH_LONG).show();
						//保存设置的数据
						skinEditor.putInt("skin", 8);
						skinEditor.commit();
						break;
						
					case 9:
						skinLayout.setBackgroundResource(R.drawable.skin_title_qingchasiyu);
						Toast.makeText(main.this, "清茶丝雨", Toast.LENGTH_LONG).show();
						//保存设置的数据
						skinEditor.putInt("skin", 9);
						skinEditor.commit();
						break;
					
					}
						
					}
					
				});
				builder.setPositiveButton("返回", new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						
						
					}
					
				});
				builder.show();
				break;
			case 1:
				//壁纸设置
				Builder bizhiBuilder=new Builder(main.this);
				bizhiBuilder.setIcon(R.drawable.siyecao30);
				bizhiBuilder.setTitle("壁纸设置");
				bizhiBuilder.setSingleChoiceItems(new String[]{"默认壁纸","绿色春光","荷塘倒影","鲜花朵朵","粉花雨露"}, -1, new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
					switch(which){
					case 0:
						//默认壁纸
						myWebView.setBackgroundColor(0);
						myWebView.setBackgroundResource(R.drawable.white);
						//保存设置数据
						bizhiEditor.putInt("bizhi", 0);
						bizhiEditor.commit();
						break;
					case 1:
						//绿色春光
						myWebView.setBackgroundColor(0);
						myWebView.setBackgroundResource(R.drawable.chuntian);
						//保存设置数据
						bizhiEditor.putInt("bizhi", 1);
						bizhiEditor.commit();
						break;
						
					case 2:
						//荷塘倒影
						myWebView.setBackgroundColor(0);
						myWebView.setBackgroundResource(R.drawable.xiatian);
						//保存设置数据
						bizhiEditor.putInt("bizhi", 2);
						bizhiEditor.commit();
						break;
					case 3:
						//鲜花朵朵
						myWebView.setBackgroundColor(0);
						myWebView.setBackgroundResource(R.drawable.xianhuaduoduo);
						//保存设置数据
						bizhiEditor.putInt("bizhi", 3);
						bizhiEditor.commit();
						break;
					case 4:
						//粉花雨露
						myWebView.setBackgroundColor(0);
						myWebView.setBackgroundResource(R.drawable.huayu);
						//保存设置数据
						bizhiEditor.putInt("bizhi", 4);
						bizhiEditor.commit();
						break;
					}
						
					}
					
				});
				bizhiBuilder.setPositiveButton("返回", new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						
						
					}
					
				});
				bizhiBuilder.show();
				break;
				
			case 2:				
				//主页设置
				Builder zhuyeBuilder=new Builder(main.this);
				zhuyeBuilder.setIcon(R.drawable.siyecao30);
				zhuyeBuilder.setTitle("主页设置");
				LayoutInflater inflater=LayoutInflater.from(main.this);
				View view=inflater.inflate(R.layout.zhuyeset, null);
				
				zhuyenameEditText=(EditText) view.findViewById(R.id.EditText01);
				zhuyeurlEditText=(EditText) view.findViewById(R.id.EditText02);
				//查询保存的值
				String queryzhuyename=zhuyesp.getString("name", "网站名称");
				String queryzhuyeurl=zhuyesp.getString("url","网址");
				//将查询的值添加到编辑框
				zhuyenameEditText.setText(queryzhuyename);
				zhuyeurlEditText.setText(queryzhuyeurl);
				
				zhuyeBuilder.setView(view);
				zhuyeBuilder.setPositiveButton("确定", new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
					//取得编辑框的值
					String zhuyename=zhuyenameEditText.getText().toString();
					String zhuyeurl=zhuyeurlEditText.getText().toString();
					//将编辑框中的值进行保存
					zhuyeEditor.putString("name", zhuyename);
					zhuyeEditor.putString("url", zhuyeurl);
					zhuyeEditor.commit();
					}
					
				});
				zhuyeBuilder.setNegativeButton("取消", new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						
						
					}
					
				});
				zhuyeBuilder.show();
				break;
				
			case 3:
				//JavaScript设置
				Builder jsBuilder=new Builder(main.this);
				jsBuilder.setIcon(R.drawable.siyecao30);
				jsBuilder.setTitle("JavaScript设置");
				jsBuilder.setSingleChoiceItems(new String[]{"打开","关闭"}, -1, new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
					switch(which){
					
					case 0:
						WebSettings myWebSettings=myWebView.getSettings();
						myWebSettings.setJavaScriptEnabled(true);
						//保存数据
						jsEditor.putBoolean("jsbln", true);
						jsEditor.commit();
						break;
					case 1:
						WebSettings myWebSettings2=myWebView.getSettings();
						myWebSettings2.setJavaScriptEnabled(false);
						//保存数据
						jsEditor.putBoolean("jsbln", false);
						jsEditor.commit();
						
						break;
					
					}
						
					}
					
				});
				jsBuilder.setPositiveButton("返回", new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
					
				});
				jsBuilder.show();
				break;
				
			case 4:
				//缓存设置
				Builder cacheBuilder=new Builder(main.this);
				cacheBuilder.setIcon(R.drawable.siyecao30);
				cacheBuilder.setTitle("缓存设置");
				cacheBuilder.setSingleChoiceItems(new String[]{"缓存打开","缓存关闭"}, -1, new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
					switch(which){
					case 0:
					WebSettings myWebSettings=myWebView.getSettings();
					myWebSettings.setAppCacheEnabled(true);
					//保存设置的数据
					cacheEditor.putInt("cache", 0);
					cacheEditor.commit();
					break;
					case 1:
					WebSettings myWebSettings2=myWebView.getSettings();
					myWebSettings2.setAppCacheEnabled(false);
					//保存设置的数据
					cacheEditor.putInt("cache", 1);
					cacheEditor.commit();
					break;
					
					}	
					}});
				cacheBuilder.setPositiveButton("返回", new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						
						
					}});
				cacheBuilder.show();
				break;
			
			case 5:
				Builder helpBuilder=new Builder(main.this);
				helpBuilder.setIcon(R.drawable.siyecao30);
				helpBuilder.setTitle("帮助");
				LayoutInflater inflater2=LayoutInflater.from(main.this);
				View view2=inflater2.inflate(R.layout.help, null);
				helpBuilder.setView(view2);
				helpBuilder.setPositiveButton("返回", new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						
						
					}
					
				});
				helpBuilder.show();
				break;
				
			case 6:
				Builder aboutBuilder=new Builder(main.this);
				aboutBuilder.setIcon(R.drawable.siyecao30);
				aboutBuilder.setTitle("关于");
				LayoutInflater inflater3=LayoutInflater.from(main.this);
				View view3=inflater3.inflate(R.layout.about, null);
				aboutBuilder.setView(view3);
				aboutBuilder.setPositiveButton("返回", new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						
						
					}});
				aboutBuilder.show();
				break;
				
			case 7:
				 setBuilder.cancel();
				break;
			
			
			}
				
			}
    		
    	});
    	//-------end:网格视图的点击事件---------------------------------------------------------
    	
	}
	
	
	//-------------end:设置对话框-----------------------------------------------------------------------
//添加完整的生命周期
	public void onStart(){
		super.onStart();
	}

    public void onResume(){
    	super.onResume();
    }
   public void  onPause(){
	   super.onPause();
   }
   public void  onRestart(){
	   super.onRestart();
   }
   public void onStop(){
	   super.onStop();
   }
   public void onDestroy(){
	   super.onDestroy();
   }
   
   
}

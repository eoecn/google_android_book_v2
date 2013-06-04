package com.tudi.cn;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class bookmarkandhistory extends Activity{

	private GridView  myGridView;
	
	//上下2层视图的布局
	private  RelativeLayout myRelativeLayout01;
	private  RelativeLayout myRelativeLayout02;
	
	//数据库
	private  MySQLiteHelper  mySQLiteHelper;
	private  SQLiteDatabase  db;
	
	//列表1
	private  ListView        myListView01;
	private  Cursor          bookmarkcursor;
	
	
	//列表2
	private  ListView        myListView02;
	private  Cursor          cursor;
	
	
	//按钮
	private  Button          btn01;
	private  Button          btn02;
	private  Button          btn03;
	private  Button          btn04;
	
	//添加书签对话框的2个编辑框
	private  EditText       et01;
	private  EditText       et02;
	//添加书签编辑框的2个编辑框
	private  EditText       et03;
	private  EditText       et04;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//无标题和无状态栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	
		
		setContentView(R.layout.bookmarkandhistory);
	
		//创建数据库的实例
		mySQLiteHelper=new MySQLiteHelper(bookmarkandhistory.this, "mydb.db", null, 1);
		db=mySQLiteHelper.getReadableDatabase();
		
		
		
	//创建上下2层视图的布局的实例
		myRelativeLayout01=(RelativeLayout) findViewById(R.id.Cent01);
		myRelativeLayout02=(RelativeLayout) findViewById(R.id.Cent02);
		
	//引入列表的实例
		myListView01=(ListView) findViewById(R.id.ListView01);
		myListView02=(ListView) findViewById(R.id.ListView02);
		
	
	//创建按钮的实例
		//添加按钮
		btn01=(Button) findViewById(R.id.Button01);
		//返回按钮
		btn02=(Button) findViewById(R.id.Button02);
		//管理按钮
		btn03=(Button) findViewById(R.id.Button03);
		//返回按钮
		btn04=(Button) findViewById(R.id.Button04);
	
	
		
		
	//书签中返回按钮的点击事件	
		btn02.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View arg0) {
				Intent intent=new Intent();
				intent.setClass(bookmarkandhistory.this, main.class);
				startActivity(intent);
				bookmarkandhistory.this.finish();//释放内存
				db.close();//关闭数据库
			}
			
		});
		
	//历史记录中返回按钮的点击事件
		btn04.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(bookmarkandhistory.this, main.class);
				startActivity(intent);
				bookmarkandhistory.this.finish();//释放内存
			//关闭数据库
				db.close();
			
			}});
		//载入GridView
	loadGridView();
	}
	
	public void loadGridView(){
	myGridView=(GridView) findViewById(R.id.GridView01);
	myGridView.setNumColumns(2);
	myGridView.setGravity(Gravity.CENTER);
	myGridView.setVerticalSpacing(10);
	myGridView.setHorizontalSpacing(10);
	
	ArrayList arrayList=new ArrayList();
	
	HashMap hashMap=new HashMap();
	hashMap.put("itemword", "书签");
	arrayList.add(hashMap);
	
	hashMap=new HashMap();
	hashMap.put("itemword", "历史");
	arrayList.add(hashMap);
	
	SimpleAdapter simpleAdapter=new SimpleAdapter(bookmarkandhistory.this, arrayList, R.layout.gridviewitem, new String[]{"itemword"}, new int[]{R.id.TextView01});
	myGridView.setAdapter(simpleAdapter);
	//----------begin:myGridView的点击事件----------------------------------------------------------
	myGridView.setOnItemClickListener(new OnItemClickListener(){

		public void onItemClick(AdapterView<?> arg0, View arg1, int which,
				long arg3) {
		switch(which){
		case 0:
			if(myRelativeLayout01.getVisibility()==View.GONE){
			//先进行一遍数据的查询
			bookmarkcursor=db.query("bookmarktable", new String[]{"_id","title","url"}, null, null, null, null, null);	
			SimpleCursorAdapter sca=new SimpleCursorAdapter(bookmarkandhistory.this, R.layout.listview01item, bookmarkcursor, new String[]{"title","url"}, new int[]{R.id.TextView01,R.id.TextView02});	
			myListView01.setAdapter(sca);	
				//设置一个动画
			int width=myRelativeLayout02.getWidth();
				
			Animation myTranslateAnimation=new TranslateAnimation(0,-width,0,0);
			myTranslateAnimation.setDuration(1000);
			myRelativeLayout02.setAnimation(myTranslateAnimation);
			
			myRelativeLayout01.setVisibility(View.VISIBLE);
			myRelativeLayout02.setVisibility(View.GONE);
			//----begin:myListView01列表视图的点击事件-------------------------------------------------
			myListView01.setOnItemClickListener(new OnItemClickListener(){

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					//弹出对话框
					Builder builder=new Builder(bookmarkandhistory.this);
					builder.setIcon(R.drawable.siyecao30);
					builder.setTitle("书签操作");
					builder.setSingleChoiceItems(new String[]{"打开","编辑","删除"}, -1, new OnClickListener(){

						public void onClick(DialogInterface dialog, int which) {
							if(which==0){
							//打开网站
							int urlindex=bookmarkcursor.getColumnIndex("url");
							String markurl=bookmarkcursor.getString(urlindex);
							//进行一下网址前是否包含：http：//
							String strindex=markurl.substring(0,7);
							 boolean bln=strindex.equalsIgnoreCase("http://");
							if(bln==false){
							markurl="http://"+markurl;	
							}
							 if(URLUtil.isNetworkUrl(markurl)){
								Uri url=Uri.parse(markurl);
								Intent intent=new Intent(Intent.ACTION_VIEW,url);
								startActivity(intent);
								
							    }else{Toast.makeText(bookmarkandhistory.this, "网址错误！", Toast.LENGTH_LONG).show();}
							
							}else if(which==1){
							//编辑
								Builder editBuilder=new Builder(bookmarkandhistory.this);
								editBuilder.setIcon(R.drawable.siyecao30);
								editBuilder.setTitle("书签编辑");
								LayoutInflater inflater=LayoutInflater.from(bookmarkandhistory.this);
								View view=inflater.inflate(R.layout.bookmarkedit,null);
								et03=(EditText) view.findViewById(R.id.EditText03);
								et04=(EditText) view.findViewById(R.id.EditText04);
								editBuilder.setView(view);
								editBuilder.setPositiveButton("确定", new OnClickListener(){

									public void onClick(DialogInterface dialog,
											int which) {
									int idindex=bookmarkcursor.getColumnIndex("_id");
									int myid=bookmarkcursor.getInt(idindex);	
									String mytitle=et03.getText().toString();
									String myurl=et04.getText().toString();
									
									ContentValues cv=new ContentValues();
									cv.put("title", mytitle);
									cv.put("url", myurl);
									db.update("bookmarktable", cv, "_id"+"="+myid, null);
									//再进行一遍数据库的查询
									
									bookmarkcursor=db.query("bookmarktable", new String[]{"_id","title","url"}, null, null, null, null, null);	
									SimpleCursorAdapter sca=new SimpleCursorAdapter(bookmarkandhistory.this, R.layout.listview01item, bookmarkcursor, new String[]{"title","url"}, new int[]{R.id.TextView01,R.id.TextView02});	
									myListView01.setAdapter(sca);	
									}});
								editBuilder.setNegativeButton("取消", new OnClickListener(){

									public void onClick(DialogInterface dialog,
											int which) {
										
										
										
									}
									
								});
								
								editBuilder.show();
								
								
								
								//
							}else if(which==2){
							//删除
								Builder deleteBuilder=new Builder(bookmarkandhistory.this);
								deleteBuilder.setIcon(R.drawable.siyecao30);
								deleteBuilder.setTitle("书签删除");	
								deleteBuilder.setMultiChoiceItems(new String[]{"删除书签"}, new boolean[]{false}, new OnMultiChoiceClickListener(){

									public void onClick(DialogInterface dialog,
											int which, boolean isChecked) {
									if(which==0){
									if(isChecked==true){
									
										int idindex=bookmarkcursor.getColumnIndex("_id");
										int myid=bookmarkcursor.getInt(idindex);
										db.delete("bookmarktable", "_id"+"="+myid, null);
										//再进行一遍数据库的查询
										bookmarkcursor=db.query("bookmarktable", new String[]{"_id","title","url"}, null, null, null, null, null);	
										SimpleCursorAdapter sca=new SimpleCursorAdapter(bookmarkandhistory.this, R.layout.listview01item, bookmarkcursor, new String[]{"title","url"}, new int[]{R.id.TextView01,R.id.TextView02});	
										myListView01.setAdapter(sca);
										
									}
									}
										
									}
									
								});
								deleteBuilder.setPositiveButton("返回", new OnClickListener(){

									public void onClick(DialogInterface dialog,
											int which) {
										
										
									}
									
								});
								
								deleteBuilder.show();
							}
							
						}});
					
					builder.show();
				}
				
			});
			
			//----end:myListView01列表视图的点击事件--------------------------------------------------
			}
			break;
		case 1:
			if(myRelativeLayout02.getVisibility()==View.GONE){
			
			//先进行数据的查询	
			cursor=db.query("history", new String[]{"_id","title","url"}, null, null, null, null, null);	
			SimpleCursorAdapter sca=new SimpleCursorAdapter(bookmarkandhistory.this, R.layout.listview02item, cursor, new String[]{"url"}, new int[]{R.id.TextView01});	
			myListView02.setAdapter(sca);	
				
			//设置一个动画
				int width=myRelativeLayout01.getWidth();
				
				Animation myTranslateAnimation=new TranslateAnimation(0,width,0,0);
				myTranslateAnimation.setDuration(1000);
				myRelativeLayout01.setAnimation(myTranslateAnimation);
				
			myRelativeLayout02.setVisibility(View.VISIBLE);	
			myRelativeLayout01.setVisibility(View.GONE);
			
			//myListView02列表视图的点击事件
			myListView02.setOnItemClickListener(new OnItemClickListener(){

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int which, long arg3) {
				
					
				Builder builder=new Builder(bookmarkandhistory.this);
				builder.setIcon(R.drawable.siyecao30);
				builder.setTitle("历史记录");
				builder.setSingleChoiceItems(new String[]{"打开","删除"}, -1, new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
					if(which==0){
					
					int   urlindex=cursor.getColumnIndex("url");
					String myurl=cursor.getString(urlindex);
					
					//进行打开网页的操作
					if(URLUtil.isNetworkUrl(myurl)){
					
						Uri url=Uri.parse(myurl);
						Intent intent=new Intent(Intent.ACTION_VIEW,url);
						startActivity(intent);
					}else{Toast.makeText(bookmarkandhistory.this, "网址错误！", Toast.LENGTH_LONG).show();}
					
					}else if(which==1){
					//删除网址的操作
					int idindex=cursor.getColumnIndex("_id");	
					int myid=cursor.getInt(idindex);	
					
					db.delete("history", "_id"+"="+myid, null);
					//再次进行数据的查询
					cursor=db.query("history", new String[]{"_id","title","url"}, null, null, null, null, null);	
					SimpleCursorAdapter sca=new SimpleCursorAdapter(bookmarkandhistory.this, R.layout.listview02item, cursor, new String[]{"url"}, new int[]{R.id.TextView01});	
					myListView02.setAdapter(sca);	
					}
						
					}});
				builder.show();
				
				}
				
			});
			
			}
			break;
		
		
		}
			
		}
		
	});
	//---------end:myGridView的点击事件-----------------------------------------------------------
	//书签中添加按钮的点击事件
	btn01.setOnClickListener(new Button.OnClickListener(){

		public void onClick(View v) {
			Builder myBuilder=new Builder(bookmarkandhistory.this);
			myBuilder.setIcon(R.drawable.siyecao30);
			myBuilder.setTitle("添加书签");
			//引入视图
			LayoutInflater inflater=LayoutInflater.from(bookmarkandhistory.this);
			View           view=inflater.inflate(R.layout.bookmarkadd, null);
			//引入视图的实例
			et01=(EditText) view.findViewById(R.id.Title01);
			et02=(EditText) view.findViewById(R.id.Url01);
			myBuilder.setView(view);
			//确定按钮
			myBuilder.setPositiveButton("确定", new OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
				String mytitle=et01.getText().toString();	
				String myurl=et02.getText().toString();
				ContentValues cv=new ContentValues();
				cv.put("title", mytitle);
				cv.put("url", myurl);
				db.insert("bookmarktable", null, cv);
				
				//查询一遍数据库
				//先进行一遍数据的查询
				bookmarkcursor=db.query("bookmarktable", new String[]{"_id","title","url"}, null, null, null, null, null);	
				SimpleCursorAdapter sca=new SimpleCursorAdapter(bookmarkandhistory.this, R.layout.listview01item, bookmarkcursor, new String[]{"title","url"}, new int[]{R.id.TextView01,R.id.TextView02});	
				myListView01.setAdapter(sca);	
				
				}
				
			});
			//取消按钮
			myBuilder.setNegativeButton("取消", new OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					
					
				}
				
			});
			myBuilder.show();
		}
		
	});
	
	
	
	//管理按钮的点击事件	
	btn03.setOnClickListener(new Button.OnClickListener(){

		public void onClick(View v) {
			
			Builder myBuilder=new Builder(bookmarkandhistory.this);
			myBuilder.setIcon(R.drawable.siyecao30);
			myBuilder.setTitle("历史记录管理");
			myBuilder.setMessage("是否全部删除？");
			myBuilder.setPositiveButton("确定", new OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					cursor=db.query("history", new String[]{"_id","title","url"}, null, null, null, null, null);
					while(cursor.moveToNext()){
					
						int idindex=cursor.getColumnIndex("_id");
						int myid=cursor.getInt(idindex);
						db.delete("history", "_id"+"="+myid, null);
					}	
					//提示语
					Toast.makeText(bookmarkandhistory.this, "历史记录已全部删除！", Toast.LENGTH_LONG).show();
					//重新查询一遍
					cursor=db.query("history", new String[]{"_id","title","url"}, null, null, null, null, null);
					SimpleCursorAdapter sca=new SimpleCursorAdapter(bookmarkandhistory.this, R.layout.listview02item, cursor, new String[]{"url"}, new int[]{R.id.TextView01});	
					myListView02.setAdapter(sca);
				}
				
			});
			myBuilder.setNegativeButton("取消", new OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					
					
				}
				
			});
			myBuilder.show();
		}
		
	});
	//
	}
	
	//设置按键的点击事件
	public boolean onKeyDown(int keyCode,KeyEvent event){
		
		switch(keyCode){
		case  KeyEvent.KEYCODE_BACK:
			Intent intent=new Intent();
			intent.setClass(bookmarkandhistory.this, main.class);
			startActivity(intent);
			bookmarkandhistory.this.finish();
			db.close();
		}
		
		return     super.onKeyDown(keyCode, event);
	}
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

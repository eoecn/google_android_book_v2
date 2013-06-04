package com.lyj.cn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class contentlist extends Activity{

	private MySQLiteHelper mySQLiteHelper;
	private SQLiteDatabase db;
	private ListView       lv;
	private TextView       tv;
	private List<Map<String,Object>> list;
	private Cursor cursor;
	private int mywhich;
	private int myid;
	private String title;
	private String content;
	private EditText et01;
	private EditText et02;
	
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content);
	
	mySQLiteHelper=new MySQLiteHelper(contentlist.this, "notepad.db", null, 1);
	tv=(TextView) findViewById(R.id.TextView01);
	lv=(ListView) findViewById(R.id.ListView01);
	//数据库
	 db=mySQLiteHelper.getReadableDatabase();
	 cursor=db.query("notepadtable", new String[]{"_id","title","content"}, null, null, null, null, null);
	if(cursor.getCount()>0){
	tv.setVisibility(View.GONE);
	}
	SimpleCursorAdapter sca=new SimpleCursorAdapter(contentlist.this, R.layout.item, cursor, new String[]{"_id","title","content"}, new int[]{R.id.TextView01,R.id.TextView02,R.id.TextView03});
	lv.setAdapter(sca);
	//列表单击事件的监听
	lv.setOnItemClickListener(new OnItemClickListener(){

		public void onItemClick(AdapterView<?> arg0, View arg1, int which,
				long arg3) {
			
			//mywhich=which-1;
			
			
			Builder builder=new Builder(contentlist.this);
			builder.setSingleChoiceItems(new String[]{"查看","修改","删除"}, 0, new OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					
				if(which==0){
					//Cursor cursor01=db.query("notepadtable", new String[]{"_id","title","content"}, null, null, null, null, null);
					
					int myidindex=cursor.getColumnIndex("_id");
					myid=cursor.getInt(myidindex);
					int titleindex=cursor.getColumnIndex("title");
					title=cursor.getString(titleindex);
					int contentindex=cursor.getColumnIndex("content");
					content=cursor.getString(contentindex);
					
					Toast.makeText(contentlist.this, myid+title+content, Toast.LENGTH_LONG).show();	
				}else if(which==2){
					int myidindex=cursor.getColumnIndex("_id");
					myid=cursor.getInt(myidindex);

					db.delete("notepadtable", "_id="+myid, null);
					
					Cursor cursor=db.query("notepadtable", new String[]{"_id","title","content"}, null, null, null, null, null);
					SimpleCursorAdapter sca=new SimpleCursorAdapter(contentlist.this, R.layout.item, cursor, new String[]{"_id","title","content"}, new int[]{R.id.TextView01,R.id.TextView02,R.id.TextView03});
					lv.setAdapter(sca);
				}else if(which==1){
				
					
					
					
					Builder builder01=new Builder(contentlist.this);
					
					builder01.setTitle("编辑");
					
					LayoutInflater inflater=LayoutInflater.from(contentlist.this);
					View view=inflater.inflate(R.layout.updatedialogeview, null);
					et01=(EditText) view.findViewById(R.id.EditText01);
					et02=(EditText) view.findViewById(R.id.EditText02);
					
					builder01.setView(view);
					builder01.setPositiveButton("确定", new DialogInterface.OnClickListener(){

						public void onClick(DialogInterface dialog, int which) {
							//取得数据
							int idindex=cursor.getColumnIndex("_id");
							int myid=cursor.getInt(idindex);
							//int titleindex=cursor.getColumnIndex("title");
							//title=cursor.getString(titleindex);
							//int contentindex=cursor.getColumnIndex("content");
							//content=cursor.getString(contentindex);
						String newtitle=et01.getText().toString();
						String newcontent=et02.getText().toString();
						ContentValues cv=new ContentValues();
						cv.put("title", newtitle);
						cv.put("content", newcontent);
						db.update("notepadtable", cv, "_id="+myid, null);
						Cursor cursor=db.query("notepadtable", new String[]{"_id","title","content"}, null, null, null, null, null);
						SimpleCursorAdapter sca=new SimpleCursorAdapter(contentlist.this, R.layout.item, cursor, new String[]{"_id","title","content"}, new int[]{R.id.TextView01,R.id.TextView02,R.id.TextView03});
						lv.setAdapter(sca);
						
						}
						
					});
					builder01.setNegativeButton("取消", new DialogInterface.OnClickListener(){

						public void onClick(DialogInterface dialog, int which) {

							
						}
						
					});
					builder01.show();
				}
				
				
				
				}
				
			});
			builder.show();
		}
		
	});
	
	
	}
	
	
}

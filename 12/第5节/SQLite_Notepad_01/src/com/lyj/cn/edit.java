package com.lyj.cn;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class edit extends Activity{

	private MySQLiteHelper mySQLiteHelper;
	
	private EditText et01;
	private EditText et02;
	private Button   btn01;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);
	
		mySQLiteHelper=new MySQLiteHelper(edit.this, "notepad.db", null, 1);
	
		et01=(EditText) findViewById(R.id.EditText01);
		et02=(EditText) findViewById(R.id.EditText02);
		btn01=(Button) findViewById(R.id.Button01);
		
		btn01.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View v) {
				SQLiteDatabase db=mySQLiteHelper.getReadableDatabase();
				String mytitle=et01.getText().toString();
				String mycontent=et02.getText().toString();
				
				ContentValues cv=new ContentValues();
				cv.put("title", mytitle);
				cv.put("content",mycontent);
				db.insert("notepadtable", null, cv);
			}
			
		});
		
	}
	
	
}

package com.lyj.cn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class main extends Activity {
    
	private Button editbtn;
	private Button querybtn;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
   
    editbtn=(Button) findViewById(R.id.Button01);
    querybtn=(Button) findViewById(R.id.Button02);
    
    //编辑按钮的点击事件
    editbtn.setOnClickListener(new Button.OnClickListener(){

		public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(main.this, edit.class);
			startActivity(intent);
		}
    	
    });
    
    querybtn.setOnClickListener(new Button.OnClickListener(){

		public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(main.this, contentlist.class);
			startActivity(intent);
		}});
    }
}
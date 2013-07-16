package com.eoeAndroid.helloworld;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HelloWorldActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//将布局文件设置为main.xml
		setContentView(R.layout.main);
		//得到两个Button控件
		Button mButton1 = (Button)findViewById(R.id.button1);
		Button mButton2 = (Button)findViewById(R.id.button2);
		 //为Button1绑定点击事件
		mButton1.setOnClickListener(new OnClickListener() {		
		    @Override
		    public void onClick(View v) {
		        // TODO Auto-generated method stub
		    	//使用intent启动ActivityB
		        Intent _intent = 
		        new Intent(HelloWorldActivity.this, ActivityB.class);
		        startActivity(_intent);
		    }
		});
		
		mButton2.setOnClickListener(new OnClickListener() { 
		    @Override
		    public void onClick(View v) {
		    // TODO Auto-generated method stub
		    Intent _intent = new Intent(HelloWorldActivity.this,  ActivityC.class);
		    startActivityForResult(_intent, 100);
		    }	
		});
	}
	
	 @Override
	 protected void onActivityResult(int requestCode,int resultCode, 
	                                            Intent data) {
	     super.onActivityResult(requestCode, resultCode, data);
	     if(requestCode == 100 && resultCode == Activity.RESULT_OK){
	         String val = data.getExtras().getString("helloworld");
	         TextView textView  =  (TextView)findViewById(R.id.tvDisplay);
	         textView.setText("来自ActivityC的值 ："+ val);
	     }
	 }

}

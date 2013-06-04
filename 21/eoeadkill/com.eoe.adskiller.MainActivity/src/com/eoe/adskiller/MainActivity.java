
package com.eoe.adskiller;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;



public class MainActivity extends Activity {

	private static final String TAG = "com.eoe.adskiller.MainActivity";
	private Button scanbtn,setbtn;
	private ImageView myimg;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		scanbtn = (Button)findViewById(R.id.check);
		setbtn = (Button)findViewById(R.id.setting);
		myimg=(ImageView)findViewById(R.id.imageView1);

       // 屏幕宽（像素，如：480px）   
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight();      
		
		myimg.setMaxHeight(screenHeight-568);
	
		
		scanbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent scanIntent=new Intent(MainActivity.this,ScanResult.class);
				MainActivity.this.startActivity(scanIntent);
			//	Log.d("start scan activity", "start time");
			}
		});
		setbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent setIntent=new Intent(MainActivity.this,SettingActivity.class);
				MainActivity.this.startActivity(setIntent);
			}
		});
	}
}

package com.tudi.cn;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class logo extends Activity implements Runnable{
    
	private ImageView  imageView;
	private AnimationDrawable animDrawable;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //无状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.logo);
        
        imageView=(ImageView) findViewById(R.id.frameview);
        animDrawable=(AnimationDrawable) imageView.getBackground();
   
    //运行多线程
        new Thread(logo.this).start();
    
    }
    //监控Activity窗口是否加载完毕
    public void onWindowFocusChanged(boolean hasFocus){
    	super.onWindowFocusChanged(hasFocus);
    	animDrawable.start();
    }
	public void run() {
		
		try {
			Thread.sleep(2000L);
		
		    Intent intent=new Intent();
		    intent.setClass(logo.this, main.class);
		    startActivity(intent);
		    
		    logo.this.finish();
		
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
	}
    //完善生命周期
	public  void onResume(){
		super.onResume();
	}
	
	public void onStart(){
		super.onStart();
	}
	
	public void onPause(){
		super.onPause();
	}
	
	public void onRestart(){
		super.onRestart();
	}
	
	public void onStop(){
		super.onRestart();
	}
	
	public void onDestroy(){
		super.onDestroy();
	}
	
	
	//
}
package com.eoe.adskiller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

public class WelComeActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);  
      
        ImageView image=(ImageView)findViewById(R.id.imageView551);    
        AlphaAnimation alphaAnimation=new AlphaAnimation(1, 1);  
        alphaAnimation.setDuration(1000);  
        alphaAnimation.setAnimationListener(new AnimationListener() {
		
		@Override
		public void onAnimationStart(Animation animation) {
			// TODO 自动生成的方法存根
			
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO 自动生成的方法存根
			
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			
			
			// TODO 自动生成的方法存根
			   Intent intent = new Intent();
               intent.setClass(WelComeActivity.this, MainActivity.class);
               startActivity(intent);
               finish();
			
			
		}
	});
      image.startAnimation(alphaAnimation);  
    }
	
  
	
}

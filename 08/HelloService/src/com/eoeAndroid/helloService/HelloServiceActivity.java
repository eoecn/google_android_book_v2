package com.eoeAndroid.helloService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelloServiceActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button button = (Button)findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HelloServiceActivity.this,HelloIntentService.class);
				startService(intent);
			}
		});
        
        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HelloServiceActivity.this,HelloIntentService.class);
				stopService(intent);
			}
		});
        
        Button button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HelloServiceActivity.this,HelloService.class);
				startService(intent);
			}
		});
    }
}
package com.eoe.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mainActivity extends Activity {
	private Button btn1;
	private Button btn2;
	private Button btn3;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btn1 = (Button) findViewById(R.id.custom);
        btn2 = (Button) findViewById(R.id.fragment);
        btn3 = (Button) findViewById(R.id.canvas);
        btn1.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), component.class);
				startActivity(intent);
			}
        });
        
        btn2.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MyFragment.class);
				startActivity(intent);
			}
        });
        
        btn3.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), dCanvas.class);
				startActivity(intent);
			}
        });
    }
}
package com.eoeAndroid.bindService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BindServiceActivity extends Activity {

	private HelloBindService binderService;
	private boolean  isBind = false; 
	private EditText editText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		editText = (EditText)findViewById(R.id.editText);
		
		Button button1 = (Button)findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isBind){
					Intent serviceIntent = new Intent(BindServiceActivity.this, HelloBindService.class);
					bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
					isBind = true;
				}
			}
		});

		Button button2 = (Button)findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isBind){
					isBind = false;
					unbindService(mConnection);
					binderService = null;
				}
			}
		});

		Button button3 = (Button)findViewById(R.id.button3);
		button3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(binderService == null){
					editText.setText("请先绑定服务");
					return;
				}
				editText.setText(binderService.getBookName());
			}
		});
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			binderService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binderService = ((HelloBindService.LocalBinder)service).getService();
		}
	};
}
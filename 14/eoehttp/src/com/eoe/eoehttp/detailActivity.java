package com.eoe.eoehttp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class detailActivity extends Activity{
	
	private TextView text ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.detail);
		text = (TextView) findViewById(R.id.text);
		
		Intent intent = getIntent();
		String content = intent.getStringExtra("content");
		if(content != null){
			text.setText(content);
		}
	}
}

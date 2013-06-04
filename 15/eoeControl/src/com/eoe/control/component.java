package com.eoe.control;

import com.eoe.control.view.progressBar;

import android.app.Activity;
import android.os.Bundle;

public class component extends Activity{
	
	 private progressBar pb;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycomponent);
		
		pb = (progressBar) findViewById(R.id.my_progressBar);
//        pb.setText(R.string.loadtext);
	}
	
}

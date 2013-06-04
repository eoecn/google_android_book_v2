package com.eoe.control;

import com.eoe.control.canvas.MyCanvas;

import android.app.Activity;
import android.os.Bundle;

public class dCanvas extends Activity{
	private MyCanvas myCanvas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		myCanvas = new MyCanvas(this);
		setContentView(myCanvas);
	}

}

package com.eoe.control.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class MyTextView extends TextView{

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		Paint paint = new Paint();
        paint.setColor(android.graphics.Color.RED);
        canvas.drawLine(0, 0, this.getWidth()-1, 0, paint); //横坐标0到this.getWidth()-1，纵坐标0到0
        canvas.drawLine(0, 0, 0, this.getHeight()-1, paint); //横坐标0到0，纵坐标0到this.getHeight()-1
        canvas.drawLine(this.getWidth()-1, 0, this.getWidth()-1, this.getHeight()-1, paint); //横坐标this.getWidth()-1到this.getWidth()-1，纵坐标0到this.getHeight()-1
        canvas.drawLine(0, this.getHeight()-1, this.getWidth()-1, this.getHeight()-1, paint); //横坐标0到this.getWidth()-1，纵坐标this.getHeight()-1到this.getHeight()-1
	}
	
	

}

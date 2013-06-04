package com.eoe.control.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.view.View;

public class MyCanvas extends View{
	
	private Paint mPaint = null;  //声明Paint对象

	public MyCanvas(Context context) {
		super(context);
		mPaint = new Paint();  //构建对象
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK); // 设置画布的颜色
		mPaint.setAntiAlias(true); // 设置取消锯齿效果
		canvas.clipRect(10, 10, 280, 260); // 设置裁剪区域
		canvas.save(); // 锁定画布
		canvas.rotate(45.0f); // 旋转画布
		mPaint.setColor(Color.RED); // 设置颜色
		canvas.drawRect(new Rect(20, 20, 160, 80), mPaint); // 绘制矩形
		canvas.restore(); // 解除画布的锁定
		// 画梯形
		mPaint.setAntiAlias(true);// 去掉边缘锯齿
		mPaint.setColor(Color.BLUE);
		mPaint.setStyle(Paint.Style.FILL);// 设置实心
		Path path1 = new Path();
		path1.moveTo(130, 20);
		path1.lineTo(190, 20);
		path1.lineTo(225, 60);
		path1.lineTo(100, 60);
		path1.close();
		canvas.drawPath(path1, mPaint);
		// 画渐变色圆形
		Shader mShader = new LinearGradient(0, 0, 100, 100, new int[] {
				Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW }, null,
				Shader.TileMode.REPEAT);// 使用着色器
		mPaint.setShader(mShader);
		canvas.drawCircle(180, 180, 40, mPaint);
		canvas.restore();
		mPaint.reset();//重置画笔
		mPaint.setColor(Color.GREEN); // 设置颜色
		canvas.drawRect(new Rect(160, 80, 280, 125), mPaint); // 绘制另一个矩形

	}

}

package com.eoe.control.view;

import java.io.UTFDataFormatException;

import com.eoe.control.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class progressBar extends LinearLayout{
	
	 protected TextView mTextView;

	public progressBar(Context context) {
		super(context);
		draw();
	}

	public progressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.progress);  //加载属性集
		draw();    //加载自定义布局
		setText(attr.getString(R.styleable.progress_text));   //attr.getString(R.styleable.progress_text)为获取XML属性中定义的值，
		setSize(attr.getDimension(R.styleable.progress_titleSize, 16)); 
		attr.recycle();
	}
	
	private void draw() {
		LayoutInflater.from(getContext()).inflate(R.layout.progress_bar, this);    //加载自定义布局
		mTextView = (TextView) findViewById(R.id.ProgressTextView);
	}
	
    public void setText(int resid) {
		mTextView.setText(resid);
	}
    
    public void setText(String title) {
    	if(title!= null){
    		mTextView.setText(title);
    	}
	}
    
    public void setSize(Float size){
    	mTextView.setTextSize(size);
    }

}

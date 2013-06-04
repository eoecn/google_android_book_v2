package com.eoe.adskiller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityEventSource;
import android.widget.TextView;

public class Alerts 
{
	public static void ShowAlerts(String msg,Context ctx,String text) 
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(ctx);

		MyonClickListener ml=new MyonClickListener();
		builder.setPositiveButton("¹Ø±Õ", ml);
	
		LayoutInflater li=LayoutInflater.from(ctx);
		View view=li.inflate(R.layout.alertlayout, null);
		TextView tV=(TextView) view.findViewById(R.id.alertmes);
		tV.setText(text);
		builder.setView(view);
		builder.setTitle(msg);
		AlertDialog adAlertDialog=builder.create();
		adAlertDialog.show();
		
		
	}
	
}
class MyonClickListener implements android.content.DialogInterface.OnClickListener
{

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
		
	}
	
}
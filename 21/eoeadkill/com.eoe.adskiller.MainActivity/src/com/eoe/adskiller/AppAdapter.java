package com.eoe.adskiller;

import java.util.ArrayList;

import android.R.raw;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AppAdapter extends BaseAdapter {
	
	Context context;
	ArrayList<AppInfo> dataList=new ArrayList<AppInfo>();
	public AppAdapter(Context context,ArrayList<AppInfo> inputDataList)
	{
		this.context=context;
		dataList.clear();
		for(int i=0;i<inputDataList.size();i++)
		{
			dataList.add(inputDataList.get(i));
		}
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		View v=convertView;
		final AppInfo appUnit=dataList.get(position);
		if(v==null)
		{
    		LayoutInflater vi=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=vi.inflate(R.layout.app_row, null);
			v.setClickable(true);
		}
    	TextView appName=(TextView)v.findViewById(R.id.appName);
    	ImageView appIcon=(ImageView)v.findViewById(R.id.icon);
    	Button btnButton=(Button)v.findViewById(R.id.button1);
    	if(appName!=null)
    	{
    		appName.setText(appUnit.appName);
    	}
    	
    	if(appIcon!=null)
    		appIcon.setImageDrawable(appUnit.appIcon);
    	btnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				AppInfo theApp=(AppInfo)dataList.get(position);
				Uri packageURI = Uri.parse("package:" +theApp.packageName);         

				Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);         

				context.startActivity(uninstallIntent);
		
				
			}
		});
		return v;
	}
}
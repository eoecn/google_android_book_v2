package com.eoe.adskiller;

import android.R.anim;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class SettingActivity extends ListActivity {

@Override
	protected void onCreate(Bundle savedInstanceState) {
	// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);      
		
		ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.settings, android.R.layout.simple_list_item_1);
	
		setListAdapter(adapter);
		
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				switch (arg2) {
				case 0:
					Alerts.ShowAlerts("反馈", SettingActivity.this,"使用中如遇到各种BUG，请发送相关信息至QQ：593330820");
					break;
				case 1:
					Alerts.ShowAlerts("关于", SettingActivity.this,"广告检测者是由Android爱好者开发的一个检测广告的小应用,开发过程中,eoe社区给了我很多帮助,感谢eoe!");
					break;
				case 2:
					
					break;
				case 3:
					
					break;
				default:
					break;
				}
				
				
			}
		});
	}
	


}
   
package com.eoe.xml;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class infoActivity extends Activity{
	private ListView list;
	private infoAdapter mInfoAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		
		list = (ListView) findViewById(R.id.list);
		
		MainApplication app = (MainApplication) getApplication();
		List<Person> students= app.getStudents();
//		for (Person person : students) {
//			Log.i("eoe", "infoActivity:"+person.toString());
//		}
		mInfoAdapter = new infoAdapter(infoActivity.this);
		mInfoAdapter.setItemDataList(students);
		list.setAdapter(mInfoAdapter);
	}

	private class infoAdapter extends BaseAdapter{
		
		protected Context mContext = null;
		protected List<Person> itemList = null ;
		protected LayoutInflater mInflater = null;
		
		public infoAdapter(Context context) {
			mContext= context;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return itemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Person student = itemList.get(position);
			convertView = mInflater.inflate(R.layout.list_row, null);
			TextView textview1 = (TextView) convertView.findViewById(R.id.item);
			textview1.setText(String.valueOf(student.getId()));
			
			TextView textview2 = (TextView) convertView.findViewById(R.id.item2);
			textview2.setText(student.getName());
			TextView textview3 = (TextView) convertView.findViewById(R.id.item3);
			textview3.setText(String.valueOf(student.getAge()));

			return convertView;
		}
		
		public void setItemDataList(List<Person> list) {
			itemList = list;
		}
	}
}

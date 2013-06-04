package com.eoe.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import com.eoe.xml.dom.DomParser;
import com.eoe.xml.model.StudentParser;
import com.eoe.xml.pull.MyPullxml;
import com.eoe.xml.sax.SaxStudentParser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class xml extends Activity {
	/** Called when the activity is first created. */
	private InputStream instream;
	private Button pull;
	private Button sax;
	private Button dom;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		pull = (Button) findViewById(R.id.pull);
		sax = (Button) findViewById(R.id.sax);
		dom = (Button) findViewById(R.id.dom);

		pull.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pull();
			}
		});
		
		sax.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pull();
			}
		});
		
		dom.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pull();
			}
		});

	}
	private void pull(){
		 MyPullxml PullParser;
			try {
				PullParser = new MyPullxml(); // 创建SaxBookParser实例
				instream = this.getAssets().open("student.xml");
				List<Person> persons = PullParser.parse(instream); // 解析输入流
				
				
				MainApplication app = (MainApplication) getApplication();
				app.setStudents(persons);
				
				Intent intengpull = new Intent();
				intengpull.setClass(getApplicationContext(), infoActivity.class);
				startActivity(intengpull);

//				for (Person person : persons) {
//					Log.i("eoe", person.toString());
//				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					instream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
	
	private void sax(){
		 StudentParser parser;
			try {
				instream = this.getAssets().open("student.xml");
				parser = new SaxStudentParser(); // 创建SaxBookParser实例
				List<Person> persons = parser.parse(instream); // 解析输入流
				
				MainApplication app = (MainApplication) getApplication();
				app.setStudents(persons);
				
				Intent intengpull = new Intent();
				intengpull.setClass(getApplicationContext(), infoActivity.class);
				startActivity(intengpull);
				
//				for (Person person : persons) {
//					Log.i("eoe", person.toString());
//				}
			} catch (Exception e) {
				Log.e("eoe", e.getMessage());
			}finally {
				try {
					instream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
	private void dom(){
		DomParser DomParser;
		try {
			DomParser = new DomParser(); // 创建SaxBookParser实例
			instream = this.getAssets().open("student.xml");
			List<Person> persons = DomParser.parse(instream); // 解析输入流
			
			MainApplication app = (MainApplication) getApplication();
			app.setStudents(persons);
			
			Intent intengpull = new Intent();
			intengpull.setClass(getApplicationContext(), infoActivity.class);
			startActivity(intengpull);

//			for (Person person : persons) {
//				Log.i("eoe", person.toString());
//			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				instream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
package com.eoeAndroid.HelloContentResolver;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HelloContentResolverActivity extends Activity {
    /** Called when the activity is first created. */
	
	private final String EOE_URI = "content://com.eoeAndroid.helloContentProvider.provider.books/book";
	private ContentResolver cr;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        cr = this.getContentResolver();
        Button mButton1 = (Button)findViewById(R.id.button1);
        Button mButton2 = (Button)findViewById(R.id.button2);
        mButton1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Cursor cursor = cr.query(Uri.parse(EOE_URI),
						null, null, null, null);
				while(cursor.moveToNext()){
					String name = cursor.getString(1);
					TextView tv = (TextView)findViewById(R.id.showText);
					tv.setText(name);
				}
				cursor.close();
			}
		});
        mButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ContentValues cv = new ContentValues();
				cv.put("bookname", "Android开发入门与实践第二版");
				int result = cr.update(Uri.parse(EOE_URI), cv, null, null);
				if(result > 0){
					Toast.makeText(HelloContentResolverActivity.this, "修改成功", 1000).show();
				}
			}
		});
    }
}
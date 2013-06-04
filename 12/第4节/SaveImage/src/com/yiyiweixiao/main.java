package com.yiyiweixiao;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class main extends Activity implements OnClickListener {

	private Button btnsave1, btnsave2, btnquery1, btnquery2;
	private ImageView iv1, iv2;
	private MySQLiteOpenHelper mySQLiteOpenHelper = null;
	private SQLiteDatabase mydb = null;
	        
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// 创建助手类的实例
		// CursorFactory的值为null,表示采用默认的工厂类
		mySQLiteOpenHelper = new MySQLiteOpenHelper(this, "saveimage.db", null,
				1);
		// 创建一个可读写的数据库
		mydb = mySQLiteOpenHelper.getWritableDatabase();
		// 创建按钮的实例
		btnsave1 = (Button) findViewById(R.id.button1);
		btnsave2 = (Button) findViewById(R.id.button2);
		btnquery1 = (Button) findViewById(R.id.button3);
		btnquery2 = (Button) findViewById(R.id.button4);
		// 设置其点击方法
		btnsave1.setOnClickListener(this);
		btnsave2.setOnClickListener(this);
		btnquery1.setOnClickListener(this);
		btnquery2.setOnClickListener(this);

	}

	public void onClick(View v) {
		if (v == btnsave1) {//保存图片
			try {
        //将图片转化为位图
			Bitmap bitmap1=BitmapFactory.decodeResource(getResources(), R.drawable.erweima);	
		    int size=bitmap1.getWidth()*bitmap1.getHeight()*4;		
		//创建一个字节数组输出流,流的大小为size
		    ByteArrayOutputStream baos=new ByteArrayOutputStream(size);    
		//设置位图的压缩格式，质量为100%，并放入字节数组输出流中    
		    bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos);
		//将字节数组输出流转化为字节数组byte[]    
		    byte[] imagedata1=baos.toByteArray(); 
		//将字节数组保存到数据库中    
		ContentValues cv=new ContentValues();
		 cv.put("_id", 1);   
		 cv.put("image", imagedata1);   
		 mydb.insert("imagetable", null, cv);
		//关闭字节数组输出流
		 baos.close();
		 
			} catch (Exception e) {
              e.printStackTrace();
			}

		} else if (v == btnsave2) {// 保存图片2
          try{
        	Bitmap bitmap2=BitmapFactory.decodeResource(getResources(), R.drawable.taohua);  
        	int    size=bitmap2.getWidth()*bitmap2.getHeight()*4;  
        	ByteArrayOutputStream baos=new ByteArrayOutputStream(size);  
        	bitmap2.compress(Bitmap.CompressFormat.PNG, 100, baos);  
        	byte[] imagedata2=baos.toByteArray();
        	ContentValues cv=new ContentValues();
        	cv.put("_id", 2);
        	cv.put("image", imagedata2);
        	mydb.insert("imagetable", null, cv);
        	baos.close();
        	
          }
			catch(Exception e){
				e.printStackTrace();
			}
			
			
			
			
			
		} else if (v == btnquery1) {// 查询图片1
        //创建一个指针
			Cursor cur=mydb.query("imagetable", new String[]{"_id","image"}, null, null, null, null, null);
			byte[] imagequery=null;
			if(cur.moveToNext()){
				//将Blob数据转化为字节数组
				imagequery=cur.getBlob(cur.getColumnIndex("image"));
			}
			//将字节数组转化为位图
			Bitmap imagebitmap=BitmapFactory.decodeByteArray(imagequery, 0, imagequery.length);
			iv1=(ImageView) findViewById(R.id.imageView1);
			//将位图显示为图片
			iv1.setImageBitmap(imagebitmap);
			
		} else if (v == btnquery2) {// 查询图片2

		Cursor cur=mydb.query("imagetable", new String[]{"_id","image"}, null, null, null, null, null);	
		byte[] imagequery=null;	
		while(cur.moveToNext()){
		
			imagequery=cur.getBlob(cur.getColumnIndex("image"));
			Bitmap imagebitmap=BitmapFactory.decodeByteArray(imagequery,0,imagequery.length);	
			iv2=(ImageView) findViewById(R.id.imageView2);
			iv2.setImageBitmap(imagebitmap);	
		}
		    
			
		}

	}
	public void onDestroy(){
		super.onDestroy();
		//退出程序时，必须关闭数据库。
		mydb.close();
		
	}
	
	
}
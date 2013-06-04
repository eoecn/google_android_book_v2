package com.lyj.cn;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class main extends Activity {
    
	private EditText  et01;
	private Button    btn01;
	private EditText  et02;
	private Button    btn02;
	
	private String    queryresult;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
    et01=(EditText) findViewById(R.id.EditText01);
    et02=(EditText) findViewById(R.id.EditText02);
    
    btn01=(Button) findViewById(R.id.Button01);
    btn02=(Button) findViewById(R.id.Button02);
    
    btn01.setOnClickListener(new Button.OnClickListener(){

		public void onClick(View v) {
			
		savefile();	
		
		}
    	
    });
    
    btn02.setOnClickListener(new Button.OnClickListener(){

		public void onClick(View v) {
			
		queryfile();	
		et02.setText(queryresult);
		
		}
    	
    });
    
    
    }
    
    //保存文件
    
    public void savefile(){
    try{
    String string=et01.getText().toString();
    
    byte[] buffer=string.getBytes();
    
    FileOutputStream  fos=openFileOutput("demo.txt", MODE_PRIVATE);	
    
    fos.write(buffer);
    fos.close();
    }
    catch(Exception e){
    	e.printStackTrace();
    }
    }
    
    //查询文件
    public void queryfile(){
    try{
    	FileInputStream  fis=openFileInput("demo.txt");
    	int length=fis.available();
    	byte[] buffer=new byte[length];
    	fis.read(buffer);
    	
    	 //queryresult=new String(buffer,"UTF-8");
    	queryresult=EncodingUtils.getString(buffer, "UTF-8");
    	fis.close();
    	
    }catch(Exception e){
    	e.printStackTrace();
    }
    }
    
    
    
}
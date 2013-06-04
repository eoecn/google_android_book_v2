package com.lyj.cn;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class main extends Activity {
    
	private EditText  et;
	private Button    btn;
	private SharedPreferences sp;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
        et=(EditText) findViewById(R.id.EditText01);
	    btn=(Button) findViewById(R.id.Button01);
        
        //创建一个SharedPreferences的实例，MODE_APPEND表示新的内容会添加在原有内容的后面
	    sp=this.getSharedPreferences("demo_01", MODE_PRIVATE);
        //创建一个按钮的点击事件
	    btn.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View v) {
	    //创建一个SharedPreferences.Editor类的实例对象
				SharedPreferences.Editor editor=sp.edit();
		//取得输入的献花的名称		
				String flowername=et.getText().toString();
		//把献花的名称放进去		
				editor.putString("name", flowername);
		//正式提交，予以生效
				editor.commit();
		//进行提交成功的提示		
		        Toast.makeText(main.this, "恭喜，献花成功！", Toast.LENGTH_LONG).show();
			}
	    	
	    });
        
        
        
	}
}
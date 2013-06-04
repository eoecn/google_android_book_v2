package com.lyj.cn;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import android.app.Activity;
import android.os.Bundle;

public class main extends Activity {
    private ObjectContainer db;
	private People people;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
    db=Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "/sdcard/db4o.data");
    
    //创建封装的类的实例对象，并传递数值
    people=new People(1,"小明",20);
    //保存对象
    db.store(people);
    //予以提交
    db.commit();
    }
    
    public void querybyexample(){
    	//假如同一个封装类创建了多个不同的对象
    	ObjectSet<People>  myObjectSet=db.queryByExample(new People());
    	String string="";
    	while(myObjectSet.hasNext()){
    		People people=myObjectSet.next();
    		string=string+people.getId()+people.getName()+people.getAge()+"\n";
    	}
    	
    	
    	
    	
    	
    }
    
}
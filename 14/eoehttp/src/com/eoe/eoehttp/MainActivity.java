package com.eoe.eoehttp;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        
        btn1.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				connect("http://www.eoeandroid.com");
			}
        });
        
        btn2.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				 connectPost("http://192.168.0.37:6666/text/index.php");    //测试地址
			}
        });
        
        btn3.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				 javaHttpGet("http://www.eoeandroid.com");
			}
        });
        
        btn4.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				javaHttpPost("http://192.168.0.37:6666/text/index.php");   //测试地址
			}
        });
         
    }

    private void connect (String url){
    	HttpClient httpClient = new DefaultHttpClient();   //新建HttpClient对象
    	HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000); //设置连接超时
    	HttpConnectionParams.setSoTimeout(httpClient.getParams(), 3000); //设置数据读取时间超时
    	ConnManagerParams.setTimeout(httpClient.getParams(), 3000);  //设置从连接池中取连接超时
    	
    	
    	HttpGet httpget = new HttpGet(url);  //获取请求
    	
    	try {
    		HttpResponse response = httpClient.execute(httpget);     //执行请求，获取响应结果
    		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  //响应通过
    			String result = EntityUtils.toString(response.getEntity(), "UTF-8");   
    			Intent httpclientIntent = new Intent();
    			httpclientIntent.putExtra("content", result);
    			httpclientIntent.setClass(getApplicationContext(), detailActivity.class);
    			startActivity(httpclientIntent);
    		}else{
    			                                               //响应未通过
    		}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
    
    private void connectPost(String url){
    	
    	  HttpClient httpClient = new DefaultHttpClient();    // 新建HttpClient对象
    	  HttpPost httpPost = new HttpPost(url);    // 新建HttpPost对象
    	  List<NameValuePair> params = new ArrayList<NameValuePair>();  //使用NameValuePair来保存要传递的Post参数
    	  params.add(new BasicNameValuePair("username", "hello"));    //添加要传递的参数
    	  params.add(new BasicNameValuePair("password", "eoe"));   
			try {
				 HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);  // 设置字符集
	    	     httpPost.setEntity(entity);    // 设置参数实体
	    	     HttpResponse httpResp = httpClient.execute(httpPost); // 获取HttpResponse实例
	    		if(httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  //响应通过
	    			String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");   
	    			
	    			Intent httpclientIntent = new Intent();
	    			httpclientIntent.putExtra("content", result);
	    			httpclientIntent.setClass(getApplicationContext(), detailActivity.class);
	    			startActivity(httpclientIntent);
	    		}else{
	    			                                               //响应未通过
	    		}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
    }
    
    private void javaHttpGet(String url){
    	try {
			URL pathUrl = new URL(url);    //创建一个URL对象
			HttpURLConnection urlConnect = (HttpURLConnection) pathUrl.openConnection();  //打开一个HttpURLConnection连接
			urlConnect.setConnectTimeout(3000);  // 设置连接超时时间
			urlConnect.connect();
			InputStreamReader in = new InputStreamReader(urlConnect.getInputStream()); //得到读取的内容
			BufferedReader buffer = new BufferedReader(in);  //为输出创建BufferedReader
			String inputLine = null;
			String resultData = null;
			while (((inputLine = buffer.readLine()) != null)) {
				//利用循环来读取数据\
				resultData += inputLine;
			}
			Intent httpclientIntent = new Intent();
			httpclientIntent.putExtra("content", resultData);
			httpclientIntent.setClass(getApplicationContext(), detailActivity.class);
			startActivity(httpclientIntent);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void javaHttpPost(String url){
    	
    	try {
    		String params = "username=" + URLEncoder.encode("hello", "UTF-8")+ "&password=" + URLEncoder.encode("eoe", "UTF-8");
    		byte[] postData = params.getBytes();
    		URL pathUrl = new URL(url); //创建一个URL对象
			HttpURLConnection urlConnect = (HttpURLConnection) pathUrl.openConnection(); 
			urlConnect.setConnectTimeout(3000);  // 设置连接超时时间
			urlConnect.setDoOutput(true);  //post请求必须设置允许输出
			urlConnect.setUseCaches(false); //post请求不能使用缓存
			urlConnect.setRequestMethod("POST");  //设置post方式请求
			urlConnect.setInstanceFollowRedirects(true); 
			urlConnect.setRequestProperty("Content-Type","application/x-www-form-urlencode");// 配置请求Content-Type
			urlConnect.connect();  // 开始连接
			DataOutputStream dos = new DataOutputStream(urlConnect.getOutputStream()); // 发送请求参数
			dos.write(postData);
			dos.flush();
			dos.close();
			if (urlConnect.getResponseCode() == 200) {    //请求成功
				byte[] data = readInputStream(urlConnect.getInputStream());   
				
    			Intent httpclientIntent = new Intent();
    			httpclientIntent.putExtra("content", new String(data, "UTF-8"));
    			httpclientIntent.setClass(getApplicationContext(), detailActivity.class);
    			startActivity(httpclientIntent);

			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
    }
    
    public  byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();//转化为二进制数据
        outStream.close();
        inStream.close();
        return data;
    }
}

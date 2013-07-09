package com.eoeandroid.booksearcher;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends Activity implements OnClickListener {
    
    private Button mStartScan;
    private ProgressDialog mProgressDialog;
    
    private DownloadHandler mHandler = new DownloadHandler(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        
        if ((result == null) || (result.getContents() == null)) {
            Log.v(Utils.TAG, "User cancel scan by pressing back hardkey.");
            return;
        }
        
        // 因为下载需耗时，为了更好的用户体验，显示进度条进行提示。
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.communicating));
        mProgressDialog.show();
        
        // 启动下载线程
        DownloadThread thread = new DownloadThread(BookAPI.URL_ISBN_BASE + result.getContents());
        thread.start();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.main_start_scan) {
            startScanner();
        }
    }

    private void initViews() {
        mStartScan = (Button) findViewById(R.id.main_start_scan);
        mStartScan.setOnClickListener(this);
    }
    
    /**
     * 通过Intent启动第三方应用"ZXing"进行图书条形码扫描
     */
    private void startScanner() {
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        integrator.initiateScan();
    }
    
    /**
     * 迁移图书籍信息显示界面，并将图书信息传与
     */
    private void startBookInfoDetailActivity(BookInfo bookInfo) {
        if (bookInfo == null) {
            return;
        }
        
        Intent intent = new Intent(this, BookInfoDetailActivity.class);
        intent.putExtra(BookInfo.class.getName(), bookInfo);  
        startActivity(intent);
    }
    
    private static class DownloadHandler extends Handler {
        
        private MainActivity mActivity;
        
        public DownloadHandler(MainActivity activity) {
            super();
            mActivity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            if ((msg.obj == null) || (mActivity.mProgressDialog == null) || (!mActivity.mProgressDialog.isShowing())) {
                return;
            }
            
            mActivity.mProgressDialog.dismiss();
            
            NetResponse response = (NetResponse) msg.obj;
            
            if (response.getCode() != BookAPI.RESPONSE_CODE_SUCCEED) {
                // 通信异常处理
                Toast.makeText(mActivity, "[" + response.getCode() + "]: "
                                                + mActivity.getString((Integer) response.getMessage()), 
                                                Toast.LENGTH_LONG).show();
            } else {
                // 通信正常时，迁移到书本显示界面
                mActivity.startBookInfoDetailActivity((BookInfo) response.getMessage());
            }
        }
        
    }

    private class DownloadThread extends Thread {
        
        private String mURL;
        
        public DownloadThread(String url) {
            super();
            mURL = url;
        }
        
        @Override
        public void run() {
            Message msg = Message.obtain();
            msg.obj = Utils.download(mURL);
            mHandler.sendMessage(msg);
        }
    }
    
}

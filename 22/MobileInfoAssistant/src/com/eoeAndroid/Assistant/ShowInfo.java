package com.eoeAndroid.Assistant;

import com.eoeAndroid.Assistant.utils.FetchData;
import com.eoeAndroid.Assistant.utils.PreferencesUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class ShowInfo extends Activity implements Runnable {
	private static final String TAG = "ShowInfo";

	TextView info;
	TextView title;
	private ProgressDialog pd;
	public String info_datas;
	public boolean is_valid = false;
	public int _id = 0;
	public String _name = "";
	public int _position = 0;
	public int _ref = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showinfo);
		revParams();
		info = (TextView) findViewById(R.id.info);
		title = (TextView) findViewById(R.id.title);
		setTitle("MobileInfoAssistant: " + _name);
		title.setText(_name);
		load_data();
	}

	private void load_data() {
		pd = ProgressDialog.show(this, "Please Wait a moment..",
				"fetch info datas...", true, false);
		Thread thread = new Thread(this);
		thread.start();
	}

	// 接收传递进来的信息
	private void revParams() {
		Log.i(TAG, "revParams.");
		Intent startingIntent = getIntent();
		if (startingIntent != null) {
			Bundle infod = startingIntent
					.getBundleExtra("android.intent.extra.info");
			if (infod == null) {
				is_valid = false;
			} else {
				_id = infod.getInt("id");
				_name = infod.getString("name");
				_position = infod.getInt("position");
				is_valid = true;
			}
		} else {
			is_valid = false;
		}
		Log.i(TAG, "_name:" + _name + ",_id="+_id);
	}

 

 

	@Override
	public void run() {
		if (_ref > 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.i("load_data", "e=" + e.toString());
			}
		}
		switch (_id) {
		case PreferencesUtil.CPU_INFO:
			info_datas = FetchData.fetch_cpu_info();
			break;
		case PreferencesUtil.DISK_INFO:
			info_datas = FetchData.fetch_disk_info();
			break;
		case PreferencesUtil.NET_STATUS:
			info_datas = FetchData.fetch_netstat_info();
			break;
		case PreferencesUtil.VER_INFO:
			info_datas = FetchData.fetch_version_info();
			break;
		case PreferencesUtil.DMESG_INFO:
			info_datas = FetchData.fetch_dmesg_info();
			break;
		case PreferencesUtil.RunningProcesses:
			info_datas = FetchData.fetch_process_info();
			break;
		case PreferencesUtil.NET_CONFIG:
			info_datas = FetchData.fetch_netcfg_info();
			break;
		case PreferencesUtil.MOUNT_INFO:
			info_datas = FetchData.fetch_mount_info();
			break;
		case PreferencesUtil.TEL_STATUS:
			info_datas = FetchData.fetch_tel_status(this);
			break;
		case PreferencesUtil.MEM_INFO:
			info_datas = FetchData.getMemoryInfo(this);
			break;
		case PreferencesUtil.SystemProperty:
			info_datas = FetchData.getSystemProperty();
			break;
		case PreferencesUtil.DisplayMetrics:
			info_datas = FetchData.getDisplayMetrics(this);
			break;
		case PreferencesUtil.RunningService:
			info_datas = FetchData.getRunningServicesInfo(this);
			break;
		case PreferencesUtil.RunningTasks:
			info_datas = FetchData.getRunningTasksInfo(this);
			break;
		}

		handler.sendEmptyMessage(0);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pd.dismiss();
			info.setText(info_datas);
		}
	};

}

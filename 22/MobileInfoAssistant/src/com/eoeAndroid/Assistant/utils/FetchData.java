package com.eoeAndroid.Assistant.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

public class FetchData {
	private static StringBuffer buffer;

	// cpu info
	public static String fetch_cpu_info() {
		String result = null;
		CMDExecute cmdexe = new CMDExecute();
		try {
			String[] args = { "/system/bin/cat", "/proc/cpuinfo" };
			result = cmdexe.run(args, "/system/bin/");
			Log.i("result", "result=" + result);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	// disk info
	public static String fetch_disk_info() {
		String result = null;
		CMDExecute cmdexe = new CMDExecute();
		try {
			String[] args = { "/system/bin/df" };
			result = cmdexe.run(args, "/system/bin/");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	// netstat info
	public static String fetch_netstat_info() {
		String result = null;
		CMDExecute cmdexe = new CMDExecute();
		try {
			String[] args = { "/system/bin/netstat" };
			result = cmdexe.run(args, "/system/bin/");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	// netstat info
	public static String fetch_version_info() {
		String result = null;
		CMDExecute cmdexe = new CMDExecute();
		try {
			String[] args = { "/system/bin/cat", "/proc/version" };
			result = cmdexe.run(args, "/system/bin/");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	// netstat info
	public static String fetch_dmesg_info() {
		Log.i("fetch_dmesg_info", "start....");
		String result = null;
		CMDExecute cmdexe = new CMDExecute();
		try {
			String[] args = { "/system/bin/dmesg" };
			result = cmdexe.run(args, "/system/bin/");
		} catch (IOException ex) {
			Log.i("fetch_dmesg_info", "ex=" + ex.toString());
		}
		return result;
	}

	// fetch_process_info
	public static String fetch_process_info() {
		Log.i("fetch_process_info", "start....");
		String result = null;
		CMDExecute cmdexe = new CMDExecute();
		try {
			String[] args = { "/system/bin/top", "-n", "1" };
			result = cmdexe.run(args, "/system/bin/");
		} catch (IOException ex) {
			Log.i("fetch_process_info", "ex=" + ex.toString());
		}
		return result;
	}

	// fetch_process_info
	public static String fetch_netcfg_info() {
		Log.i("fetch_process_info", "start....");
		String result = null;
		CMDExecute cmdexe = new CMDExecute();
		try {
			String[] args = { "/system/bin/netcfg" };
			result = cmdexe.run(args, "/system/bin/");
		} catch (IOException ex) {
			Log.i("fetch_process_info", "ex=" + ex.toString());
		}
		return result;
	}

	// fetch_mount_info
	public static String fetch_mount_info() {
		Log.i("fetch_process_info", "start....");
		String result = null;
		CMDExecute cmdexe = new CMDExecute();
		try {
			String[] args = { "/system/bin/mount" };
			result = cmdexe.run(args, "/system/bin/");
		} catch (IOException ex) {
			Log.i("fetch_process_info", "ex=" + ex.toString());
		}
		return result;
	}

	public static String fetch_tel_status(Context cx) {
		String result = null;
		TelephonyManager tm = (TelephonyManager) cx
				.getSystemService(Context.TELEPHONY_SERVICE);//    
		String str = "";
		str += "DeviceId(IMEI) = " + tm.getDeviceId() + "\n";
		str += "DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion()
				+ "\n";
		str += "Line1Number = " + tm.getLine1Number() + "\n";
		str += "NetworkCountryIso = " + tm.getNetworkCountryIso() + "\n";
		str += "NetworkOperator = " + tm.getNetworkOperator() + "\n";
		str += "NetworkOperatorName = " + tm.getNetworkOperatorName() + "\n";
		str += "NetworkType = " + tm.getNetworkType() + "\n";
		str += "PhoneType = " + tm.getPhoneType() + "\n";
		str += "SimCountryIso = " + tm.getSimCountryIso() + "\n";
		str += "SimOperator = " + tm.getSimOperator() + "\n";
		str += "SimOperatorName = " + tm.getSimOperatorName() + "\n";
		str += "SimSerialNumber = " + tm.getSimSerialNumber() + "\n";
		str += "SimState = " + tm.getSimState() + "\n";
		str += "SubscriberId(IMSI) = " + tm.getSubscriberId() + "\n";
		str += "VoiceMailNumber = " + tm.getVoiceMailNumber() + "\n";

		int mcc = cx.getResources().getConfiguration().mcc;
		int mnc = cx.getResources().getConfiguration().mnc;
		str += "IMSI MCC (Mobile Country Code):" + String.valueOf(mcc) + "\n";
		str += "IMSI MNC (Mobile Network Code):" + String.valueOf(mnc) + "\n";
		result = str;
		return result;
	}

	/**
	 * 系统内存情况查看
	 */
	public static String getMemoryInfo(Context context) {
		StringBuffer memoryInfo = new StringBuffer();
		final ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
	
		ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(outInfo);
		memoryInfo.append("\nTotal Available Memory :").append(
				outInfo.availMem >> 10).append("k");
		memoryInfo.append("\nTotal Available Memory :").append(
				outInfo.availMem >> 20).append("M");
		memoryInfo.append("\nIn low memory situation:").append(
				outInfo.lowMemory);
		
		String result = null;
		CMDExecute cmdexe = new CMDExecute();
		try {
			String[] args = { "/system/bin/cat","/proc/meminfo" };
			result = cmdexe.run(args, "/system/bin/");
		} catch (IOException ex) {
			Log.i("fetch_process_info", "ex=" + ex.toString());
		}
		
		return memoryInfo.toString()+"\n\n"+result;
	}
	


	/**
	 * 系统信息查看方法
	 */
	public static String getSystemProperty() {
		buffer = new StringBuffer();
		initProperty("java.vendor.url", "java.vendor.url");
		initProperty("java.class.path", "java.class.path");
		initProperty("user.home", "user.home");
		initProperty("java.class.version", "java.class.version");
		initProperty("os.version", "os.version");
		initProperty("java.vendor", "java.vendor");
		initProperty("user.dir", "user.dir");
		initProperty("user.timezone", "user.timezone");
		initProperty("path.separator", "path.separator");
		initProperty(" os.name", " os.name");
		initProperty("os.arch", "os.arch");
		initProperty("line.separator", "line.separator");
		initProperty("file.separator", "file.separator");
		initProperty("user.name", "user.name");
		initProperty("java.version", "java.version");
		initProperty("java.home", "java.home");
		return buffer.toString();
	}

	private static String initProperty(String description, String propertyStr) {
		if (buffer == null) {
			buffer = new StringBuffer();
		}
		buffer.append(description).append(":");
		buffer.append(System.getProperty(propertyStr)).append("\n");
		return buffer.toString();
	}

	public static String getDisplayMetrics(Context cx) {
		String str = "";
		DisplayMetrics dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		float density = dm.density;
		float xdpi = dm.xdpi;
		float ydpi = dm.ydpi;
		str += "The absolute width:" + String.valueOf(screenWidth) + "pixels\n";
		str += "The absolute heightin:" + String.valueOf(screenHeight)
				+ "pixels\n";
		str += "The logical density of the display.:" + String.valueOf(density)
				+ "\n";
		str += "X dimension :" + String.valueOf(xdpi) + "pixels per inch\n";
		str += "Y dimension :" + String.valueOf(ydpi) + "pixels per inch\n";
		return str;
	}
	
	//RunningServicesInfo
	public static String getRunningServicesInfo(Context context) {
		StringBuffer serviceInfo = new StringBuffer();
		final ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> services = activityManager.getRunningServices(100);

		Iterator<RunningServiceInfo> l = services.iterator();
		while (l.hasNext()) {
			RunningServiceInfo si = (RunningServiceInfo) l.next();
			serviceInfo.append("pid: ").append(si.pid);
			serviceInfo.append("\nprocess: ").append(si.process);
			serviceInfo.append("\nservice: ").append(si.service);
			serviceInfo.append("\ncrashCount: ").append(si.crashCount);
			serviceInfo.append("\nclientCount: ").append(si.clientCount);
			serviceInfo.append("\nactiveSince: ").append(ToolHelper.formatData(si.activeSince));
			serviceInfo.append("\nlastActivityTime: ").append(ToolHelper.formatData(si.lastActivityTime));
			serviceInfo.append("\n\n");
		}
		return serviceInfo.toString();
	}
	
	//RunningServicesInfo
	public static String getRunningTasksInfo(Context context) {
		StringBuffer sInfo = new StringBuffer();
		final ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = activityManager.getRunningTasks(100);
		Iterator<RunningTaskInfo> l = tasks.iterator();
		while (l.hasNext()) {
			RunningTaskInfo ti = (RunningTaskInfo) l.next();
			sInfo.append("id: ").append(ti.id);
			sInfo.append("\nbaseActivity: ").append(ti.baseActivity.flattenToString());
			sInfo.append("\nnumActivities: ").append(ti.numActivities);
			sInfo.append("\nnumRunning: ").append(ti.numRunning);
			sInfo.append("\ndescription: ").append(ti.description);
			sInfo.append("\n\n");
		}
		return sInfo.toString();
	}
	
 
	
//	//getPowerInfo
//	public static String getPowerInfo(Context context) {
//		StringBuffer sInfo = new StringBuffer();
//		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//		 PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
//		 wl.acquire();
////		   ..screen will stay on during this section..
//		 wl.release();
//
//		return sInfo.toString();
//	}


}

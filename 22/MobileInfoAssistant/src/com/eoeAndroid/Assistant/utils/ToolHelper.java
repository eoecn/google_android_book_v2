package com.eoeAndroid.Assistant.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

public class ToolHelper {
	private static final String TAG = "ToolHelper";
	private static StringBuffer buffer;

	// humanizeFileSize
	protected String humanizeFileSize(int bytes) {
		String file_size;
		double file_size_k;
		double file_size_m;

		file_size_k = bytes / 1024;
		file_size_m = file_size_k / 1024;

		if (Math.round(file_size_m) > 0) {
			file_size = String.valueOf(file_size_m) + "m";
		} else if (Math.round(file_size_k) > 0) {
			file_size = String.valueOf(file_size_k) + "k";
		} else {
			file_size = String.valueOf(bytes) + "b";
		}

		return file_size;
	}

	public void download_apk(Activity act) throws URISyntaxException {
		FileOutputStream out;
		URL u;
		BufferedReader in;
		try {
			out = act.openFileOutput("Note_Pad.apk", 0);
			u = new URL("http://undroid.net/repo/packages/Note_Pad.apk");
			in = new BufferedReader(new InputStreamReader(u.openStream()));
			int buf = 0;
			while ((buf = in.read()) >= 0) {
				out.write((char) buf);
			}
			out.flush();
			out.close();
			in.close();

		} catch (Exception e) {
			Log.d(TAG, "Oops");
		}
	}

	public static void copy_apk(Activity act, String _sourceDir,
			String packageName) {
		String path = "/sdcard/imapps/backup/";
		File imapp = new File(path);
		if (!imapp.exists())
			imapp.mkdirs();
		FileOutputStream out;
		BufferedReader in;
		try {
			out = act.openFileOutput("bak_" + packageName, 0);
			// FileWriter f = new FileWriter(path+packageName);
			in = new BufferedReader(new FileReader(_sourceDir));
			int buf = 0;
			while ((buf = in.read()) >= 0) {
				out.write((char) buf);
			}
			out.flush();
			out.close();
			in.close();

		} catch (Exception e) {
			Log.d(TAG, "Exception:e=" + e.toString());
		}
		// FileOutputStream fOut=null;
		// fOut= new FileOutputStream("/data/imsc/cfg/wrapper.cfg");
		// OutputStreamWriter osw = null;
		// osw = new OutputStreamWriter(fOut);
		// osw.write(data);
	}

	public static void install_apk(Activity act, String apk_path) {
		// apk_path =
		// "file:///data/data/com.bitsetters.undroid/files/Note_Pad.apk"
		Uri apk = Uri.parse(apk_path);
		
		//act.getPackageManager().installPackage(apk);
		// PackageManager.installPackage(apk);
	}

	// 显示网络上的图片
	public static Bitmap returnBitMap(String url) {
		Log.i("returnBitMap", "url=" + url);
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	// Install APK file
	public static Intent install_apk() {
		Uri installUri = Uri.fromParts("package", "xxx", null);
		Intent returnIt = new Intent(Intent.ACTION_PACKAGE_ADDED, installUri);
		return returnIt;
	}

	// UnInstall APK file
	public static Intent uninstall_apk() {
		Uri uninstallUri = Uri.fromParts("package", "xxx", null);
		Intent returnIt = new Intent(Intent.ACTION_DELETE, uninstallUri);
		return returnIt;
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
		return memoryInfo.toString();
	}
	


	/**
	 * 系统信息查看方法
	 */
	public static String getSystemProperty() {
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

	// formart data long to string
	public static String formatData(long data) {
		Date d = new Date(data);
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format2.format(d);
		return str;
	}

	// upload file to server
	public static void doFileUpload(String file_path) {
		Log.i("doFileUpload", "file_path="+file_path);
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		DataInputStream inStream = null;

		String exsistingFileName = file_path;
		// Is this the place are you doing something wrong.

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		String responseFromServer = "";
		String urlString = "http://api.yobo.com/index";

		try {
			// ------------------ CLIENT REQUEST
			Log.i("doFileUpload", "Inside second Method");
			FileInputStream fileInputStream = new FileInputStream(new File(
					exsistingFileName));
			// open a URL connection to the Servlet
			URL url = new URL(urlString);
			// Open a HTTP connection to the URL
			conn = (HttpURLConnection) url.openConnection();
			// Allow Inputs
			conn.setDoInput(true);
			// Allow Outputs
			conn.setDoOutput(true);
			// Don't use a cached copy.
			conn.setUseCaches(false);
			// Use a post method.
			Log.i("doFileUpload", "init ok,set header...");
			
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			
			Log.i("doFileUpload", "set header ok..");
			
			dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos
					.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
							+ exsistingFileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			Log.i("doFileUpload", "Headers are written");

			// create a buffer of maximum size

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// close streams
			Log.e("doFileUpload", "File is written");
			fileInputStream.close();
			dos.flush();
			dos.close();
		} catch (MalformedURLException ex) {
			Log.e("doFileUpload", "error: " + ex.getMessage(), ex);
		} catch (IOException ioe) {
			Log.e("doFileUpload", "error: " + ioe.getMessage(), ioe);
		}
		// ------------------ read the SERVER RESPONSE

		try {
			inStream = new DataInputStream(conn.getInputStream());
			String str;
			while ((str = inStream.readLine()) != null) {
				Log.e("doFileUpload", "Server Response" + str);
			}
			inStream.close();

		} catch (IOException ioex) {
			Log.e("doFileUpload", "error: " + ioex.getMessage(), ioex);
		}

	}
	
	
	public static String formatMemorySize(long size) {
		String suffix = null;
 
		if (size >= 1024) {
			suffix = "KB";
			size /= 1024;
			if (size >= 1024) {
				suffix = "MB";
				size /= 1024;
			}
		}
 
		StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
 
		int commaOffset = resultBuffer.length() - 3;
		while (commaOffset > 0) {
			resultBuffer.insert(commaOffset, ',');
			commaOffset -= 3;
		}
 
		if (suffix != null)
			resultBuffer.append(suffix);
		return resultBuffer.toString();
	}
}

package com.eoeAndroid.Assistant.utils;

import android.view.Menu;

//相关配置信息
public class PreferencesUtil {
	public static final int VER_INFO = Menu.FIRST + 1;
	public static final int CPU_INFO = VER_INFO + 1;
	public static final int MEM_INFO = CPU_INFO + 1;
	public static final int DISK_INFO = MEM_INFO + 1;
	public static final int NET_CONFIG = DISK_INFO + 1;
	public static final int NET_STATUS = NET_CONFIG + 1;
	public static final int MOUNT_INFO = NET_STATUS + 1;
	public static final int DMESG_INFO = MOUNT_INFO + 1;
	public static final int TEL_STATUS = DMESG_INFO + 1;
	public static final int SystemProperty = TEL_STATUS + 1;
	public static final int DisplayMetrics = SystemProperty + 1;
	public static final int RunningProcesses = DisplayMetrics + 1;
	public static final int RunningService = RunningProcesses + 1;
	public static final int RunningTasks = RunningService + 1;
	public static final int MemoryStatus = RunningTasks + 1;
	public static final int BatteryLevel = MemoryStatus + 1;
	
}

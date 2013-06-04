package com.eoeandroid.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HelloBroadcastReceiver extends BroadcastReceiver {
	
	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		showNotification(intent);
	}

	private void showNotification(Intent intent) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification(R.drawable.ic_launcher,
				intent.getExtras().getString("content"),
				System.currentTimeMillis());

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, MainActivity.class), 0);
		
		notification.setLatestEventInfo(context,
				intent.getExtras().getString("content"), null, pendingIntent);
		
		notificationManager.notify(R.layout.main, notification);
	}
}

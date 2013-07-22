package com.example.teselayout;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyNotify extends Activity {
    
    Button btn1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifmain);
        btn1 = (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new Notification(R.drawable.icon,
                        "我的通知", System.currentTimeMillis());
                PendingIntent pendingIntent = PendingIntent.getActivity(
                        MyNotify.this, 0, new Intent(MyNotify.this,
                                MyNotify.class), 0);
                notification.setLatestEventInfo(getApplicationContext(),
                        "通知标题", "这是一个新的通知", pendingIntent);
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.defaults |= Notification.DEFAULT_SOUND;
                manager.notify(0, notification);
            }
        });

    }
}

package com.usv.yzzkao;


import com.usv.activity.MainActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class alarm_notifycation extends BroadcastReceiver{

	private PendingIntent pi;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		
		Intent  intent2 =new Intent(context,MainActivity.class);
		pi = PendingIntent.getActivity(context, 0, intent2, 0);
		
		
		NotificationCompat.Builder mBuilder =
	            new NotificationCompat.Builder(context)
	            .setSmallIcon(R.drawable.symbol7272)
	            .setContentIntent(pi)
	            .setWhen(System.currentTimeMillis())
	            .setContentTitle("Ӯ���п�")
	            .setContentText("��������Ѿ���ʼ������ʲô���ɲ�����!");
		

	    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	    mNotificationManager.notify(1, mBuilder.build());
		
		
	}

}

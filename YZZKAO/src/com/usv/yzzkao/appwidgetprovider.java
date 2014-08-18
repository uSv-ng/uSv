package com.usv.yzzkao;

import com.usv.activity.MainActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

public class appwidgetprovider extends AppWidgetProvider{

	

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		for(int i=0;i<appWidgetIds.length;i++){
			//创建一个Intent 对象
			Intent  intent = new Intent(context,MainActivity.class);
			//创建一个PendingIntnet对象
			PendingIntent pendingintent= PendingIntent.getActivity(context, 0, intent, 0);
			//创建RemoteViews 对象，RemoteViews指的就是在HomeScreen上面的widget
			RemoteViews remoteviews = new RemoteViews(context.getPackageName(),R.layout.widgetview);
			//设置RemoteViews的Onclick方法，指的是如果RemoteViews中有Onclick动作，执行pendingintent方法。
			remoteviews.setOnClickPendingIntent(R.id.qiandao, pendingintent);
			//更新AppWidget
			//第一个参数用来指定被更新AppWidget的Id
			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteviews);
		}
		
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

}

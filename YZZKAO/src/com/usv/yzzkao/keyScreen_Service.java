package com.usv.yzzkao;

import com.usv.activity.lock_screen;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class keyScreen_Service  extends Service{


	private BroadcastReceiver receiver;
	
	Intent startIntent=null;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		
		
		return null;
	}
	
	
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		System.out.println("yours service is running !!!");
		
		
		
		IntentFilter filter=new IntentFilter();

        //filter.addAction(Intent.ACTION_SCREEN_ON);//声明当屏幕打开的时候动作

        filter.addAction(Intent.ACTION_SCREEN_OFF);//声明当屏幕关闭的时候动作
       
        //声明广播接收器
        receiver = new BroadcastReceiver() {
			//声明onReceive方法
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub

				System.out.println(" receive yours broadcast!!!");
				
		        String action=intent.getAction();  

		        System.out.println("action is "+action);  

		        Intent lockIntent=new Intent(context,lock_screen.class);   

		        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  

		        context.startActivity(lockIntent);  

			}
		};

        registerReceiver(receiver, filter);//注册一个广播第一个参数为 广播，第二个参数为过滤器
        
        
		
		super.onStart(intent, startId);
	}


	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);  
        
	}
	
	
	 

}

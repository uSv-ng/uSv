package com.usv.yzzkao;



import com.tencent.stat.StatService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

public class networkchangeReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		 State wifiState = null;  
	        State mobileState = null;  
	        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
	        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();  
	        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();  
	        if (wifiState != null && mobileState != null  
	                && State.CONNECTED != wifiState  
	                && State.CONNECTED == mobileState) {
	        	
	        
	        	
	        	
	        	System.out.println(" �ֻ��������ӳɹ����������������������������� ");
	            // �ֻ��������ӳɹ�  
	        } 
	        else if (wifiState != null && mobileState != null  
	                && State.CONNECTED != wifiState  
	                && State.CONNECTED != mobileState) {  
	        	
	        	System.out.println("�ֻ�û���κ������磡������������������������������");
	            // �ֻ�û���κε�����  
	        }
	        else if (wifiState != null && State.CONNECTED == wifiState) {  
	        	
	        	System.out.println("wife���ӳɹ�����������������������������");
	        	
	            // �����������ӳɹ�  
	        }  
	  
		
	}

}

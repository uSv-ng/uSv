package com.usv.yzzkao;

import java.util.Calendar;

import com.usv.activity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

public class isfrist extends Activity{

	
	public static int MODE=MODE_PRIVATE;
	
	public static final String PREFERENCE_NAME="saveisfrist";
	
	public SharedPreferences  spisfirst;
	
	public Calendar c;
	private int mYear, mMonth, mDay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.isfrist);
		
		
		//实例化日历类，用于设置年月
		c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH)+1;
		mDay = c.get(Calendar.DAY_OF_MONTH);

		
		String yearstr = mYear+"";
		String monrthstr = chagecalendr(mMonth);
		String daystr    = chagecalendr(mDay);
		String str  = yearstr+monrthstr+daystr;
		
		spisfirst=getSharedPreferences(PREFERENCE_NAME, MODE);		
		Editor   editor = spisfirst.edit();
		
		if(spisfirst.contains("frist")){
			Intent intent_Mainactivity = new Intent();
			intent_Mainactivity.setClass(this, MainActivity.class);
			startActivity(intent_Mainactivity);
			finish();
		}
		else{
			
			
			editor.putString("frist", str);
			editor.commit();
			
			Intent intent_Welcomeactivity = new Intent();
			intent_Welcomeactivity.setClass(this, welcome.class);
			startActivity(intent_Welcomeactivity);
			
			System.out.println(str+"!!!!!!!!!!!!!!!!!!!");
			
			finish();
			
		}
	}

	public String chagecalendr(int i){
		String str;
		
		if(i<10){
			str= "0"+i;
		
		}
		else{
			str=i+"";
		}
		
		
		return str;
		
	}

	
	
}

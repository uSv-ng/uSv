package com.usv.activity;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.usv.yzzkao.R;
import com.usv.yzzkao.ViewPagerAdapter;
import com.usv.yzzkao.R.id;
import com.usv.yzzkao.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class lock_screen extends Activity{

 
	
	
	private ViewPager lockviewpager = null;
    private List<View> locklist = null;
    
    private TextView systemtime;
    private TextView lockScreen_Date;
    public Handler handler = new Handler() {
	};
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		
		final Calendar c = Calendar.getInstance();  
	     Time t=new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�  
	     t.setToNow(); // ȡ��ϵͳʱ�䡣  
	     int month = t.month+1;  //ϵͳ�ڲ�����month�Ǵ�0��ʼ��
	     int date = t.monthDay;  //��ȡ����
	     int hour = t.hour; // 0-23  
	     int minute = t.minute;  //��ȡʱ��
	     
	     String mWay= String.valueOf(c.get(Calendar.DAY_OF_WEEK)); //��ȡ���ڼ�
         
	     
			systemtime = (TextView)findViewById(R.id.systemtime);
			lockScreen_Date = (TextView)findViewById(R.id.lockScreen_Date);
			
		     
		     
		     lockScreen_Date.setText(month+"��"+date+"��"+" "+"��"+changeWeekday(mWay));//��ʾ��ǰxx��xx�� ��x
		     
		     systemtime.setText(hour+":"+changetime_tozero(minute));//��ʾ��ǰʱ��
		
		
		super.onStart();
	}
	
	
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);  
		
		
		setContentView(R.layout.lock_screen);
		
	
		final Calendar c = Calendar.getInstance();  
		
		lockviewpager = (ViewPager)findViewById(R.id.lock_screen_viewpager);//����viewpager
		
		
		 locklist = new ArrayList<View>();
		 
		 locklist.add(getLayoutInflater().inflate(R.layout.lockviewpager1, null));
	     locklist.add(getLayoutInflater().inflate(R.layout.lockviewpager2, null));
	     
	     
	     lockviewpager.setAdapter(new ViewPagerAdapter(locklist));
	     lockviewpager.setOnPageChangeListener(new ViewPagerPageChangeListener());
	     
	     
	     
	}
	
	class ViewPagerPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		
		}

		@Override
		public void onPageSelected(int pager) {
			// TODO Auto-generated method stub
			
			
			switch (pager) {
			case 1:
				finish();
				
			break;
			
		}
	
	}
	}

	//��С��10������ǰ���0
	public String changetime_tozero(int i){
		
		String str;
		if(i<10){
			str="0"+i;
		}
		else{
			str=i+"";
		}
		
		
		return str;
		
	}
	//���ܼ����ֱ������
	public String changeWeekday(String mWay){
		
		  if("1".equals(mWay)){  
	            mWay ="��";  
	        }else if("2".equals(mWay)){  
	            mWay ="һ";  
	        }else if("3".equals(mWay)){  
	            mWay ="��";  
	        }else if("4".equals(mWay)){  
	            mWay ="��";  
	        }else if("5".equals(mWay)){  
	            mWay ="��";  
	        }else if("6".equals(mWay)){  
	            mWay ="��";  
	        }else if("7".equals(mWay)){  
	            mWay ="��";  
	        }  
		
		
		return mWay;
		
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("onDestroy---------------------!!!!!!!!!!!!!!!");
		super.onDestroy();
	}
	
	

  
	
	
	
}

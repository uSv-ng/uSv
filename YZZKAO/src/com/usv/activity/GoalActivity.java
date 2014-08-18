package com.usv.activity;




import com.usv.yzzkao.R;
import com.usv.yzzkao.keyScreen_Service;
import com.usv.yzzkao.R.anim;
import com.usv.yzzkao.R.drawable;
import com.usv.yzzkao.R.id;
import com.usv.yzzkao.R.layout;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class GoalActivity extends Activity  {

	private GoalActivity main = null;
	private TextView tv;
	private TextView tg;
	private Button back;
	private Button writeyoursdream;
	public static int MODE = MODE_PRIVATE;//�������ģʽΪ˽��ģʽ
    public static final String PREFERENCE_NAME = "saveInfo2";//���ñ���ʱ���ļ�������
	
    
    public keyScreen_Service lock_service;
    
    private ToggleButton lock_button=null;
    
    public SharedPreferences  sp2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goal);
		View view = (View)findViewById(R.id.goalLayoutId);
		
		tv=(TextView)findViewById(R.id.tv);//����textview
		tg= (TextView)findViewById(R.id.textView4);//����EditText
		writeyoursdream = (Button)findViewById(R.id.setmengxiang);//¼���Լ����������ʱ���button
		back = (Button)findViewById(R.id.back);
		
		
		AssetManager mgr=getAssets();//�õ�AssetManager
		Typeface tf=Typeface.createFromAsset(mgr, "fonts/DroidSans.ttf");//����·���õ�Typeface
		tv.setTypeface(tf);//��������    
		tg.setTypeface(tf);
		
		lock_service = new keyScreen_Service();
		
		
		
		lock_button = (ToggleButton)findViewById(R.id.toggleButton1);
		lock_button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(buttonView.isChecked()){
					startService(new Intent(getBaseContext(), keyScreen_Service.class));//����lockScreenService
			
				}
				if(buttonView.isChecked()==false){
					stopService(new Intent(getBaseContext(), keyScreen_Service.class));
				}
				
			}
		});
		
		
		
		
		
		
		
		
		
		
		
		buttonlistener  listener = new buttonlistener();
		writeyoursdream.setOnClickListener(listener);
		
		
		back.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.in_alpha_scale, R.anim.push_right_out);
				
			}
		});
		
		
		
		
		
		
		sp2 = getSharedPreferences(PREFERENCE_NAME, MODE);
		
		
		if(sp2.contains("time")&&sp2.contains("gaole")){
			tv.setText(sp2.getString("time", "�������Ͻ���д����п�ʱ���������룡"));
			tg.setText(sp2.getString("gaole", ""));
		}
		
	}


	
	

	
	//����ʱ��������dialog
	public class  buttonlistener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			
		final	TimePicker tp;
		final	EditText et;
			
			AlertDialog.Builder builder = new AlertDialog.Builder(GoalActivity.this); 
			LayoutInflater factory = LayoutInflater.from(GoalActivity.this);  
			 final View textEntryView = factory.inflate(R.layout.setdate, null); 
			 
			 tp= (TimePicker)textEntryView.findViewById(R.id.getup_time);
			 et= (EditText)textEntryView.findViewById(R.id.yours_dream);
				
				tp.setIs24HourView(true);
			 
			 
			 builder.setIcon(R.drawable.symbol7272);  
		     builder.setTitle("д���������ʱ�������");  
		     builder.setView(textEntryView); 
			
		     builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					
					SharedPreferences.Editor editor = sp2.edit();
					editor.putString("time", tp.getCurrentHour()+":"+currenminute(tp.getCurrentMinute()));
					editor.putString("gaole", et.getText()+"");
					editor.commit();
					
					AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
					
					Intent intent = new Intent();
					
					intent.setAction("com.usv.alarm_notifycation_test_ACTION");
					
					PendingIntent sender = PendingIntent.getBroadcast(GoalActivity.this, 0, intent,
							PendingIntent.FLAG_CANCEL_CURRENT);
					
					
					Time t=new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�  
				     t.setToNow(); // ȡ��ϵͳʱ�䡣  
				     
				     
				     int hour = t.hour; // 0-23  
				     int minute = t.minute;  //��ȡʱ��
				     int number;
				     
				     int hourtp=tp.getCurrentHour();
				     int minutetp=tp.getCurrentMinute();
				     int number2;
				     
				     
				     hour=hour*60;
				     minute=minute+hour;
				     number=minute*60*1000;
				     
				     hourtp=hourtp*60;
				     minutetp=hourtp+minutetp;
				     number2 = minutetp*60*1000;
					
					System.out.println(timeNumber(number2,number));
					
				     am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+timeNumber(number2,number), 86400000, sender); 		
				     
					
					tv.setText(tp.getCurrentHour()+":"+currenminute(tp.getCurrentMinute()));
					tg.setText(et.getText()+"");
				}
			}); 
		    builder.show();  
			
		}	
		
	}
	
	
	//��С��ʮ�ķ��ӣ�ǰ�����
	public  String   currenminute(int i){
		String str=null;
		if(i<10){
			str="0"+i;
		}
		else{
			str=i+"";
		}
		return str;
	}
	
	
	
	public int timeNumber(int slectTime ,int systemTime ){
		 int i=0 ;
		
		
		if(slectTime<systemTime){
			
			i=24*60*60*1000-systemTime+slectTime;
			
		}
		else{
			i=slectTime-systemTime;
		}
		
		return i;
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		overridePendingTransition(R.anim.in_alpha_scale, R.anim.push_right_out);
		return super.onKeyDown(keyCode, event);
	}
	
}

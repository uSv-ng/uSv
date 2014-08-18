package com.usv.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.usv.data.ZKKPData;
import com.usv.yzzkao.R;
import com.usv.yzzkao.R.anim;
import com.usv.yzzkao.R.id;
import com.usv.yzzkao.R.layout;
import com.usv.yzzkao.R.raw;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CSActivity extends Activity {
	
	public static int mode=MODE_PRIVATE;
	public static final String name="ChineseSencence";
	public SharedPreferences CSData;
	Editor editor;
	String s = null;
	public static boolean isInit = false;
	private ZKKPData Zkkdata;
	private SQLiteDatabase db;
	private String sentence;
	
	private TextView wordText,numberText;
	private Button beforeButton, nextButton;
	private Button   KP_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cs);
		
		wordText = (TextView)findViewById(R.id.chineseSentenceId);
		numberText = (TextView)findViewById(R.id.chinesenumbertextId);
		beforeButton = (Button)findViewById(R.id.pre);
		beforeButton.setOnClickListener(new BeforeListener());
		nextButton = (Button)findViewById(R.id.nex);
		nextButton.setOnClickListener(new NextListener());
		
		
		KP_back =  (Button)findViewById(R.id.chinese_back);
		KP_back.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.in_alpha_scale, R.anim.push_right_out);
			}
		});
		initApp(this);
		Zkkdata = new ZKKPData(this);     
        db = Zkkdata.getReadableDatabase();
		CSData = getSharedPreferences(name, mode);
		editor = CSData.edit();
		s = CSData.getString("number", "");

		
		if(s.equals("1")) {
//			beforeButton.setVisibility(View.GONE);
		}
		if(s.equals("70")) {
//			nextButton.setVisibility(View.GONE);
		}
		if(s.equals("")) {
			s = "1";
			editor.putString("number", "1");
			editor.commit();
			settext(s);
		}
		else {
			String sql ="select * from  CHINESE where id=" + s;
			settext(s);
		}
		
	}
	
	public class BeforeListener implements OnClickListener  {
		@Override
		public void onClick(View v) {
//			nextButton.setVisibility(View.VISIBLE);
			int n = Integer.valueOf(s) - 1;
			if(n>0) {
				s = n + "";
				settext(s);
				editor.putString("number", s);
				editor.commit();
				if(1==n) {
//					beforeButton.setVisibility(View.GONE);
				}
			}
			else {
//				beforeButton.setVisibility(View.GONE);
			}
		}
	}
	public class NextListener implements OnClickListener  {
		@Override
		public void onClick(View v) {
//			beforeButton.setVisibility(View.VISIBLE);
			int n = Integer.valueOf(s) + 1;
			if(n<71) {
				s = n + "";
				settext(s);
				editor.putString("number", s);
				editor.commit();
				
				if(70==n) {
//					nextButton.setVisibility(View.GONE);
				}
			}
			else {
//				nextButton.setVisibility(View.GONE);
			}
		}
		
	}
	
	public static boolean initApp(Context context)
	{
		if(isInit) return true;
	
		isInit=initDb(context);
		return isInit;
	}
	public static boolean initDb(Context context)
	{
			try{
				if(!new File("/sdcard/YZZK/zkzsd.db").exists()) {
					File tempFile1=new File("/sdcard/YZZK");
					if (!tempFile1.isDirectory()) {
						tempFile1.mkdirs();
					}
					InputStream is=context.getResources().openRawResource(R.raw.zkzsd);
					File file = new File("/sdcard/YZZK/zkzsd.db");
					if (file != null) {
						FileOutputStream fos=new FileOutputStream(file);
						byte buffer[]=new byte[8192];
						int count=0;
						while((count=is.read(buffer))>0)
						{
							fos.write(buffer, 0, count);
						}	
						fos.close();
						is.close();
					}
				}
				return true;
			} catch(Exception e){
				e.printStackTrace();
				return false;
			}	
	}
	
	public void settext(String string) {
		String sql ="select * from  CHINESE where id=" + string;
		Cursor c =Zkkdata.OnSelect(sql);
        while(c.moveToNext()) {
			sentence = c.getString(c.getColumnIndex("mj")).toString();
			wordText.setText(sentence);
		}
        string = string + "/70";
		numberText.setText(string);
        c.close();
		Zkkdata.CloseDB();
	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		overridePendingTransition(R.anim.in_alpha_scale, R.anim.push_right_out);
		return super.onKeyDown(keyCode, event);
	}
	
	
}

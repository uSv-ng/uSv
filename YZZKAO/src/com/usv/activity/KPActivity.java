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
import android.content.Intent;
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

public class KPActivity extends Activity {
	
	public static int mode=MODE_PRIVATE;
	public static final String name="savewordnumber";
	public SharedPreferences wordData;
	Editor editor;
	String s = null;
	public static boolean isInit = false;
	private ZKKPData Zkkdata;
	private SQLiteDatabase db;
	private String word, mean;
	
	private TextView wordText, meanText, numberText;
	private Button beforeButton, nextButton;
	private Button   KP_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kp);
		
		TextView title = (TextView) findViewById(R.id.englishTitleId);
		title.setText("Ó¢Óïµ¥´Ê");
		
		wordText = (TextView)findViewById(R.id.english_word);
		meanText = (TextView)findViewById(R.id.chinese_mean);
		numberText = (TextView)findViewById(R.id.numberId);
		beforeButton = (Button)findViewById(R.id.before);
		beforeButton.setOnClickListener(new BeforeListener());
		nextButton = (Button)findViewById(R.id.next);
		nextButton.setOnClickListener(new NextListener());
		
		
		KP_back =  (Button)findViewById(R.id.english_back);
		
		
		KP_back.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				
				finish();
				overridePendingTransition(R.anim.in_alpha_scale, R.anim.push_right_out);
				
			}
		});
		
		
		
		
		initApp(this);
		Zkkdata = new ZKKPData(this);     
        db = Zkkdata.getReadableDatabase();
		wordData = getSharedPreferences(name, mode);
		editor = wordData.edit();
		s = wordData.getString("number", "");

		
		if(s.equals("1")) {
//			beforeButton.setVisibility(View.GONE);
		}
		if(s.equals("2170")) {
//			nextButton.setVisibility(View.GONE);
		}
		if(s.equals("")) {
			s = "1";
			editor.putString("number", "1");
			editor.commit();
			settext(s);
		}
		else {
			String sql ="select * from  ENGLISHw where id=" + s;
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
			if(n<2171) {
				s = n + "";
				settext(s);
				editor.putString("number", s);
				editor.commit();
				
				
				
				
				
				if(2170==n) {
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
		String sql ="select * from  ENGLISHw where id=" + string;
		Cursor c =Zkkdata.OnSelect(sql);
        while(c.moveToNext()) {
			word = c.getString(c.getColumnIndex("word")).toString();
			mean = c.getString(c.getColumnIndex("meaning")).toString();
	        int i = word.length();
	        if(i>17) {wordText.setTextSize(28);}
	        if(i<=17) {wordText.setTextSize(35);}
			wordText.setText(word);
			meanText.setText(mean);
		}
        string = string + "/2170";
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

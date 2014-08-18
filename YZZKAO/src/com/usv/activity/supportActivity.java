package com.usv.activity;

import com.usv.yzzkao.R;
import com.usv.yzzkao.R.anim;
import com.usv.yzzkao.R.id;
import com.usv.yzzkao.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class supportActivity extends Activity{
	private Button button_back;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_support);
		
		
		 WebView wView = (WebView)findViewById(R.id.support_view);     
	     WebSettings wSet = wView.getSettings();     
	     wSet.setJavaScriptEnabled(true);  
	     wView.loadUrl("file:///android_asset/html/support.html");
		
	     
	     
	     button_back  = (Button)findViewById(R.id.support_back);
	     button_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();
				overridePendingTransition(R.anim.in_alpha_scale, R.anim.push_right_out);
			}
		});
	     
	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		overridePendingTransition(R.anim.in_alpha_scale, R.anim.push_right_out);
		return super.onKeyDown(keyCode, event);
	}
	

}

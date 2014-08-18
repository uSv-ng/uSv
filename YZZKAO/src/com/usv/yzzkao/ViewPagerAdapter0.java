package com.usv.yzzkao;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewPagerAdapter0 extends PagerAdapter{

	private static final int MODE_PRIVATE = 0;
	private List<View> list = null;
	 private WebView lofterview;
	 
	 public static int mode=MODE_PRIVATE;
	 public static final String name="savewordnumber";
	 
	 public SharedPreferences wordData2;
	 
	
	 
	 
     public ViewPagerAdapter0(List<View> list) {
         this.list = list;
     }

     @Override
     public int getCount() {
         return list.size();
     }

     @Override
     public Object instantiateItem(ViewGroup container, int position) {
    	 
    	 
    	 switch (position) {
		case 0:
			
			lofterview = (WebView)list.get(position).findViewById(R.id.lofter_view);
			
			
			lofterview.loadUrl("http://usvstudio.diandian.com/");
			lofterview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			
			
			
			break;

			
		case 2:
			
			
			
			break;
			
		}
    	 
    	 container.addView(list.get(position));
         return list.get(position);
     }



	@Override
     public void destroyItem(ViewGroup container, int position, Object object) {
         container.removeView(list.get(position));
     }

     @Override
     public boolean isViewFromObject(View arg0, Object arg1) {
         return arg0 == arg1;
     }

   
 }

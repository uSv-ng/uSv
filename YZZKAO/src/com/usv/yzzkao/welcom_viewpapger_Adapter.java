package com.usv.yzzkao;

import java.util.List;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class welcom_viewpapger_Adapter extends PagerAdapter{

	private List<View> list = null;

	 public welcom_viewpapger_Adapter(List<View> list) {
         this.list = list;
     }

     @Override
     public int getCount() {
         return list.size();
     }

     @Override
     public Object instantiateItem(ViewGroup container, int position) {
    	 
    	 switch(position){
    	 case 0:
    		 System.out.println("初始化第一个界面--------!!!!!");
    		 
    		 
    		 break;
    		 
    	 case 1:
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

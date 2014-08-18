package com.usv.yzzkao;

import java.util.ArrayList;
import java.util.List;

import com.usv.activity.MainActivity;




import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class welcome extends Activity {

    private ViewPager viewpager = null;
    private List<View> list = null;
    
    private ImageView imagepoint0,imagepoint1,imagepoint2;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_main);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        
        imagepoint0 = (ImageView)findViewById(R.id.imageview1);
        imagepoint1 = (ImageView)findViewById(R.id.imageview2);
        imagepoint2 = (ImageView)findViewById(R.id.imageview3);
        
        
        list = new ArrayList<View>();
        list.add(getLayoutInflater().inflate(R.layout.viewpager1, null));
        list.add(getLayoutInflater().inflate(R.layout.viewpager2, null));
        list.add(getLayoutInflater().inflate(R.layout.viewpager3, null));


        viewpager.setAdapter(new welcom_viewpapger_Adapter(list));
        viewpager.setOnPageChangeListener(new ViewPagerPageChangeListener());
        
    }
    
    
    class ViewPagerPageChangeListener implements OnPageChangeListener {

        /*
         * state：网上通常说法：1的时候表示正在滑动，2的时候表示滑动完毕了，0的时候表示什么都没做，就是停在那；
         * 我的认为：1是按下时，0是松开，2则是新的标签页的是否滑动了
         * (例如：当前页是第一页，如果你向右滑不会打印出2，如果向左滑直到看到了第二页，那么就会打印出2了)；
         * 个人认为一般情况下是不会重写这个方法的
         */
        @Override
        public void onPageScrollStateChanged(int state) {
        	
        	System.out.println("onPageScrollStateChanged----------->"+state);
        	
        	
        }

        /*
         * page：看名称就看得出，当前页； positionOffset：位置偏移量，范围[0,1]；
         * positionoffsetPixels：位置像素，范围[0,屏幕宽度)； 个人认为一般情况下是不会重写这个方法的
         */
        @Override
        public void onPageScrolled(int page, float positionOffset,
                int positionOffsetPixels) {
        	
        	
        	
        }

        @Override
        public void onPageSelected(int page) {
            //更新图标
        	
        	switch(page){
        	case 0:
        		imagepoint0.setBackgroundResource(R.drawable.page_indicator_focused);
        		imagepoint1.setBackgroundResource(R.drawable.page_indicator_unfocused);
        		imagepoint2.setBackgroundResource(R.drawable.page_indicator_unfocused);
        		
        	break;
        	
        	case 1:
        		imagepoint0.setBackgroundResource(R.drawable.page_indicator_unfocused);
        		imagepoint1.setBackgroundResource(R.drawable.page_indicator_focused);
        		imagepoint2.setBackgroundResource(R.drawable.page_indicator_unfocused);
        	break;
        	
        	case 2:
        	imagepoint0.setBackgroundResource(R.drawable.page_indicator_unfocused);
    		imagepoint1.setBackgroundResource(R.drawable.page_indicator_unfocused);
    		imagepoint2.setBackgroundResource(R.drawable.page_indicator_focused);
    		
    		
    		Button jinru = (Button)findViewById(R.id.jinru);
    		
    		jinru.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent  = new Intent();
					intent.setClass(welcome.this, MainActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.out_alpha_scale);
					
					finish();
				}
			});
    		
        	break;
        	}
        	
        }
    }
    
    
  
	
}



package com.usv.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.usv.data.YZZKDataBase;
import com.usv.yzzkao.R;
import com.usv.yzzkao.R.anim;
import com.usv.yzzkao.R.drawable;
import com.usv.yzzkao.R.id;
import com.usv.yzzkao.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class GradeActivity extends Activity{
	
	private ListView gradeList;
	private TextView nameText, scoreText, rankingText, dateText;
	private EditText nameEdit, scoreEdit, rankingEdit;
	private ImageView tendencyImage;
	private Button addButton;
	private View view = null;
	private Button back_button=null;
	
	private int screenWidth;  
	private int screenHeight; 
	
	private PopupWindow mPopupWindow1=null;
	
	String preranking = "meiyou";
	String shanchu = null;
	
	private YZZKDataBase YZZKDB;
	private SQLiteDatabase sdbw, sdbr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grade);
		
		
		WindowManager windowManager = getWindowManager();  
		Display display = windowManager.getDefaultDisplay();  
		screenWidth = display.getWidth();  
		screenHeight = display.getHeight();  
		
		
		back_button  = (Button)findViewById(R.id.chengji_back);
		back_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.in_alpha_scale, R.anim.push_right_out);
			}
		});
		
		YZZKDB = new YZZKDataBase(this);
		sdbw = YZZKDB.getWritableDatabase();
		sdbr = YZZKDB.getReadableDatabase();
		String sql = "create table if not exists CHENGJI (name varchar(30), score varchar(10), ranking varchar(10), date varchar(10), tendency varchar(5))";
		sdbw.execSQL(sql);
		
		gradeList = (ListView) findViewById(R.id.listofgradeId);
		nameText = (TextView) findViewById(R.id.nameTextId);
		scoreText = (TextView) findViewById(R.id.scoreTextId);
		rankingText = (TextView) findViewById(R.id.rankingTextId);
		addButton = (Button) findViewById(R.id.addChengjiId);
		
		addButton.setOnClickListener(new AddButtonListener());
		gradeList.setOnItemClickListener(new ListListener());
		
		SetListView();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		overridePendingTransition(R.anim.in_alpha_scale, R.anim.push_right_out);
		return super.onKeyDown(keyCode, event);
	}
	
	public class ListListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int arg2,
				long arg3) {
			TextView textView = (TextView) view.findViewById(R.id.nameTextId);
			shanchu = textView.getText().toString();
			getPopupWindowInstance1();  
			//设置popupwindow的位置，在view正上方
			int[] location = new int[2];  
			view.getLocationOnScreen(location);  
	        mPopupWindow1.showAtLocation(view, Gravity.NO_GRAVITY, view.getWidth()/2, location[1]-mPopupWindow1.getHeight());
			
		}
		
	}
	
	public class AddButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Cursor cursor = sdbr.query("CHENGJI", new String[]{"ranking"},  null, null, null, null, null);
			while(cursor.moveToNext()) {
				String ranking = cursor.getString(cursor.getColumnIndex("ranking"));
			}
			AddOkListener addok = new AddOkListener();
			LayoutInflater inflater = (LayoutInflater) GradeActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.chengji_tianjia_dialog, null);
			new AlertDialog.Builder(GradeActivity.this)
			 	.setIcon(R.drawable.symbol7272) 
			   .setTitle("赶快录入自己的成绩吧")
			   .setView(view)
			   .setPositiveButton("录入成绩", addok)
			   .setNegativeButton("取消录入", null)
			   .show();
		}
	}
	
	public class AddOkListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			nameEdit = (EditText)view.findViewById(R.id.nameEditId);
			scoreEdit = (EditText)view.findViewById(R.id.yuwenEditId);
			rankingEdit = (EditText)view.findViewById(R.id.nianpaiEditId);
			String name = nameEdit.getText().toString();
			String score = scoreEdit.getText().toString();
			String ranking = rankingEdit.getText().toString();
			String date = getNowDate();
			String tendency = "0";
			
			Cursor c = sdbr.query("CHENGJI", new String[]{"ranking"}, null, null, null, null, null);
			while(c.moveToNext()) {
				preranking = c.getString(c.getColumnIndex("ranking"));
			}
			if((!preranking.equals("meiyou")) && (!ranking.equals(""))) {
				int pre = Integer.valueOf(preranking);
				int now = Integer.valueOf(ranking);
				if(pre>now) {tendency = "-1";}
				//if(pre==now) {rendency = "0";}
				if(pre<now) {tendency = "1";}
			}
			Cursor nameC = sdbr.query("CHENGJI", null, "name=?", new String[]{name}, null, null, null);
			int i = 0;
			while(nameC.moveToNext()) {
				i = 1;
			}
			if(0 == i) {
				if((!name.equals(""))&&(!score.equals(""))&&(!ranking.equals(""))) {
					ContentValues values = new ContentValues();
					values.put("name", name);
					values.put("score", score);
					values.put("ranking", ranking);
					values.put("date", date);
					values.put("tendency", tendency);
					sdbw.insert("CHENGJI", null, values);
					SetListView();
				}
				else {
					new AlertDialog.Builder(GradeActivity.this)
					   .setTitle("提示")
					   .setMessage("不能有空的哦")
					   .setPositiveButton("确定", null)
					   .show();
				}
			}
			else {
				new AlertDialog.Builder(GradeActivity.this)
				   .setTitle("添加失败")
				   .setMessage("该名称已存在")
				   .setPositiveButton("确定", null)
				   .show();
			}
			
		}
	}
	
	public void SetListView() {
		ArrayList<HashMap<String,Object>> mData = new ArrayList<HashMap<String,Object>>();
		Cursor cursor = sdbr.query("CHENGJI", new String[]{"name", "score", "ranking", "date", "tendency"},  null, null, null, null, null);
		while(cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex("name")).toString();
			String score = cursor.getString(cursor.getColumnIndex("score")).toString();
			String ranking = cursor.getString(cursor.getColumnIndex("ranking"));
			String date = cursor.getString(cursor.getColumnIndex("date")).toString();
			String tendency = cursor.getString(cursor.getColumnIndex("tendency")).toString();
			int tend = 0;
			if(tendency.equals("0")){tend = R.drawable.pingarrwon50;}
			if(tendency.equals("1")){tend = R.drawable.bottomarrwon50;}
			if(tendency.equals("-1")){tend = R.drawable.toparrwon50;}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
        	map.put("score", score);
        	map.put("ranking", ranking);
        	map.put("date", date);
        	map.put("tendency", tend);
        	mData.add(map);
        	System.out.println(name + "  " + score + "  " + ranking);
		}
		SimpleAdapter adapter = new SimpleAdapter(GradeActivity.this,
    			mData,
				R.layout.list_of_grade,
				new String[]{"name", "score", "ranking", "date", "tendency"},
				new int[]{R.id.nameTextId, R.id.scoreTextId, R.id.rankingTextId,});
        gradeList.setAdapter(adapter);
	}
	
	public String getNowDate() {
		Calendar c = Calendar.getInstance();
		String Year = c.get(Calendar.YEAR) + "";
		String Month, Day;
		Month = c.get(Calendar.MONTH) + 1 + "";
		Day = c.get(Calendar.DAY_OF_MONTH) + "";
		String date = Year + "." + Month + "." + Day;
		return date;
	}
	
	private void getPopupWindowInstance1() {  
        if (null != mPopupWindow1) {  
            mPopupWindow1.dismiss();  
            return;  
        } else {  
            initPopuptWindow1();  
        }  
    }  

 /* 
     * 创建PopupWindow 
     */  
    private void initPopuptWindow1() {  
    	//声明popuwindow所显示的view
        LayoutInflater layoutInflater = LayoutInflater.from(this);  
        View popupWindow = layoutInflater.inflate(R.layout.popupwindow_only_delete, null);  
        // 创建一个PopupWindow  
        mPopupWindow1 = new PopupWindow(popupWindow, screenWidth/5, screenHeight/10);  //第一个参数为popupWindow所要显示的layout;第二个参数为popupWindow的宽;第三个参数为popupWindow的高度
       //设置popupWindow点击外边框取消
        mPopupWindow1.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow1.setOutsideTouchable(true);  
        mPopupWindow1.setFocusable(true);  
        
        final Button button_delete = (Button)popupWindow.findViewById(R.id.only_delete);
        
        
        button_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sdbw.delete("CHENGJI", "name=?", new String[]{shanchu});
				SetListView();
				mPopupWindow1.dismiss();
			}
		});
    }  
}

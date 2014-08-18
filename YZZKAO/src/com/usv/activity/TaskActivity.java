package com.usv.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.usv.data.YZZKDataBase;
import com.usv.tools.JsonParser;
import com.usv.yzzkao.ItemAdapter;
import com.usv.yzzkao.R;
import com.usv.yzzkao.SlideCutListView;
import com.usv.yzzkao.SlideCutListView.RemoveListener;


public class TaskActivity extends Activity implements RemoveListener {
	
	//所有计划个数的sharedpreferences     文件名是  jihuasavenumber  数据的KEY是  alljihua
	//已完成计划个数的sharedpreferences   文件名是  finishednumber   数据的KEY是  finisedjihua
	//在其他地方获取该数据时 使用
	
	private Button addButton;
	private EditText editText = null;
	private PopupWindow mPopupWindow=null;
	private PopupWindow mPopupWindow1=null;
	private Button task_back=null;
	
	public static int mode=MODE_PRIVATE;
	public static final String alljihua="jihuasavenumber", finished = "finishednumber";
	SharedPreferences jihuaData, finishedData;
	Editor editor, finishededitor;
	
	private SlideCutListView slideCutListView;
	private ItemAdapter adapter;
	private ArrayList<String> jihuaList = new ArrayList<String>();
	private ArrayList<String> strbg = new ArrayList<String>();
	private ArrayList<String> timelist = new ArrayList<String>();
	private String singlejihua;
	
	ArrayList<HashMap<String,Object>> mData = new ArrayList<HashMap<String,Object>>();
	HashMap<String, Object> map = new HashMap<String, Object>();
	
	private YZZKDataBase YZZKDB;
	private SQLiteDatabase sdbw, sdbr;
	
	
	private int screenWidth;  
	private int screenHeight;  
	
	SpeechRecognizer mIat;
	private static final String APPID = "appid=53f0ba87";
	private RecognizerDialog iatDialog;
	private ImageButton yuyin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);
		
		SpeechUtility.createUtility(this, APPID);
		mIat = SpeechRecognizer.createRecognizer(this, null);
		iatDialog = new RecognizerDialog(this, mInitListener);
		
		mIat.setParameter(SpeechConstant.DOMAIN, "iat");
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				"/sdcard/YZZK/wavaudio.pcm");
		//yuyin.setOnClickListener(new YuyinButtonListener());
		
		addButton = (Button)findViewById(R.id.addButton);
		addButton.setOnClickListener(new AddButtonListener());
		
		
		task_back =  (Button)findViewById(R.id.task_back);
		
		WindowManager windowManager = getWindowManager();  
		Display display = windowManager.getDefaultDisplay();  
		screenWidth = display.getWidth();  
		screenHeight = display.getHeight();  
		
		
		
		
		
		task_back.setOnClickListener(new OnClickListener() {
			
	        
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();
				overridePendingTransition(R.anim.in_alpha_scale, R.anim.push_right_out);
				
			}
		});
		
		
		
		
		jihuaData = getSharedPreferences(alljihua, mode);
		editor = jihuaData.edit();
		finishedData = getSharedPreferences(finished, mode);
		finishededitor = finishedData.edit();
		
		YZZKDB = new YZZKDataBase(this);
		sdbw = YZZKDB.getWritableDatabase();
		sdbr = YZZKDB.getReadableDatabase();
		String sql = "create table if not exists JIHUA (things varchar(30), checked varchar(5), time varchar(20))";
		sdbw.execSQL(sql);
		init();
	}
	
	private void init() {
		slideCutListView = (SlideCutListView)findViewById(R.id.taskListId);
		slideCutListView.setOnItemClickListener(new ListLongClick());
		slideCutListView.setRemoveListener(this);
		jihuaList.clear();
		strbg.clear();
		Cursor c = sdbr.query("JIHUA", new String[]{"things","checked","time"}, null, null, null, null, null, null);
		while(c.moveToNext()) {
			String thing = c.getString(c.getColumnIndex("things")).toString();
			String check = c.getString(c.getColumnIndex("checked")).toString();
			String time = c.getString(c.getColumnIndex("time")).toString();
			jihuaList.add(thing);
			strbg.add(check);
			timelist.add(time);
		}
		int datasize = strbg.size();
		int[] bgdatas = new int[datasize];
		for(int i = 0; i<=datasize-1; i++) {
			if(strbg.get(i).equals("1")) {
				bgdatas[i] = R.drawable.task_finish;
			}
			
		}
		
		adapter = new ItemAdapter(this);
		adapter.setData(jihuaList, bgdatas, timelist);
		slideCutListView.setAdapter(adapter);
	}

	//下面是点击“完成”执行的代码  可以在这里面改变背景   
	//用 slideCutListView.itemView.findViewById() 去找到你想要改的控件的Id
	public void removeItem(int position) {
			SlideCutListView.isSlide = false;
			SlideCutListView.itemView.findViewById(R.id.tv_coating).setVisibility(View.VISIBLE);
			//这行代码是隐藏完成的项 被我注释了
			//jihuaList.remove(position);
			
			//更新数据库的是否完成
			TextView jihuaText = (TextView)SlideCutListView.itemView.findViewById(R.id.tv_item);
			TextView jihuaBg = (TextView)SlideCutListView.itemView.findViewById(R.id.tv_item);
			String rejihua = jihuaText.getText().toString();
			ContentValues values = new ContentValues();
			values.put("checked", "1");
			sdbw.update("JIHUA", values, "things=?", new String[]{rejihua});
			adapter.notifyDataSetChanged();
			//更新存在sharedpreferences里的完成了多少项
			String s = finishedData.getString("finishedjihua", "0");
			int i = Integer.valueOf(s) + 1;
			s = i + "";
			finishededitor.putString("finisedjihua", s);
			finishededitor.commit();
			System.out.println("完成了" + s +"个计划");
			
			init();
	}
	
	//添加按钮 的Listener
	public class AddButtonListener implements OnClickListener {
		AddOkListener addok = new AddOkListener();
		@Override
		public void onClick(View v) {
			
			LayoutInflater factory = LayoutInflater.from(TaskActivity.this);  
			 final View textEntryView = factory.inflate(R.layout.set_task_dialog, null); 
			 yuyin = (ImageButton) textEntryView.findViewById(R.id.set_task_dialog_yuyin);
			editText = (EditText)textEntryView.findViewById(R.id.task_text);
			 
			new AlertDialog.Builder(TaskActivity.this)
				.setIcon(R.drawable.symbol7272) 
			   .setTitle("赶快写下自己的计划")
			   .setView(textEntryView)
			   .setPositiveButton("开始计划", addok)
			   .setNegativeButton("取消计划", null)
			   .show();
			yuyin.setOnClickListener(new YuyinButtonListener());
		}
	}
	//添加对话框 的Listener
	public class AddOkListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			String str = editText.getText().toString();
			Cursor cursor = sdbr.query("JIHUA", null, "things=?", new String[]{str},
					null, null, null);
			int r = 0;
			while(cursor.moveToNext()) {
				r = 1;
			}
			if(0 == r) {
				if(!str.equals("")) {
					
					//向数据库里添加新的数据
					Calendar c = Calendar.getInstance();
					String Year = c.get(Calendar.YEAR) + "";
					String Month, Day;
					int mMonth = c.get(Calendar.MONTH) + 1;
					Month = mMonth + "";
					//if(mMonth < 10) { Month = ".0" + mMonth; }else{ Month = "." + mMonth; }
					int mDay = c.get(Calendar.DAY_OF_MONTH);
					Day = mDay + "";
					//if(mDay < 10) { Day = ".0" + mDay; }else{ Day = "." + mDay; }
					String time = Year + "." + Month + "." + Day;
//					time = "2014.5.19";
					System.out.println(time);

					ContentValues values = new ContentValues();
					values.put("things", str);
					values.put("checked", "0");
					values.put("time", time);
					sdbw.insert("JIHUA", null, values);
					
					
					init();
					//更新存在sharedpreferences里的共添加了多少项计划的个数
					String s = jihuaData.getString("alljihua", "0");
					int i = Integer.valueOf(s) + 1;
					s = i + "";
					editor.putString("alljihua", s);
					editor.commit();
					System.out.println("共" + s +"个计划");
				}
			}
			else {
				new AlertDialog.Builder(TaskActivity.this)
					.setIcon(R.drawable.symbol7272) 
				   .setTitle("温馨提示")
				   .setMessage("该计划已存在哦")
				   .setPositiveButton("确定", null)
				   .show();
			}
		}
	}
	
	public class ListLongClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int arg2,
				long arg3) {
			
			TextView textView = (TextView)view.findViewById(R.id.tv_item);
			singlejihua = textView.getText().toString();
			
			Cursor cur = sdbr.query("JIHUA", new String[]{"checked"}, "things=?", new String[]{singlejihua}, null, null, null, null);
			String s = null;
			while(cur.moveToNext()) {
				s = cur.getString(cur.getColumnIndex("checked"));
			}
			if(s.endsWith("0")) {

				System.out.println("这个是0！！！！！！！！！！！！！");
				getPopupWindowInstance();  
				//设置popupwindow的位置，在view正上方
				int[] location = new int[2];  
				view.getLocationOnScreen(location);  
		        mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, view.getWidth()/2, location[1]-mPopupWindow.getHeight());
				
			}
			else if(s.endsWith("1")) {

				System.out.println("这个是1！！！！！！！！！！！！！");
				getPopupWindowInstance1();  
				//设置popupwindow的位置，在view正上方
				int[] location = new int[2];  
				view.getLocationOnScreen(location);  
		        mPopupWindow1.showAtLocation(view, Gravity.NO_GRAVITY, view.getWidth()/2, location[1]-mPopupWindow1.getHeight());
				
			}
			
		}
	}
	
	public class UpdateOkListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			String str = editText.getText().toString();
			Cursor cursor = sdbr.query("JIHUA", null, "things=?", new String[]{str}, 
					null, null, null);
			int r = 0;
			while(cursor.moveToNext()) {
				r = 1;
			}
			if(0 == r) {
				if(!str.equals("")) {
					ContentValues v = new ContentValues();
					v.put("things", str);
					sdbw.update("JIHUA", v, "things=?", new String[]{singlejihua});
					init();
				}
			}
			else {
				new AlertDialog.Builder(TaskActivity.this)
				.setIcon(R.drawable.symbol7272) 
				   .setTitle("温馨提示")
				   .setMessage("该计划已存在哦")
				   .setPositiveButton("确定", null)
				   .show();
			}
		}
		
	}
	
	public class QingKongOkListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			sdbw.delete("JIHUA", null, null);
			init();
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		overridePendingTransition(R.anim.in_alpha_scale, R.anim.push_right_out);
		return super.onKeyDown(keyCode, event);
	}
	
	
	//得到popupwindow对象
		 private void getPopupWindowInstance() {  
		        if (null != mPopupWindow) {  
		            mPopupWindow.dismiss();  
		            return;  
		        } else {  
		            initPopuptWindow();  
		        }  
		    }  
		
		 /* 
		     * 创建PopupWindow 
		     */  
		    private void initPopuptWindow() {  
		    	//声明popuwindow所显示的view
		        LayoutInflater layoutInflater = LayoutInflater.from(this);  
		        View popupWindow = layoutInflater.inflate(R.layout.popuwindow, null);  
		        // 创建一个PopupWindow  
		        mPopupWindow = new PopupWindow(popupWindow, screenWidth/4, screenHeight/10);  //第一个参数为popupWindow所要显示的layout;第二个参数为popupWindow的宽;第三个参数为popupWindow的高度
		       //设置popupWindow点击外边框取消
		        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		        mPopupWindow.setOutsideTouchable(true);  
		        mPopupWindow.setFocusable(true);  
		        
		        final Button button_gai = (Button)popupWindow.findViewById(R.id.xiugai);
		        final Button button_finish = (Button)popupWindow.findViewById(R.id.finish);
		        
		        button_gai.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						UpdateOkListener uol = new UpdateOkListener();
						
						new AlertDialog.Builder(TaskActivity.this)
							.setIcon(R.drawable.symbol7272) 
						   .setTitle("请修改计划")
						   .setView(editText = new EditText(TaskActivity.this))
						   .setPositiveButton("确定", uol)
						   .setNegativeButton("取消", null)
						   .show();
						editText.setText(singlejihua);
						editText.selectAll();
						
						mPopupWindow.dismiss();
					}
				});
		        
		        
		        button_finish.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						sdbw.delete("JIHUA", "things=?", new String[]{singlejihua});
						init();
						mPopupWindow.dismiss();
					}
				});
		    }  
		
	
		    
		  //得到popupwindow对象
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
							// TODO Auto-generated method stub
							sdbw.delete("JIHUA", "things=?", new String[]{singlejihua});
							init();
							mPopupWindow1.dismiss();
						}
					});
			    }  
	    private InitListener mInitListener = new InitListener() {

			@Override
			public void onInit(int code) {
				if (code == ErrorCode.SUCCESS) {
					yuyin.setEnabled(true);
				}
			}
		};
		private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
			public void onResult(RecognizerResult results, boolean isLast) {
				String text = JsonParser.parseIatResult(results.getResultString());
				editText.append(text);
				editText.setSelection(editText.length());
			}
			public void onError(SpeechError error) {
				System.out.println("失败了。。。。。。。。。");
			}
		};
		public class YuyinButtonListener implements OnClickListener {
			@Override
			public void onClick(View arg0) {
				iatDialog.setListener(recognizerDialogListener);
				iatDialog.show();
			}
		}
	
}

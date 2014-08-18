package com.usv.activity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.usv.data.YZZKDataBase;
import com.usv.yzzkao.ConnectionDetector;
import com.usv.yzzkao.R;
import com.usv.yzzkao.Utility;
import com.usv.yzzkao.ViewPagerAdapter0;
import com.usv.yzzkao.R.anim;
import com.usv.yzzkao.R.drawable;
import com.usv.yzzkao.R.id;
import com.usv.yzzkao.R.layout;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ViewPager viewpager = null;
	private List<View> list = null;
	private DatePicker datepicker = null;

	private TextView henggang0, henggang1, henggang2, henggang3;

	private Button qingchutext, daojishitext, xuexitext, jilu;
	private long exitTime = 0;
	private int mYear, mMonth, mDay;

	private TextView Daytext;// 设置天数的textview
	private TextView fenzhongtext;// 设置小时的textview
	private TextView daojishi_min;// 设置分钟的textview
	private TextView daojishi_miao;// 设置秒textview
	private TextView riqitext;// 设置日期的textview
	private TextView nianyuetext;// 设置年月的textview
	private TextView daojishu_jiyu;

	private TextView jianchi;
	private TextView task;
	private TextView gushi;
	private TextView duihua;
	private TextView word;
	private TextView chaoguotongxue;

	private ListView listview_plan;
	private ListView listview_dream;
	private ListView listview_about;

	public static int MODE = MODE_PRIVATE;// 定义访问模式为私有模式
	public static final String PREFERENCE_NAME = "saveInfo";// 设置保存时的文件的名称

	public Calendar c;

	private SQLiteDatabase jiluDatabase;
	private YZZKDataBase YzzkDatabase;

	public SharedPreferences sharedpreferences;// 声明一个SharePreferences

	public final String name = "savewordnumber";
	public final String task_name = "finishednumber";
	public final String english_sencece_name = "EnglishSencence";
	public final String chinese_sencece_name = "ChineseSencence";
	public final String frist_time = "saveisfrist";

	public SharedPreferences wordData2;
	public SharedPreferences finish_task_sharepreferences;
	public SharedPreferences finish_english_sencece;
	public SharedPreferences chinese_sencece;
	public SharedPreferences isfrist;

	public ImageView network;

	public Handler handler = new Handler() {
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);

		// 监测网络连接的方法，通过ConnectionDetector工具类实现
		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

		Boolean isInternetPresent = cd.isConnectingToInternet(); // true or
																	// false返回true为网络连接成功,返回false为网络连接失败

		// 初始化广告
		AdManager.getInstance(this).init("4ae67375fba14129",
				"4eb7af786cfa05d1", true);
		// 预加载插屏广告
		SpotManager.getInstance(this).loadSpotAds();
		// 展示插屏广告
		SpotManager.getInstance(this).showSpotAds(this);
		// 最小间隔时间
		SpotManager.getInstance(this).setShowInterval(400);
		// 插屏广告自动关闭
		SpotManager.getInstance(this).setAutoCloseSpot(true);// 设置自动关闭插屏开关
		SpotManager.getInstance(this).setCloseTime(5000); // 设置关闭插屏时间
		// 用户数据统计功能
		AdManager.getInstance(this).setUserDataCollect(true);

		// 实例化日历类，用于设置年月
		c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// 倒计时所用到的sharedpreferences初始化
		sharedpreferences = getSharedPreferences(PREFERENCE_NAME, MODE);// 得到一个SharedPreferences，第一参数为保存的文件名称，第二个参数为存储模式

		// 记录界面所用到的sharedpreferences初始化
		wordData2 = getSharedPreferences(name, MODE);
		finish_task_sharepreferences = getSharedPreferences(task_name, MODE);
		finish_english_sencece = getSharedPreferences(english_sencece_name,
				MODE);
		chinese_sencece = getSharedPreferences(chinese_sencece_name, MODE);
		isfrist = getSharedPreferences(frist_time, MODE);

		viewpager = (ViewPager) findViewById(R.id.viewpager);

		LayoutInflater inflater = getLayoutInflater();

		// viewpapger 中 view声明
		View view0 = inflater.inflate(R.layout.welcome, null);
		View view1 = inflater.inflate(R.layout.activity_main, null);
		View view2 = inflater.inflate(R.layout.jilu, null);
		View view3 = inflater.inflate(R.layout.activity_setting, null);

		listview_plan = (ListView) view3.findViewById(R.id.settingListViewId_0);
		listview_dream = (ListView) view3
				.findViewById(R.id.settingListViewId_1);
		listview_about = (ListView) view3
				.findViewById(R.id.settingListViewId_2);

		// 声明倒计时界面中的textview控件
		Daytext = (TextView) view1.findViewById(R.id.activity_main_daytime);
		fenzhongtext = (TextView) view1
				.findViewById(R.id.activity_main_hour_number);
		riqitext = (TextView) view1.findViewById(R.id.activity_main_date_text);
		nianyuetext = (TextView) view1
				.findViewById(R.id.activity_main_yearandmoonth_text);
		daojishi_min = (TextView) view1
				.findViewById(R.id.acitvity_main_min_number);
		daojishi_miao = (TextView) view1
				.findViewById(R.id.activity_main_miao_number);
		daojishu_jiyu = (TextView) view1
				.findViewById(R.id.activity_main_jiyu_text);

		// 记录界面所用到的textview控件
		jianchi = (TextView) view2.findViewById(R.id.jianchi_date);
		task = (TextView) view2.findViewById(R.id.fnish_task_number);
		gushi = (TextView) view2.findViewById(R.id.fnish_gushi);
		duihua = (TextView) view2.findViewById(R.id.fnish_duihua);
		word = (TextView) view2.findViewById(R.id.fnish_word);
		chaoguotongxue = (TextView) view2.findViewById(R.id.chaoguodetongxue);

		// 学习界面listview初始化
		listview_plan_inti();
		listview_dream_inti();
		listview_about_inti();
		// 倒计时界面初始化
		daojishi_inti();
		// 记录界面初始化
		jilu_inti();

		qingchutext = (Button) findViewById(R.id.qingchuntext);
		daojishitext = (Button) findViewById(R.id.daojishitext);
		xuexitext = (Button) findViewById(R.id.xuexitext);
		jilu = (Button) findViewById(R.id.jilu);

		henggang0 = (TextView) findViewById(R.id.henggang0);
		henggang1 = (TextView) findViewById(R.id.henggang1);
		henggang2 = (TextView) findViewById(R.id.henggang2);
		henggang3 = (TextView) findViewById(R.id.henggang3);

		list = new ArrayList<View>();
		list.add(view0);
		list.add(view1);
		list.add(view2);
		list.add(view3);

		viewpager.setAdapter(new ViewPagerAdapter0(list));
		viewpager.setOnPageChangeListener(new ViewPagerPageChangeListener());

		// 设置标题点击更换view
		qingchutext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewpager.setCurrentItem(0, true);// 第一个参数为view的编号
			}
		});
		// 倒计时标题点击更换事件
		daojishitext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewpager.setCurrentItem(1, true);
			}
		});

		// 记录标题点击view事件
		jilu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewpager.setCurrentItem(2, true);
			}
		});

		// 学习标题点击更换view事件
		xuexitext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewpager.setCurrentItem(3, true);
			}
		});

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
		public void onPageSelected(int page) {
			// TODO Auto-generated method stub

			switch (page) {
			case 0:
				qingchutext.setTextColor(Color.rgb(51, 181, 229));
				daojishitext.setTextColor(Color.rgb(110, 110, 110));
				jilu.setTextColor(Color.rgb(110, 110, 110));
				xuexitext.setTextColor(Color.rgb(110, 110, 110));

				henggang0.setBackgroundColor(Color.rgb(51, 181, 229));
				henggang1.setBackgroundColor(Color.rgb(255, 255, 255));
				henggang2.setBackgroundColor(Color.rgb(255, 255, 255));
				henggang3.setBackgroundColor(Color.rgb(255, 255, 255));

				break;

			case 1:

				qingchutext.setTextColor(Color.rgb(110, 110, 110));
				daojishitext.setTextColor(Color.rgb(51, 181, 229));
				jilu.setTextColor(Color.rgb(110, 110, 110));
				xuexitext.setTextColor(Color.rgb(110, 110, 110));

				henggang0.setBackgroundColor(Color.rgb(255, 255, 255));
				henggang1.setBackgroundColor(Color.rgb(51, 181, 229));
				henggang2.setBackgroundColor(Color.rgb(255, 255, 255));
				henggang3.setBackgroundColor(Color.rgb(255, 255, 255));

				break;

			case 2:
				qingchutext.setTextColor(Color.rgb(110, 110, 110));
				daojishitext.setTextColor(Color.rgb(110, 110, 110));
				jilu.setTextColor(Color.rgb(51, 181, 229));
				xuexitext.setTextColor(Color.rgb(110, 110, 110));

				henggang0.setBackgroundColor(Color.rgb(255, 255, 255));
				henggang1.setBackgroundColor(Color.rgb(255, 255, 255));
				henggang2.setBackgroundColor(Color.rgb(51, 181, 229));
				henggang3.setBackgroundColor(Color.rgb(255, 255, 255));

				break;

			case 3:

				qingchutext.setTextColor(Color.rgb(110, 110, 110));
				daojishitext.setTextColor(Color.rgb(110, 110, 110));
				jilu.setTextColor(Color.rgb(110, 110, 110));
				xuexitext.setTextColor(Color.rgb(51, 181, 229));

				henggang0.setBackgroundColor(Color.rgb(255, 255, 255));
				henggang1.setBackgroundColor(Color.rgb(255, 255, 255));
				henggang2.setBackgroundColor(Color.rgb(255, 255, 255));
				henggang3.setBackgroundColor(Color.rgb(51, 181, 229));

			}

		}

	}

	// 监测点击2次返回退出程序
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "真的要退出了吗？那就再按一次吧>_<",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();

				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 获取倒计时的小时
	public String getDistanceOfDate_hour(String testDate) {
		Date date1 = new Date(System.currentTimeMillis());// 获取当前时间
		String str2 = testDate; // "yyyyMMdd"格式 如 20131022
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHH:mm:ss");// 输入日期的格式

		Date date2 = null;

		try {
			date2 = simpleDateFormat.parse(str2);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GregorianCalendar cal1 = new GregorianCalendar();
		GregorianCalendar cal2 = new GregorianCalendar();
		cal1.setTime(date1);
		cal2.setTime(date2);
		double dayCount = (cal2.getTimeInMillis() - cal1.getTimeInMillis())
				/ (1000 * 3600 * 24);// 从间隔毫秒变成间隔天数
		double dayCount2 = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000);
		double dayCount3 = dayCount * 24 * 3600;
		double dayCount4 = dayCount2 - dayCount3;
		// System.out.println(dayCount4);
		double hourCount = dayCount4 / 3600;
		// System.out.println("hour--------------->"+ (int)hourCount);
		double mintnite = (dayCount4 - (int) hourCount * 3600) / 60;
		// System.out.println("分钟--------------->"+mintnite);
		double miao = dayCount4 - (int) hourCount * 3600 - (int) mintnite * 60;
		// System.out.println((int)hourCount+"小时"+(int)mintnite+"分钟"+(int)miao+"秒");
		// 修改textview的属性

		return (int) hourCount + "";
	}

	// 获取倒计时的分钟
	public String getDistanceOfDate_min(String testDate) {
		Date date1 = new Date(System.currentTimeMillis());// 获取当前时间
		String str2 = testDate; // "yyyyMMdd"格式 如 20131022
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHH:mm:ss");// 输入日期的格式

		Date date2 = null;

		try {
			date2 = simpleDateFormat.parse(str2);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GregorianCalendar cal1 = new GregorianCalendar();
		GregorianCalendar cal2 = new GregorianCalendar();
		cal1.setTime(date1);
		cal2.setTime(date2);
		double dayCount = (cal2.getTimeInMillis() - cal1.getTimeInMillis())
				/ (1000 * 3600 * 24);// 从间隔毫秒变成间隔天数
		double dayCount2 = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000);
		double dayCount3 = dayCount * 24 * 3600;
		double dayCount4 = dayCount2 - dayCount3;
		// System.out.println(dayCount4);
		double hourCount = dayCount4 / 3600;
		// System.out.println("hour--------------->"+ (int)hourCount);
		double mintnite = (dayCount4 - (int) hourCount * 3600) / 60;
		// System.out.println("分钟--------------->"+mintnite);
		double miao = dayCount4 - (int) hourCount * 3600 - (int) mintnite * 60;
		// System.out.println((int)hourCount+"小时"+(int)mintnite+"分钟"+(int)miao+"秒");
		// 修改textview的属性

		return (int) mintnite + "";
	}

	// 获取倒计时的秒
	public String getDistanceOfDate_miao(String testDate) {
		Date date1 = new Date(System.currentTimeMillis());// 获取当前时间
		String str2 = testDate; // "yyyyMMdd"格式 如 20131022
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHH:mm:ss");// 输入日期的格式

		Date date2 = null;

		try {
			date2 = simpleDateFormat.parse(str2);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GregorianCalendar cal1 = new GregorianCalendar();
		GregorianCalendar cal2 = new GregorianCalendar();
		cal1.setTime(date1);
		cal2.setTime(date2);
		double dayCount = (cal2.getTimeInMillis() - cal1.getTimeInMillis())
				/ (1000 * 3600 * 24);// 从间隔毫秒变成间隔天数
		double dayCount2 = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000);
		double dayCount3 = dayCount * 24 * 3600;
		double dayCount4 = dayCount2 - dayCount3;
		// System.out.println(dayCount4);
		double hourCount = dayCount4 / 3600;
		// System.out.println("hour--------------->"+ (int)hourCount);
		double mintnite = (dayCount4 - (int) hourCount * 3600) / 60;
		// System.out.println("分钟--------------->"+mintnite);
		double miao = dayCount4 - (int) hourCount * 3600 - (int) mintnite * 60;
		// System.out.println((int)hourCount+"小时"+(int)mintnite+"分钟"+(int)miao+"秒");
		// 修改textview的属性

		return (int) miao + "";

	}

	// 获取倒计时的天数
	public int getDistanceOfDate2(String testDate) {
		Date date1 = new Date(System.currentTimeMillis());// 获取当前时间
		String str2 = testDate; // "yyyyMMdd"格式 如 20131022
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");// 输入日期的格式

		Date date2 = null;
		try {
			date2 = simpleDateFormat.parse(str2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		GregorianCalendar cal1 = new GregorianCalendar();
		GregorianCalendar cal2 = new GregorianCalendar();
		cal1.setTime(date1);
		cal2.setTime(date2);
		double dayCount = (cal2.getTimeInMillis() - cal1.getTimeInMillis())
				/ (1000 * 3600 * 24);// 从间隔毫秒变成间隔天数
		int intdayCount = (int) dayCount;
		return intdayCount;
	}

	// 将int型的月份变为英文简写
	public String changeDate(int intmonth) {
		String strmonth = null;
		if (intmonth == 0) {
			strmonth = "Jan";
		} else if (intmonth == 1) {
			strmonth = "Feb";
		} else if (intmonth == 2) {
			strmonth = "Mar";
		} else if (intmonth == 3) {
			strmonth = "Apr";
		} else if (intmonth == 4) {
			strmonth = "May";
		} else if (intmonth == 5) {
			strmonth = "Jun";
		} else if (intmonth == 6) {
			strmonth = "Jul";
		} else if (intmonth == 7) {
			strmonth = "Aug";
		} else if (intmonth == 8) {
			strmonth = "Sep";
		} else if (intmonth == 9) {
			strmonth = "Oct";
		} else if (intmonth == 10) {
			strmonth = "Nov";
		} else if (intmonth == 11) {
			strmonth = "Dec";
		}
		return strmonth;

	}

	public String changeDateto_(Calendar c) {
		String str = null;
		int i = (int) c.get(Calendar.DAY_OF_MONTH);
		if (i < 10) {
			str = "0" + i;
		} else {
			str = i + "";
		}

		return str;

	}

	/**
	 * 计算使用软件的时间
	 * 
	 * @param testDate
	 *            第一次使用软件的日期，通过isfrist.java中保存的数据
	 * @return 与当前时间的差值，默认加一
	 */
	public int getDistanceOfDate3(String testDate) {
		Date date1 = new Date(System.currentTimeMillis());// 获取当前时间
		String str2 = testDate; // "yyyyMMdd"格式 如 20131022
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");// 输入日期的格式

		Date date2 = null;
		try {
			date2 = simpleDateFormat.parse(str2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		GregorianCalendar cal1 = new GregorianCalendar();
		GregorianCalendar cal2 = new GregorianCalendar();
		cal1.setTime(date1);
		cal2.setTime(date2);
		double dayCount = (cal1.getTimeInMillis() - cal2.getTimeInMillis())
				/ (1000 * 3600 * 24);// 从间隔毫秒变成间隔天数
		int intdayCount = (int) dayCount;

		return intdayCount + 1;

	}

	/**
	 * 
	 * 计算超过的同学人数
	 * 
	 * @param insist_number
	 *            坚持天数
	 * @param finished_task
	 *            完成任务
	 * @param master_word
	 *            掌握单词
	 * @param master_pome
	 *            掌握诗词
	 * @param master_dialog
	 *            掌握对话
	 * @return 超过人数的String
	 */
	public String calculate_over_person(double insist_number,
			double finished_task, double master_word, double master_pome,
			double master_dialog) {
		String str = null;
		double count;
		insist_number = insist_number * 0.1;
		finished_task = finished_task * 0.01;
		master_word = master_word * 0.01;
		master_pome = master_pome * 0.1;
		master_dialog = master_dialog * 0.01;

		DecimalFormat df = new DecimalFormat("######0.00");

		return str = df.format(insist_number + finished_task + master_word
				+ master_pome + master_dialog)
				+ "%";

	}

	public void listview_plan_inti() {

		listview_plan.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int i,
					long arg3) {
				// TODO Auto-generated method stub
				switch (i) {
				case 0: {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, TaskActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.out_alpha_scale);
					break;
				}
				case 1: {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, KPActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.out_alpha_scale);
					break;
				}
				case 2: {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, ESActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.out_alpha_scale);
					break;
				}
				case 3: {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, CSActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.out_alpha_scale);
					break;
				}
				}

			}
		});
		ArrayList<HashMap<String, Object>> mData_0 = new ArrayList<HashMap<String, Object>>();

		String[] function_0 = new String[] { "每日任务", "英语单词", "英语对话", "语文诗词" };
		int[] Image_0 = new int[] { R.drawable.myplan, R.drawable.word50,
				R.drawable.sentence50, R.drawable.niao50x50 };
		for (int i = 0; i < 4; i++) {
			HashMap<String, Object> map_0 = new HashMap<String, Object>();
			map_0.put("image1", Image_0[i]);
			map_0.put("text", function_0[i]);
			mData_0.add(map_0);
		}
		SimpleAdapter listAdapter_plan;
		listAdapter_plan = new SimpleAdapter(MainActivity.this, mData_0,
				R.layout.setting_listview_layout, new String[] { "image1",
						"text", "image2" }, new int[] {
						R.id.settingListViewImage1Id,
						R.id.settingListViewTextId,
						R.id.settingListViewImage2Id });
		listview_plan.setAdapter(listAdapter_plan);

		Utility.setListViewHeightBasedOnChildren(listview_plan);

	}

	public void listview_dream_inti() {

		listview_dream.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int i,
					long arg3) {
				// TODO Auto-generated method stub

				switch (i) {
				case 0: {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, GoalActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.out_alpha_scale);
					break;
				}
				case 1: {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, GradeActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.out_alpha_scale);
					break;
				}
				}

			}
		});

		ArrayList<HashMap<String, Object>> mData_1 = new ArrayList<HashMap<String, Object>>();
		String[] function_1 = new String[] { "我的梦想", "我的成绩" };
		int[] Image_1 = new int[] { R.drawable.dream50x50, R.drawable.myg50x50 };
		for (int i = 0; i < 2; i++) {
			HashMap<String, Object> map_1 = new HashMap<String, Object>();
			map_1.put("image1", Image_1[i]);
			map_1.put("text", function_1[i]);
			mData_1.add(map_1);
		}
		SimpleAdapter listAdapter_dream;

		listAdapter_dream = new SimpleAdapter(MainActivity.this, mData_1,
				R.layout.setting_listview_layout, new String[] { "image1",
						"text", "image2" }, new int[] {
						R.id.settingListViewImage1Id,
						R.id.settingListViewTextId,
						R.id.settingListViewImage2Id });
		listview_dream.setAdapter(listAdapter_dream);

		Utility.setListViewHeightBasedOnChildren(listview_dream);

	}

	public void listview_about_inti() {

		listview_about.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int i,
					long arg3) {
				// TODO Auto-generated method stub

				switch (i) {
				case 0: {
					Intent intent = new Intent(MainActivity.this,
							ShareActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.out_alpha_scale);
					break;
				}
				case 1: {
					Intent intent = new Intent(MainActivity.this,
							guanzhuus.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.out_alpha_scale);
					break;
				}
				case 2: {
					Intent intent = new Intent(MainActivity.this,
							supportActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.out_alpha_scale);
					break;
				}
				}

			}
		});

		ArrayList<HashMap<String, Object>> mData_2 = new ArrayList<HashMap<String, Object>>();
		String[] function_2 = new String[] { "好友分享", "关注我们", "支持开发者" };
		int[] Image_2 = new int[] { R.drawable.ic_share_normal,
				R.drawable.contact50x50, R.drawable.setting_evaluateour };
		for (int i = 0; i < 3; i++) {
			HashMap<String, Object> map_2 = new HashMap<String, Object>();
			map_2.put("image1", Image_2[i]);
			map_2.put("text", function_2[i]);
			mData_2.add(map_2);

		}

		SimpleAdapter listAdapter_about;
		listAdapter_about = new SimpleAdapter(MainActivity.this, mData_2,
				R.layout.setting_listview_layout, new String[] { "image1",
						"text", "image2" }, new int[] {
						R.id.settingListViewImage1Id,
						R.id.settingListViewTextId,
						R.id.settingListViewImage2Id });
		listview_about.setAdapter(listAdapter_about);

		Utility.setListViewHeightBasedOnChildren(listview_about);

	}

	public void daojishi_inti() {

		 daojishu_jiyu.setText(getrandomString());
		// 所有textview的settext
		riqitext.setText(changeDateto_(c));
		nianyuetext.setText(changeDate(mMonth) + "," + mYear);

		Button setDate = (Button) findViewById(R.id.setDate);

		final AssetManager mgr = getAssets();// 得到AssetManager

		// 声明计时器，让其1秒刷新一次，用于设计倒计时小时分秒
		Timer timer = new Timer();

		if (sharedpreferences.contains("Date")) {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {

					// TODO Auto-generated method stub
					// 由于子线程中无法更改UI，所以要把子线程的数据通过hanler传到主线程中，再进行UI的修改
					handler.post(new Runnable() {
						public void run() {
							fenzhongtext
									.setText(getDistanceOfDate_hour(sharedpreferences
											.getString("Date", "") + "00:00:00"));
							daojishi_min
									.setText(getDistanceOfDate_min(sharedpreferences
											.getString("Date", "") + "00:00:00"));
							daojishi_miao
									.setText(getDistanceOfDate_miao(sharedpreferences
											.getString("Date", "") + "00:00:00"));
						}
					});
				}
			}, 0, 1000);

			Daytext.setText(getDistanceOfDate2(sharedpreferences.getString(
					"Date", "")) + "");

		} else {
			Daytext.setText("0");
		}

		setDate.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// DPOkListener dpok = new DPOkListener();
				new AlertDialog.Builder(MainActivity.this)
						.setTitle("请选择您的中考时间")
						.setView(datepicker = new DatePicker(MainActivity.this))
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method
										// stub

										int day = datepicker.getDayOfMonth();
										int mon = datepicker.getMonth() + 1;
										int year = datepicker.getYear();
										String monn = changeDate(mon);
										String dayn = changeDate(day);
										String yearn = year + "";
										String datestring = yearn + monn + dayn;
										System.out.println(datestring);
										sharedpreferences = getSharedPreferences(
												PREFERENCE_NAME, MODE);// 通过getSharedPreferences(String
																		// name,int
																		// mode)得到SharedPreferences接口。该方法的第一个参数是文件名称，第二个参数是操作模式
										SharedPreferences.Editor editor = sharedpreferences
												.edit();// 调用SharedPreferences.Editor方法对SharedPreferences进行修改
										editor.putString("Date", datestring);
										editor.commit();
										String str = sharedpreferences
												.getString("Date", "");

										Timer timer = new Timer();

										timer.schedule(new TimerTask() {
											@Override
											public void run() {

												// TODO Auto-generated
												// method stub
												// 由于子线程中无法更改UI，所以要把子线程的数据通过hanler传到主线程中，再进行UI的修改
												handler.post(new Runnable() {
													public void run() {
														fenzhongtext
																.setText(getDistanceOfDate_hour(sharedpreferences
																		.getString(
																				"Date",
																				"")
																		+ "00:00:00"));
														daojishi_min
																.setText(getDistanceOfDate_min(sharedpreferences
																		.getString(
																				"Date",
																				"")
																		+ "00:00:00"));
														daojishi_miao
																.setText(getDistanceOfDate_miao(sharedpreferences
																		.getString(
																				"Date",
																				"")
																		+ "00:00:00"));
													}
												});
											}
										}, 0, 1000);

										Daytext.setText(getDistanceOfDate2(sharedpreferences
												.getString("Date", "")) + "");

									}

									public String changeDate(int date) {
										String datestring;
										if (date < 10) {
											datestring = "0" + date;
										} else
											datestring = date + "";
										return datestring;
									}

								}).setNegativeButton("取消", null).show();
			}
		});

	}

	public void jilu_inti() {
		if (wordData2.contains("number")) {

			word.setText(wordData2.getString("number", ""));
		}

		if (finish_task_sharepreferences.contains("finisedjihua")) {
			task.setText(finish_task_sharepreferences.getString("finisedjihua",
					""));
		}

		if (finish_english_sencece.contains("number")) {
			duihua.setText(finish_english_sencece.getString("number", ""));
		}

		if (chinese_sencece.contains("number")) {
			gushi.setText(chinese_sencece.getString("number", ""));
		}
		if (isfrist.contains("frist")) {
			jianchi.setText(getDistanceOfDate3(isfrist.getString("frist", ""))
					+ "");
		}

		String str = calculate_over_person(
				Double.parseDouble(getDistanceOfDate3(isfrist.getString(
						"frist", "0")) + ""),
				Double.parseDouble(finish_task_sharepreferences.getString(
						"finisedjihua", "0")), Double.parseDouble(wordData2
						.getString("number", "0")),
				Double.parseDouble(chinese_sencece.getString("number", "0")),
				Double.parseDouble(finish_english_sencece.getString("number",
						"0")));
		String text = "通过你的坚持和努力，你已经超过了" + str + "的同学";
		SpannableStringBuilder style = new SpannableStringBuilder(text);
		style.setSpan(new ForegroundColorSpan(Color.rgb(0, 153, 204)), 16,
				16 + str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

		chaoguotongxue.setText(style);
	}

	public String getrandomString() {

		String jiyutextString[] = {
				"没有常胜，只有常省。",
				"笑看人生峰高处，唯有磨难多正果。",
				"勇者，必以决斗之勇气与五张试卷一决雌雄；懦夫，概以鼠目之寸光量人生此战必输无疑！",
				"你想是怎样的人，你就是怎样的人；你想成为怎样的人，你就会离这个目标不会太远。",
				"不要回避哪怕是一个简单得不好意思的问题，其实它对你很重要，其实它对别人也是一个了不起的难题。",
				"天道酬勤 勤能补拙 拼搏一年 天高地阔!",
				"不思，故有惑；不求，故无得；不问，故不知。",
				"一腔热血备中考，满腹经纶方成功。",
				"困难挑战机会助我成功，时间效率健康帮俺成材。",
				"悲观些看成功，乐观些看失败。轻松些看自己，宽容些看别人。理智些看问题，纯真些看人生。",
				"青春是打开了就合不上的书，人生是踏上了就回不了头的路，爱情是扔出了就收不回的赌注。",
				"每天把牢骚拿出来晒晒太阳，心情就不会缺钙。",
				"在人之上，要把人当人：在人之下 ，要把自己当人。",
				"路是大地一道难愈的伤痕，因此人生每一步都是隐隐的痛。",
				"有些故事的开始和结局造就注定了的，不管过程能用月光宝盒更改多少次。",
				"别总埋怨老天对你不公，其实老天根本不知道你是谁。",
				"所谓的低调就是不露痕迹的高调。",
				"为什么要在意别人的想法，那不是你应该管的。你看那些不被人理解的人，最后变成了什么。比如乔布斯",
				"选择我爱的，爱我选择的。",
				"成功就在你面前,只要你不顾一切的努力",
				"现在的痛苦都是以后幸福的前兆。",
				"不问收获，但问耕耘！天道酬勤。",
				"逝去的岁月，怎么找得回来？你曾经的微笑，在回忆里却散不开。",
				"为什么我们总要到过了很久， 总要等退无可退， 才知道我们曾经亲手舍弃的东西， 在后来的日子里再也遇不到了。",
				"不必每分钟都学习，但求学习中每分钟都有收获。",
				"不要自卑，你不比别人笨。不要自满，别人不比你笨。",
				"每道错题做三遍。第一遍：讲评时；第二遍：一周后；第三遍：考试前。",
				"没有平日的失败，就没有最终的成功。重要的是分析失败原因并吸取教训。",
				"对不起，我能创造奇迹！from(uSv令狐昂--送给所有走在拼搏道路上的同学！)",
				"没有目标就没有方向，每一个学习阶段都应该给自己树立一个目标。",
				"说穿了，其实提高成绩并不难，就看你是不是肯下功夫积累——多做题，多总结。",
				"这世界上没有不适合学习的人，只是有人没有找到适合自己的学习方法罢了。",
				"人有时是要勉强自己的。我们需要一种来自自身的强有力的能量推动自己闯出一个新的境界来。",
				"此刻打盹,你将做梦；而此刻学习,你将圆梦!",
				"勿将今日之事拖到明日。",
				"学习时的痛苦是暂时的,未学到的痛苦是终生的。",
				"学习并不是人生的全部。但既然连人生的一部分---学习也无法征服,还能做什么?",
				"只有比别人更早、更勤奋地努力，才能尝到成功的滋味。",
				"谁也不能随随便便地成功，它来自彻底的自我管理和毅力。",
				"伤口就像我一样，是个倔强的孩子，不肯愈合，因为内心是温暖潮湿的地方，适合任何东西生长。",
				"狗一样地学，绅士一样地玩。",
				"我就像现在一样看着你微笑,沉默,得意,失落,于是我跟着你开心也跟着你难过,只是我一直站在现在而你却永远停留过去",
				"一天过完，不会再来。",
				"风吹起如花般破碎的流年，而你的笑容摇晃摇晃，成为我命途中最美的点缀，看天，看雪，看季节深深的暗影。",
				"即使现在，对手也在不停地翻动书页。",
				"我流泪的时候，你也在流泪。我认为你没我孤独。 我微笑的时候，你还在流泪。原来你比我寂寞。",
				"再冷的石头，坐上三年也会暖。",
				"我决定不再流泪，就像你决定要离开我一般地坚定。只是一天离开了你，你就狼狈得像是一个只需要我安慰的孩子。",
				"小夕说，她看见明媚的阳光照耀在山上。我只是对着她微笑，没有说我看见整片悲伤的轮廓压在突兀的群山绿影中。",
				".不过，当我们决定了孤独地上路，一切的诅咒一切的背叛都丢在身后，我们可以倔强地微笑，难过地哭泣，可是依然把脚步继续铿锵。",
				"旁观者的姓名永远爬不到比赛的记分板上。",
				"伟人所达到并保持着的高度，并不是一飞就到的，而是他们在同伴们都睡着的时候，一步步艰辛地向上攀爬着。",
				"空想会想出很多绝妙的主意，但却办不成任何事情。",
				"成功是什么？就是走过了所有通向失败的路，只剩下一条路，那就是成功的路。",
				"很多事先天注定，那是“命”；但你可以决定怎么面对，那是“运”！",
				"三十年河东，三十年河西，莫欺少年穷！",
				"不大可能的事也许今天实现，根本不可能的事也许明天会实现。",
				"别人只能给你指路，而不能帮你走路，自己的人生路，还需要自己走。",
				" 人不是为失败而生的,一个人可以被消灭,但不能被打败。",
				"百里之行半九十。",
				"并不想营造某种如梦的哀愁，不肯让幸福如流沙般从指尖滑落，只是强烈地渴望着去投入生活，去融入那群背着名牌包，哼着流行歌曲不去忧伤的人群。",
				"人生如棋，走一步看一步是庸者，走一步算三步是常者，走一步定十步是智者。",
				"活在别人的掌声中，是禁不起考验的人。",
				"不要刻意去猜测他人的想法，如果你没有智慧与经验的正确判断，通常都会有错误的。",
				"要了解一个人，只需要看他的出发点与目的地是否相同，就可以知道他是否真心的。",
				"不洗澡的人，硬擦香水是不会香的。名声与尊贵，是来自于真才实学的。有德自然香。",
				"与其你去排斥它已成的事实，你不如去接受它。",
				"逆境是成长必经的过程，能勇于接受逆境的人，生命就会日渐的茁壮。",
				"如果你能像看别人缺点一样，如此准确般的发现自己的缺点，那么你的生命将会不平凡。",
				"你要感谢告诉你缺点的人。",
				"能为别人设想的人，永远不寂寞",
				"永远扭曲别人善意的人，无药可救。",
				"夸奖我们，赞叹我们的，这都不是名师。会讲我们，指示我们的，这才是良师，有了他们我们才会进步。",
				"你目前所拥有的都将随着你的死亡而成为他人的，那为何不现在就乐施给真正需要的人呢？",
				"你接受比抱怨还要好，对于不可改变的事实，你除了接受以外，没有更好的办法了。",
				"别人讲我们不好，不用生气、难过。说我们好也不用高兴，这不好中有好，好中有坏，就看你会不会用？",
				"当你的错误显露时，可不要发脾气，别以为任性或吵闹，可以隐藏或克服你的缺点。",
				"不要因为小小的争执，远离了你至亲的好友，也不要因为小小的怨恨，忘记了别人的大恩。",
				"一个常常看别人缺点的人，自己本身就不够好，因为他没有时间检讨他自己。",
				"虽然你讨厌一个人，但却又能发觉他的优点好处，像这样子有修养的人，天下真是太少了。",
				"大多数的人一辈子只做了三件事；自欺、欺人、被人欺。",
				"朋友老是为你挡风遮雨，假如你在远方承受风雪，而我无能为力，我也会祈祷，让那些风雪降临在我的身上。",
				"请珍惜你身边默默爱你的人。或许，有一天当他真的离开了。你会发现，离不开彼此的，是你，不是他。",
				"每个说不想谈恋爱的人，心里都装着一个不可能的人。",
				"人都是要经得起平淡的，孤独地向前，踏着雕刻的时光，一点一点，成长。",
				"与其说是别人让你痛苦，不如说自己的修养不够。",
				"别说别人可怜，自己更可怜，自己修行又如何？自己又懂得人生多少？",
				"如果可以和你在一起，我宁愿让天上所有的星星都陨落，因为你眼睛，是我生命里最亮的光芒。",
				"17岁时青春的尾巴，短暂而灰败；像一首钢琴曲的最后一个音符那样，无论用上多么高亢的调，结局都是消失与离开。",
				"朋友总是为你挡风遮雨，如果你在远方承受风雪，而我无能为力，我也会祈祷，让那些风雪降临在我的身上。",
				"在这个忧伤而明媚的三月，我从我单薄的青春里打马而过，穿过紫堇，穿过木棉，穿过时隐时现的悲喜和无常。",
				"成长就是这样，痛并快乐着。你得接受这个世界带给你的所有伤害，然后无所谓惧的长大。",
				"我站得太久说的太久了我自己都累了，你怎么还是听不懂？我写的太多了写得太久了我自己都累了，你怎么还是看不懂？",
				"天空的飞鸟，是你的寂寞比我多，还是我的忧伤比你多，剩下的时光，你陪我，好不好，这样你不寂寞，我也不会忧伤。",
				"人生路那么长，每个时刻都有人与自己邂逅、同行、离开。感激他们丰富了生命，然后就这样子，慢慢的成长了吧。",
				"青春理应勇猛过人而非怯懦怕事，应积极进取而非苟安现状。如此气概，二十后生虽有，六旬之人更甚。年岁有加，未必已垂老，理想若失，则已堕暮年。",
				"飞机场的骚乱一会儿就停止了，这里的人都是有着自己的方向的，匆匆地起飞，匆匆地下降，带走别人的故事，留下自己的回忆。",
				"我常常在思索我们的青春，它真是一个奇形怪状的玩意儿，短短的身子偏偏拖了一个长长的尾巴，像翅膀一样招摇着，久久不肯离去。",
				"青春不是年华，而是心境；青春不是桃面，丹唇，柔膝，而是深沉的意志，恢宏的理想，炽热的感情；青春是生命的源泉在不息的涌流。",
				"爱情本来并不复杂，来来去去不过三个字，不是我爱你、我恨你，便是算了吧、你好吗、对不起。",
				"原以为自己很坚强也很浪漫，也许每一个早恋的女孩都会这么想。其实走过以后才会知道，自己承受不住那样的负荷，因为还没到那个年龄。",
				"我一直都是美丽世界里的孤儿，孤单，寂寞，执着。一旦和温暖相遇，便注定了要溃不成军。",
				"你永远要宽恕众生，不论他有多坏，甚至他伤害过你，你一定要放下，才能得到真正的快乐。",
				"你不要一直不满人家，你应该一直检讨自己才对。不满人家，是苦了你自己。",
				"当你劝告别人时，若不顾及别人的自尊心，那么再好的言语都没有用的。",
				"忌妒别人，不会给自己增加任何的好处。忌妒别人，也不可能减少别人的成就。",
				"创造机会的人是勇者。等待机会的人是愚者。",
				"不是某人使我烦恼，而是我拿某人的言行来烦恼自己。",
				"寂寞的人有两种，一种是什么话都听的明白，一种则是什么都听不明白。",
				"人生没有彩排，每天都在现场直播。",
				"福报不够的人，就会常常听到是非；福报够的人，从来就没听到过是非。",
				"你永远要感谢给你逆境的众生。",
				"欲望得不到满足痛苦；欲望一旦满足就无聊，生命就是在痛苦和无聊之间摇摆。",
				" 狂妄的人有救，自卑的人没有救。",
				"要输就输给追求，要嫁就嫁给幸福。",
				"承认自己的伟大，就是认同自己的愚疑。",
				"如果你为着错过夕阳而哭泣，那么你就要错群星了",
				"人活着 总是要得罪一些人的 就要看那些人是否值得得罪 ",
				"我问上帝：怎样才可以对悲伤的事情一边笑一边忘记？  上帝回答：把自己弄的疯掉。",
				"因为陌生，所以勇敢，因为距离，所以美丽",
				"只有轮回继续转，日升月沉草木枯荣。是谁说过：时间仍在，是我们在飞逝。",
				"叶，只有在飞舞飘落的瞬间，才是最美丽动人的。",
				"一个人身边的位置只有那么多,你能给的也只有那么多,在这个狭小的圈子里,有些人要进来,就有一些人不得不离开。",
				"一个人总要走陌生的路,看陌生的风景,听陌生的歌,然后在某个不经意的瞬间,你会发现,原本费劲心机,想要忘记的事情真的就这么忘记了。 ",
				"如果回忆像钢铁般坚硬那么我是该微笑还是哭泣,如果钢铁记忆般腐蚀那这里是幻城,还是废墟。 ",
				"我总是告诉自己,就算有一天我们不在一起了,也要像在一起一样",
				"有些事情还没有讲完就算了吧,每个人都是一个国王,在自己的世界里纵横跋扈,你不要听我的,但你也不要让我听你的。",
				"在每个星光陨落的晚上,一遍一遍数我的寂寞。",
				"曾经也有一个笑容出现在我的生命里,可是最后还是如雾般消散,而那个笑容,就成为我中心深深埋藏的一条湍急河流,无法泅渡,那河流的声音,就成为我每日每夜绝望的歌唱。 ",
				"只要知道你还活在这个世上,我就可以了无牵挂。",
				"不经意间，时间不留一丝痕迹得离去。带给了我们几多惆怅，几多愁。沧桑与无奈也许是我们现在的符号，被迫的选择，被迫的接受。",
				"成长的滋味，深得体会，成长，成长，不要长大好不好。越长大越孤单，越长大越不安。",
				"打开心窗，想要阳光洒满每个角落，可它却那么吝啬，洒在窗口，照不到左心房。莫名的寂寞，莫名的惆怅，莫名的流泪，莫名的长大。",
				"当感觉疲惫时总是忍不住想起很多，以往的一幕幕，让人有一种莫名的心酸，心中的愁绪更浓，好想找一人倾诉，却发现身边的人都在忙碌，累了，才发现原来可以和自己说说心里话的人，好少…好少……",
				"华丽的姿态，能有几人知其内幕？逞强的成熟，又有谁能知其内心的幼稚？？",
				"还记得我们青春的摸样吗，多年之后，还会不会记得我们曾经傻傻的笑，一起漫步于校园。看着那一个个远去的背影，会不会我们也成为路人甲，随之消失在茫茫人海中。",
				"或许想要的很简单，或许很复杂，究竟青春岁月留给了自己什么。———只是那流年染指了那青春，那容颜。",
				"离开，悄无声息的，就如同幸福从来不曾出现过一样，我如同流星一般，划过天空，光芒诉说这我的悲伤，而归宿，是归于尘埃。",
				"偶然回想，已踏过的岁月。有多少值得保留，又有多少需要删除。泛黄的笔记，是那般陈旧。这年头什么最珍贵？",
				"生命中曾出现太多太多的美好让我拼尽全力去珍惜，然而结局却上演了独自生活，独自哀伤，一生孤独的流泪幸福。",
				"谁曾从谁的青春里走过，留下了笑靥；谁曾在谁的花季里停留，温暖了想念；谁又从谁的雨季里消失，泛滥了眼泪。",
				"我生命里的温暖就那么多，我全部给了你，但是你离开了我，你叫我以后怎么再对别人笑",
				"一起长大的约定，那样真心，与你有聊不完的曾经。而我已经分不清，你是友情，还是错过的爱情。",
				"在这个寂寞的晚上、在这样的一个空荡荡的房间里、我不再担心别人的讥笑我的脆弱我不在担心有人会打扰我我不再担心会让别人看见我的眼泪。",
				"曾经也有一个笑容出现在我的生命里，可是最后还是如雾般消散，而那个笑容，就成为我心中深深埋藏的一条湍急河流，无法泅渡，那河流的声音，就成为我每日每夜绝望的歌唱。 ",
				"我们的痛苦固然重要，但如果我们能为别人分到你痛苦，那么我们的痛苦就显得微不足道了。",
				"在生活中，所有人都有需要完成的使命和属于自己的位置，不要让任何事或任何人阻止我们认识和享受我们存在的美妙真谛。",
				"一个人得到整个世界，却失去了自我，又有何益？", "你战胜苦难，它就是你的财富；苦难战胜你，它就是你的屈辱。" };
		int j = jiyutextString.length;
		Random ran = new Random();
		int i = ran.nextInt(j);
		String randomjiyutext = jiyutextString[i];
		return randomjiyutext;

	}

	// 退出时注销广播

	@Override
	protected void onDestroy() {
		SpotManager.getInstance(this).unregisterSceenReceiver();
		super.onDestroy();
	}

}

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

	private TextView Daytext;// ����������textview
	private TextView fenzhongtext;// ����Сʱ��textview
	private TextView daojishi_min;// ���÷��ӵ�textview
	private TextView daojishi_miao;// ������textview
	private TextView riqitext;// �������ڵ�textview
	private TextView nianyuetext;// �������µ�textview
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

	public static int MODE = MODE_PRIVATE;// �������ģʽΪ˽��ģʽ
	public static final String PREFERENCE_NAME = "saveInfo";// ���ñ���ʱ���ļ�������

	public Calendar c;

	private SQLiteDatabase jiluDatabase;
	private YZZKDataBase YzzkDatabase;

	public SharedPreferences sharedpreferences;// ����һ��SharePreferences

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

		// ����������ӵķ�����ͨ��ConnectionDetector������ʵ��
		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

		Boolean isInternetPresent = cd.isConnectingToInternet(); // true or
																	// false����trueΪ�������ӳɹ�,����falseΪ��������ʧ��

		// ��ʼ�����
		AdManager.getInstance(this).init("4ae67375fba14129",
				"4eb7af786cfa05d1", true);
		// Ԥ���ز������
		SpotManager.getInstance(this).loadSpotAds();
		// չʾ�������
		SpotManager.getInstance(this).showSpotAds(this);
		// ��С���ʱ��
		SpotManager.getInstance(this).setShowInterval(400);
		// ��������Զ��ر�
		SpotManager.getInstance(this).setAutoCloseSpot(true);// �����Զ��رղ�������
		SpotManager.getInstance(this).setCloseTime(5000); // ���ùرղ���ʱ��
		// �û�����ͳ�ƹ���
		AdManager.getInstance(this).setUserDataCollect(true);

		// ʵ���������࣬������������
		c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// ����ʱ���õ���sharedpreferences��ʼ��
		sharedpreferences = getSharedPreferences(PREFERENCE_NAME, MODE);// �õ�һ��SharedPreferences����һ����Ϊ������ļ����ƣ��ڶ�������Ϊ�洢ģʽ

		// ��¼�������õ���sharedpreferences��ʼ��
		wordData2 = getSharedPreferences(name, MODE);
		finish_task_sharepreferences = getSharedPreferences(task_name, MODE);
		finish_english_sencece = getSharedPreferences(english_sencece_name,
				MODE);
		chinese_sencece = getSharedPreferences(chinese_sencece_name, MODE);
		isfrist = getSharedPreferences(frist_time, MODE);

		viewpager = (ViewPager) findViewById(R.id.viewpager);

		LayoutInflater inflater = getLayoutInflater();

		// viewpapger �� view����
		View view0 = inflater.inflate(R.layout.welcome, null);
		View view1 = inflater.inflate(R.layout.activity_main, null);
		View view2 = inflater.inflate(R.layout.jilu, null);
		View view3 = inflater.inflate(R.layout.activity_setting, null);

		listview_plan = (ListView) view3.findViewById(R.id.settingListViewId_0);
		listview_dream = (ListView) view3
				.findViewById(R.id.settingListViewId_1);
		listview_about = (ListView) view3
				.findViewById(R.id.settingListViewId_2);

		// ��������ʱ�����е�textview�ؼ�
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

		// ��¼�������õ���textview�ؼ�
		jianchi = (TextView) view2.findViewById(R.id.jianchi_date);
		task = (TextView) view2.findViewById(R.id.fnish_task_number);
		gushi = (TextView) view2.findViewById(R.id.fnish_gushi);
		duihua = (TextView) view2.findViewById(R.id.fnish_duihua);
		word = (TextView) view2.findViewById(R.id.fnish_word);
		chaoguotongxue = (TextView) view2.findViewById(R.id.chaoguodetongxue);

		// ѧϰ����listview��ʼ��
		listview_plan_inti();
		listview_dream_inti();
		listview_about_inti();
		// ����ʱ�����ʼ��
		daojishi_inti();
		// ��¼�����ʼ��
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

		// ���ñ���������view
		qingchutext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewpager.setCurrentItem(0, true);// ��һ������Ϊview�ı��
			}
		});
		// ����ʱ�����������¼�
		daojishitext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewpager.setCurrentItem(1, true);
			}
		});

		// ��¼������view�¼�
		jilu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewpager.setCurrentItem(2, true);
			}
		});

		// ѧϰ����������view�¼�
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

	// �����2�η����˳�����
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "���Ҫ�˳������Ǿ��ٰ�һ�ΰ�>_<",
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

	// ��ȡ����ʱ��Сʱ
	public String getDistanceOfDate_hour(String testDate) {
		Date date1 = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String str2 = testDate; // "yyyyMMdd"��ʽ �� 20131022
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHH:mm:ss");// �������ڵĸ�ʽ

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
				/ (1000 * 3600 * 24);// �Ӽ�������ɼ������
		double dayCount2 = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000);
		double dayCount3 = dayCount * 24 * 3600;
		double dayCount4 = dayCount2 - dayCount3;
		// System.out.println(dayCount4);
		double hourCount = dayCount4 / 3600;
		// System.out.println("hour--------------->"+ (int)hourCount);
		double mintnite = (dayCount4 - (int) hourCount * 3600) / 60;
		// System.out.println("����--------------->"+mintnite);
		double miao = dayCount4 - (int) hourCount * 3600 - (int) mintnite * 60;
		// System.out.println((int)hourCount+"Сʱ"+(int)mintnite+"����"+(int)miao+"��");
		// �޸�textview������

		return (int) hourCount + "";
	}

	// ��ȡ����ʱ�ķ���
	public String getDistanceOfDate_min(String testDate) {
		Date date1 = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String str2 = testDate; // "yyyyMMdd"��ʽ �� 20131022
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHH:mm:ss");// �������ڵĸ�ʽ

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
				/ (1000 * 3600 * 24);// �Ӽ�������ɼ������
		double dayCount2 = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000);
		double dayCount3 = dayCount * 24 * 3600;
		double dayCount4 = dayCount2 - dayCount3;
		// System.out.println(dayCount4);
		double hourCount = dayCount4 / 3600;
		// System.out.println("hour--------------->"+ (int)hourCount);
		double mintnite = (dayCount4 - (int) hourCount * 3600) / 60;
		// System.out.println("����--------------->"+mintnite);
		double miao = dayCount4 - (int) hourCount * 3600 - (int) mintnite * 60;
		// System.out.println((int)hourCount+"Сʱ"+(int)mintnite+"����"+(int)miao+"��");
		// �޸�textview������

		return (int) mintnite + "";
	}

	// ��ȡ����ʱ����
	public String getDistanceOfDate_miao(String testDate) {
		Date date1 = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String str2 = testDate; // "yyyyMMdd"��ʽ �� 20131022
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHH:mm:ss");// �������ڵĸ�ʽ

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
				/ (1000 * 3600 * 24);// �Ӽ�������ɼ������
		double dayCount2 = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000);
		double dayCount3 = dayCount * 24 * 3600;
		double dayCount4 = dayCount2 - dayCount3;
		// System.out.println(dayCount4);
		double hourCount = dayCount4 / 3600;
		// System.out.println("hour--------------->"+ (int)hourCount);
		double mintnite = (dayCount4 - (int) hourCount * 3600) / 60;
		// System.out.println("����--------------->"+mintnite);
		double miao = dayCount4 - (int) hourCount * 3600 - (int) mintnite * 60;
		// System.out.println((int)hourCount+"Сʱ"+(int)mintnite+"����"+(int)miao+"��");
		// �޸�textview������

		return (int) miao + "";

	}

	// ��ȡ����ʱ������
	public int getDistanceOfDate2(String testDate) {
		Date date1 = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String str2 = testDate; // "yyyyMMdd"��ʽ �� 20131022
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");// �������ڵĸ�ʽ

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
				/ (1000 * 3600 * 24);// �Ӽ�������ɼ������
		int intdayCount = (int) dayCount;
		return intdayCount;
	}

	// ��int�͵��·ݱ�ΪӢ�ļ�д
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
	 * ����ʹ�������ʱ��
	 * 
	 * @param testDate
	 *            ��һ��ʹ����������ڣ�ͨ��isfrist.java�б��������
	 * @return �뵱ǰʱ��Ĳ�ֵ��Ĭ�ϼ�һ
	 */
	public int getDistanceOfDate3(String testDate) {
		Date date1 = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String str2 = testDate; // "yyyyMMdd"��ʽ �� 20131022
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");// �������ڵĸ�ʽ

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
				/ (1000 * 3600 * 24);// �Ӽ�������ɼ������
		int intdayCount = (int) dayCount;

		return intdayCount + 1;

	}

	/**
	 * 
	 * ���㳬����ͬѧ����
	 * 
	 * @param insist_number
	 *            �������
	 * @param finished_task
	 *            �������
	 * @param master_word
	 *            ���յ���
	 * @param master_pome
	 *            ����ʫ��
	 * @param master_dialog
	 *            ���նԻ�
	 * @return ����������String
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

		String[] function_0 = new String[] { "ÿ������", "Ӣ�ﵥ��", "Ӣ��Ի�", "����ʫ��" };
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
		String[] function_1 = new String[] { "�ҵ�����", "�ҵĳɼ�" };
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
		String[] function_2 = new String[] { "���ѷ���", "��ע����", "֧�ֿ�����" };
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
		// ����textview��settext
		riqitext.setText(changeDateto_(c));
		nianyuetext.setText(changeDate(mMonth) + "," + mYear);

		Button setDate = (Button) findViewById(R.id.setDate);

		final AssetManager mgr = getAssets();// �õ�AssetManager

		// ������ʱ��������1��ˢ��һ�Σ�������Ƶ���ʱСʱ����
		Timer timer = new Timer();

		if (sharedpreferences.contains("Date")) {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {

					// TODO Auto-generated method stub
					// �������߳����޷�����UI������Ҫ�����̵߳�����ͨ��hanler�������߳��У��ٽ���UI���޸�
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
						.setTitle("��ѡ�������п�ʱ��")
						.setView(datepicker = new DatePicker(MainActivity.this))
						.setPositiveButton("ȷ��",
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
												PREFERENCE_NAME, MODE);// ͨ��getSharedPreferences(String
																		// name,int
																		// mode)�õ�SharedPreferences�ӿڡ��÷����ĵ�һ���������ļ����ƣ��ڶ��������ǲ���ģʽ
										SharedPreferences.Editor editor = sharedpreferences
												.edit();// ����SharedPreferences.Editor������SharedPreferences�����޸�
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
												// �������߳����޷�����UI������Ҫ�����̵߳�����ͨ��hanler�������߳��У��ٽ���UI���޸�
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

								}).setNegativeButton("ȡ��", null).show();
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
		String text = "ͨ����ļ�ֺ�Ŭ�������Ѿ�������" + str + "��ͬѧ";
		SpannableStringBuilder style = new SpannableStringBuilder(text);
		style.setSpan(new ForegroundColorSpan(Color.rgb(0, 153, 204)), 16,
				16 + str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

		chaoguotongxue.setText(style);
	}

	public String getrandomString() {

		String jiyutextString[] = {
				"û�г�ʤ��ֻ�г�ʡ��",
				"Ц��������ߴ���Ψ��ĥ�Ѷ�������",
				"���ߣ����Ծ���֮�����������Ծ�һ�����ۣ�ų�򣬸�����Ŀ֮�����������ս�������ɣ�",
				"�������������ˣ�������������ˣ������Ϊ�������ˣ���ͻ������Ŀ�겻��̫Զ��",
				"��Ҫ�ر�������һ���򵥵ò�����˼�����⣬��ʵ���������Ҫ����ʵ���Ա���Ҳ��һ���˲�������⡣",
				"������� ���ܲ�׾ ƴ��һ�� ��ߵ���!",
				"��˼�����л󣻲��󣬹��޵ã����ʣ��ʲ�֪��",
				"һǻ��Ѫ���п����������ڷ��ɹ���",
				"������ս�������ҳɹ���ʱ��Ч�ʽ����ﰳ�ɲġ�",
				"����Щ���ɹ����ֹ�Щ��ʧ�ܡ�����Щ���Լ�������Щ�����ˡ�����Щ�����⣬����Щ��������",
				"�ഺ�Ǵ��˾ͺϲ��ϵ��飬������̤���˾ͻز���ͷ��·���������ӳ��˾��ղ��صĶ�ע��",
				"ÿ�����ɧ�ó���ɹɹ̫��������Ͳ���ȱ�ơ�",
				"����֮�ϣ�Ҫ���˵��ˣ�����֮�� ��Ҫ���Լ����ˡ�",
				"·�Ǵ��һ���������˺ۣ��������ÿһ������������ʹ��",
				"��Щ���µĿ�ʼ�ͽ�����ע���˵ģ����ܹ��������¹ⱦ�и��Ķ��ٴΡ�",
				"������Թ������㲻������ʵ���������֪������˭��",
				"��ν�ĵ͵����ǲ�¶�ۼ��ĸߵ���",
				"ΪʲôҪ������˵��뷨���ǲ�����Ӧ�ùܵġ��㿴��Щ�����������ˣ��������ʲô�������ǲ�˹",
				"ѡ���Ұ��ģ�����ѡ��ġ�",
				"�ɹ���������ǰ,ֻҪ�㲻��һ�е�Ŭ��",
				"���ڵ�ʹ�඼���Ժ��Ҹ���ǰ�ס�",
				"�����ջ񣬵��ʸ��ţ�������ڡ�",
				"��ȥ�����£���ô�ҵû�������������΢Ц���ڻ�����ȴɢ������",
				"Ϊʲô������Ҫ�����˺ܾã� ��Ҫ�����޿��ˣ� ��֪�������������������Ķ����� �ں�������������Ҳ�������ˡ�",
				"����ÿ���Ӷ�ѧϰ������ѧϰ��ÿ���Ӷ����ջ�",
				"��Ҫ�Ա����㲻�ȱ��˱�����Ҫ���������˲����㱿��",
				"ÿ�����������顣��һ�飺����ʱ���ڶ��飺һ�ܺ󣻵����飺����ǰ��",
				"û��ƽ�յ�ʧ�ܣ���û�����յĳɹ�����Ҫ���Ƿ���ʧ��ԭ����ȡ��ѵ��",
				"�Բ������ܴ����漣��from(uSv�����--�͸���������ƴ����·�ϵ�ͬѧ��)",
				"û��Ŀ���û�з���ÿһ��ѧϰ�׶ζ�Ӧ�ø��Լ�����һ��Ŀ�ꡣ",
				"˵���ˣ���ʵ��߳ɼ������ѣ��Ϳ����ǲ��ǿ��¹�����ۡ��������⣬���ܽᡣ",
				"��������û�в��ʺ�ѧϰ���ˣ�ֻ������û���ҵ��ʺ��Լ���ѧϰ�������ˡ�",
				"����ʱ��Ҫ��ǿ�Լ��ġ�������Ҫһ�����������ǿ�����������ƶ��Լ�����һ���µľ�������",
				"�˿̴���,�㽫���Σ����˿�ѧϰ,�㽫Բ��!",
				"�𽫽���֮���ϵ����ա�",
				"ѧϰʱ��ʹ������ʱ��,δѧ����ʹ���������ġ�",
				"ѧϰ������������ȫ��������Ȼ��������һ����---ѧϰҲ�޷�����,������ʲô?",
				"ֻ�бȱ��˸��硢���ڷܵ�Ŭ�������ܳ����ɹ�����ζ��",
				"˭Ҳ����������سɹ��������Գ��׵����ҹ����������",
				"�˿ھ�����һ�����Ǹ���ǿ�ĺ��ӣ��������ϣ���Ϊ��������ů��ʪ�ĵط����ʺ��κζ���������",
				"��һ����ѧ����ʿһ�����档",
				"�Ҿ�������һ��������΢Ц,��Ĭ,����,ʧ��,�����Ҹ����㿪��Ҳ�������ѹ�,ֻ����һֱվ�����ڶ���ȴ��Զͣ����ȥ",
				"һ����꣬����������",
				"�紵���绨����������꣬�����Ц��ҡ��ҡ�Σ���Ϊ����;�������ĵ�׺�����죬��ѩ������������İ�Ӱ��",
				"��ʹ���ڣ�����Ҳ�ڲ�ͣ�ط�����ҳ��",
				"�������ʱ����Ҳ�����ᡣ����Ϊ��û�ҹ¶��� ��΢Ц��ʱ���㻹�����ᡣԭ������Ҽ�į��",
				"�����ʯͷ����������Ҳ��ů��",
				"�Ҿ����������ᣬ���������Ҫ�뿪��һ��ؼᶨ��ֻ��һ���뿪���㣬����Ǳ�������һ��ֻ��Ҫ�Ұ�ο�ĺ��ӡ�",
				"СϦ˵�����������ĵ�������ҫ��ɽ�ϡ���ֻ�Ƕ�����΢Ц��û��˵�ҿ�����Ƭ���˵�����ѹ��ͻأ��Ⱥɽ��Ӱ�С�",
				".�����������Ǿ����˹¶�����·��һ�е�����һ�еı��Ѷ�����������ǿ��Ծ�ǿ��΢Ц���ѹ��ؿ�����������Ȼ�ѽŲ�������ϡ�",
				"�Թ��ߵ�������Զ�����������ļǷְ��ϡ�",
				"ΰ�����ﵽ�������ŵĸ߶ȣ�������һ�ɾ͵��ģ�����������ͬ���Ƕ�˯�ŵ�ʱ��һ�������������������š�",
				"���������ܶ��������⣬��ȴ�첻���κ����顣",
				"�ɹ���ʲô�������߹�������ͨ��ʧ�ܵ�·��ֻʣ��һ��·���Ǿ��ǳɹ���·��",
				"�ܶ�������ע�������ǡ�������������Ծ�����ô��ԣ����ǡ��ˡ���",
				"��ʮ��Ӷ�����ʮ�������Ī�������",
				"������ܵ���Ҳ�����ʵ�֣����������ܵ���Ҳ�������ʵ�֡�",
				"����ֻ�ܸ���ָ·�������ܰ�����·���Լ�������·������Ҫ�Լ��ߡ�",
				" �˲���Ϊʧ�ܶ�����,һ���˿��Ա�����,�����ܱ���ܡ�",
				"����֮�а��ʮ��",
				"������Ӫ��ĳ�����εİ���������Ҹ�����ɳ���ָ�⻬�䣬ֻ��ǿ�ҵؿ�����ȥͶ�����ȥ������Ⱥ�������ư����������и�����ȥ���˵���Ⱥ��",
				"�������壬��һ����һ����ӹ�ߣ���һ���������ǳ��ߣ���һ����ʮ�������ߡ�",
				"���ڱ��˵������У��ǽ���������ˡ�",
				"��Ҫ����ȥ�²����˵��뷨�������û���ǻ��뾭�����ȷ�жϣ�ͨ�������д���ġ�",
				"Ҫ�˽�һ���ˣ�ֻ��Ҫ�����ĳ�������Ŀ�ĵ��Ƿ���ͬ���Ϳ���֪�����Ƿ����ĵġ�",
				"��ϴ����ˣ�Ӳ����ˮ�ǲ�����ġ���������������������ʵѧ�ġ��е���Ȼ�㡣",
				"������ȥ�ų����ѳɵ���ʵ���㲻��ȥ��������",
				"�澳�ǳɳ��ؾ��Ĺ��̣������ڽ����澳���ˣ������ͻ��ս�����׳��",
				"��������񿴱���ȱ��һ�������׼ȷ��ķ����Լ���ȱ�㣬��ô����������᲻ƽ����",
				"��Ҫ��л������ȱ����ˡ�",
				"��Ϊ����������ˣ���Զ����į",
				"��ԶŤ������������ˣ���ҩ�ɾȡ�",
				"�佱���ǣ���̾���ǵģ��ⶼ������ʦ���ὲ���ǣ�ָʾ���ǵģ��������ʦ�������������ǲŻ������",
				"��Ŀǰ��ӵ�еĶ������������������Ϊ���˵ģ���Ϊ�β����ھ���ʩ��������Ҫ�����أ�",
				"����ܱȱ�Թ��Ҫ�ã����ڲ��ɸı����ʵ������˽������⣬û�и��õİ취�ˡ�",
				"���˽����ǲ��ã������������ѹ���˵���Ǻ�Ҳ���ø��ˣ��ⲻ�����кã������л����Ϳ���᲻���ã�",
				"����Ĵ�����¶ʱ���ɲ�Ҫ��Ƣ��������Ϊ���Ի��֣��������ػ�˷����ȱ�㡣",
				"��Ҫ��ΪСС����ִ��Զ���������׵ĺ��ѣ�Ҳ��Ҫ��ΪСС��Թ�ޣ������˱��˵Ĵ����",
				"һ������������ȱ����ˣ��Լ�����Ͳ����ã���Ϊ��û��ʱ��������Լ���",
				"��Ȼ������һ���ˣ���ȴ���ܷ��������ŵ�ô��������������������ˣ���������̫���ˡ�",
				"���������һ����ֻ���������£����ۡ����ˡ������ۡ�",
				"��������Ϊ�㵲�����꣬��������Զ�����ܷ�ѩ����������Ϊ������Ҳ����������Щ��ѩ�������ҵ����ϡ�",
				"����ϧ�����ĬĬ������ˡ�������һ�쵱������뿪�ˡ���ᷢ�֣��벻���˴˵ģ����㣬��������",
				"ÿ��˵����̸�������ˣ����ﶼװ��һ�������ܵ��ˡ�",
				"�˶���Ҫ������ƽ���ģ��¶�����ǰ��̤�ŵ�̵�ʱ�⣬һ��һ�㣬�ɳ���",
				"����˵�Ǳ�������ʹ�࣬����˵�Լ�������������",
				"��˵���˿������Լ����������Լ���������Σ��Լ��ֶ����������٣�",
				"������Ժ�����һ������Ը���������е����Ƕ����䣬��Ϊ���۾������������������Ĺ�â��",
				"17��ʱ�ഺ��β�ͣ����ݶ��Ұܣ���һ�׸����������һ�������������������϶�ô�߿��ĵ�����ֶ�����ʧ���뿪��",
				"��������Ϊ�㵲�����꣬�������Զ�����ܷ�ѩ����������Ϊ������Ҳ����������Щ��ѩ�������ҵ����ϡ�",
				"��������˶����ĵ����£��Ҵ��ҵ������ഺ������������������������ľ�ޣ�����ʱ��ʱ�ֵı�ϲ���޳���",
				"�ɳ�����������ʹ�������š���ý���������������������˺���Ȼ������ν��ĳ���",
				"��վ��̫��˵��̫�������Լ������ˣ�����ô��������������д��̫����д��̫�������Լ������ˣ�����ô���ǿ�������",
				"��յķ�������ļ�į���Ҷ࣬�����ҵ����˱���࣬ʣ�µ�ʱ�⣬�����ң��ò��ã������㲻��į����Ҳ�������ˡ�",
				"����·��ô����ÿ��ʱ�̶��������Լ����ˡ�ͬ�С��뿪���м����Ƿḻ��������Ȼ��������ӣ������ĳɳ��˰ɡ�",
				"�ഺ��Ӧ���͹��˶�����ų���£�Ӧ������ȡ���ǹ�����״��������ţ���ʮ�������У���Ѯ֮�˸����������мӣ�δ���Ѵ��ϣ�������ʧ�����Ѷ�ĺ�ꡣ",
				"�ɻ�����ɧ��һ�����ֹͣ�ˣ�������˶��������Լ��ķ���ģ��Ҵҵ���ɣ��Ҵҵ��½������߱��˵Ĺ��£������Լ��Ļ��䡣",
				"�ҳ�����˼�����ǵ��ഺ��������һ�����ι�״����������̶̵�����ƫƫ����һ��������β�ͣ�����һ����ҡ�ţ��þò�����ȥ��",
				"�ഺ�����껪�������ľ����ഺ�������棬��������ϥ�������������־���ֺ�����룬���ȵĸ��飻�ഺ��������ԴȪ�ڲ�Ϣ��ӿ����",
				"���鱾���������ӣ�����ȥȥ���������֣������Ұ��㡢�Һ��㣬�������˰ɡ�����𡢶Բ���",
				"ԭ��Ϊ�Լ��ܼ�ǿҲ��������Ҳ��ÿһ��������Ů��������ô�롣��ʵ�߹��Ժ�Ż�֪�����Լ����ܲ�ס�����ĸ��ɣ���Ϊ��û���Ǹ����䡣",
				"��һֱ��������������Ĺ¶����µ�����į��ִ�š�һ������ů��������ע����Ҫ�����ɾ���",
				"����ԶҪ��ˡ�������������ж໵���������˺����㣬��һ��Ҫ���£����ܵõ������Ŀ��֡�",
				"�㲻Ҫһֱ�����˼ң���Ӧ��һֱ�����Լ��Ŷԡ������˼ң��ǿ������Լ���",
				"����Ȱ�����ʱ�������˼����˵������ģ���ô�ٺõ����ﶼû���õġ�",
				"�ɶʱ��ˣ�������Լ������κεĺô����ɶʱ��ˣ�Ҳ�����ܼ��ٱ��˵ĳɾ͡�",
				"���������������ߡ��ȴ�������������ߡ�",
				"����ĳ��ʹ�ҷ��գ���������ĳ�˵������������Լ���",
				"��į���������֣�һ����ʲô�����������ף�һ������ʲô���������ס�",
				"����û�в��ţ�ÿ�춼���ֳ�ֱ����",
				"�����������ˣ��ͻ᳣�������Ƿǣ����������ˣ�������û�������Ƿǡ�",
				"����ԶҪ��л�����澳��������",
				"�����ò�������ʹ�ࣻ����һ����������ģ�����������ʹ�������֮��ҡ�ڡ�",
				" ���������оȣ��Ա�����û�оȡ�",
				"Ҫ������׷��Ҫ�޾ͼ޸��Ҹ���",
				"�����Լ���ΰ�󣬾�����ͬ�Լ������ɡ�",
				"�����Ϊ�Ŵ��Ϧ������������ô���Ҫ��Ⱥ����",
				"�˻��� ����Ҫ����һЩ�˵� ��Ҫ����Щ���Ƿ�ֵ�õ��� ",
				"�����ϵۣ������ſ��ԶԱ��˵�����һ��Цһ�����ǣ�  �ϵۻش𣺰��Լ�Ū�ķ����",
				"��Ϊİ���������¸ң���Ϊ���룬��������",
				"ֻ���ֻؼ���ת�������³���ľ���١���˭˵����ʱ�����ڣ��������ڷ��š�",
				"Ҷ��ֻ���ڷ���Ʈ���˲�䣬�������������˵ġ�",
				"һ������ߵ�λ��ֻ����ô��,���ܸ���Ҳֻ����ô��,�������С��Ȧ����,��Щ��Ҫ����,����һЩ�˲��ò��뿪��",
				"һ������Ҫ��İ����·,��İ���ķ羰,��İ���ĸ�,Ȼ����ĳ���������˲��,��ᷢ��,ԭ���Ѿ��Ļ�,��Ҫ���ǵ�������ľ���ô�����ˡ� ",
				"���������������Ӳ��ô���Ǹ�΢Ц���ǿ���,�����������㸯ʴ�������ǻó�,���Ƿ��档 ",
				"�����Ǹ����Լ�,������һ�����ǲ���һ����,ҲҪ����һ��һ��",
				"��Щ���黹û�н�������˰�,ÿ���˶���һ������,���Լ����������ݺ����,�㲻Ҫ���ҵ�,����Ҳ��Ҫ��������ġ�",
				"��ÿ���ǹ����������,һ��һ�����ҵļ�į��",
				"����Ҳ��һ��Ц�ݳ������ҵ�������,����������������ɢ,���Ǹ�Ц��,�ͳ�Ϊ������������ص�һ���ļ�����,�޷�����,�Ǻ���������,�ͳ�Ϊ��ÿ��ÿҹ�����ĸ質�� ",
				"ֻҪ֪���㻹�����������,�ҾͿ�������ǣ�ҡ�",
				"������䣬ʱ�䲻��һ˿�ۼ�����ȥ�����������Ǽ�����꣬������ɣ������Ҳ�����������ڵķ��ţ����ȵ�ѡ�񣬱��ȵĽ��ܡ�",
				"�ɳ�����ζ�������ᣬ�ɳ����ɳ�����Ҫ����ò��á�Խ����Խ�µ���Խ����Խ������",
				"���Ĵ�����Ҫ��������ÿ�����䣬����ȴ��ô���ģ����ڴ��ڣ��ղ������ķ���Ī���ļ�į��Ī������꣬Ī�������ᣬĪ���ĳ���",
				"���о�ƣ��ʱ�����̲�ס����ܶ࣬������һĻĻ��������һ��Ī�������ᣬ���еĳ�����Ũ��������һ�����ߣ�ȴ������ߵ��˶���æµ�����ˣ��ŷ���ԭ�����Ժ��Լ�˵˵���ﻰ���ˣ����١����١���",
				"��������̬�����м���֪����Ļ����ǿ�ĳ��죬����˭��֪�����ĵ����ɣ���",
				"���ǵ������ഺ�������𣬶���֮�󣬻��᲻��ǵ���������ɵɵ��Ц��һ��������У԰��������һ����Զȥ�ı�Ӱ���᲻������Ҳ��Ϊ·�˼ף���֮��ʧ��ãã�˺��С�",
				"������Ҫ�ĺܼ򵥣�����ܸ��ӣ������ഺ�����������Լ�ʲô��������ֻ��������Ⱦָ�����ഺ�������ա�",
				"�뿪��������Ϣ�ģ�����ͬ�Ҹ������������ֹ�һ��������ͬ����һ�㣬������գ���â��˵���ҵı��ˣ������ޣ��ǹ��ڳ�����",
				"żȻ���룬��̤�������¡��ж���ֵ�ñ��������ж�����Ҫɾ�������Ƶıʼǣ����ǰ�¾ɡ�����ͷʲô�����",
				"������������̫��̫�����������ƴ��ȫ��ȥ��ϧ��Ȼ�����ȴ�����˶���������԰��ˣ�һ���¶��������Ҹ���",
				"˭����˭���ഺ���߹���������Ц�̣�˭����˭�Ļ�����ͣ������ů�����˭�ִ�˭���꼾����ʧ�����������ᡣ",
				"�����������ů����ô�࣬��ȫ�������㣬�������뿪���ң�������Ժ���ô�ٶԱ���Ц",
				"һ�𳤴��Լ�����������ģ��������Ĳ���������������Ѿ��ֲ��壬�������飬���Ǵ���İ��顣",
				"�������į�����ϡ���������һ���յ����ķ�����Ҳ��ٵ��ı��˵ļ�Ц�ҵĴ����Ҳ��ڵ������˻�������Ҳ��ٵ��Ļ��ñ��˿����ҵ����ᡣ",
				"����Ҳ��һ��Ц�ݳ������ҵ����������������������ɢ�����Ǹ�Ц�ݣ��ͳ�Ϊ������������ص�һ���ļ��������޷����ɣ��Ǻ������������ͳ�Ϊ��ÿ��ÿҹ�����ĸ質�� ",
				"���ǵ�ʹ���Ȼ��Ҫ�������������Ϊ���˷ֵ���ʹ�࣬��ô���ǵ�ʹ����Ե�΢������ˡ�",
				"�������У������˶�����Ҫ��ɵ�ʹ���������Լ���λ�ã���Ҫ���κ��»��κ�����ֹ������ʶ���������Ǵ��ڵ��������С�",
				"һ���˵õ��������磬ȴʧȥ�����ң����к��棿", "��սʤ���ѣ���������ĲƸ�������սʤ�㣬������������衣" };
		int j = jiyutextString.length;
		Random ran = new Random();
		int i = ran.nextInt(j);
		String randomjiyutext = jiyutextString[i];
		return randomjiyutext;

	}

	// �˳�ʱע���㲥

	@Override
	protected void onDestroy() {
		SpotManager.getInstance(this).unregisterSceenReceiver();
		super.onDestroy();
	}

}

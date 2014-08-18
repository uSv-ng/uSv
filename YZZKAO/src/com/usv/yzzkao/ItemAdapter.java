package com.usv.yzzkao;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.usv.data.YZZKDataBase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<String> datas, timelist;
	private int[] bgdatas;
	
	private YZZKDataBase YZZKDB;
	private SQLiteDatabase sdbw, sdbr;

	public ItemAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	public void setData(ArrayList<String> datas, int[] bgdatas, ArrayList<String> timelist) {
		this.datas = datas;
		this.bgdatas = bgdatas;
		this.timelist = timelist;
	}

	public int getCount() {
		return datas.size();
	}

	public Object getItem(int position) {
		return datas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.list_of_task, null);
			holder.textView = (TextView) convertView.findViewById(R.id.tv_item);
			holder.coating = (TextView) convertView.findViewById(R.id.tv_coating);
			holder.functions = (TextView) convertView.findViewById(R.id.tv_functions);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String time = timelist.get(position);
		Calendar c = Calendar.getInstance();
		String Year = c.get(Calendar.YEAR) + "";
		String Month = (c.get(Calendar.MONTH) + 1) + "";
		String Day = c.get(Calendar.DAY_OF_MONTH) + "";
		String nowtime = Year + "." + Month + "." + Day;
		
		if(time.equals(nowtime)) {
			time = "今天";
		}
		
		//position 从0开始的
		holder.textView.setText(datas.get(position));
		holder.coating.setText(time);
		holder.textView.setBackgroundResource(bgdatas[position]);
		holder.coating.setVisibility(View.VISIBLE);
		
		holder.functions.setClickable(false);
		
		return convertView;
	}

	public final class ViewHolder {
		public TextView textView;
		public TextView coating;
		public TextView functions;
	}
}

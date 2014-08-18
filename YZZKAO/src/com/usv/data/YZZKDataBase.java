package com.usv.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class YZZKDataBase extends SQLiteOpenHelper {

	
	public YZZKDataBase(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	public YZZKDataBase(Context context, String name, CursorFactory factory) {
		super(context, name, factory, 1);
	}
	
	public YZZKDataBase(Context context, String name) {
		super(context, name, null, 1);
	}
	public YZZKDataBase(Context context) {
		super(context, "yzzkDB", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}

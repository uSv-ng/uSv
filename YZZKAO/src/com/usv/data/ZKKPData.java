package com.usv.data;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ZKKPData extends SQLiteOpenHelper {
	public static String DATABASE_NAME = "zkzsd.db";
	public static String DATABASE_PATH = "/sdcard/YZZK/";
	public static int DATABASE_VERSION = 1;
	private File file = null;
	private SQLiteDatabase db = null;
	public Cursor rs = null;

	public ZKKPData(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	private void OpenDB() {
		file = new File(DATABASE_PATH + DATABASE_NAME);
		if (file != null) {
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
	}
	
	
	public void execSqlComment(String sql) {
		OpenDB();
		db.execSQL(sql);
		CloseDB();
	}
	
	public void CloseDB() {
		if(db != null) {
			db.close();
		}
	}
	
	public Cursor OnSelect(String sql) {
		OpenDB();
		rs = db.rawQuery(sql, null);
		return rs;
	}
	
	public SQLiteDatabase getDatabaseObj() {
		OpenDB();
		return db;
	}
}

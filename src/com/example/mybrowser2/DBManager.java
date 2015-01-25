package com.example.mybrowser2;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DBManager {
	private Context context;
	private MySQLiteOpenHelper helper;

	public DBManager(Context context) {
		this.context = context;
		helper = new MySQLiteOpenHelper(context);
	}
	
	public void add(String title, String url, String timestamp) {
		SQLiteDatabase db = helper.getWritableDatabase();

		db.execSQL("insert into history (title, url, timestamp) values (?, ?, ?)",
				new Object[] { title, url, timestamp });
	}
	
	public List<HistoryItem> findAll() {
		SQLiteDatabase db = helper.getWritableDatabase();
		List<HistoryItem> items = new ArrayList<HistoryItem>();
		Cursor cursor = db.rawQuery("select * from history", null);

		while (cursor.moveToNext()) {
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String url = cursor.getString(cursor.getColumnIndex("url"));
			int timestamp = cursor.getInt(cursor.getColumnIndex("timestamp"));
			
			HistoryItem p = new HistoryItem(title, url, timestamp);
			items.add(p);
		}
		db.close();
		return items;
	}
}
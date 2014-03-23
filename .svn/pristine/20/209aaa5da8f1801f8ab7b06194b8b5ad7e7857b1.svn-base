package com.bjor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database help class; an extension of the SQLiteOpenHelper class
 * 
 * @author Arnar Orri Eyj&#243lfsson
 * @version 1 
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

	/** Database strings; used to communicate with the database */
	public static final String TABLE_HISTORY = "history";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TIME = "time";
	public static final String COLUMN_BUZZ = "buzz";
	public static final String COLUMN_NOD = "nod";
	public static final String COLUMN_DURATION = "duration";
	public static final String COLUMN_TYPE = "type";

	/** Database name and version */
	public static final String DATABASE_NAME = "history.db";
	private static final int DATABASE_VERSION = 1;

	/** Database creation sql statement */
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_HISTORY + "( " +
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_TIME + " nvarchar(256) not null," +
			COLUMN_BUZZ + " int not null," +
			COLUMN_NOD + " int not null," + 
			COLUMN_DURATION + " int not null," +
			COLUMN_TYPE + " int not null);";

	/**
	 * Constructor
	 * @param context Application context
	 */
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
		onCreate(db);
	}

}

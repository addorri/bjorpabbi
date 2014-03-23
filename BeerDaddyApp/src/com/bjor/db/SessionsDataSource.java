package com.bjor.db;

import java.util.ArrayList;
import java.util.List;

import com.bjor.models.Global;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author arnar
 * @version 1
 * 
 * This class can best be described as a database manipulator.
 *
 */
public class SessionsDataSource {

	// Database fields
	/** An instance of an SQLiteDatabase */
	private SQLiteDatabase database;
	/** An instance of a Database helper */
	private MySQLiteHelper dbHelper;
	/** String array of the columns in our database */
	private String[] allColumns = { 
			MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_TIME,
			MySQLiteHelper.COLUMN_BUZZ,
			MySQLiteHelper.COLUMN_NOD,
			MySQLiteHelper.COLUMN_DURATION,
			MySQLiteHelper.COLUMN_TYPE };

	/**
	 * Constructor 
	 * @param context Application context
	 */
	public SessionsDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	/**
	 * Opens a writable database
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	/**
	 * Closes the database
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * Creates a new instance of Session and adds it to the database
	 * @return A new instance of Session
	 */
	public Session createSession() {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TIME, "");
		values.put(MySQLiteHelper.COLUMN_BUZZ, Global.getBuzz());
		values.put(MySQLiteHelper.COLUMN_NOD, Global.getNod());
		values.put(MySQLiteHelper.COLUMN_DURATION, Global.getDuration());
		values.put(MySQLiteHelper.COLUMN_TYPE, Global.getType());
		long insertId = database.insert(MySQLiteHelper.TABLE_HISTORY, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_HISTORY,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Session newComment = cursorToSession(cursor);
		cursor.close();
		return newComment;
	}

	/**
	 * Deletes a selected Session from database
	 * @param session Session to delete
	 */
	public void deleteSession(Session session) {
		long id = session.getId();
		System.out.println("Session deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_HISTORY, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	/**
	 * Lists all Sessions in database
	 * @return A List of Sessions
	 */
	public List<Session> getAllSessions() {
		List<Session> sessions = new ArrayList<Session>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_HISTORY,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Session comment = cursorToSession(cursor);
			sessions.add(comment);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return sessions;
	}

	/**
	 * Converts a Cursor to Session
	 * @param cursor Cursor to convert
	 * @return Cursor converted to a Session
	 */
	private Session cursorToSession(Cursor cursor) {
		Session session = new Session();
		session.setId(cursor.getLong(0));
		session.setTime(cursor.getString(1));
		session.setBuzz(cursor.getInt(2));
		session.setNod(cursor.getInt(3));
		session.setDuration(cursor.getInt(4));
		session.setType(cursor.getInt(5));
		return session;
	}
}

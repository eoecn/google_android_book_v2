package cn.eoe.wiki.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import cn.eoe.wiki.utils.L;
/**
 * 为程序提供数据的支持
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-2
 * @version 1.0.0
 */
public class DatabaseProvider extends ContentProvider {

	private static final int 		WIKI 		= 1;
	private static final int 		UPDATE 		= 2;
	private static final int 		FAVORITE 	= 3;
	private DatabaseHelper 			mDBHelper = null;
	private static final UriMatcher URIMATCHER;
	static {
		URIMATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URIMATCHER.addURI(DatabaseColumn.AUTHORITY, WikiColumn.TABLE_NAME,	WIKI);
		URIMATCHER.addURI(DatabaseColumn.AUTHORITY, UpdateColumn.TABLE_NAME,	UPDATE);
		URIMATCHER.addURI(DatabaseColumn.AUTHORITY, FavoriteColumn.TABLE_NAME,	FAVORITE);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		int witch = URIMATCHER.match(uri);
		switch (witch) {
		case WIKI:
			count = delete(WikiColumn.TABLE_NAME, selection, selectionArgs);
			notifyChange(uri);
			break;
		case UPDATE:
			count = delete(UpdateColumn.TABLE_NAME, selection, selectionArgs);
			notifyChange(uri);
			break;
		case FAVORITE:
			count = delete(FavoriteColumn.TABLE_NAME, selection, selectionArgs);
			notifyChange(uri);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		return count;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowId = 0;
		int witch = URIMATCHER.match(uri);
		switch (witch) {
		case WIKI:
			rowId = insert(WikiColumn.TABLE_NAME, null, values);
			break;
		case UPDATE:
			rowId = insert(UpdateColumn.TABLE_NAME, null, values);
			break;
		case FAVORITE:
			rowId = insert(FavoriteColumn.TABLE_NAME, null, values);
			break;
		default:
			break;
		}
		if (rowId > 0) {
			Uri rowUri = null;
			switch (witch) {
			case WIKI:
				rowUri = ContentUris.withAppendedId(WikiColumn.CONTENT_URI,
						rowId);
				break;
			case UPDATE:
				rowUri = ContentUris.withAppendedId(UpdateColumn.CONTENT_URI,
						rowId);
				break;
			case FAVORITE:
				rowUri = ContentUris.withAppendedId(FavoriteColumn.CONTENT_URI,
						rowId);
				break;
			}
			List<Uri> list = new ArrayList<Uri>();
			list.add(uri);
			list.add(rowUri);
			notifyChange(list);
			return rowUri;
		} else {
			throw new SQLException("Failed to insert row into " + uri);
		}
	}

	@Override
	public boolean onCreate() {
		mDBHelper = new DatabaseHelper(getContext());
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		return (db == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor c = null;
		switch (URIMATCHER.match(uri)) {
		case WIKI:
			c = query(WikiColumn.TABLE_NAME, projection, selection,
					selectionArgs, null, null, sortOrder);
			break;
		case UPDATE:
			c = query(UpdateColumn.TABLE_NAME, projection, selection,
					selectionArgs, null, null, sortOrder);
			break;
		case FAVORITE:
			c = query(FavoriteColumn.TABLE_NAME, projection, selection,
					selectionArgs, null, null, sortOrder);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (c != null) {
			c.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		switch (URIMATCHER.match(uri)) {
		case WIKI:
			count = update(WikiColumn.TABLE_NAME, values, selection,
					selectionArgs);
			notifyChange(uri);
			break;
		case UPDATE:
			count = update(UpdateColumn.TABLE_NAME, values, selection,
					selectionArgs);
			notifyChange(uri);
			break;
		case FAVORITE:
			count = update(FavoriteColumn.TABLE_NAME, values, selection,
					selectionArgs);
			notifyChange(uri);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		return count;
	}

	private Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		synchronized (mDBHelper) {
			SQLiteDatabase db = mDBHelper.getReadableDatabase();
			if (db == null || !db.isOpen()) {
				return null;
			}
			try {
				return db.query(table, columns, selection, selectionArgs,
						groupBy, having, orderBy);
			} catch (Exception e) {
				return null;
			}
		}
	}

	private int delete(String table, String where, String[] whereArgs) {
		synchronized (mDBHelper) {
			SQLiteDatabase db = mDBHelper.getReadableDatabase();
			if (db == null || !db.isOpen()) {
				return 0;
			}
			try {
				return db.delete(table, where, whereArgs);
			} catch (Exception e) {
				return 0;
			}
		}
	}

	private int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		synchronized (mDBHelper) {
			SQLiteDatabase db = mDBHelper.getReadableDatabase();
			if (db == null || !db.isOpen()) {
				return 0;
			}
			try {
				return db.update(table, values, whereClause, whereArgs);
			} catch (Exception e) {
				return 0;
			}
		}
	}

	private long insert(String table, String nullColumnHack,
			ContentValues values) {
		synchronized (mDBHelper) {
			SQLiteDatabase db = mDBHelper.getReadableDatabase();
			if (db == null || !db.isOpen()) {
				return 0;
			}
			return db.insert(table, nullColumnHack, values);
		}
	}

	private void notifyChange(List<Uri> uri) {
		ContentResolver cr = getContext().getContentResolver();
		for (int i = 0; i < uri.size(); i++) {
			cr.notifyChange(uri.get(i), null);
		}
		L.e("notify url:" + uri);
	}

	private void notifyChange(Uri uri) {
		ContentResolver cr = getContext().getContentResolver();
		cr.notifyChange(uri, null);
		L.e("notify url:" + uri);
	}
}

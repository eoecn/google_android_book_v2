package cn.eoe.wiki.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import cn.eoe.wiki.db.DatabaseColumn;
import cn.eoe.wiki.utils.SqliteWrapper;
/**
 * 所有dao类的基本类
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-7
 * @version 1.0.0
 * @param <T> extends DatabaseColumn
 */
public class GeneralDao<T extends DatabaseColumn> {
	public T tableClass;
	protected Context context;
	protected Uri uri;
	
	public GeneralDao(T tableClass,Context context) {
		this.tableClass = tableClass;
		this.context = context;
		uri = Uri.parse("content://" + DatabaseColumn.AUTHORITY + "/" + this.tableClass.getTableName());
	}
	
	public Cursor queryAll() {
		return SqliteWrapper.query(context, context.getContentResolver(), uri, this.tableClass.getColumns(), null, null, null);
	}
	
	public Cursor queryByPage(int page,int length) {
		if(page<=0) {
			throw new IllegalArgumentException("invalid page :"+page);
		}
		return SqliteWrapper.query(context, context.getContentResolver(), uri, this.tableClass.getColumns(), null, null, DatabaseColumn._ID+" limit "+(page-1)*length+","+length);
	}
	
	public Cursor queryById(long id) {
		return SqliteWrapper.query(context, context.getContentResolver(), Uri.withAppendedPath(uri, String.valueOf(id)), this.tableClass.getColumns(), null, null, null);
	}
	public Cursor queryByParameter(String name, long value) {
		return SqliteWrapper.query(context, context.getContentResolver(), uri, this.tableClass.getColumns(), name+"="+value, null, null);
	}
	public Cursor queryByParameter(String name, String value) {
		return SqliteWrapper.query(context, context.getContentResolver(), uri, this.tableClass.getColumns(), name+"='"+value+"'", null, null);
	}
	public Uri insert(ContentValues values) {
		return SqliteWrapper.insert(context, context.getContentResolver(), uri, values);
	}
	
	public int delete(long id) {
		return SqliteWrapper.delete(context, context.getContentResolver(), uri, BaseColumns._ID+"="+id, null);
	}
	public int delete(Uri uri) {
		return SqliteWrapper.delete(context, context.getContentResolver(), uri, null, null);
	}
	
	public int update(ContentValues values)
	{
		long id = values.getAsLong(BaseColumns._ID);
		if(id>0) {
			return SqliteWrapper.update(context, context.getContentResolver(), uri, values, BaseColumns._ID+ "="+id, null);
		}
		else {
			return 0;
		}
	}
}

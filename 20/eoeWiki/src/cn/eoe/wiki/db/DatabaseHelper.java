package cn.eoe.wiki.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import cn.eoe.wiki.utils.L;
/**
 * DatabaseHelper,帮助程序用来创建与管理数据库
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-2
 * @version 1.0.0
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context, DatabaseColumn.DATABASE_NAME, null, DatabaseColumn.DATABASE_VERSION);

	}

	/**
	 * Called when the database is created for the first time. This is where the
	 * creation of tables and the initial population of the tables should
	 * happen.
	 * 
	 * @param sqlDB
	 *            The database.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			operateTable(db, "");
			// TODO　初始化一些信息
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * Called when the database needs to be upgraded. The implementation should
	 * use this method to drop tables, add tables, or do anything else it needs
	 * to upgrade to the new schema version.
	 * 
	 * @param sqlDB
	 *            The database. oldVersion The old database version. newVersion
	 *            The new database version.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		L.e("Database onUpgrade");
		if (oldVersion == newVersion) {
			return;
		}
		operateTable(db, "DROP TABLE IF EXISTS ");
		onCreate(db);
	}

	/**
	 * Execute operation about creating or drop tables in traffic database.
	 * 
	 * @param sqlDB
	 *            The database.
	 * @param actionString
	 *            which identifies to complete creating or drop tables. If it is
	 *            "" or null, operate creating tables. Otherwise operate drop
	 *            tables.
	 */
	public void operateTable(SQLiteDatabase db, String actionString) {
		Class<DatabaseColumn>[] columnsClasses = DatabaseColumn.getSubClasses();
		DatabaseColumn columns = null;

		for (int i = 0; i < columnsClasses.length; i++) {
			try {
				columns = columnsClasses[i].newInstance();
				if ("".equals(actionString) || actionString == null) {
					db.execSQL(columns.getTableCreateor());
				} else {
					db.execSQL(actionString + columns.getTableName());
				}
			} catch (Exception e) {
				L.e("operate table exception.",e);
			}
		}

	}
}


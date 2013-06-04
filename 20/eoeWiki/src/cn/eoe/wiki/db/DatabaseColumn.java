package cn.eoe.wiki.db;

import java.util.ArrayList;
import java.util.Map;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 所有cloumn的基类。可以定义一些共有的东西
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-2
 * @version 1.0.0
 */
public abstract class DatabaseColumn implements BaseColumns{
	/**
	 * The identifier to indicate a specific ContentProvider
	 */
	public static final String 		AUTHORITY	 		= "cn.eoe.wiki.provider";
	/**
	 * The database's name
	 */
	public static final String 		DATABASE_NAME 		= "wiki.db";
	/**
	 * The version of current database
	 */
	public static final int 		DATABASE_VERSION 	= 12;
	/**
	 * Classes's name extends from this class.
	 */
	public static final String[] 	SUBCLASSES			= new String[] { 
			"cn.eoe.wiki.db.WikiColumn",
			"cn.eoe.wiki.db.FavoriteColumn",
			"cn.eoe.wiki.db.UpdateColumn"
		};

	/**
	 * add date
	 */
	public static final String 		DATE_ADD 			= "date_add";
	/**
	 * mofify date
	 */
	public static final String 		DATE_MODIFY 		= "date_modify";
	
	
	/**
	 * This method create a SQL sentence to create this table in database by
	 * using the Columns Map.
	 * 
	 * @return <br>
	 *         The SQL sentence to create table</br>
	 */
	public String getTableCreateor() {
		return getTableCreator(getTableName(), getTableMap());
	}

	/**
	 * Get all columns' name in this table.
	 * 
	 * @return A String array contains the columns' name.
	 */
	public String[] getColumns() {
		return getTableMap().keySet().toArray(new String[0]);
	}

	/**
	 * Get sub-classes of this class.
	 * 
	 * @return Array of sub-classes.
	 */
	@SuppressWarnings("unchecked")
	public static final Class<DatabaseColumn>[] getSubClasses() {
		ArrayList<Class<DatabaseColumn>> classes = new ArrayList<Class<DatabaseColumn>>();
		Class<DatabaseColumn> subClass = null;
		for (int i = 0; i < SUBCLASSES.length; i++) {
			try {
				subClass = (Class<DatabaseColumn>) Class.forName(SUBCLASSES[i]);
				classes.add(subClass);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				continue;
			}
		}
		return classes.toArray(new Class[0]);
	}

	/**
	 * Create a sentence to create a table by using a hash-map.
	 * 
	 * @param tableName
	 *            The table's name to create.
	 * @param map
	 *            A map to store table columns info.
	 * @return
	 */
	private static final String getTableCreator(String tableName,
			Map<String, String> map) {
		String[] keys = map.keySet().toArray(new String[0]);
		String value = null;
		StringBuilder creator = new StringBuilder();
		creator.append("CREATE TABLE ").append(tableName).append("( ");
		int length = keys.length;
		for (int i = 0; i < length; i++) {
			value = map.get(keys[i]);
			creator.append(keys[i]).append(" ");
			creator.append(value);
			if (i < length - 1) {
				creator.append(",");
			}
		}
		creator.append(")");
		return creator.toString();
	}

	abstract public String getTableName();
	abstract public Uri getTableContent();

	abstract protected Map<String, String> getTableMap();
}

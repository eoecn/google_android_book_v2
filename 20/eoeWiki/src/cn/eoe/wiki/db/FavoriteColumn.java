package cn.eoe.wiki.db;

import java.util.HashMap;
import java.util.Map;

import android.net.Uri;
/**
 * 存放收藏wiki的数据表
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-11
 * @version 1.0.0
 */
public class FavoriteColumn extends DatabaseColumn {

	/**
     * This table's name
     */
    public static final String 		TABLE_NAME 		= "favorites";
    
    public static final String		TITLE			="title";
    public static final String		URL				="url";
    public static final String		REVID			="revid";
    
    public static final String[]	COLUMNS			= new String[]{
    	_ID,TITLE,URL,REVID,DATE_ADD,DATE_MODIFY
    };
    
    public static final Uri 		CONTENT_URI 	= Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    private static final Map<String, String> mColumnsMap = new HashMap<String, String>();
    static {
		mColumnsMap.put(_ID, "integer primary key autoincrement not null");
		mColumnsMap.put(TITLE, "text not null");
		mColumnsMap.put(REVID, "text not null");
		mColumnsMap.put(URL, "text not null");
		mColumnsMap.put(DATE_ADD, "localtime");
		mColumnsMap.put(DATE_MODIFY, "localtime");
    };
    
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	@Override
	public Uri getTableContent() {
		return CONTENT_URI;
	}

	@Override
	protected Map<String, String> getTableMap() {
		return mColumnsMap;
	}

}

package cn.eoe.wiki.db;

import java.util.HashMap;
import java.util.Map;

import android.net.Uri;
/**
 * wiki的数据表
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-11
 * @version 1.0.0
 */
public class UpdateColumn extends DatabaseColumn {

    /**
     * This table's name
     */
    public static final String 		TABLE_NAME 		= "updates";
    /**
     *  这个uri是本wiki的uri
     */
    public static final String		URI				="uri";
    public static final String		DATE_UPDATE		="date_update";
    
    public static final Uri 		CONTENT_URI 	= Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    private static final Map<String, String> mColumnsMap = new HashMap<String, String>();
    static {
		mColumnsMap.put(_ID, "integer primary key autoincrement not null");
		mColumnsMap.put(URI, "text not null");
		mColumnsMap.put(DATE_UPDATE, "localtime");
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

package cn.eoe.wiki;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * save the preference value
 *
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @since  2011-10-9  3:49:31 pm
 * @version 1.0.0
 */
public class PreferenceManager {
	
	/**
	 * the perfenrence file name
	 */
	public static final String PREFERENCE_NAME 			= Constants.APP_NAME;
	public static final String PREFEN_UPDATE_TIME		= "update_time";
	
	
	private Context mContext;
	public PreferenceManager(Context context) {
		mContext = context;
	}
	/**
	 * get the shared proferences for getting or setting 
	 * @return
	 */
	public SharedPreferences getSharedPreferences()
	{
		return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
	}
	/**
	 * get the editor  for saving the key value
	 * @return
	 */
	private Editor getEditer() {
		return getSharedPreferences().edit();
	}
	/**
	 * get the last check update time
	 * @return
	 */
	public long getLastUpdateTime() {
		SharedPreferences swg = getSharedPreferences();
		
		return swg.getLong(PREFEN_UPDATE_TIME,0);
	}

	/**
	 * set the update time
	 * @param time
	 */
	public void setLastUpdateTime(long time) {
		Editor editor = getEditer();
		editor.putLong(PREFEN_UPDATE_TIME, time);
		editor.commit();
	}

}

package cn.eoe.wiki.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.widget.TextView;
import cn.eoe.wiki.db.UpdateColumn;
import cn.eoe.wiki.db.WikiArgumentException;
import cn.eoe.wiki.db.WikiColumn;
import cn.eoe.wiki.db.entity.UpdateEntity;
import cn.eoe.wiki.utils.L;
/**
 * WikiDao<br>
 * 操作wiki 更新的相关的数据
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-7
 * @version 1.0.0
 */
public class UpdateDao extends GeneralDao<UpdateColumn> {

	public UpdateDao(Context context) {
		super(new UpdateColumn(), context);
	}

	/**
	 * get the wiki updateby the url
	 * @param url
	 * @return
	 */
	public UpdateEntity getWikiUpdateByUrl(String url)
	{
		Cursor cursor = queryByParameter(WikiColumn.URI, url);
		return buildWikiUpdateEntity(cursor);
	}
	/**
	 * 如果能从数据库是找到就是更新。否则为insert
	 * @param url
	 * @return
	 */
	public boolean addOrUpdateTime(String url)
	{
		L.d("add or update time:"+url);
		if(TextUtils.isEmpty(url))
			throw new IllegalArgumentException("Need a url param");
		UpdateEntity entity = getWikiUpdateByUrl(url);
		if(entity==null)
		{
			//Add
			saveTime(url);
		}
		else
		{
			//update
			updateTime(entity);
		}
		return true;
	}
	/**
	 * save the url update time
	 * @param url
	 * @return
	 */
	public boolean saveTime(String url)
	{
		L.d("save the update time:"+url);
		if(TextUtils.isEmpty(url))
			throw new IllegalArgumentException("Need a valid url");
		UpdateEntity entity = new UpdateEntity();
		entity.setUri(url);
		long current = System.currentTimeMillis();
		entity.setUpdateDate(current);
		entity.setModifyDate(current);
		entity.setAddDate(current);
		if(insert(change2ContentValues(entity))!=null)
		{
			return true;
		}
		return false;
	}
	/**
	 * refreah the update time
	 * @param url
	 * @return
	 */
	public boolean updateTime(UpdateEntity entity)
	{
		if(entity==null)
			throw new IllegalArgumentException("Need a valid UpdateEntity");
		L.d("refresh the update time:"+entity.getUri());
		long current = System.currentTimeMillis();
		entity.setUpdateDate(current);
		entity.setModifyDate(current);
		if(update(change2ContentValues(entity))>0)
		{
			return true;
		}
		return false;
	}
	
	public UpdateEntity buildWikiUpdateEntity(Cursor cursor)
	{
		UpdateEntity entity = null;
		if(cursor!=null && cursor.moveToFirst())
		{
			entity = new UpdateEntity();
			entity.setId(cursor.getLong(cursor.getColumnIndex(UpdateColumn._ID)));
			entity.setAddDate(cursor.getLong(cursor.getColumnIndex(UpdateColumn.DATE_ADD)));
			entity.setModifyDate(cursor.getLong(cursor.getColumnIndex(UpdateColumn.DATE_MODIFY)));
			entity.setUpdateDate(cursor.getLong(cursor.getColumnIndex(UpdateColumn.DATE_UPDATE)));
			entity.setUri(cursor.getString(cursor.getColumnIndex(UpdateColumn.URI)));
		}
		if(cursor!=null)
		{
			cursor.close();
		}
		return entity;
	}
	
	/**
	 * 将一个WikiEntity转化成ContentValues
	 * @param entity
	 * @return
	 */
	public ContentValues change2ContentValues(UpdateEntity entity)
	{

		ContentValues values = new ContentValues();
		if( entity.getId()>0)
		{
			values.put(UpdateColumn._ID, entity.getId());
		}
		if( entity.getAddDate()>0)
		{
			values.put(UpdateColumn.DATE_ADD, entity.getAddDate());
		}
		if( entity.getUpdateDate()>0)
		{
			values.put(UpdateColumn.DATE_UPDATE, entity.getUpdateDate());
		}
		if( entity.getModifyDate()>0)
		{
			values.put(UpdateColumn.DATE_MODIFY, entity.getModifyDate());
		}
		if(!TextUtils.isEmpty(entity.getUri()))
		{
			values.put(UpdateColumn.URI, entity.getUri());
		}
		return values;
	}
}

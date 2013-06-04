package cn.eoe.wiki.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import cn.eoe.wiki.db.FavoriteColumn;
import cn.eoe.wiki.db.entity.FavoriteEntity;
/**
 * FavoriteDao<br>
 * 操作Favorites相关的数据
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-8
 * @version 1.0.0
 */
public class FavoriteDao extends GeneralDao<FavoriteColumn> {
	
	public FavoriteDao(Context context) {
		super(new FavoriteColumn(), context);
	}
	/**
	 * get the total count of the favorite
	 * @return
	 */
	public int getFavoriteCount()
	{
		int count = 0;
		Cursor cursor = queryAll();
		if(cursor!=null)
		{
			count = cursor.getCount();
			cursor.close();
		}
		return count;
	}
	/**
	 * get the favorite
	 * @param page
	 * @param length
	 * @return
	 */
	public List<FavoriteEntity> getFavorites(int page, int length)
	{
		List<FavoriteEntity> favorites = null;
		Cursor cursor = queryByPage(page, length);
		if(cursor!=null)
		{
			favorites = new ArrayList<FavoriteEntity>();
			while (cursor.moveToNext()) {
				favorites.add(readFavoriteEntity(cursor));
			}
			cursor.close();
		}
		return favorites;
	}
	/**
	 * 通过revid获取FavoriteEntity
	 * @param revid
	 * @return
	 */
	public FavoriteEntity getFavoriteByRevid(String revid)
	{
		Cursor cursor = queryByParameter(FavoriteColumn.REVID, revid);
		return buildFavoriteEntity(cursor);
	}
	/**
	 * 添加收藏.如果数据库中已经有了，则会更新
	 * @param pageid
	 * @param title
	 * @param url
	 * @return
	 */
	public boolean addFavorite(String revid,String title,String url)
	{
		if(TextUtils.isEmpty(revid))
		{
			throw new IllegalArgumentException("Need a rev id");
		}
		if(TextUtils.isEmpty(title))
		{
			throw new IllegalArgumentException("Need a title");
		}
		if(TextUtils.isEmpty(url))
		{
			throw new IllegalArgumentException("Need a url");
		}
		FavoriteEntity entity = getFavoriteByRevid(revid);
		long current = System.currentTimeMillis();
		boolean isUpdate = false;
		if(entity==null)
		{
			entity = new FavoriteEntity();
			entity.setAddDate(current);
			isUpdate = false;
		}
		else
		{
			isUpdate = true;
		}
		entity.setRevid(revid);
		entity.setTitle(title);
		entity.setUrl(url);
		entity.setModifyDate(current);
		ContentValues values= change2ContentValues(entity);
		if(isUpdate)
		{
			if(update(values)>0)
			{
				return true;
			}
		}
		else
		{
			if(insert(values)!=null)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * read a favorite entity , will not close the cursor
	 * @param cursor
	 * @return
	 */
	private FavoriteEntity readFavoriteEntity(Cursor cursor)
	{
		FavoriteEntity entity = new FavoriteEntity();
		entity.setId(cursor.getLong(cursor.getColumnIndex(FavoriteColumn._ID)));
		entity.setAddDate(cursor.getLong(cursor.getColumnIndex(FavoriteColumn.DATE_ADD)));
		entity.setModifyDate(cursor.getLong(cursor.getColumnIndex(FavoriteColumn.DATE_MODIFY)));
		entity.setTitle(cursor.getString(cursor.getColumnIndex(FavoriteColumn.TITLE)));
		entity.setRevid(cursor.getString(cursor.getColumnIndex(FavoriteColumn.REVID)));
		entity.setUrl(cursor.getString(cursor.getColumnIndex(FavoriteColumn.URL)));
		return entity;
	}
	/**
	 * build the favorite entity. will close the cursor
	 * @param cursor
	 * @return
	 */
	public FavoriteEntity buildFavoriteEntity(Cursor cursor)
	{
		FavoriteEntity entity = null;
		if(cursor!=null && cursor.moveToFirst())
		{
			entity = readFavoriteEntity(cursor);
		}
		if(cursor!=null)
		{
			cursor.close();
		}
		return entity;
	}
	public ContentValues change2ContentValues(FavoriteEntity entity)
	{

		ContentValues values = new ContentValues();
		if( entity.getId()>0)
		{
			values.put(FavoriteColumn._ID, entity.getId());
		}
		if( entity.getAddDate()>0)
		{
			values.put(FavoriteColumn.DATE_ADD, entity.getAddDate());
		}
		if( entity.getModifyDate()>0)
		{
			values.put(FavoriteColumn.DATE_MODIFY, entity.getModifyDate());
		}
		if(!TextUtils.isEmpty(entity.getRevid()))
		{
			values.put(FavoriteColumn.REVID, entity.getRevid());
		}
		if(!TextUtils.isEmpty(entity.getUrl()))
		{
			values.put(FavoriteColumn.URL, entity.getUrl());
		}
		if(!TextUtils.isEmpty(entity.getTitle()))
		{
			values.put(FavoriteColumn.TITLE, entity.getTitle());
		}
		return values;
	}
}

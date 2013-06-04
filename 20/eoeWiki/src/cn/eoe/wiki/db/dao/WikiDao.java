package cn.eoe.wiki.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import cn.eoe.wiki.db.WikiArgumentException;
import cn.eoe.wiki.db.WikiColumn;
import cn.eoe.wiki.db.entity.WikiEntity;
import cn.eoe.wiki.utils.FileUtil;
import cn.eoe.wiki.utils.L;
/**
 * WikiDao<br>
 * 操作wiki相关的数据
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-7
 * @version 1.0.0
 */
public class WikiDao extends GeneralDao<WikiColumn> {

	private UpdateDao updateDao = null;
	
	public WikiDao(Context context) {
		super(new WikiColumn(), context);
		updateDao = new UpdateDao(context);
	}

	/**
	 * get the wiki by the page id 
	 * @param pageid
	 * @return
	 */
	public WikiEntity getWikiByPageId(String pageid)
	{
		Cursor cursor = queryByParameter(WikiColumn.PAGEID, pageid);
		return buildWikiEntity(cursor);
	}
	/**
	 * get the wiki by the url
	 * @param url
	 * @return
	 */
	public WikiEntity getWikiByUrl(String url)
	{
		Cursor cursor = queryByParameter(WikiColumn.URI, url);
		return buildWikiEntity(cursor);
	}
	/**
	 * 如果是更新，则一定要传入id值.
	 * <br>如果没有id,则为更新。
	 * <br>如果有id ,看pageid .如果 没有page id ,则会抛出异常。
	 * <br>接着去数据库中去找。如果找到一个，再判断version。再看该不该更新
	 * <br>如果没找到，则为创建
	 * @param entity
	 * @throws WikiArgumentException 
	 */
	public boolean saveOrUpdateWiki(WikiEntity entity, String content) throws WikiArgumentException
	{
		long id = entity.getId();
		if(id>0)
		{
			//update
			return updateWiki(entity);
		}
		else
		{
			String pageid = entity.getPageId();
			if(TextUtils.isEmpty(pageid))
			{
				throw new WikiArgumentException("Need a page id ");
			}
			Cursor cursor = queryByParameter(WikiColumn.PAGEID, pageid);
			if(cursor!=null)
			{
				if(cursor.getCount()>0)
				{
					cursor.moveToFirst();
					int version = cursor.getInt(cursor.getColumnIndex(WikiColumn.VERSION));
					id = cursor.getLong(cursor.getColumnIndex(WikiColumn._ID));
					String path = cursor.getString(cursor.getColumnIndex(WikiColumn.PATH));
					cursor.close();
					if(version< entity.getVersion() || !FileUtil.isFileExist(path))
					{
						//一种是版本有变化了，还有一种是文件不存在
						L.e("need to update the wiki table");
						entity.setId(id);
						if(entity.saveWikiFile(content))
						{
							//delete the older file .and save the new file
							FileUtil.deleteFile(path);
							//can find a wiki data and save success,then try to update
							return updateWiki(entity);
						}
						L.e("save the content failed");
					}
					//No need to update
					return false;
				}
				else
				{
					cursor.close();
					if(entity.saveWikiFile(content))
					{
						//can not find a wiki data and save success,then try to update
						return saveWiki(entity);
					}
				}
			}
			return false;
		}
	}
	/**
	 * 保存一个wiki到数据库
	 * @param entity
	 * @return
	 * @throws WikiArgumentException
	 */
	public boolean saveWiki(WikiEntity entity) throws WikiArgumentException
	{
		L.d("begin to save the wiki:"+entity.getPageId());
		String pageid = entity.getPageId();
		if(TextUtils.isEmpty(pageid))
		{
			throw new WikiArgumentException("Need a page id for updating");
		}
		long currentTime = System.currentTimeMillis();
		if(entity.getAddDate()==0)
		{
			entity.setAddDate(currentTime);
		}
		if(entity.getModifyDate()==0)
		{
			entity.setModifyDate(currentTime);
		}
		
		if(insert(change2ContentValues(entity))!=null)
		{
			updateDao.addOrUpdateTime(entity.getUri());
			return true;
		}
		return false;
	}
	/**
	 * 更新wiki到数据库<br>
	 * 在这里会重置modify date = current time<br>
	 * add date ==0 ,这样就不会让程序去修改到数据库中的add date 
	 * @param entity
	 * @return
	 * @throws WikiArgumentException
	 */
	public boolean updateWiki(WikiEntity entity) throws WikiArgumentException
	{
		L.d("begin to update the wiki:"+entity.getPageId());
		long id = entity.getId();
		if(id<=0)
		{
			throw new WikiArgumentException("Need a id for updating");
		}
		String pageid = entity.getPageId();
		if(TextUtils.isEmpty(pageid))
		{
			throw new WikiArgumentException("Need a page id for updating");
		}
		entity.setModifyDate(System.currentTimeMillis());
		entity.setAddDate(0);
		if(update(change2ContentValues(entity))>0)
		{
			updateDao.addOrUpdateTime(entity.getUri());
			return true;
		}
		return false;
	}
	
	public WikiEntity buildWikiEntity(Cursor cursor)
	{
		WikiEntity entity = null;
		if(cursor!=null && cursor.moveToFirst())
		{
			entity = new WikiEntity();
			entity.setId(cursor.getLong(cursor.getColumnIndex(WikiColumn._ID)));
			entity.setAddDate(cursor.getLong(cursor.getColumnIndex(WikiColumn.DATE_ADD)));
			entity.setModifyDate(cursor.getLong(cursor.getColumnIndex(WikiColumn.DATE_MODIFY)));
			entity.setPageId(cursor.getString(cursor.getColumnIndex(WikiColumn.PAGEID)));
			entity.setPath(cursor.getString(cursor.getColumnIndex(WikiColumn.PATH)));
			entity.setUri(cursor.getString(cursor.getColumnIndex(WikiColumn.URI)));
			entity.setDisplayTitle(cursor.getString(cursor.getColumnIndex(WikiColumn.DISPLAY_TITLE)));
			entity.setVersion(cursor.getInt(cursor.getColumnIndex(WikiColumn.VERSION)));
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
	public ContentValues change2ContentValues(WikiEntity entity)
	{

		ContentValues values = new ContentValues();
		if( entity.getId()>0)
		{
			values.put(WikiColumn._ID, entity.getId());
		}
		if( entity.getAddDate()>0)
		{
			values.put(WikiColumn.DATE_ADD, entity.getAddDate());
		}
		if( entity.getModifyDate()>0)
		{
			values.put(WikiColumn.DATE_MODIFY, entity.getModifyDate());
		}
		if(!TextUtils.isEmpty(entity.getPageId()))
		{
			values.put(WikiColumn.PAGEID, entity.getPageId());
		}
		if(!TextUtils.isEmpty(entity.getUri()))
		{
			values.put(WikiColumn.URI, entity.getUri());
		}
		if(!TextUtils.isEmpty(entity.getDisplayTitle()))
		{
			values.put(WikiColumn.DISPLAY_TITLE, entity.getDisplayTitle());
		}
		if(entity.getVersion()>0)
		{
			values.put(WikiColumn.VERSION, entity.getVersion());
		}
		if(!TextUtils.isEmpty(entity.getPath()))
		{
			values.put(WikiColumn.PATH, entity.getPath());
		}
		return values;
	}
}

package cn.eoe.wiki.activity;

import org.codehaus.jackson.type.TypeReference;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import cn.eoe.wiki.R;
import cn.eoe.wiki.db.dao.UpdateDao;
import cn.eoe.wiki.db.dao.WikiDao;
import cn.eoe.wiki.db.entity.UpdateEntity;
import cn.eoe.wiki.db.entity.WikiEntity;
import cn.eoe.wiki.http.HttpManager;
import cn.eoe.wiki.http.ITransaction;
import cn.eoe.wiki.json.CategoryJson;
import cn.eoe.wiki.utils.DateUtil;
import cn.eoe.wiki.utils.FileUtil;
import cn.eoe.wiki.utils.L;
/**
 * 用来处理最外层分类的界面
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-5
 * @version 1.0.0
 */
public abstract class CategoryActivity extends SliderActivity{

	private static final int	HANDLER_DISPLAY_CATEGORY 	= 0x0001;
	private static final int	HANDLER_LOAD_CATEGORY_ERROR = 0x0002;
	private static final int	HANDLER_LOAD_CATEGORY_DB 	= 0x0003;
	private static final int	HANDLER_LOAD_CATEGORY_NET 	= 0x0004;
	private static final int	HANDLER_REFRESH_CATEGORY_NET= 0x0005;
	private static final int	HANDLER_DISPLAY_RECENT_UPDATED= 0x0006;


	protected CategoryJson 	mResponseObject 	= null;
	private String 			mUrl 				= null;
	private WikiDao			mWikiDao 			= null;
	private UpdateDao		mWikiUpdateDao 		= null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWikiDao = new WikiDao(mContext);
		mWikiUpdateDao = new UpdateDao(mContext);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	void getCategory(String url) {
		if(TextUtils.isEmpty(url))
			throw new IllegalArgumentException("You must give a not empty url.");
		
		this.mUrl = url;
		mHandler.sendEmptyMessage(HANDLER_LOAD_CATEGORY_DB);
	}


	private boolean saveWikiCategory(int version,String pageid,String result) {
		if(!FileUtil.isExternalStorageEnable())//no dscard , return
			return false;
		WikiEntity entity = new WikiEntity();
		entity.setPageId(pageid);
		entity.setUri(mUrl);
		entity.setVersion(version);
		try {
			return mWikiDao.saveOrUpdateWiki(entity,result);
		} catch (Exception e) {
			L.e("save failed",e);
		}
		return false;
	}

	abstract void getCategoriesError(String showText);
	abstract void generateCategories(CategoryJson responseObject);
	
	/**
	 * mapper the string to the json bean
	 * @param result
	 * @param fromNet
	 */
	private void mapperJson(String result,boolean fromNet) {
		try {
			mResponseObject = mObjectMapper.readValue(result, new TypeReference<CategoryJson>() { });
			L.e("version:"+mResponseObject.getVersion());
			if(fromNet) {
				//if it is load from net ,save category
				saveWikiCategory(mResponseObject.getVersion(),mResponseObject.getPageId(), result);
			}
			else {
				//check the net wiki
				UpdateEntity updateEntity = mWikiUpdateDao.getWikiUpdateByUrl(mUrl);
				if(updateEntity!=null) {
					long current = System.currentTimeMillis();
					if((current-updateEntity.getUpdateDate())>DateUtil.DAY_MILLIS) {
						L.d("need to refreah the cache:"+mUrl);
						//check the new wiki every day
						mHandler.sendEmptyMessage(HANDLER_REFRESH_CATEGORY_NET);
					}
				}
				else {
					//如果这次是多缓存中读取的，但是去没有一个update的时候，则更新update数据库
					mWikiUpdateDao.addOrUpdateTime(mUrl);
				}
			}
			mHandler.obtainMessage(HANDLER_DISPLAY_CATEGORY, mResponseObject).sendToTarget();
		} catch (Exception e) {
			L.e("getCategory Transaction exception", e);
			if(!fromNet) {
				L.d("category content is erro which is read from the cache dir");
				//如果不是从网络来的。错误了还得从网络去拿一次
				mHandler.sendEmptyMessage(HANDLER_LOAD_CATEGORY_NET);
			}
			else {
				//如果是从网络上面来的，错误了就错误了
				mHandler.obtainMessage(HANDLER_LOAD_CATEGORY_ERROR).sendToTarget();
			}
		}
	}

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_DISPLAY_CATEGORY:
				L.d("HANDLER_DISPLAY_CATEGORY");
				generateCategories((CategoryJson)msg.obj);
				break;
			case HANDLER_LOAD_CATEGORY_ERROR:
				getCategoriesError(getString(R.string.tip_get_category_error));
				break;
			case HANDLER_LOAD_CATEGORY_DB:
				new LoadCategoryFromDb().execute(mUrl);
				break;
			case HANDLER_LOAD_CATEGORY_NET:
				new HttpManager(mUrl,null, HttpManager.GET, getCategoriesTransaction).start();
				break;
			case HANDLER_REFRESH_CATEGORY_NET:
				new HttpManager(mUrl,null, HttpManager.GET, refreshCategoriesTransaction).start();
				break;
			case HANDLER_DISPLAY_RECENT_UPDATED:
				//
				break;
			default:
				break;
			}
		}
		
	};

	public ITransaction getCategoriesTransaction = new ITransaction() {

		@Override
		public void transactionOver(String result) {
			mapperJson(result,true);
			L.d("get the category from the net");
		}
		
		@Override
		public void transactionException(int erroCode,String result, Exception e) {
			mHandler.obtainMessage(HANDLER_LOAD_CATEGORY_ERROR).sendToTarget();
		}
	};

	public ITransaction refreshCategoriesTransaction = new ITransaction() {

		@Override
		public void transactionOver(String result) {
			L.d("refresh the category from the net");
			try {
				CategoryJson responseObject = mObjectMapper.readValue(result, new TypeReference<CategoryJson>() { });
				//save category
				saveWikiCategory(responseObject.getVersion(),responseObject.getPageId(), result);
			}catch (Exception e) {
				L.e("refresh category error[mapper json]");
			}
		}
		
		@Override
		public void transactionException(int erroCode,String result, Exception e) {
			L.e("Refresh the category exception:"+erroCode);
		}
	};

	class LoadCategoryFromDb extends AsyncTask<String, Integer, Boolean>
	{

		@Override
		protected Boolean doInBackground(String... params) {
			String url = params[0];
			WikiEntity wiki = mWikiDao.getWikiByUrl(url);
			if(wiki!=null) {
				String content = wiki.getWikiFileContent();
				mapperJson(content,false);
				L.d("get the category from the cache");
			}
			else
			{
				L.d("can not get the content from the db");
				mHandler.sendEmptyMessage(HANDLER_LOAD_CATEGORY_NET);
			}
			return null;
		}
		
	}
}

package cn.eoe.wiki.activity;


import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.type.TypeReference;

import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.eoe.wiki.R;
import cn.eoe.wiki.WikiApplication;
import cn.eoe.wiki.db.dao.FavoriteDao;
import cn.eoe.wiki.db.entity.FavoriteEntity;
import cn.eoe.wiki.http.HttpManager;
import cn.eoe.wiki.http.ITransaction;
import cn.eoe.wiki.json.WikiDetailErrorJson;
import cn.eoe.wiki.json.WikiDetailJson;
import cn.eoe.wiki.utils.L;
import cn.eoe.wiki.utils.WikiUtil;

public class WikiContentActivity extends SliderActivity implements OnClickListener{

	private static final int		HANDLER_DISPLAY_WIKIDETAIL 		= 0x0001;
	private static final int		HANDLER_GET_WIKIERROR 			= 0x0002;
	private static final int		HANDLER_GET_WIKIDETAIL_ERROR 	= 0x0003;
	private static final int		HANDLER_REFRESH_FAVORITE_STATUS	= 0x0004;
	private static final String 	WIKI_URL_PRE = "http://wiki.eoeandroid.com/";
	private static final String 	WIKI_URL_AFTER = "/api.php?action=parse&format=json&page=";
	
	private RelativeLayout			mWikiProcessLayout;
	private LayoutInflater 			mInflater;
	private boolean					mProgressVisible;
	
	private ImageButton 			mBtnParentDirectory;
	private ImageButton 			mBtnFullScreen;
	private ImageButton 			mBtnFavorite;
	private ImageButton 			mBtnShare;
	private ImageButton				mBtnBack;
	private TextView				mTvFistCategoryName;
	private TextView				mTvSecondCategoryName;
	
	private RelativeLayout			mWikiDetailTitle;
	private LinearLayout 			mLayoutFunctions;
	private ScrollView 				mWikiScrollView;
	
	public static final 	String  WIKI_CONTENT = "wiki_content";
	public static final		String 	KEY_PARENT_TITLE	= "parent_title";
	public static final		String 	KEY_SUB_PARENT_TITLE	= "sub_parent_title";
	private ParamsEntity			mParamsEntity;
	
	protected WikiDetailJson 		responseObject = null;
	protected WikiDetailErrorJson	responseError = null;
	private WebView 				mWebView;
	
	private boolean 				isFullScreen;//当前是否已经全屏
	private float[]					lastTouch;	//记录上一次的坐标，现在未用到
	private Timer					mDoubleClickTimer;//timer,用于判断双击计时 
	private boolean 				isReadyDoubleClick;//是否已经第一次点击了
	private DoubleClickTask			mDoubleClickTask;//双击过时任务
	private boolean 				canFullScreen;
	private boolean 				canShare;
	private boolean 				canFavorite;
	
	private boolean 				hasFavorite;
	private long 					favoriteID;
	private FavoriteDao				favoriteDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wiki_detail);
		MobclickAgent.onEvent(this, "content-detail", "enter");
		mInflater = LayoutInflater.from(mContext);
		Intent intent = getIntent();
		if(intent == null){
			throw new NullPointerException("Must give a Wiki Uri in the intent");
		}
		mParamsEntity = (ParamsEntity)intent.getParcelableExtra(WIKI_CONTENT);
		if(mParamsEntity == null){
			throw new NullPointerException("Must give a Wiki Uri in the intent");
		}
		lastTouch = new float[]{0,0};
		isReadyDoubleClick = false;
		mDoubleClickTimer = new Timer();
		favoriteDao = new FavoriteDao(mContext);
		initComponent();
		initData();
	}

	void initComponent() {
		mWikiProcessLayout = (RelativeLayout)findViewById(R.id.layout_wiki_detail_process);
		
		mTvFistCategoryName = (TextView)findViewById(R.id.tv_title_parent);
		mTvSecondCategoryName = (TextView)findViewById(R.id.tv_second_parent_title);
		mBtnBack=(ImageButton)findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		mWebView = (WebView)findViewById(R.id.wiki_detail_content);
		mBtnParentDirectory = (ImageButton)findViewById(R.id.btn_parent_directory);
		mBtnFullScreen = (ImageButton)findViewById(R.id.btn_fullscreen);
		mBtnFavorite = (ImageButton)findViewById(R.id.btn_favorite);
		mBtnShare = (ImageButton)findViewById(R.id.btn_share);
		
		mWikiDetailTitle = (RelativeLayout)findViewById(R.id.wiki_detail_title);
		mLayoutFunctions = (LinearLayout)findViewById(R.id.layout_functions);
		mWikiScrollView = (ScrollView)findViewById(R.id.sv_wiki_scroll);
		
		mBtnParentDirectory.setOnClickListener(this);
		mBtnFullScreen.setOnClickListener(this);
		mBtnFavorite.setOnClickListener(this);
		mBtnShare.setOnClickListener(this);
		
	}
	
	void initData(){
		isFullScreen = false;
		canShare = false;
		canFavorite = false;
		//TODO set the first parent title
		mTvFistCategoryName.setText(mParamsEntity.getFirstTitle());
		//TODO set the second parent title
		if(TextUtils.isEmpty(mParamsEntity.getSecondTitle())){
			mTvSecondCategoryName.setVisibility(View.GONE);
		}else{
			mTvSecondCategoryName.setText(mParamsEntity.getSecondTitle());
		}
		mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				L.e("url:"+url);
				if(WikiUtil.isImageUrl(url))
					return true;
				if(url.startsWith("http://")||url.startsWith("https://") || url.startsWith("www.")){
					Uri uri = Uri.parse(url);  
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
					return true;
				}
				mParamsEntity.setUri(WIKI_URL_PRE+WIKI_URL_AFTER+url.substring(1));
				getWikiDetail();
				return true;
			}
        	
        });
        mWebView.setBackgroundColor(WikiUtil.getResourceColor(R.color.deep_grey, mContext));
	}
	
	void getWikiDetail()
	{
		showProgressLayout();
		HttpManager manager = new HttpManager(mParamsEntity.getUri(),null, HttpManager.GET, getWikiDetailTransaction);
		manager.start();
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_DISPLAY_WIKIDETAIL:
				generateWiki((WikiDetailJson)msg.obj);
				new JudgeFavoriteTask().execute();
				break;
			case HANDLER_GET_WIKIERROR:
				generateWikiError((WikiDetailErrorJson)msg.obj);
				break;
			case HANDLER_GET_WIKIDETAIL_ERROR:
				getWikiError(getString(R.string.tip_get_category_error));
				break;
			case HANDLER_REFRESH_FAVORITE_STATUS:
				Boolean ret = (Boolean)msg.obj;
				hasFavorite = ret.booleanValue();
				L.d("HANDLER_REFRESH_FAVORITE_STATUS:"+hasFavorite);
				if (hasFavorite) {
					mBtnFavorite.setImageResource(R.drawable.ico_favorite);
				}
				else
				{
					mBtnFavorite.setImageResource(R.drawable.ico_favorite_not);
				}
				break;
			default:
				break;
			}
		}
		
	};
	
	private void generateWiki(WikiDetailJson pWikiDetailJson){
		mWikiProcessLayout.removeAllViews();
		mProgressVisible = false;
		mWikiScrollView.setVisibility(View.VISIBLE);
		canFullScreen = true;
		canShare = true;
		canFavorite =true;
		String html = pWikiDetailJson.getParse().getText().getHtml();
		String htmlContent = WikiApplication.getWikiHtml().toString().replace("_HTML_", removeEditWord(html));
		mWebView.loadDataWithBaseURL("about:blank", htmlContent,  "text/html","utf-8", null);
	}
	
	private String removeEditWord(String html)
	{
		return html.replaceAll("<span class=\"editsection\">(.*?)</span>", "");
	}
	
	private void generateWikiError(WikiDetailErrorJson pWikiDetailErrorJson){
		mWikiScrollView.setVisibility(View.GONE);
		if("missingtitle".equals(pWikiDetailErrorJson.getError().getCode())){
			getWikiError(getString(R.string.no_such_article));
		}else{
			getWikiError(getString(R.string.unknow_error));
		}
	}
	
	private void getWikiError(String pError){
		mWikiProcessLayout.removeAllViews();
		mProgressVisible = false;
		canFullScreen = false;
		canShare = false;
		canFavorite = false;
		responseObject = null;
		fullScreen(false);//关闭全屏
		View viewError = mInflater.inflate(R.layout.loading_error, null);
		LayoutParams errorParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		errorParams.topMargin = WikiUtil.dip2px(mContext, 10);
		viewError.setLayoutParams(errorParams);
		
		TextView tvErrorTip =  (TextView)viewError.findViewById(R.id.tv_error_tip);
		tvErrorTip.setText(pError);
		Button btnTryAgain =  (Button)viewError.findViewById(R.id.btn_try_again);
		btnTryAgain.setOnClickListener(this);
		mWikiProcessLayout.addView(viewError);
	}
	
	public ITransaction getWikiDetailTransaction = new ITransaction() {
		
		@Override
		public void transactionOver(String result) {
			try {
				responseError = mObjectMapper.readValue(result, new TypeReference<WikiDetailErrorJson>() { });
				if(responseError.getError() == null){
					responseObject = mObjectMapper.readValue(result, new TypeReference<WikiDetailJson>() { });
					mHandler.obtainMessage(HANDLER_DISPLAY_WIKIDETAIL, responseObject).sendToTarget();
				}else{
					mHandler.obtainMessage(HANDLER_GET_WIKIERROR, responseError).sendToTarget();
				}
			} catch (Exception e) {
				L.e("getWikiDetailTransaction exception", e);
				mHandler.obtainMessage(HANDLER_GET_WIKIDETAIL_ERROR).sendToTarget();
			}
		}
		
		@Override
		public void transactionException(int erroCode,String result, Exception e) {
			mHandler.obtainMessage(HANDLER_GET_WIKIDETAIL_ERROR).sendToTarget();
		}
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
		case R.id.btn_parent_directory:
			MobclickAgent.onEvent(this, "content-detail", "btn_parentDirectory");
			closeSlider();
			break;
		case R.id.btn_fullscreen:
			MobclickAgent.onEvent(this, "content-detail", "btn_fullScreen");
			if(canFullScreen)
			{
				fullScreen(true);
			}
			else
			{
				Toast.makeText(mContext, R.string.tip_cannt_full_screent, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_favorite:
			MobclickAgent.onEvent(this, "content-detail", "btn_favorite");
			if(canFavorite)
			{
				collectionFavorite();
			}
			else
			{
				Toast.makeText(mContext, R.string.tip_cannt_favorite, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_share:
			MobclickAgent.onEvent(this, "content-detail", "btn_share");
			if(canShare)
			{
				shareToFriend();
			}
			else
			{
				Toast.makeText(mContext, R.string.tip_cannt_share, Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	private void collectionFavorite(){
		if(responseObject==null)
		{
			return;
		}
		boolean ret = false;
		if(hasFavorite){
			if(favoriteDao.delete(favoriteID)>0)
			{
				L.e("delete true");
				ret =  true;
			}
			else
			{
				L.e("delete false");
				ret = false;
			}
		}else{
			ret = favoriteDao.addFavorite(responseObject.getParse().getRevid(), responseObject.getParse().getDisplayTitle(), mParamsEntity.getUri());
		}
		if(ret)
		{
			if(hasFavorite)
			{
				Toast.makeText(mContext, R.string.favorite_cancel_success, Toast.LENGTH_SHORT).show();
			}
			else
			{
				FavoriteEntity fe = favoriteDao.getFavoriteByRevid(responseObject.getParse().getRevid());
				if(fe != null){
					favoriteID = fe.getId();
				}
				Toast.makeText(mContext, R.string.favorite_success, Toast.LENGTH_SHORT).show();
			}
			hasFavorite=!hasFavorite;
			mHandler.obtainMessage(HANDLER_REFRESH_FAVORITE_STATUS, Boolean.valueOf(hasFavorite)).sendToTarget();
		}
		else
		{
			if(hasFavorite)
			{
				Toast.makeText(mContext, R.string.favorite_cancel_failed, Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(mContext, R.string.favorite_failed, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private class JudgeFavoriteTask extends AsyncTask<String, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			FavoriteEntity fe = favoriteDao.getFavoriteByRevid(responseObject.getParse().getRevid());
			if(fe != null){
				favoriteID = fe.getId();
				return true;
			}else{
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			L.d("onPostExecute:"+result);
			mHandler.obtainMessage(HANDLER_REFRESH_FAVORITE_STATUS, result).sendToTarget();
		}
	}
	
	private void showProgressLayout()
	{
		mWikiScrollView.setVisibility(View.GONE);
		canFullScreen = false;
		View progressView = mInflater.inflate(R.layout.loading, null);
		mWikiProcessLayout.removeAllViews();
		mWikiProcessLayout.addView(progressView);
		mProgressVisible = true;
	}
	
	private void fullScreen(boolean full){
		if(full){
//			mWikiDetailTitle.setVisibility(View.GONE);
			mLayoutFunctions.setVisibility(View.GONE);
			Toast.makeText(WikiContentActivity.this, getString(R.string.screen_back), Toast.LENGTH_SHORT).show();
			isFullScreen = true;
		}else{
//			mWikiDetailTitle.setVisibility(View.VISIBLE);
			mLayoutFunctions.setVisibility(View.VISIBLE);
			isFullScreen = false;
		}
	}
	
	private void shareToFriend(){
		if(responseObject==null)
		{
			return;
		}
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		String title = responseObject.getParse().getTitle();
		String titleForUrl = title.replace(" ", "_");
		intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.content_share,new Object[]{title,WIKI_URL_PRE+titleForUrl}));
		Intent it = Intent.createChooser(intent, getString(R.string.share_chooser_title));
		startActivity(it);
	}
	
	@Override
	public void onSlidebarOpened() {
		if(!mProgressVisible)
		{
			showProgressLayout();
		}
		getWikiDetail();
	}
	

	@Override
	public void onSlidebarClosed() {
		
	}
	
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(isFullScreen)
		{
			int action = ev.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				lastTouch[0] = ev.getX();
				lastTouch[1] = ev.getY();
				L.e("ACTION_DOWN");
				if(isReadyDoubleClick)
				{
					return true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				L.e("ACTION_MOVE");
				if(isReadyDoubleClick)
				{
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
				L.e("ACTION_UP");
				if(!isReadyDoubleClick)
				{
					//first
					isReadyDoubleClick = true;
					if(mDoubleClickTask!=null)
					{
						mDoubleClickTask.cancel();
					}
					mDoubleClickTask = new DoubleClickTask();
					mDoubleClickTimer.schedule(mDoubleClickTask, 500);  
				}
				else
				{
					L.e("Double click");
					fullScreen(false);
					return true;
				}
				break;
			default:
				break;
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	class DoubleClickTask extends TimerTask {  
        
        @Override 
        public void run() {
            isReadyDoubleClick = false;
        }  
    }

}

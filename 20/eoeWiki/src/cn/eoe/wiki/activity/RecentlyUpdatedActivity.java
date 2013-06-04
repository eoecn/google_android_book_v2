package cn.eoe.wiki.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.eoe.wiki.R;
import cn.eoe.wiki.activity.adapter.RecentUpdateAdapter;
import cn.eoe.wiki.http.HttpManager;
import cn.eoe.wiki.http.ITransaction;
import cn.eoe.wiki.json.CategoryChild;
import cn.eoe.wiki.json.RecentJson;
import cn.eoe.wiki.json.RecentUpdateQueryContinusJson;
import cn.eoe.wiki.json.RecentlyUpdatedJson;
import cn.eoe.wiki.utils.L;
import cn.eoe.wiki.utils.WikiUtil;
/**
 * 最新更新文章列表的Activity
 * @author <a href="mailto:realh3@gmail.com">Real Xu</a>
 * @data  2012-8-17
 * @version 1.0.0
 */
public class RecentlyUpdatedActivity extends SliderActivity implements OnClickListener, OnScrollListener,OnItemClickListener{
	private static final String WIKI_URL_HOST = "http://wiki.eoeandroid.com/";
	private static final String WIKI_URL_DETAIL = "api.php?action=parse&format=json&page=";
	private static final String WIKI_URL_LOCATION = "api.php?action=query&list=recentchanges&rclimit=50&format=json";
	
	final int HANDLER_LOAD_CONTENT_NET = 0;
	final int HANDLER_DISPLAY_CONTENT = 1;
	final int HANDLER_LOAD_ERROR = 2;
	final int HANDLER_NO_CONTENT = 3;
	
	private List<RecentlyUpdatedJson> mUpdateResluts;
	private RecentUpdateAdapter mAdapter;
	
	private LinearLayout    mLayoutLoading;
	private View 			mNoUpdateResult;
	private View 			mLayoutError;
	private LinearLayout	mContentLayout;
	private LayoutInflater 	mInflater;
	private ImageButton		mBtnBack;
	private TextView		mTvParentName;
	private View 			mLayoutProgress;
	private ListView 		mListView;
	private Button 			mBtnTryAgain;
	
	private String 			mRcstart = "";
	private boolean			isRefreshing;
	private CategoryChild	mParentCategory;
	private String			mParentName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recently_updated);
		mInflater = LayoutInflater.from(mContext);
		mUpdateResluts = new ArrayList<RecentlyUpdatedJson>();
		Intent intent = getIntent();
		if(intent==null)
		{
			throw new NullPointerException("Must give a CategoryChild in the intent");
		}
		initComponent();
		initData();
	}
	
	void initComponent() {
		mListView = (ListView) findViewById(R.id.ListView);
		mListView.setDividerHeight(0);
		mBtnTryAgain = (Button) findViewById(R.id.btn_try_again);
		mTvParentName = (TextView)findViewById(R.id.tv_title_parent);
		mNoUpdateResult = findViewById(R.id.layout_no_update_result);
		mLayoutError = findViewById(R.id.layout_update_result_error);
		mLayoutLoading = (LinearLayout) findViewById(R.id.layout_loading);
		mContentLayout = (LinearLayout)findViewById(R.id.layout_category);
		mBtnBack=(ImageButton)findViewById(R.id.btn_back);
		mBtnTryAgain.setOnClickListener(this);
		mBtnBack.setOnClickListener(this);
		
	}

	void initData() {
		isRefreshing = false;
		View footerView = mInflater.inflate(R.layout.favorite_footer, null);
		mLayoutProgress = footerView.findViewById(R.id.layout_progress);
		mLayoutProgress.setVisibility(View.GONE);
		mListView.addFooterView(footerView);
		mListView.setOnScrollListener(this);
		mTvParentName.setText(R.string.title_update_recent);
		
		mLayoutLoading.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
		mNoUpdateResult.setVisibility(View.GONE);
		mLayoutError.setVisibility(View.GONE);                                                   
	}
	
	public void getRecentUpdate(){
		mLayoutProgress.setVisibility(View.VISIBLE);
		HashMap<String,String> requestData = new HashMap<String,String>();
		
		String url = WIKI_URL_HOST + WIKI_URL_LOCATION;
		if(TextUtils.isEmpty(mRcstart)){
			
		}else{
			requestData.put("rcstart", mRcstart);
		}
		requestData.put("format", "json");
		HttpManager manager = new HttpManager(url,requestData,HttpManager.GET,getRecentlyUpdatedTransaction);
		manager.start();
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_DISPLAY_CONTENT:
				isRefreshing = false;
				mLayoutLoading.setVisibility(View.GONE);
				mLayoutProgress.setVisibility(View.GONE);
				
				if(mUpdateResluts.size() == 0){
					mListView.setVisibility(View.GONE);
					mNoUpdateResult.setVisibility(View.VISIBLE);
				}else{
					mNoUpdateResult.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					if(mAdapter == null){
						mAdapter = new RecentUpdateAdapter(RecentlyUpdatedActivity.this,mUpdateResluts);
						mListView.setAdapter(mAdapter);
						mListView.setOnItemClickListener(RecentlyUpdatedActivity.this);
					}else{
						mAdapter.setSearchResults(mUpdateResluts);
					}
				}
				break;
			case HANDLER_LOAD_CONTENT_NET:
				new HttpManager(WIKI_URL_HOST + WIKI_URL_LOCATION, null, HttpManager.GET, getRecentlyUpdatedTransaction).start();
				break;
			case HANDLER_LOAD_ERROR:
				mLayoutLoading.setVisibility(View.GONE);
				mLayoutProgress.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
				mNoUpdateResult.setVisibility(View.GONE);
				mLayoutError.setVisibility(View.VISIBLE);
				break;
			case HANDLER_NO_CONTENT:
				mLayoutLoading.setVisibility(View.GONE);
				mLayoutProgress.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
				mNoUpdateResult.setVisibility(View.VISIBLE);
				mLayoutError.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}
	};
	
	public ITransaction getRecentlyUpdatedTransaction = new ITransaction() {
		@Override
		public void transactionOver(String result) {
			mapperJson(result,true);
			L.d("get the category from the net");
		}
		@Override
		public void transactionException(int erroCode, String result, Exception e) {
			mHandler.obtainMessage(HANDLER_LOAD_ERROR).sendToTarget();
		}
	};
	
	private void mapperJson(String result, boolean fromNet)
	{
		RecentJson recentJson;
		try {
			recentJson = mObjectMapper.readValue(result,new TypeReference<RecentJson>(){});
			List<RecentlyUpdatedJson> recentChilds = recentJson.getQuery().getQuery();
			RecentUpdateQueryContinusJson queryContinus = recentJson.getQueryContinue();
			if(queryContinus==null){
				mRcstart = "";
			}else{
				mRcstart = queryContinus.getRecentchanges().getRcstart();
			}
			if(recentChilds!=null&&recentChilds.size()!=0){
				HashSet<Integer> set = new HashSet<Integer>();
				for(int i = 0;i < recentChilds.size();i++){
					RecentlyUpdatedJson item = recentChilds.get(i);
					if(!set.contains(item.getPageid())){
						mUpdateResluts.add(item);
						set.add(item.getPageid());
					}
				}
			}
			if(mUpdateResluts.size()==0){
				mHandler.obtainMessage(HANDLER_NO_CONTENT).sendToTarget();
			}else{
				mHandler.obtainMessage(HANDLER_DISPLAY_CONTENT).sendToTarget();
			}
		} catch (Exception e) {
			L.e("getCategorysTransaction exception", e);
		}
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSlidebarOpened() {
		if(WikiUtil.getNetworkStatus(this)){
			mLayoutLoading.setVisibility(View.VISIBLE);
			getRecentUpdate();
		}else{
			mHandler.obtainMessage(HANDLER_LOAD_ERROR).sendToTarget();
		}
	}
	@Override
	public void onSlidebarClosed() {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			closeSlider();
			break;
		case R.id.btn_try_again:
			mLayoutError.setVisibility(View.GONE);
			onSlidebarOpened();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		RecentlyUpdatedJson item = mUpdateResluts.get(position);
		String title = item.getTitle();
		title = title.replace(" ", "_");
		String mSecondTitle = "";
		String mUri = WIKI_URL_HOST + WIKI_URL_DETAIL+title;
		Intent intent = new Intent(mContext, WikiContentActivity.class);
		ParamsEntity pe = new ParamsEntity();
		pe.setFirstTitle(mContext.getString(R.string.title_update_recent));
		pe.setSecondTitle(mSecondTitle);
		pe.setUri(mUri);
		intent.putExtra(WikiContentActivity.WIKI_CONTENT, pe);
		getmMainActivity().showView(2, intent);
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_FLING || scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			if (view.getLastVisiblePosition() == view.getCount() - 1) {
				if(!isRefreshing && !TextUtils.isEmpty(mRcstart)) {
					isRefreshing = true;
					L.i("isrefreshing-->" + isRefreshing);
					getRecentUpdate();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}

}
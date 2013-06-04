package cn.eoe.wiki.activity;

import java.util.ArrayList;
import java.util.HashMap;
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
import cn.eoe.wiki.activity.adapter.SearchResultAdapter;
import cn.eoe.wiki.http.HttpManager;
import cn.eoe.wiki.http.ITransaction;
import cn.eoe.wiki.json.SearchItemJson;
import cn.eoe.wiki.json.SearchQueryContinusJson;
import cn.eoe.wiki.json.SearchResultJson;
import cn.eoe.wiki.utils.L;
import cn.eoe.wiki.utils.WikiUtil;

/**
 * 检索结果展示
 * 
 * @author tyutNo4
 * @data 2012-8-11
 * @version 1.0.0
 */
public class SearchResultActivity extends SliderActivity implements OnClickListener, OnScrollListener,OnItemClickListener {
	public static final String KEY_SEARCH_TEXT 			= "search_text";
	public static final 	int		PAGE_COUNT 			= 20;
	private static final int HANDLER_DISPLAY_SEARCH 	= 0x0001;
	private static final int HANDLER_GET_SEARCH_ERROR 	= 0x0002;

	private ListView				mListView;
	private SearchResultAdapter		mAdapter;
	private List<SearchItemJson>	mSearchResults;
	
	private LinearLayout	mLayoutLoading;
	private View			mNoSearchResult;
	private ImageButton		mBtnBack;
	private TextView		mTvTitle;
	private View 			mLayoutPrgogress;
	private View 			mLayoutError;
	private Button 			mbtnTryAgain;
	
	private LayoutInflater 	mInflater;
	private int 			mOffset = 0;
	private boolean			isRefreshing;

	private String 			mSearchText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result);
		mInflater = LayoutInflater.from(mContext);
		mSearchResults = new ArrayList<SearchItemJson>();
		Intent intent = getIntent();
		if (intent == null) {
			throw new NullPointerException("Must give a keyword in the intent");
		}
		mSearchText = intent.getStringExtra(KEY_SEARCH_TEXT);
		if(TextUtils.isEmpty(mSearchText)) {
			throw new NullPointerException("Must give a keyword in the intent");
		}
		initComponent();
		initData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	void initComponent() {
		mTvTitle = (TextView)findViewById(R.id.tv_title_parent);
		mListView = (ListView)findViewById(R.id.ListView);
		mListView.setDividerHeight(0);
		mLayoutLoading = (LinearLayout)findViewById(R.id.layout_loading);
		mNoSearchResult = findViewById(R.id.layout_no_search_result);
		mBtnBack=(ImageButton)findViewById(R.id.btn_back);
		mLayoutError = (LinearLayout)findViewById(R.id.layout_search_result_error);
		mbtnTryAgain = (Button)findViewById(R.id.btn_try_again);
		mBtnBack.setOnClickListener(this);
		mbtnTryAgain.setOnClickListener(this);
	}

	/**
	 * 初始化loading
	 */
	void initData() {
		isRefreshing = false;
//		String title = getString(R.string.title_search_result)+"-"+mSearchText;
		mTvTitle.setText(getString(R.string.title_search_result,mSearchText));
		
		TextView blankHeaderView = new TextView(mContext);
		blankHeaderView.setHeight(WikiUtil.dip2px(mContext, 10));
		mListView.addHeaderView(blankHeaderView);

		View footerView = mInflater.inflate(R.layout.favorite_footer, null);
		mLayoutPrgogress = footerView.findViewById(R.id.layout_progress);
		mLayoutPrgogress.setVisibility(View.GONE);
		mListView.addFooterView(footerView);
		mListView.setOnScrollListener(this);
		
		mLayoutLoading.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
		mNoSearchResult.setVisibility(View.GONE);
		mLayoutError.setVisibility(View.GONE);
	}

	public void getSearchResult() {
		mLayoutPrgogress.setVisibility(View.VISIBLE);
		String url = "http://wiki.eoeandroid.com/api.php";
		HashMap<String, String> requestData = new HashMap<String, String>();
		requestData.put("action", "query");
		requestData.put("list", "search");
		requestData.put("srwhat", "text");
		requestData.put("format", "json");
		requestData.put("sroffset", String.valueOf(mOffset));
		requestData.put("srlimit",String.valueOf(PAGE_COUNT));
		requestData.put("srsearch", mSearchText);
		HttpManager manager = new HttpManager( url, requestData, HttpManager.GET, getSearchResultTransaction);
		manager.start();
	}

	/**
	 * 滑动全部展开后会调用 在这里开始去加载数据
	 * 
	 */
	@Override
	public void onSlidebarOpened() {
		getSearchResult();
	}

	@Override
	public void onSlidebarClosed() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_try_again:
			mLayoutError.setVisibility(View.GONE);
			getSearchResult();
			break;
		case R.id.btn_back:
			closeSlider();
			break;
		default:
			break;
		}
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_FLING || scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			if (view.getLastVisiblePosition() == view.getCount() - 1) {
				if(!isRefreshing && mOffset> 0) {
					isRefreshing = true;
					L.i("isrefreshing-->" + isRefreshing);
					getSearchResult();
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterview, View view, int position, long id) {
		if (position < 1) {
			return;
		}
		SearchItemJson itemJson = mSearchResults.get(position-1);
		String titile = itemJson.getTitle();
		titile = titile.replace(" ", "_");
		String url = "http://wiki.eoeandroid.com/api.php?action=parse&format=json&page="+titile;
		Intent intent = new Intent (mContext,WikiContentActivity.class);
		ParamsEntity pe = new ParamsEntity();
		pe.setFirstTitle(getString(R.string.title_search_result));
		pe.setSecondTitle("");
		pe.setUri(url);
		intent.putExtra(WikiContentActivity.WIKI_CONTENT, pe);
		getmMainActivity().showView(2, intent);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_DISPLAY_SEARCH:
				isRefreshing = false;
				mLayoutLoading.setVisibility(View.GONE);
				mLayoutPrgogress.setVisibility(View.GONE);
				mLayoutError.setVisibility(View.GONE);
				
				if (mSearchResults.size() == 0) {
					//No favorite to display
					mListView.setVisibility(View.GONE);
					mNoSearchResult.setVisibility(View.VISIBLE);
				}
				else
				{
					mNoSearchResult.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					if(mAdapter==null) {
						mAdapter = new SearchResultAdapter(SearchResultActivity.this, mSearchResults);
						mListView.setAdapter(mAdapter);
						mListView.setOnItemClickListener(SearchResultActivity.this);
					}
					else {
						mAdapter.setSearchResults(mSearchResults);
					}
				}
				break;
			case HANDLER_GET_SEARCH_ERROR:
				mLayoutLoading.setVisibility(View.GONE);
				mLayoutPrgogress.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
				mNoSearchResult.setVisibility(View.GONE);
				mLayoutError.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
		}

	};

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}
	public ITransaction getSearchResultTransaction = new ITransaction() {

		@Override
		public void transactionOver(String result) {
			SearchResultJson responseObject;
			try {
				responseObject = mObjectMapper.readValue(result, new TypeReference<SearchResultJson>() { });
				List<SearchItemJson> results = responseObject.getQuery().getSearch();
				SearchQueryContinusJson queryContinus = responseObject.getQueryContinue();
				if(queryContinus==null) {
					mOffset=-1;
				}
				else {
					mOffset = queryContinus.getSearch().getSroffset();
				}
				if(results!=null && results.size()!=0) {
					mSearchResults.addAll(results);
				}
				mHandler.obtainMessage(HANDLER_DISPLAY_SEARCH, responseObject)
						.sendToTarget();
			} catch (Exception e) {
				L.e("getGiftsTransaction exception", e);
				mHandler.obtainMessage(HANDLER_GET_SEARCH_ERROR).sendToTarget();
			}
		}

		@Override
		public void transactionException(int erroCode, String result,
				Exception e) {
			mHandler.obtainMessage(HANDLER_GET_SEARCH_ERROR).sendToTarget();
		}
	};

}

package cn.eoe.wiki.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.eoe.wiki.R;
import cn.eoe.wiki.activity.adapter.FavoriteAdapter;
import cn.eoe.wiki.db.dao.FavoriteDao;
import cn.eoe.wiki.db.entity.FavoriteEntity;
import cn.eoe.wiki.utils.L;
import cn.eoe.wiki.utils.WikiUtil;

/**
 * displya the favorite page
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @date Aug 15, 2012
 * @version 1.0.0
 *
 */
public class FavoriteActivity extends SliderActivity implements OnClickListener,OnScrollListener{
	public static final 	int		PAGE_COUNT 			= 20;
	private static final 	int		HANDLER_REFRESH 	= 0x0001;
	private static final 	int		HANDLER_LOADING_MORE = 0x0002;

	private ListView		mListView;
	private FavoriteAdapter	mAdapter;

	private LinearLayout	mLayoutLoading;
	private View			mNoFavorite;
	private ImageButton		mBtnBack;
	private TextView		mTvTitle;
	private View 			mLayoutPrgogress;
	
	private FavoriteDao		mFavoriteDao;
	
	private int 			currentPage = 0;
	private int 			totalCount = -1;
	private int				displayCount = -1;
	private List<FavoriteEntity> mFavorites;
	private boolean			isRefreshing;
	private LayoutInflater 	mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorites);
		mFavoriteDao = new FavoriteDao(mContext);
		mInflater = LayoutInflater.from(mContext);
		mFavorites = new ArrayList<FavoriteEntity>();
		initComponent();
		initData();
	}

	void initComponent() {
		mTvTitle = (TextView)findViewById(R.id.tv_title_parent);
		mListView = (ListView)findViewById(R.id.ListView);
		mListView.setDividerHeight(0);
		mLayoutLoading = (LinearLayout)findViewById(R.id.layout_loading);
		mNoFavorite = findViewById(R.id.layout_no_favorite);
		mBtnBack=(ImageButton)findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
	}

	void initData() {
		isRefreshing = false;
		mTvTitle.setText(R.string.title_favorite);
		
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
		mNoFavorite.setVisibility(View.GONE);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			closeSlider();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSlidebarOpened() {
		L.e("favorite onSidebarOpened");
		new LoadFavoriteFromDb().execute(currentPage+1);
	}

	@Override
	public void onSlidebarClosed() {
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_FLING || scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			if (view.getLastVisiblePosition() == view.getCount() - 1) {
				if(!isRefreshing && displayCount<totalCount) {
					isRefreshing = true;
					L.i("isrefreshing-->" + isRefreshing);
					mHandler.sendEmptyMessage(HANDLER_LOADING_MORE);
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_REFRESH:
				L.d("Favorite Handler HANDLER_REFRESH");
				isRefreshing = false;
				mLayoutLoading.setVisibility(View.GONE);
				mLayoutPrgogress.setVisibility(View.GONE);

				if(mFavorites.size()==0) {
					//No favorite to display
					mListView.setVisibility(View.GONE);
					mNoFavorite.setVisibility(View.VISIBLE);
				}
				else {
					mNoFavorite.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					boolean ret = ((Boolean)msg.obj).booleanValue();
					if(!ret)
						return;//如果没有更新成功则返回 
					currentPage++;
					displayCount = mFavorites.size();
					if(mAdapter==null)
					{
						mAdapter = new FavoriteAdapter(FavoriteActivity.this, mFavorites);
						mListView.setAdapter(mAdapter);
					}
					else
					{
						mAdapter.setFavorites(mFavorites);
					}
				}
				break;
			case HANDLER_LOADING_MORE:
				mLayoutPrgogress.setVisibility(View.VISIBLE);
				new LoadFavoriteFromDb().execute(currentPage+1);
				break;

			default:
				break;
			}
		}
		
	};

	class LoadFavoriteFromDb extends AsyncTask<Integer, Integer, Boolean>
	{
		@Override
		protected Boolean doInBackground(Integer... params) {
			//get the total count
			if(totalCount==-1) {
				totalCount = mFavoriteDao.getFavoriteCount();
			}
			int loadPage = params[0];
			L.d("favorite doInBackground:"+loadPage);
			List<FavoriteEntity> favorites = mFavoriteDao.getFavorites(loadPage, PAGE_COUNT);
			if(favorites==null || favorites.size()==0) {
				mHandler.obtainMessage(HANDLER_REFRESH, Boolean.valueOf(false)).sendToTarget();
			}
			else {
				mFavorites.addAll(favorites);
				mHandler.obtainMessage(HANDLER_REFRESH, Boolean.valueOf(true)).sendToTarget();
			}
			
			return true;
		}
		
	}
}

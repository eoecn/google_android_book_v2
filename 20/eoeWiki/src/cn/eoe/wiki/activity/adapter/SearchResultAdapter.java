package cn.eoe.wiki.activity.adapter;

import java.util.List;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.eoe.wiki.R;
import cn.eoe.wiki.activity.SearchResultActivity;
import cn.eoe.wiki.json.SearchItemJson;
/**
 * seach Adapter
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @date Aug 20, 2012
 * @version 1.0.0
 *
 */
public class SearchResultAdapter extends BaseAdapter {
	
	private SearchResultActivity 			mContext;
	private List<SearchItemJson>			mSearchResults;
	private LayoutInflater 					mInflater;

	public SearchResultAdapter(SearchResultActivity mContext,
			List<SearchItemJson> searchResults) {
		this.mContext = mContext;
		this.mSearchResults = searchResults;
		mInflater = LayoutInflater.from(mContext);
	}
	public void setSearchResults(List<SearchItemJson> searchResults)
	{
		this.mSearchResults = searchResults;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return mSearchResults==null?0:mSearchResults.size();
	}

	@Override
	public Object getItem(int position) {
		return mSearchResults==null?null:mSearchResults.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TagHolder hodler = null;
		if(convertView==null)
		{
			convertView = mInflater.inflate(R.layout.search_result_item, null);
			hodler = new TagHolder();
			hodler.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			hodler.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			hodler.layout = (LinearLayout)convertView.findViewById(R.id.layout);
			convertView.setTag(hodler);
		}
		else
		{
			hodler = (TagHolder)convertView.getTag();
		}
		SearchItemJson item = mSearchResults.get(position);
		hodler.tvTitle.setText(item.getTitle());
		String content = item.getSnippet();
		hodler.tvContent.setText(Html.fromHtml(content));
		int count = mSearchResults.size();
		if(count==1)
		{
			hodler.layout.setBackgroundResource(R.drawable.bg_item);
		}
		else
		{
			if(position==0)
			{
				hodler.layout.setBackgroundResource(R.drawable.bg_item_top);
			}
			else if(position==(count-1))
			{
				hodler.layout.setBackgroundResource(R.drawable.bg_item_bottom);
			}
			else
			{
				hodler.layout.setBackgroundResource(R.drawable.bg_item_nocorners);
			}
		}
		return convertView;
	}
	static class TagHolder
	{
		LinearLayout layout;
		TextView tvTitle;
		TextView tvContent;
	}
}

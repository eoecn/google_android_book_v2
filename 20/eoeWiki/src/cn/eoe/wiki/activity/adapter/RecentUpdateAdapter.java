package cn.eoe.wiki.activity.adapter;

import java.util.List;

import cn.eoe.wiki.R;
import cn.eoe.wiki.json.RecentlyUpdatedJson;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecentUpdateAdapter extends BaseAdapter {

	private Context 					mContext;
	private List<RecentlyUpdatedJson> 	mUpdateResluts;
	private LayoutInflater				mInflater;
	
	public RecentUpdateAdapter(Context con,List<RecentlyUpdatedJson> lists){
		this.mContext = con;
		this.mUpdateResluts = lists;
		mInflater = LayoutInflater.from(con);
	}
	
	public void setSearchResults(List<RecentlyUpdatedJson> updateResults){
		this.mUpdateResluts = updateResults;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mUpdateResluts==null? 0 :mUpdateResluts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mUpdateResluts==null?null:mUpdateResluts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.recent_update_item,null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.item_title);
			holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		RecentlyUpdatedJson item = mUpdateResluts.get(position);
		holder.tvTitle.setText(item.getTitle());
		int count = mUpdateResluts.size();
		if(count==1){
			holder.layout.setBackgroundResource(R.drawable.bg_item);
		}else
		{
			if(position==0)
			{
				holder.layout.setBackgroundResource(R.drawable.bg_item_top);
			}
			else if(position==(count-1))
			{
				holder.layout.setBackgroundResource(R.drawable.bg_item_bottom);
			}
			else
			{
				holder.layout.setBackgroundResource(R.drawable.bg_item_nocorners);
			}
		}
		return convertView;
	}
	
	static class ViewHolder{
		LinearLayout layout;
		TextView tvTitle;
	}

}

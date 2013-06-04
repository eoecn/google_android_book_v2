package cn.eoe.wiki.activity.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.eoe.wiki.R;
import cn.eoe.wiki.activity.BaseActivity;
import cn.eoe.wiki.activity.FavoriteActivity;
import cn.eoe.wiki.activity.SubCategoryActivity;
import cn.eoe.wiki.db.entity.FavoriteEntity;
import cn.eoe.wiki.listener.FavoriteItemListener;
import cn.eoe.wiki.listener.SubCategoryListener;
import cn.eoe.wiki.utils.L;
/**
 * 收藏夹的适配器
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @date Aug 15, 2012
 * @version 1.0.0
 *
 */
public class FavoriteAdapter extends BaseAdapter{
	private FavoriteActivity		context;
	private List<FavoriteEntity> 	favorites;
	private LayoutInflater 			inflater;
	
	public FavoriteAdapter(FavoriteActivity context,List<FavoriteEntity> favorites)
	{
		this.context = context;
		this.favorites = favorites;
		inflater = LayoutInflater.from(context);
	}
	/**
	 * set the favorites. and notify the data set
	 * @param favorites
	 */
	public void setFavorites(List<FavoriteEntity> favorites)
	{
		this.favorites = favorites;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return favorites==null?0:favorites.size();
	}

	@Override
	public Object getItem(int position) {
		return favorites==null?null:favorites.get(position);
	}

	@Override
	public long getItemId(int position) {
		return favorites==null?0:favorites.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		L.d("favorite adapter get view position:"+position);
		if(convertView==null)
		{
			convertView = inflater.inflate(R.layout.favorite_item, null);
			TextView text = (TextView)convertView.findViewById(R.id.textView);
			FavoriteEntity entity = favorites.get(position);
			text.setText(entity.getTitle());
			int count = favorites.size();
			if(count==1)
			{
				text.setBackgroundResource(R.drawable.bg_item);
			}
			else
			{
				if(position==0)
				{
					text.setBackgroundResource(R.drawable.bg_item_top);
				}
				else if(position==(count-1))
				{
					text.setBackgroundResource(R.drawable.bg_item_bottom);
				}
				else
				{
					text.setBackgroundResource(R.drawable.bg_item_nocorners);
				}
			}
			text.setOnClickListener(new FavoriteItemListener(entity.getUrl(), context,context.getString(R.string.title_favorite),""));
		}
		return convertView;
	}

}

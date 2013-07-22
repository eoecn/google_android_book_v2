package com.example.teselayout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGridView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridmain);

        GridView gridview;
        String[] titles = { "赵1", "钱2", "张三", "李四", "王五" };
        int buf = R.drawable.sample;
        int[] resIds = { buf, buf, buf, buf, buf };
        gridview = (GridView) this.findViewById(R.id.gridview);
        gridview.setAdapter(new MyAdapter(titles, resIds));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                TextView title = (TextView) arg1.findViewById(R.id.itemTitle);
                Log.d("mygridview:", "我点击的是：" + title.getText() + "的照片");
            }
        });
    }

    public class MyAdapter extends BaseAdapter {
        String[] itemTitles, itemTexts;
        int[] itemImageRes;

        public MyAdapter(String[] itemTitles, int[] itemImageRes) {
            this.itemTitles = itemTitles;
            this.itemImageRes = itemImageRes;
        }

        public int getCount() {
            return itemTitles.length;
        }

        public Object getItem(int position) {
            return itemTitles[position];
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) MyGridView.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View itemView = inflater.inflate(R.layout.griditem, null);
                TextView title = (TextView) itemView
                        .findViewById(R.id.itemTitle);
                title.setText(itemTitles[position]);
                ImageView image = (ImageView) itemView
                        .findViewById(R.id.itemImage);
                image.setImageResource(itemImageRes[position]);
                return itemView;
            } else {
                TextView title = (TextView) convertView
                        .findViewById(R.id.itemTitle);
                title.setText(itemTitles[position]);
                ImageView image = (ImageView) convertView
                        .findViewById(R.id.itemImage);
                image.setImageResource(itemImageRes[position]);
                return convertView;
            }
        }
    }
}

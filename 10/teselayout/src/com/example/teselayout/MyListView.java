package com.example.teselayout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyListView extends Activity {
    
    ListView listView;
    TextView showinfo;
    String[] titles = { "赵1", "钱2", "张三", "李四", "王五" };
    String[] texts = { "13910000000", "13910000001", "13910000002",
            "13910000003", "13910000004" };
    int buf = R.drawable.ic_launcher;
    int[] resIds = { buf, buf, buf, buf, buf };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listmain);
        listView = (ListView) this.findViewById(R.id.list);
        showinfo = (TextView) this.findViewById(R.id.T1);
        listView.setAdapter(new MyAdapter(titles, texts, resIds));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                TextView title = (TextView) arg1.findViewById(R.id.itemTitle);
                String info = "点击的联系人是:" + title.getText();
                TextView text = (TextView) arg1.findViewById(R.id.itemText);
                info = info + "\n联系电话：" + text.getText();
                showinfo.setText(info);
            }
        });
    }

    public class MyAdapter extends BaseAdapter {
        String[] itemTitles, itemTexts;
        int[] itemImageRes;

        public MyAdapter(String[] itemTitles, String[] itemTexts,
                int[] itemImageRes) {
            this.itemTitles = itemTitles;
            this.itemTexts = itemTexts;
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
                LayoutInflater inflater = (LayoutInflater) MyListView.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View itemView = inflater.inflate(R.layout.listitem, null);
                TextView title = (TextView) itemView
                        .findViewById(R.id.itemTitle);
                title.setText(itemTitles[position]);
                TextView text = (TextView) itemView.findViewById(R.id.itemText);
                text.setText(itemTexts[position]);
                ImageView image = (ImageView) itemView
                        .findViewById(R.id.itemImage);
                image.setImageResource(itemImageRes[position]);
                return itemView;
            } else {
                TextView title = (TextView) convertView
                        .findViewById(R.id.itemTitle);
                title.setText(itemTitles[position]);
                TextView text = (TextView) convertView
                        .findViewById(R.id.itemText);
                text.setText(itemTexts[position]);
                ImageView image = (ImageView) convertView
                        .findViewById(R.id.itemImage);
                image.setImageResource(itemImageRes[position]);
                return convertView;
            }
        }
    }

}

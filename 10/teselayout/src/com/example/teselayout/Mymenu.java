package com.example.teselayout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Mymenu extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gridmain);
        GridView gridview;
        String[] titles = { "赵1", "钱2", "张三", "李四", "王五" };
        int buf = R.drawable.sample;
        int[] resIds = { buf, buf, buf, buf, buf };
        gridview = (GridView) this.findViewById(R.id.gridview);
        gridview.setAdapter(new MyAdapter(titles, resIds));
        registerForContextMenu(gridview);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                TextView title = (TextView) arg1.findViewById(R.id.itemTitle);
                Log.d("mygridview:", "我点击的是：" + title.getText() + "的照片");
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        switch (item.getItemId()) {
        case R.id.contextitem1:
            Toast.makeText(this, "上下文菜单子项1", Toast.LENGTH_SHORT).show();
            break;
        case R.id.contextitem2:
            Toast.makeText(this, "上下文菜单子项2", Toast.LENGTH_SHORT).show();
            break;
        case R.id.contextitem3:
            Toast.makeText(this, "上下文菜单子项3", Toast.LENGTH_SHORT).show();
            break;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.optionmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.optionitem1:
            Toast.makeText(this, "点击了选项1", Toast.LENGTH_SHORT).show();
            return true;
        case R.id.optionitem2:
            Toast.makeText(this, "点击了选项2", Toast.LENGTH_SHORT).show();
            return true;
        case R.id.optionitem3:
            Toast.makeText(this, "点击了选项3", Toast.LENGTH_SHORT).show();
            return true;
        case R.id.subitem1:
            Toast.makeText(this, "点击子菜单选项1", Toast.LENGTH_SHORT).show();
            return true;
        case R.id.subitem2:
            Toast.makeText(this, "点击子菜单选项2", Toast.LENGTH_SHORT).show();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
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
                LayoutInflater inflater = (LayoutInflater) Mymenu.this
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

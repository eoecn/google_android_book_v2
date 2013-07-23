package com.example.teselayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button lltest, rltest, listtest, gridtest, inputtest, dialogtest,
            menutest, notiftest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getview();
        setview();

    }

    private void getview() {
        lltest = (Button) this.findViewById(R.id.lltest);
        rltest = (Button) this.findViewById(R.id.rltest);
        listtest = (Button) this.findViewById(R.id.listtest);
        gridtest = (Button) this.findViewById(R.id.gridtest);
        inputtest = (Button) this.findViewById(R.id.inputtest);
        dialogtest = (Button) this.findViewById(R.id.dialogtest);
        menutest = (Button) this.findViewById(R.id.menutest);
        notiftest = (Button) this.findViewById(R.id.notiftest);
    }

    private void setview() {
        lltest.setOnClickListener(clicklister);
        rltest.setOnClickListener(clicklister);
        listtest.setOnClickListener(clicklister);
        gridtest.setOnClickListener(clicklister);
        inputtest.setOnClickListener(clicklister);
        dialogtest.setOnClickListener(clicklister);
        menutest.setOnClickListener(clicklister);
        notiftest.setOnClickListener(clicklister);
    }

    private View.OnClickListener clicklister = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
            case R.id.lltest:
                intent.setClass(MainActivity.this, LinearlayoutAc.class);
                startActivity(intent);

                break;
            case R.id.rltest:
                intent.setClass(MainActivity.this, RelativelayoutAc.class);
                startActivity(intent);
                break;
            case R.id.listtest:
                intent.setClass(MainActivity.this, MyListView.class);
                startActivity(intent);
                break;
            case R.id.gridtest:
                intent.setClass(MainActivity.this, MyGridView.class);
                startActivity(intent);
                break;
            case R.id.inputtest:
                intent.setClass(MainActivity.this, InputTest.class);
                startActivity(intent);
                break;
            case R.id.dialogtest:
                intent.setClass(MainActivity.this, Testdialog.class);
                startActivity(intent);
                break;
            case R.id.menutest:
                intent.setClass(MainActivity.this, Mymenu.class);
                startActivity(intent);
                break;
            case R.id.notiftest:
                intent.setClass(MainActivity.this, MyNotify.class);
                startActivity(intent);
                break;

            }

        }
    };
}

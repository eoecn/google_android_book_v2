package com.example.teselayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class LinearlayoutAc extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.linearlayout1);

    }

}

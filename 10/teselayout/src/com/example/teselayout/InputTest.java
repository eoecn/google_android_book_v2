package com.example.teselayout;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class InputTest extends Activity {

    private ImageView ImageView1;
    private EditText editText1;
    private Spinner spinner1;
    private RadioGroup RadioGroup1;
    private CheckBox checkbox1, checkbox2;
    private ToggleButton ToggleButton1;
    private ArrayAdapter<String> adapter;
    private DatePicker datePicker;
    private Button button1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputmain);
        findwidget();
    }

    void findwidget() {
        ImageView1 = (ImageView) findViewById(R.id.ImageView1);
        ImageView1.setImageResource(R.drawable.sample);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                // TODO Auto-generated method stub
            }
        });
        RadioGroup1 = (RadioGroup) findViewById(R.id.RadioGroup1);
        RadioGroup1
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup arg0, int checkedId) {
                        switch (checkedId) {
                        case R.id.RadioButton1:
                            Log.d("TestActivity", "select RadioButton1");
                            break;
                        case R.id.RadioButton2:
                            Log.d("TestActivity", "select RadioButton2");
                            break;
                        default:
                            break;
                        }
                    }
                });
        checkbox1 = (CheckBox) findViewById(R.id.checkbox1);
        checkbox2 = (CheckBox) findViewById(R.id.checkbox2);
        OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                switch (buttonView.getId()) {
                case R.id.checkbox1: // action
                    Log.d("TestActivity", "checkbox1:" + isChecked + "="
                            + buttonView.getText());
                    break;
                case R.id.checkbox2: // action
                    Log.d("TestActivity", "checkbox2:" + isChecked + "="
                            + buttonView.getText());
                    break;
                case R.id.ToggleButton1: // action
                    Log.d("TestActivity", "ToggleButton1:" + isChecked + "="
                            + buttonView.getText());
                    break;
                }
            }
        };
        checkbox1.setOnCheckedChangeListener(listener);
        checkbox2.setOnCheckedChangeListener(listener);
        ToggleButton1 = (ToggleButton) findViewById(R.id.ToggleButton1);
        ToggleButton1.setOnCheckedChangeListener(listener);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.init(2012, 9, 8, new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year,
                    int monthOfYear, int dayOfMonth) {
                Log.d("TestActivity", "datePicker您选择的日期是：" + year + "年"
                        + (monthOfYear + 1) + "月" + dayOfMonth + "日。");
            }
        });
        final String[] from = { "中国", "美国", "俄罗斯", "加拿大" };
        spinner1 = (Spinner) findViewById(R.id.Spinner1);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, from);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
                Log.d("TestActivity", "我点击的是spinner1：" + from[arg2]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TestActivity:", "我点击的是：button");
            }
        });
    }
}

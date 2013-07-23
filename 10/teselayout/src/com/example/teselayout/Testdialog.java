package com.example.teselayout;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class Testdialog extends Activity {
    
    private Button button1, button2, button3, button4;
    final int MyAlertDialog = 1, MyDatePickerDialog = 2,
            MyTimePickerDialog = 3, MyProgressDialog = 4;
    Calendar dateAndTime = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testdialog);
        findwidget();
    }

    void findwidget() {
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button1.setOnClickListener(Btocl);
        button2.setOnClickListener(Btocl);
        button3.setOnClickListener(Btocl);
        button4.setOnClickListener(Btocl);
    }

    View.OnClickListener Btocl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.button1:
                showDialog(MyAlertDialog);
                break;
            case R.id.button2:
                showDialog(MyDatePickerDialog);
                break;
            case R.id.button3:
                showDialog(MyTimePickerDialog);
                break;
            case R.id.button4:
                showDialog(MyProgressDialog);
                break;
            }
        }
    };

    @Override
    public Dialog onCreateDialog(int id) {
        switch (id) {
        case MyAlertDialog:
            Dialog dialog = new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_launcher).setTitle("MY对话框")
                    .setMessage("一个自己设置的对话框哦，好看不?")
                    .setNegativeButton("不好看", ocl).setNeutralButton("一般般", ocl)
                    .setPositiveButton("很喜欢", ocl).create();
            return dialog;
        case MyDatePickerDialog:
            DatePickerDialog dateDatePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                int monthOfYear, int dayOfMonth) {
                        }
                    }, dateAndTime.get(Calendar.YEAR),
                    dateAndTime.get(Calendar.MONTH),
                    dateAndTime.get(Calendar.DAY_OF_MONTH));
            return dateDatePickerDialog;
        case MyTimePickerDialog:
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                int minute) {
                        }
                    }, dateAndTime.get(Calendar.HOUR_OF_DAY),
                    dateAndTime.get(Calendar.MINUTE), true);
            return timePickerDialog;
        case MyProgressDialog:
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(true);
            return progressDialog;
        }
        return null;
    }

    OnClickListener ocl = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
            case Dialog.BUTTON_NEGATIVE:
                break;
            case Dialog.BUTTON_NEUTRAL:
                break;
            case Dialog.BUTTON_POSITIVE:
                break;
            }
        }
    };
}

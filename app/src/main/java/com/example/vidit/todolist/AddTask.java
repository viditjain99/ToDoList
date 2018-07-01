package com.example.vidit.todolist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTask extends AppCompatActivity
{
    public static final int ADD_RESULT_CODE=2;
    public static final String TITLE_KEY="title";
    public static final String DESC_KEY="description";
    public static final String DAY_KEY="day";
    public static final String MONTH_KEY="month";
    public static final String YEAR_KEY="year";
    public static final String CHECKED_KEY="checked";
    public static final String HOUR_KEY="hour";
    public static final String MINUTE_KEY="minute";
    public DatePicker datePicker;
    public TimePicker timePicker;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Add Task");
        datePicker=findViewById(R.id.datePicker);
        datePicker.setSpinnersShown(true);
        datePicker.setMinDate(System.currentTimeMillis()-1000);
        timePicker=findViewById(R.id.timePicker);
        timePicker.setIs24HourView(false);
    }
    public void saveTask(View view)
    {
        EditText titleEditText=findViewById(R.id.titleEditText);
        EditText descEditText=findViewById(R.id.descEditText);
        CheckBox importantCheckBox=findViewById(R.id.importantCheckBox);
        String title=titleEditText.getText().toString();
        String desc=descEditText.getText().toString();
        String day=String.valueOf(datePicker.getDayOfMonth());
        String month=String.valueOf(datePicker.getMonth()+1);
        String year=String.valueOf(datePicker.getYear());
        String hour=String.valueOf(timePicker.getHour());
        String minute=String.valueOf(timePicker.getMinute());
        boolean flag=importantCheckBox.isChecked();
        Intent data=new Intent();
        data.putExtra(TITLE_KEY,title);
        data.putExtra(DESC_KEY,desc);
        data.putExtra(DAY_KEY,day);
        data.putExtra(MONTH_KEY,month);
        data.putExtra(YEAR_KEY,year);
        data.putExtra(CHECKED_KEY,flag);
        data.putExtra(HOUR_KEY,hour);
        data.putExtra(MINUTE_KEY,minute);
        setResult(ADD_RESULT_CODE,data);
        finish();
    }
}

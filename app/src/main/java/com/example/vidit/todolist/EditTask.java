package com.example.vidit.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditTask extends AppCompatActivity
{
    public static int EDIT_RESULT_CODE=4;
    public static final String EDIT_TITLE="title";
    public static final String EDIT_DESC="desc";
    public static final String POSITION="pos";
    public static final String EDIT_CHECKED="important";
    public static final String EDIT_DATE="date";
    public static final String EDIT_TIME="time";
    Intent intent;
    EditText titleEditText,descEditText,dateEditText,timeEditText;
    CheckBox importantCheckBox;
    String position;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Edit Task");
        intent=getIntent();
        titleEditText=findViewById(R.id.titleEditText);
        descEditText=findViewById(R.id.descEditText);
        dateEditText=findViewById(R.id.dateEditText);
        timeEditText=findViewById(R.id.timeEditText);
        importantCheckBox=findViewById(R.id.importantCheckBox);
        importantCheckBox.setChecked(intent.getBooleanExtra(MainActivity.IMPORTANT,false));
        titleEditText.setText(intent.getStringExtra(MainActivity.TITLE));
        descEditText.setText(intent.getStringExtra(MainActivity.DESC));
        dateEditText.setText(intent.getStringExtra(MainActivity.DATE));
        timeEditText.setText(intent.getStringExtra(MainActivity.TIME));
        final Calendar calendar=Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                String format="dd/MM/yy";
                SimpleDateFormat sdf=new SimpleDateFormat(format, Locale.ENGLISH);
                dateEditText.setText(sdf.format(calendar.getTime()));
            }
        };
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour=calendar.get(Calendar.HOUR_OF_DAY);
                int minute=calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog;
                timePickerDialog=new TimePickerDialog(EditTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String HOUR=String.valueOf(hour);
                        String MINUTE=String.valueOf(minute);
                        if(hour==0)
                        {
                            HOUR=hour+"0";
                        }
                        if(minute==0 || minute<10)
                        {
                            MINUTE="0"+minute;
                        }
                        timeEditText.setText(HOUR + ":" + MINUTE);
                    }
                },hour,minute,false);
                timePickerDialog.show();
            }
        });
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new DatePickerDialog(EditTask.this,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        position=intent.getStringExtra(MainActivity.POS);
    }
    public void saveTask(View view)
    {
        String title=titleEditText.getText().toString();
        String desc=descEditText.getText().toString();
        String date=dateEditText.getText().toString();
        String time=timeEditText.getText().toString();
        boolean flag=importantCheckBox.isChecked();
        if(date.length()==0 || time.length()==0 || title.length()==0)
        {
            Snackbar.make(view,"Please Enter the details",Snackbar.LENGTH_LONG).show();
            return;
        }
        Intent editData=new Intent();
        editData.putExtra(EDIT_TITLE,title);
        editData.putExtra(EDIT_DESC,desc);
        editData.putExtra(POSITION,position);
        editData.putExtra(EDIT_CHECKED,flag);
        editData.putExtra(EDIT_DATE,date);
        editData.putExtra(EDIT_TIME,time);
        setResult(EDIT_RESULT_CODE,editData);
        finish();
    }
}

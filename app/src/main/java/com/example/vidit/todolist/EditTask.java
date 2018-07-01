package com.example.vidit.todolist;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class EditTask extends AppCompatActivity
{
    public static int EDIT_RESULT_CODE=4;
    public static final String EDIT_TITLE="title";
    public static final String EDIT_DESC="desc";
    public static final String POSITION="pos";
    public static final String EDIT_DAY="day";
    public static final String EDIT_MONTH="month";
    public static final String EDIT_YEAR="year";
    public static final String EDIT_CHECKED="important";
    public static final String EDIT_HOUR="hour";
    public static final String EDIT_MINUTE="minute";
    public DatePicker datePicker;
    public TimePicker timePicker;
    Intent intent;
    EditText titleEditText,descEditText;
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
        datePicker=findViewById(R.id.datePicker);
        datePicker.setSpinnersShown(true);
        datePicker.setMinDate(System.currentTimeMillis()-1000);
        timePicker=findViewById(R.id.timePicker);
        titleEditText=findViewById(R.id.titleEditText);
        descEditText=findViewById(R.id.descEditText);
        importantCheckBox=findViewById(R.id.importantCheckBox);
        importantCheckBox.setChecked(intent.getBooleanExtra(MainActivity.IMPORTANT,false));
        titleEditText.setText(intent.getStringExtra(MainActivity.TITLE));
        descEditText.setText(intent.getStringExtra(MainActivity.DESC));
        String monthString=intent.getStringExtra(MainActivity.MONTH);
        String yearString=intent.getStringExtra(MainActivity.YEAR);
        String dayString=intent.getStringExtra(MainActivity.DAY);
        String hourString=intent.getStringExtra(MainActivity.HOUR);
        String minuteString=intent.getStringExtra(MainActivity.MINUTE);
        if(monthString!=null && yearString!=null && dayString!=null)
        {
            int month=Integer.parseInt(monthString);
            int day=Integer.parseInt(dayString);
            int year=Integer.parseInt(yearString);
            datePicker.updateDate(year,month,day);
            timePicker.setHour(Integer.parseInt(hourString));
            timePicker.setMinute(Integer.parseInt(minuteString));
            timePicker.setIs24HourView(false);
        }
        position=intent.getStringExtra(MainActivity.POS);
    }
    public void saveTask(View view)
    {
        String title=titleEditText.getText().toString();
        String desc=descEditText.getText().toString();
        String day=String.valueOf(datePicker.getDayOfMonth());
        String month=String.valueOf(datePicker.getMonth()+1);
        String year=String.valueOf(datePicker.getYear());
        boolean flag=importantCheckBox.isChecked();
        String hour=String.valueOf(timePicker.getHour());
        String minute=String.valueOf(timePicker.getMinute());
        Intent editData=new Intent();
        editData.putExtra(EDIT_TITLE,title);
        editData.putExtra(EDIT_DESC,desc);
        editData.putExtra(POSITION,position);
        editData.putExtra(EDIT_DAY,day);
        editData.putExtra(EDIT_MONTH,month);
        editData.putExtra(EDIT_YEAR,year);
        editData.putExtra(EDIT_CHECKED,flag);
        editData.putExtra(EDIT_HOUR,hour);
        editData.putExtra(EDIT_MINUTE,minute);
        setResult(EDIT_RESULT_CODE,editData);
        finish();
    }
}

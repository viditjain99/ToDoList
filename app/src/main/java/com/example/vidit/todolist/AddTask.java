package com.example.vidit.todolist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.example.vidit.todolist.R.id.timeEditText;

public class AddTask extends AppCompatActivity {
    public static final int ADD_RESULT_CODE = 2;
    public static final String TITLE_KEY = "title";
    public static final String DESC_KEY = "description";
    public static final String CHECKED_KEY = "checked";
    public static final String DATE_KEY = "date";
    public static final String TIME_KEY = "time";
    EditText titleEditText, descEditText, dateEditText, timeEditText;
    Intent intent;
    TaskAdapter adapter;
    String from = "from";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Add Task");
        adapter = new TaskAdapter(this, MainActivity.tasks);
        intent = getIntent();
        titleEditText = findViewById(R.id.titleEditText);
        descEditText = findViewById(R.id.descEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                String format = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
                dateEditText.setText(sdf.format(calendar.getTime()));
            }
        };
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(AddTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String HOUR=String.valueOf(hour);
                        String MINUTE=String.valueOf(minute);
                        if(hour==0)
                        {
                            HOUR=hour+"0";
                        }
                        if(minute==0)
                        {
                            MINUTE=minute+"0";
                        }
                        timeEditText.setText(HOUR + ":" + MINUTE);
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddTask.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        from = intent.getStringExtra("uniquid");
        if (from == null) {
            from = "from";
        }
        String action = intent.getAction();
        if (action != null) {
            String type = intent.getType();
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (action.equals(Intent.ACTION_SEND) && type != null) {
                if (type.equals("text/plain")) {
                    if (sharedText != null) {
                        titleEditText.setText(sharedText);
                    }
                }
            }
        }
    }

    public void saveTask(View view) {
        CheckBox importantCheckBox = findViewById(R.id.importantCheckBox);
        String title = titleEditText.getText().toString();
        String desc = descEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String time = timeEditText.getText().toString();
        boolean flag = importantCheckBox.isChecked();
        if(date.length()==0 || time.length()==0 || title.length()==0)
        {
            Snackbar.make(view,"Please Enter the details",Snackbar.LENGTH_LONG).show();
            return;
        }
        if (from.equals("mainActivity")) {
            Intent data = new Intent();
            data.putExtra(TITLE_KEY, title);
            data.putExtra(DESC_KEY, desc);
            data.putExtra(DATE_KEY, date);
            data.putExtra(CHECKED_KEY, flag);
            data.putExtra(TIME_KEY, time);
            setResult(ADD_RESULT_CODE, data);
            finish();
        }
        else
        {
            if(date.length()==0 || time.length()==0)
            {
                Snackbar.make(view,"Please Enter the details",Snackbar.LENGTH_LONG).show();
                return;
            }
            Task task=new Task(title,desc,date);
            task.setDate(date);
            task.setImportant(flag);
            task.setTime(time);
            TaskOpenHelper openHelper=TaskOpenHelper.getInstance(getApplicationContext());
            SQLiteDatabase database=openHelper.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(Contract.Task.COLUMN_TITLE,task.getTitle());
            contentValues.put(Contract.Task.COLUMN_DESC,task.getDescription());
            contentValues.put(Contract.Task.COLUMN_DATE,task.getDate());
            contentValues.put(Contract.Task.COLUMN_IMPORTANT,task.getImportant());
            contentValues.put(Contract.Task.COLUMN_TIME,task.getTime());
            long id=database.insert(Contract.Task.TABLE_NAME,null,contentValues);
            if(id>-1L)
            {
                task.setId(id);
            }
            MainActivity.tasks.add(task);
            adapter.notifyDataSetChanged();
            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
            Intent intent=new Intent(this,MyReciever.class);
            intent.putExtra("TITLE_N",title);
            intent.putExtra("ID",task.getId());
            PendingIntent pendingIntent=PendingIntent.getBroadcast(this,(int) task.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar calendar= Calendar.getInstance();
            String[] Date=date.split("/");
            String day=Date[0];
            String month=Date[1];
            String year=Date[2];
            year="20"+year;
            String[] Time=time.split(":");
            String hour=Time[0];
            String minute=Time[1];
            calendar.set(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day),Integer.parseInt(hour),Integer.parseInt(minute));
            long currentTime=calendar.getTimeInMillis();
            alarmManager.set(AlarmManager.RTC_WAKEUP,currentTime,pendingIntent);
            finish();
        }
    }
}

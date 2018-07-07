package com.example.vidit.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener{

    public static ArrayList<Task> tasks=new ArrayList<>();
    public TaskAdapter adapter;
    public static int ADD_TASK_REQUEST_CODE=1;
    public static int EDIT_TASK_REQUEST_CODE=3;
    public static final String TITLE="title";
    public static final String DESC="desc";
    public static final String POS="position";
    public static final String DATE="date";
    public static final String TIME="time";
    public static final String IMPORTANT="important";
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.taskList);
        FloatingActionButton fab=findViewById(R.id.fab);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TaskOpenHelper openHelper=TaskOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase database=openHelper.getReadableDatabase();
        Cursor cursor=database.query(Contract.Task.TABLE_NAME,null,null,null,null,null,null);
        while(cursor.moveToNext())
        {
            String title=cursor.getString(cursor.getColumnIndex(Contract.Task.COLUMN_TITLE));
            String desc=cursor.getString(cursor.getColumnIndex(Contract.Task.COLUMN_DESC));
            long id=cursor.getLong(cursor.getColumnIndex(Contract.Task.COLUMN_ID));
            String date=cursor.getString(cursor.getColumnIndex(Contract.Task.COLUMN_DATE));
            String flag=cursor.getString(cursor.getColumnIndex(Contract.Task.COLUMN_IMPORTANT));
            String time=cursor.getString(cursor.getColumnIndex(Contract.Task.COLUMN_TIME));
            Task task=new Task(title,desc,date);
            task.setId(id);
            task.setDate(date);
            task.setTime(time);
            task.setImportant(Boolean.parseBoolean(flag));
            tasks.add(task);
        }
        adapter=new TaskAdapter(this,tasks);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(MainActivity.this,AddTask.class);
                intent.putExtra("uniquid","mainActivity");
                startActivityForResult(intent,ADD_TASK_REQUEST_CODE);
            }
        });
//        final SmsManager manager=SmsManager.getDefault();
//        BroadcastReceiver receiver=new BroadcastReceiver()
//        {
//            @Override
//            public void onReceive(Context context, Intent intent)
//            {
//                if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
//                {
//                    Bundle bundle = intent.getExtras();
//                    SmsMessage[] msgs=null;
//                    if (bundle != null) {
//                        try
//                        {
//                            Object[] pdus = (Object[]) bundle.get("pdus");
//                            msgs = new SmsMessage[pdus.length];
//                            for (int i = 0; i < msgs.length; i++)
//                            {
//                                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
//                                String msgBody = msgs[i].getMessageBody();
//                                Intent intent1=new Intent();
//                                intent1.setAction(Intent.ACTION_SEND);
//                                intent1.setType("text/plain");
//                                intent1.putExtra(Intent.EXTRA_TEXT,msgBody);
//                                startActivity(intent1);
//                            }
//                        }
//                        catch (Exception e)
//                        {
//                            //
//                        }
//                    }
//                }
//            }
//        };
//        IntentFilter intentFilter=new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
//        registerReceiver(receiver,intentFilter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.functions,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==R.id.importantTasks)
        {
            Intent intent=new Intent(this,ImportantTasks.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        final Task task=tasks.get(i);
        final int position=i;
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Do you really want to delete?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TaskOpenHelper openHelper=TaskOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase database=openHelper.getWritableDatabase();
                long id=task.getId();
                String[] selectionArgs={id+""};
                ContentValues contentValues=new ContentValues();
                contentValues.put(Contract.Task.COLUMN_IMPORTANT,false);
                database.update(Contract.Task.TABLE_NAME,contentValues,Contract.Task.COLUMN_ID+" = ?",selectionArgs);
                database.delete(Contract.Task.TABLE_NAME,Contract.Task.COLUMN_ID+" = ?",selectionArgs);
                tasks.remove(position);
                AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                Intent intent=new Intent(MainActivity.this,MyReciever.class);
                intent.putExtra("TITLE_N",task.getTitle());
                intent.putExtra("ID",task.getId());
                PendingIntent pendingIntent=PendingIntent.getBroadcast(MainActivity.this,(int) task.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ;
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
        return true;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        final TextView titleTextView,descTextView;
        titleTextView=new TextView(this);
        descTextView=new TextView(this);
        final int position=i;
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LinearLayout layout=new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        titleTextView.setLayoutParams(params1);
        LinearLayout.LayoutParams params2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        descTextView.setLayoutParams(params2);
        titleTextView.setPadding(50,45,25,0);
        titleTextView.setTextSize(20);
        String t="<b>"+tasks.get(position).getTitle()+"</b>";
        titleTextView.setText(Html.fromHtml(t));
        titleTextView.setTextColor(Color.BLACK);
        descTextView.setText(tasks.get(position).getDescription());
        descTextView.setPadding(50,30,25,0);
        descTextView.setTextSize(15);
        descTextView.setTextColor(Color.BLACK);
        layout.addView(titleTextView);
        layout.addView(descTextView);
        builder.setView(layout);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ;
            }
        });
        builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(MainActivity.this,EditTask.class);
                intent.putExtra(TITLE,titleTextView.getText().toString());
                intent.putExtra(DESC,descTextView.getText().toString());
                intent.putExtra(POS,String.valueOf(position));
                Task task=tasks.get(position);
                intent.putExtra(DATE,task.getDate());
                intent.putExtra(TIME,task.getTime());
                intent.putExtra(IMPORTANT,task.getImportant());
                startActivityForResult(intent,EDIT_TASK_REQUEST_CODE);
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==ADD_TASK_REQUEST_CODE)
        {
            if(resultCode==AddTask.ADD_RESULT_CODE)
            {
                String title=data.getStringExtra(AddTask.TITLE_KEY);
                String desc=data.getStringExtra(AddTask.DESC_KEY);
                String date=data.getStringExtra(AddTask.DATE_KEY);
                String time=data.getStringExtra(AddTask.TIME_KEY);
                boolean flag=data.getBooleanExtra(AddTask.CHECKED_KEY,false);
                if(title.length()==0)
                {
                    Snackbar.make(listView,"Cannot Add Task",Snackbar.LENGTH_LONG).show();
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
                tasks.add(task);
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
            }
        }
        if(requestCode==EDIT_TASK_REQUEST_CODE)
        {
            if(resultCode==EditTask.EDIT_RESULT_CODE)
            {
                String title=data.getStringExtra(EditTask.EDIT_TITLE);
                String desc=data.getStringExtra(EditTask.EDIT_DESC);
                String pos=data.getStringExtra(EditTask.POSITION);
                String date=data.getStringExtra(EditTask.EDIT_DATE);
                String time=data.getStringExtra(EditTask.EDIT_TIME);
                boolean flag=data.getBooleanExtra(EditTask.EDIT_CHECKED,false);
                if(title.length()==0)
                {
                    Toast.makeText(this,"Cannot Edit Task",Toast.LENGTH_SHORT).show();
                    return;
                }
                Task task=tasks.get(Integer.parseInt(pos));
                task.setDate(date);
                task.setImportant(flag);
                TaskOpenHelper openHelper=TaskOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase database=openHelper.getWritableDatabase();
                ContentValues contentValues=new ContentValues();
                task.setTitle(title);
                task.setDescription(desc);
                task.setTime(time);
                contentValues.put(Contract.Task.COLUMN_TITLE,task.getTitle());
                contentValues.put(Contract.Task.COLUMN_DESC,task.getDescription());
                contentValues.put(Contract.Task.COLUMN_DATE,task.getDate());
                contentValues.put(Contract.Task.COLUMN_IMPORTANT,task.getImportant());
                contentValues.put(Contract.Task.COLUMN_TIME,task.getTime());
                long id=task.getId();
                String[] selectionArgs={id+""};
                database.update(Contract.Task.TABLE_NAME,contentValues,Contract.Task.COLUMN_ID+" = ?",selectionArgs);
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
            }
        }
    }
}

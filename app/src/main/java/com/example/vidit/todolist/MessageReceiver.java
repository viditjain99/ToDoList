package com.example.vidit.todolist;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MessageReceiver extends BroadcastReceiver
{
    String title;
    long timeStamp;
    String desc=null;
    String time;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle data = intent.getExtras();
        if(data != null) {
            Object[] pdus = (Object[]) data.get("pdus");

            for (int i = 0; i < pdus.length; ++i) {

                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i], "3gpp");
                title = smsMessage.getDisplayMessageBody();
                timeStamp=smsMessage.getTimestampMillis();
                time="12:00";
            }
            Log.d("rt",title);
            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy");
            String date=sdf.format(new Date(timeStamp));
            Task task=new Task(title,null,date);
            task.setTime(time);
            MainActivity.tasks.add(task);
            TaskOpenHelper openHelper=TaskOpenHelper.getInstance(context);
            SQLiteDatabase sqLiteDatabase=openHelper.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(Contract.Task.COLUMN_TITLE,title);
            contentValues.put(Contract.Task.COLUMN_DESC,desc);
            contentValues.put(Contract.Task.COLUMN_DATE,date);
            contentValues.put(Contract.Task.COLUMN_TIME,time);
            long id=sqLiteDatabase.insert(Contract.Task.TABLE_NAME,null,contentValues);
            if(id>-1L)
            {
                Toast.makeText(context,"Task Added for 12:00 AM. You can edit the task",Toast.LENGTH_LONG).show();
                MainActivity.adapter.notifyDataSetChanged();
                if(MainActivity.tasks.size()>0)
                {
                    MainActivity.rootLayout.setBackgroundResource(R.drawable.white_back);
                }
            }
        }
    }
}

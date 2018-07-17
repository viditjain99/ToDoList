package com.example.vidit.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ImportantTasks extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    ListView importantTasks;
    TaskAdapter adapter;
    ArrayList<Task> tasks=new ArrayList<>();
    Intent intent;
    LinearLayout importantTasksLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_task);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        importantTasksLayout=findViewById(R.id.importantTaskLayout);
        intent=getIntent();
        int count=intent.getIntExtra("Count",0);
        if(count==0)
        {
            importantTasksLayout.setBackgroundResource(R.drawable.important_back_2);
        }
        setTitle("Important Tasks");
        importantTasks=findViewById(R.id.importantTasksList);
        TaskOpenHelper openHelper=TaskOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase database=openHelper.getReadableDatabase();
        String[] selectionArgs={"1"};
        Cursor cursor=database.query(Contract.Task.TABLE_NAME,null,Contract.Task.COLUMN_IMPORTANT+" = ?",selectionArgs,null,null,null);
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
            task.setImportant(Boolean.parseBoolean(flag));
            task.setTime(time);
            tasks.add(task);
        }
        adapter=new TaskAdapter(this,tasks);
        adapter.notifyDataSetChanged();
        importantTasks.setAdapter(adapter);
        importantTasks.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    @Override
    public void onBackPressed()
    {
        finish();
    }
}

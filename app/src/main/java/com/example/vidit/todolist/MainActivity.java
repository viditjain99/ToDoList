package com.example.vidit.todolist;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener{

    ArrayList<Task> tasks=new ArrayList<>();
    TaskAdapter adapter;
    public static int ADD_TASK_REQUEST_CODE=1;
    public static int EDIT_TASK_REQUEST_CODE=3;
    public static final String TITLE="title";
    public static final String DESC="desc";
    public static final String POS="position";
    public static final String DAY="day";
    public static final String MONTH="month";
    public static final String YEAR="year";
    public static final String IMPORTANT="important";
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.taskList);
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
            Task task=new Task(title,desc,date);
            task.setId(id);
            task.setDate(date);
            task.setImportant(Boolean.parseBoolean(flag));
            tasks.add(task);
        }
        adapter=new TaskAdapter(this,tasks);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
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
        if(id==R.id.addtask)
        {
            Intent intent=new Intent(this,AddTask.class);
            startActivityForResult(intent,ADD_TASK_REQUEST_CODE);
        }
        else if(id==R.id.importantTasks)
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
        builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(MainActivity.this,EditTask.class);
                intent.putExtra(TITLE,titleTextView.getText().toString());
                intent.putExtra(DESC,descTextView.getText().toString());
                intent.putExtra(POS,String.valueOf(position));
                Task task=tasks.get(position);
                intent.putExtra(DAY,task.getDay());
                intent.putExtra(MONTH,task.getMonth());
                intent.putExtra(YEAR,task.getYear());
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
                String day=data.getStringExtra(AddTask.DAY_KEY);
                String month=data.getStringExtra(AddTask.MONTH_KEY);
                String year=data.getStringExtra(AddTask.YEAR_KEY);
                boolean flag=data.getBooleanExtra(AddTask.CHECKED_KEY,false);
                String date=day+"/"+month+"/"+year;
                if(title.length()==0)
                {
                    Toast.makeText(this,"Cannot Add Task",Toast.LENGTH_SHORT).show();
                    return;
                }
                Task task=new Task(title,desc,date);
                task.setDay(day);
                task.setMonth(month);
                task.setYear(year);
                task.setDate(date);
                task.setImportant(flag);
                TaskOpenHelper openHelper=TaskOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase database=openHelper.getWritableDatabase();
                ContentValues contentValues=new ContentValues();
                contentValues.put(Contract.Task.COLUMN_TITLE,task.getTitle());
                contentValues.put(Contract.Task.COLUMN_DESC,task.getDescription());
                contentValues.put(Contract.Task.COLUMN_DATE,task.getDate());
                contentValues.put(Contract.Task.COLUMN_IMPORTANT,task.getImportant());
                database.insert(Contract.Task.TABLE_NAME,null,contentValues);
                tasks.add(task);
                adapter.notifyDataSetChanged();
            }
        }
        if(requestCode==EDIT_TASK_REQUEST_CODE)
        {
            if(resultCode==EditTask.EDIT_RESULT_CODE)
            {
                String title=data.getStringExtra(EditTask.EDIT_TITLE);
                String desc=data.getStringExtra(EditTask.EDIT_DESC);
                String pos=data.getStringExtra(EditTask.POSITION);
                String day=data.getStringExtra(EditTask.EDIT_DAY);
                String month=data.getStringExtra(EditTask.EDIT_MONTH);
                String year=data.getStringExtra(EditTask.EDIT_YEAR);
                boolean flag=data.getBooleanExtra(EditTask.EDIT_CHECKED,false);
                String date=day+"/"+month+"/"+year;
                if(title.length()==0)
                {
                    Toast.makeText(this,"Cannot Edit Task",Toast.LENGTH_SHORT).show();
                    return;
                }
                Task task=tasks.get(Integer.parseInt(pos));
                task.setDay(day);
                task.setMonth(month);
                task.setYear(year);
                task.setDate(date);
                task.setImportant(flag);
                TaskOpenHelper openHelper=TaskOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase database=openHelper.getWritableDatabase();
                ContentValues contentValues=new ContentValues();
                task.setTitle(title);
                task.setDescription(desc);
                contentValues.put(Contract.Task.COLUMN_TITLE,task.getTitle());
                contentValues.put(Contract.Task.COLUMN_DESC,task.getDescription());
                contentValues.put(Contract.Task.COLUMN_DATE,task.getDate());
                contentValues.put(Contract.Task.COLUMN_IMPORTANT,task.getImportant());
                long id=task.getId();
                String[] selectionArgs={id+""};
                database.update(Contract.Task.TABLE_NAME,contentValues,Contract.Task.COLUMN_ID+" = ?",selectionArgs);
                adapter.notifyDataSetChanged();
            }
        }
    }
}

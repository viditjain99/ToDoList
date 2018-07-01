package com.example.vidit.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskOpenHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME="task_db";
    public static final int VERSION=1;
    private static TaskOpenHelper instance;
    public static TaskOpenHelper getInstance(Context context)
    {
        if(instance==null)
        {
            instance=new TaskOpenHelper(context.getApplicationContext());
        }
        return instance;
    }
    private TaskOpenHelper(Context context)
    {
        super(context,DATABASE_NAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String taskSql="CREATE TABLE "+Contract.Task.TABLE_NAME+" ( "+Contract.Task.COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "+Contract.Task.COLUMN_TITLE+" TEXT , "+Contract.Task.COLUMN_DESC+" TEXT , "+Contract.Task.COLUMN_DATE+" TEXT , "+Contract.Task.COLUMN_IMPORTANT+" BOOLEAN , "+Contract.Task.COLUMN_TIME+" TEXT )";
        sqLiteDatabase.execSQL(taskSql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int i,int i1)
    {

    }
}

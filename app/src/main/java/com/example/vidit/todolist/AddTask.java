package com.example.vidit.todolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class AddTask extends AppCompatActivity
{
    public static final int ADD_RESULT_CODE=2;
    public static final String TITLE_KEY="title";
    public static final String DESC_KEY="description";
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
    }
    public void saveTask(View view)
    {
        EditText titleEditText=findViewById(R.id.titleEditText);
        EditText descEditText=findViewById(R.id.descEditText);
        String title=titleEditText.getText().toString();
        String desc=descEditText.getText().toString();
        Intent data=new Intent();
        data.putExtra(TITLE_KEY,title);
        data.putExtra(DESC_KEY,desc);
        setResult(ADD_RESULT_CODE,data);
        finish();
    }
}

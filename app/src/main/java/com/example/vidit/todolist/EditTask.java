package com.example.vidit.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditTask extends AppCompatActivity
{
    public static int EDIT_RESULT_CODE=4;
    public static final String EDIT_TITLE="title";
    public static final String EDIT_DESC="desc";
    public static final String POSITION="pos";
    Intent intent;
    EditText titleEditText,descEditText;
    String position;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        intent=getIntent();
        titleEditText=findViewById(R.id.titleEditText);
        descEditText=findViewById(R.id.descEditText);
        titleEditText=findViewById(R.id.titleEditText);
        descEditText=findViewById(R.id.descEditText);
        titleEditText.setText(intent.getStringExtra(MainActivity.TITLE));
        descEditText.setText(intent.getStringExtra(MainActivity.DESC));
        position=intent.getStringExtra(MainActivity.POS);
    }
    public void saveTask(View view)
    {
        String title=titleEditText.getText().toString();
        String desc=descEditText.getText().toString();
        Intent editData=new Intent();
        editData.putExtra(EDIT_TITLE,title);
        editData.putExtra(EDIT_DESC,desc);
        editData.putExtra(POSITION,position);
        setResult(EDIT_RESULT_CODE,editData);
        finish();
    }
}

package com.example.vidit.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
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
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.taskList);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        adapter=new TaskAdapter(this,tasks);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.add_task,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==R.id.addtask)
        {
//            AlertDialog.Builder builder=new AlertDialog.Builder(this);
//            builder.setTitle("Add a Task");
//            final EditText titleEditText,descEditText;
//            titleEditText=new EditText(this);
//            LinearLayout layout=new LinearLayout(this);
//            layout.setOrientation(LinearLayout.VERTICAL);
//            LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//            params1.setMarginStart(50);
//            params1.setMarginEnd(25);
//            titleEditText.setLayoutParams(params1);
//            titleEditText.setHint("Title");
//            layout.addView(titleEditText);
//            descEditText=new EditText(this);
//            LinearLayout.LayoutParams params2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//            params2.setMarginStart(50);
//            params2.setMarginEnd(25);
//            descEditText.setLayoutParams(params2);
//            descEditText.setHint("Description");
//            layout.addView(descEditText);
//            builder.setView(layout);
//            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    String task=titleEditText.getText().toString();
//                    String desc=descEditText.getText().toString();
//                    if((task.length()==0 && desc.length()==0) || (task.length()==0 && desc.length()!=0))
//                    {
//                        Toast.makeText(MainActivity.this,"Cannot Add Task",Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    Task t=new Task(task,desc);
//                    tasks.add(t);
//                    listView.setAdapter(adapter);
//                }
//            });
//            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    ;
//                }
//            });
//            AlertDialog dialog=builder.create();
//            dialog.show();
            Intent intent=new Intent(this,AddTask.class);
            startActivityForResult(intent,ADD_TASK_REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        final int position=i;
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Do you really want to delete?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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
                if(title.length()==0)
                {
                    Toast.makeText(this,"Cannot Add Task",Toast.LENGTH_SHORT).show();
                    return;
                }
                Task task=new Task(title,desc);
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
                if(title.length()==0)
                {
                    Toast.makeText(this,"Cannot Edit Task",Toast.LENGTH_SHORT).show();
                    return;
                }
                Task task=tasks.get(Integer.parseInt(pos));
                task.setTitle(title);
                task.setDescription(desc);
                adapter.notifyDataSetChanged();
            }
        }
    }
}

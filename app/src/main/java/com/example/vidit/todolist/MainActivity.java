package com.example.vidit.todolist;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.taskList);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        /*for(int i=0;i<20;i++)
        {
            Task task=new Task("Task "+i,"Description "+i);
            tasks.add(task);
        }
        listView.setAdapter(adapter);
        View view=new View(this);*/
        adapter=new TaskAdapter(this,tasks);
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
        //Toast.makeText(this,String.valueOf(id),Toast.LENGTH_LONG).show();
        if(id==R.id.addtask)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Add a Task");
            final EditText titleEditText,descEditText;
            titleEditText=new EditText(this);
            LinearLayout layout=new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            titleEditText.setLayoutParams(params1);
            titleEditText.setHint("Title");
            layout.addView(titleEditText);
            descEditText=new EditText(this);
            LinearLayout.LayoutParams params2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            descEditText.setLayoutParams(params2);
            descEditText.setHint("Description");
            layout.addView(descEditText);
            builder.setView(layout);
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String task=titleEditText.getText().toString();
                    String desc=descEditText.getText().toString();
                    Task t=new Task(task,desc);
                    tasks.add(t);
                    //adapter=new TaskAdapter(this,tasks);
                    listView.setAdapter(adapter);
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
        TextView titleTextView,descTextView;
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
        titleTextView.setPadding(25,45,25,0);
        titleTextView.setTextSize(20);
        String t="<b>"+tasks.get(position).getTitle()+"</b>";
        titleTextView.setText(Html.fromHtml(t));
        titleTextView.setTextColor(Color.BLACK);
        descTextView.setText(tasks.get(position).getDescription());
        descTextView.setPadding(25,30,25,0);
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
}

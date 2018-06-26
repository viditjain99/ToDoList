package com.example.vidit.todolist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter
{
    ArrayList<Task> tasks;
    LayoutInflater inflater;
    public TaskAdapter(Context context, ArrayList<Task> tasks)
    {
        super(context,0,tasks);
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tasks=tasks;
    }
    @Override
    public int getCount()
    {
        return tasks.size();
    }
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View output=inflater.inflate(R.layout.row_layout,parent,false);
        TextView titleTextView=output.findViewById(R.id.titleTextView);
        titleTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        titleTextView.setTextColor(Color.BLACK);
        titleTextView.setTextSize(25);
        titleTextView.setPadding(25,0,25,0);
        //TextView descTextView=output.findViewById(R.id.descTextView);
        Task task=tasks.get(position);
        titleTextView.setText(task.getTitle());
        //descTextView.setText(task.getDescription());
        return output;
    }
}

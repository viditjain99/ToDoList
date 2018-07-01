package com.example.vidit.todolist;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
        View output=convertView;
        if(output==null)
        {
            output=inflater.inflate(R.layout.row_layout,parent,false);
            TextView titleTextView=output.findViewById(R.id.titleTextView);
            TextView dateTextView=output.findViewById(R.id.dateTextView);
            TaskViewHolder viewHolder=new TaskViewHolder();
            titleTextView.setTextColor(Color.BLACK);
            titleTextView.setTextSize(20);
            titleTextView.setPadding(50,15,25,0);
            dateTextView.setTextSize(15);
            viewHolder.title=titleTextView;
            viewHolder.date=dateTextView;
            output.setTag(viewHolder);
        }
        TaskViewHolder viewHolder=(TaskViewHolder) output.getTag();
        Task task=tasks.get(position);
        viewHolder.date.setText(task.getTime()+'\n'+task.getDate());
        viewHolder.title.setText(task.getTitle());
        return output;
    }
}

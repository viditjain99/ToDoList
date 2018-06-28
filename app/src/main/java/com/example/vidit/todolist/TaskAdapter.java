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
        View output=convertView;
        if(output==null)
        {
            output=inflater.inflate(R.layout.row_layout,parent,false);
            TextView titleTextView=output.findViewById(R.id.titleTextView);
            TaskViewHolder viewHolder=new TaskViewHolder();
//            titleTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            titleTextView.setTextColor(Color.BLACK);
            titleTextView.setTextSize(20);
            titleTextView.setPadding(50,15,25,0);
            viewHolder.title=titleTextView;
            output.setTag(viewHolder);
        }
        TaskViewHolder viewHolder=(TaskViewHolder) output.getTag();
        Task task=tasks.get(position);
        viewHolder.title.setText(task.getTitle());
        return output;
    }
}

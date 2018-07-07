package com.example.vidit.todolist;

public class Task
{
    private String title;
    private String description;
    private long id;
    private String date;
    private boolean flag;
    private String time;

    public Task(String title, String description,String date) {
        this.title=title;
        this.description=description;
        this.date=date;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title=title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description=description;
    }

    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id=id;
    }
    public String getDate()
    {
        return date;
    }
    public void setDate(String date)
    {
        this.date=date;
    }
    public void setImportant(boolean flag)
    {
        this.flag=flag;
    }
    public boolean getImportant()
    {
        return flag;
    }
    public void setTime(String time)
    {
        this.time=time;
    }
    public String getTime()
    {
        return time;
    }
}

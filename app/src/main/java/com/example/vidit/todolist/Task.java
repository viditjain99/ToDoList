package com.example.vidit.todolist;

public class Task
{
    private String title;
    private String description;
    private long id;
    private String date;
    private String day;
    private String month;
    private String year;
    private boolean flag;

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
    public void setDay(String day)
    {
        this.day=day;
    }
    public String getDay()
    {
        return day;
    }
    public void setMonth(String month)
    {
        this.month=month;
    }
    public String getMonth()
    {
        return month;
    }
    public void setYear(String year)
    {
        this.year=year;
    }
    public String getYear()
    {
        return year;
    }
    public void setImportant(boolean flag)
    {
        this.flag=flag;
    }
    public boolean getImportant()
    {
        return flag;
    }
}

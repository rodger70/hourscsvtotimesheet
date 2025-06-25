package com.github.rodger70.hourscsvtotimesheet;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
    private Date date;
    private String name;
    private String start;
    private String end;

    public Task(Date date, String name, String start, String end) {
        this.date = date;
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public Task(Date date) {
        this.date = date;
        this.name = "";
        this.start = "";
        this.end = "";
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String toCSVString(int row) {
        SimpleDateFormat daynameDayMonthYearSdf = new SimpleDateFormat("EEEE d MMMM yyyy");

        return (daynameDayMonthYearSdf.format(date) + "," + name + "," + start + "," + end + ",0:30,=(D" + row + "-C" + row + ")-E" + row);
    }    

    @Override
    public String toString() {
        SimpleDateFormat daynameDayMonthYearSdf = new SimpleDateFormat("EEEE d MMMM yyyy");

        return (daynameDayMonthYearSdf.format(date) + "," + name + "," + start + "," + end);
    }    
}

// End of Task.java

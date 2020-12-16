
package com.example.conradto_dolistapplication;

// Created this class. It represents one item in the To-Do list.

public class ToDoEvent {

    // These are the global variables representing each field in one To-Do item.
    private int id;
    private String text;
    private String about;
    private String date;
    private boolean done;

    // This constructor sets all of the global variables equal to what was passed to the constructor.
    public ToDoEvent(int id, String text, String about, String date, boolean done) {
        this.id = id;
        this.text = text;
        this.about = about;
        this.date = date;
        this.done = done;
    }

    // These methods return the value of each respective variable.
    public String getText() { return text; }

    public String getAbout() { return about; }

    public String getDate() { return date; }

    public boolean isDone() { return done; }

    public int getId() { return id; }

}

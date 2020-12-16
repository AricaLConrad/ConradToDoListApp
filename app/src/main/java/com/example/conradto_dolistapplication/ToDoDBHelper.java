package com.example.conradto_dolistapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ToDoDBHelper extends SQLiteOpenHelper {

    // These variables store the names of the database, table, and columns.
    public static final String DATABASE_NAME = "ToDoList";
    public static final int VERSION = 1;
    public static final String TABLE_NAME = "ToDo";
    public static final String ID_COL = "_id";
    public static final String TEXT_COL = "text";
    public static final String ABOUT_COL = "about";
    public static final String DATE_COL = "date";
    public static final String DONE_COL = "done";


    // This is the constructor.
    public ToDoDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    // Added this method.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // This creates the table in the database.
        sqLiteDatabase.execSQL("CREATE TABLE " + ToDoDBHelper.TABLE_NAME + " (" +
                ToDoDBHelper.ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ToDoDBHelper.TEXT_COL + " TEXT," +
                ToDoDBHelper.ABOUT_COL + " TEXT," +
                ToDoDBHelper.DATE_COL + " TEXT," +
                ToDoDBHelper.DONE_COL + " INTEGER" + ")");
    }


    // Added this method, but leaving it blank.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {    }


    // This is the "Create" part of the "CRUD" operation.
    public int insertItem(String text, String about, String date) {
        // This takes in three parameters (the event title, about the event, and the date) and inserts
        // them into the database, along with false for the Done column (an event will start out to be
        // not checked). It will then return the row the data was inserted into.
        ContentValues contentValues = new ContentValues();
        contentValues.put(TEXT_COL, text);
        contentValues.put(ABOUT_COL, about);
        contentValues.put(DATE_COL, date);
        contentValues.put(DONE_COL, false);
        return (int) getWritableDatabase().insert(TABLE_NAME, null, contentValues);
    }


    // This is the "Read" part of the "CRUD" operation.
    public ArrayList<ToDoEvent> getAllItems() {
        // This query will get all of the events in the table.
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, new String[]{ID_COL, TEXT_COL, ABOUT_COL, DATE_COL, DONE_COL},
                null,
                null,
                null, null, null);

        // Create a new ArrayList.
        ArrayList<ToDoEvent> events = new ArrayList<>();

        // Store all of the events in the ArrayList.
        if (cursor.moveToFirst()) {
            do {
                ToDoEvent event = new ToDoEvent(
                    cursor.getInt(cursor.getColumnIndex(ID_COL)),
                    cursor.getString(cursor.getColumnIndex(TEXT_COL)),
                    cursor.getString(cursor.getColumnIndex(ABOUT_COL)),
                    cursor.getString(cursor.getColumnIndex(DATE_COL)),
                    // This will be true if equal to 1 and false if equal to 0 (representing checked
                    // and unchecked, respectively).
                    cursor.getInt(cursor.getColumnIndex(DONE_COL)) == 1
                );
                events.add(event);
            } while (cursor.moveToNext());
        }

        // Closing the cursor is a must.
        cursor.close();
        // Return the ArrayList.
        return events;
    }


    // This is the "Update" part of the "CRUD" operation.
    public void setDone(int id, boolean done) {
        // Create a new ContentValues, putting the value of "Done" in it.
        ContentValues contentValues = new ContentValues();
        contentValues.put(DONE_COL, done);
        // The last parameter is expecting an array of strings, so that is why the ids need to be
        // converted to strings.
        // This will update the database, changing 0 to 1 and vice versa. This represents the checkbox
        // being checked and unchecked.
        getWritableDatabase().update(TABLE_NAME, contentValues, "_id=?", new String[]{Integer.toString(id)});
    }


    // This is the "Delete" part of the "CRUD" operation.
    public boolean deleteItem(int id) {
        // This will delete the specified event from the database and return true if the deletion
        // was successful.
        return getWritableDatabase().delete(TABLE_NAME,
                "_id=?",
                new String[]{Integer.toString(id)}) > 0;
    }
}

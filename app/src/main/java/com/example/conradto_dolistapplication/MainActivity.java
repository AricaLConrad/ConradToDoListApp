package com.example.conradto_dolistapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    // This is a reference to the RecyclerView in my layout.
    private RecyclerView toDoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a new intent for the CreateEventActivity.
        Intent intent = new Intent(this, CreateEventActivity.class);
        // Access the "Add Event" floating action button.
        FloatingActionButton fab = findViewById(R.id.add_event_fab);
        // Add a listener to the button, so when the button is pressed, the user is navigated to
        // the next activity.
        fab.setOnClickListener((view -> startActivity(intent)));
        // Access the RecyclerView in the layout.
        toDoList = findViewById(R.id.to_do_list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reference the ToDoDBHelper.
        ToDoDBHelper toDoDBHelper = new ToDoDBHelper(this, ToDoDBHelper.DATABASE_NAME, null, ToDoDBHelper.VERSION);
        // This time, we want to read from the database.
        SQLiteDatabase reader = toDoDBHelper.getReadableDatabase();
        // This string array stores all of the names of the columns of the table.
        String[] columns = {"_id", ToDoDBHelper.TEXT_COL, ToDoDBHelper.ABOUT_COL, ToDoDBHelper.DATE_COL, ToDoDBHelper.DONE_COL};
        // A cursor is essentially a pointer that points to a container that has all of the rows of
        // the table in it. This is the safer way to query the database instead of writing an SQL statement.
        Cursor cursor = reader.query(ToDoDBHelper.TABLE_NAME, columns, null, null, null, null, null);
        // Now create the ToDoListAdapter, which is the connection between the data in the database
        // and the RecyclerView in the activity_main layout.
        ToDoListAdapter toDoListAdapter = new ToDoListAdapter(getSupportFragmentManager(), toDoDBHelper);

        // Reference the RecyclerView in the layout.
        toDoList = findViewById(R.id.to_do_list);
        // Set the adapter to the RecyclerView in the layout.
        toDoList.setAdapter(toDoListAdapter);
        // Add a LinearLayoutManager for displaying all of the events.
        toDoList.setLayoutManager(new LinearLayoutManager(this));

        // This will log everything that is in the table in the database.
        if (cursor.moveToFirst()) {
            do {
                Log.i("INFO", String.format("%s | %s | %s | %s",
                        cursor.getString(cursor.getColumnIndex(toDoDBHelper.TEXT_COL)),
                        cursor.getString(cursor.getColumnIndex(toDoDBHelper.ABOUT_COL)),
                        cursor.getString(cursor.getColumnIndex(toDoDBHelper.DATE_COL)),
                        cursor.getString(cursor.getColumnIndex(toDoDBHelper.DONE_COL))));
            } while (cursor.moveToNext());
        }
    }
}
package com.example.conradto_dolistapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateEventActivity extends AppCompatActivity {

    // Some global variables for the EditTexts, the date picker, and the string inputs from the user.
    private EditText eventTitleInput;
    private EditText aboutEventInput;
    private DatePickerDialog datePicker;
    private Button createEventButton;
    private TextView chosenDate;
    public String date;
    private String eventTitleString;
    private String aboutEventString;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // This references the "Create Event" button.
        createEventButton = findViewById(R.id.create_event_button);

        // Create the date picker dialog box.
        datePicker = new DatePickerDialog(this);
        // Get a reference to the TextView that will display the selected date to the user.
        chosenDate = findViewById(R.id.date_chosen);
        // Add a listener to the date picker, making sure to increase the month by 1. This will call
        // the showDate method, which will make a string based on the input and then add it to the
        // TextView in the layout.
        datePicker.setOnDateSetListener((view, year, month, dayOfMonth) ->
                showDate(year, (month + 1), dayOfMonth));


        // Access the database by creating a new ToDoDBHelper class.
        ToDoDBHelper toDoDBHelper = new ToDoDBHelper(this, ToDoDBHelper.DATABASE_NAME, null, ToDoDBHelper.VERSION);
        // Reference the SQLite database so that you can "write" or modify the database, so we can
        // add values to the database.
        SQLiteDatabase sqLiteDatabase = toDoDBHelper.getWritableDatabase();

        // Reference the EditText fields in the layout.
        eventTitleInput = findViewById(R.id.event_text_input);
        aboutEventInput = findViewById(R.id.about_event_text_input);

        // Create a listener for the "Create Event" button in the layout.
        createEventButton.setOnClickListener((view) -> {
            // Access what was typed into the text fields and save it as a string.
            eventTitleString = eventTitleInput.getText().toString();
            aboutEventString = aboutEventInput.getText().toString();

            // Check the values to make sure they are not empty. If they are, a Toast message will
            // be displayed, and the user will not be able to press the button.
            if (TextUtils.isEmpty(eventTitleString) || TextUtils.isEmpty(aboutEventString) || TextUtils.isEmpty(date)) {
                Log.i("INFO", "Empty values.");
                Toast.makeText(this, "Event unable to be created - All fields must be filled in for event to be created.", Toast.LENGTH_LONG).show();
                return;
            }

            // Otherwise, insert the values into the database.
            int result = toDoDBHelper.insertItem(eventTitleString, aboutEventString, date);
            // For debugging purposes: This returns the row of the data if the insertion was successful.
            // Otherwise, it will return -1.
            if (result == -1)
            {
                Log.i("INFO", "Insertion failed. Row = " + result);
            }
            else {
                Log.i("INFO", "Insertion succeeded. Row = " + result);
            }

            // Clear out the text and date fields for the user to input a new event and bring up a
            // Toast message saying the event was successfully added.
            eventTitleInput.setText("");
            aboutEventInput.setText("");
            chosenDate.setText("");
            Toast.makeText(this, "Event successfully created.", Toast.LENGTH_LONG).show();

            // Close the database to be safe.
            sqLiteDatabase.close();
        });
    }

    // This method will show the date the user chose in a TextView. The date is also saved as string
    // to be inserted into the database.
    public void showDate(int year, int month, int day) {
        chosenDate.setText(new StringBuilder().append(month).append("/").append(day).append("/").append(year));
        date = chosenDate.getText().toString();
    }

    // When the "Pick a Date" button is clicked on, this method will be called, which will show
    // the DatePicker dialog box.
    public void pickDate(View view) {
        datePicker.show();
    }
}
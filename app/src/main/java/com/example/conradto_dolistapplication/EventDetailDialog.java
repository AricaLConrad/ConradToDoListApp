package com.example.conradto_dolistapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

public class EventDetailDialog extends DialogFragment {

    // Some more global variables, representing the TextViews in the dialog box's layout, as well
    // as the strings that will fill those TextViews.
    private TextView dialogEventTitle;
    private TextView dialogDate;
    private TextView dialogAboutEvent;

    private String displayEventTitle;
    private String displayDate;
    private String displayAboutEvent;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // This creates the dialog box.
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("About This Event");
        View dialogView = inflater.inflate(R.layout.event_detail_dialog, null);
        builder.setView(dialogView);

        // Access all of the dialog box's TextViews.
        this.dialogEventTitle = dialogView.findViewById(R.id.dialog_event_title);
        this.dialogDate = dialogView.findViewById(R.id.dialog_date);
        this.dialogAboutEvent = dialogView.findViewById(R.id.dialog_about_event);

        // Retrieve and save the arguments that were passed in the bundle to the dialog box.
        displayEventTitle = getArguments().getString("event_title_text");
        displayDate = getArguments().getString("date_text");
        displayAboutEvent = getArguments().getString("about_text");

        // Set all of the TextViews' text to the bundle's arguments.
        dialogEventTitle.setText(displayEventTitle);
        dialogDate.setText(displayDate);
        dialogAboutEvent.setText(displayAboutEvent);

        // Create the positive button.
        builder.setPositiveButton("Close", (dialog, which) -> {
            Log.i("INFO", "Positive button pressed!");
        });

        // Then return.
        return builder.create();
    }
}

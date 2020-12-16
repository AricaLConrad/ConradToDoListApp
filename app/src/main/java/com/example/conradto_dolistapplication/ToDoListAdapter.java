package com.example.conradto_dolistapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder> {

    // Some necessary global variables for this adapter to work.
    public ArrayList<ToDoEvent> toDoEvents;
    public EventDetailDialog eventDetailDialog;
    private FragmentManager fragmentManager;
    private ToDoDBHelper toDoDBHelper;

    // Added this method. This is the constructor for the adapter.
    public ToDoListAdapter(FragmentManager fragmentManager,
                           ToDoDBHelper toDoDBHelper) {
        this.toDoEvents = toDoDBHelper.getAllItems();
        this.fragmentManager = fragmentManager;
        this.toDoDBHelper = toDoDBHelper;
    }

    // This inflates the layout and returns a new ViewHolder.
    @NonNull
    @Override
    public ToDoListAdapter.ToDoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View eventView = inflater.inflate(R.layout.event_item, null);
        return new ToDoListViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoListAdapter.ToDoListViewHolder holder, int position) {
        // These access the setter and getter methods in the ToDoEvent.java file (the file that defines
        // what a single to-do event is).
        ToDoEvent event = toDoEvents.get(position);
        holder.doneCheckBox.setChecked(event.isDone());
        holder.eventTextView.setText(event.getText());
        holder.dateTextView.setText(event.getDate());

        // This is where the detail dialog will go. The detail dialog will show up when a to-do list
        // event is pressed and held on.
        eventDetailDialog = new EventDetailDialog();
        holder.itemView.setOnLongClickListener(v -> {
            // Create a new bundle and store the string values in each column at that specific position.
            Bundle bundle = new Bundle();
            bundle.putString("event_title_text", toDoEvents.get(position).getText());
            bundle.putString("date_text", toDoEvents.get(position).getDate());
            bundle.putString("about_text", toDoEvents.get(position).getAbout());

            // This shows the EventDetailDialog, passing in the bundle.
            eventDetailDialog.setArguments(bundle);
            eventDetailDialog.show(fragmentManager, "event_details");
            return position == 1;
        });

        // This sets a listener on the delete button.
        holder.deleteButton.setOnClickListener(v -> {
            // Access the item that is being deleted.
            ToDoEvent itemToDelete = toDoEvents.get(holder.getAdapterPosition());
            // This removes the item from the database, from the adapter, and notifies that something
            // was changed, as long as the database returns true for a successful deletion.
            // A Toast message is also shown, saying that the event was successfully deleted.
            if (toDoDBHelper.deleteItem(itemToDelete.getId())) {
                toDoEvents.remove(holder.getAdapterPosition());
                notifyItemRemoved(position);
                Toast.makeText(v.getContext(), "Event successfully deleted.", Toast.LENGTH_LONG).show();
            }
        });

        // This sets a listener on the checkbox.
        holder.doneCheckBox.setOnCheckedChangeListener((v, checked) -> {
            // If the checkbox is checked, pass true to the database, where the database will update
            // the column to say true (1).
            if (checked == true) {
                toDoDBHelper.setDone(toDoEvents.get(holder.getAdapterPosition()).getId(), true);
            }
            // Otherwise, false (0) will be passed to the database.
            else {
                toDoDBHelper.setDone(toDoEvents.get(holder.getAdapterPosition()).getId(), false);
            }
        });
    }

    // This returns the number of events in the adapter's Arraylist.
    @Override
    public int getItemCount() {
        return toDoEvents.size();
    }

    // The ViewHolder gets a reference to all of the views within the event_item layout.
    public class ToDoListViewHolder extends RecyclerView.ViewHolder {

        // Some more global variables representing all of the views within the layout.
        TextView eventTextView;
        TextView dateTextView;
        CheckBox doneCheckBox;
        Button deleteButton;

        // These reference all of the views within the layout for a single event item.
        public ToDoListViewHolder(@NonNull View eventView) {
            super(eventView);
            eventTextView = eventView.findViewById(R.id.event_title_text);
            dateTextView = eventView.findViewById(R.id.date_text);
            doneCheckBox = eventView.findViewById(R.id.done_checkbox);
            deleteButton = eventView.findViewById(R.id.delete_button);
        }
    }
}

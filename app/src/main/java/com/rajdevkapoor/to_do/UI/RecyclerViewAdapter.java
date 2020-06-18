package com.rajdevkapoor.to_do.UI;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.rajdevkapoor.to_do.Activities.DetailsActivity;
import com.rajdevkapoor.to_do.Data.DatabaseHandler;
import com.rajdevkapoor.to_do.Model.Tasks;
import com.rajdevkapoor.to_do.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Tasks> tasksItems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private CheckBox cb;

    public RecyclerViewAdapter(Context context, List<Tasks> tasksItems) {
        this.context = context;
        this.tasksItems = tasksItems;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {


        if (tasksItems != null) {
            Tasks tasks = tasksItems.get(position);
            //set the values here
            holder.tasksItemName.setText(tasks.getTitle());
            holder.taskDescription.setText(tasks.getDescription());
            holder.dateAdded.setText(tasks.getDateItemAdded());
            updateUi(tasks , holder);
            //continue from here...

        } else {
            // Covers the case of data not being ready yet.

        }


    }

    private void updateUi(Tasks tasks, ViewHolder holder) {
        String s=""+tasks.getIsChecked();
        if(s.equals("true")){
            Log.d("dsc","Hello");
            holder.checkBox.setChecked(true);

            holder.tasksItemName.setPaintFlags(holder.tasksItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.taskDescription.setPaintFlags(holder.taskDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            Log.d("dsc","Hell2o");
            holder.checkBox.setChecked(false);
        }
    }


    @Override
    public int getItemCount() {
        return tasksItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tasksItemName;
        public TextView taskDescription;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public CheckBox checkBox;
        public int id;


        public ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;

            tasksItemName = (TextView) view.findViewById(R.id.title);
            taskDescription = (TextView) view.findViewById(R.id.description);
            dateAdded = (TextView) view.findViewById(R.id.dateAdded);
            checkBox=(CheckBox) view.findViewById(R.id.checkbox);
            editButton = (Button) view.findViewById(R.id.editButton);
            deleteButton = (Button) view.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to next screen/ DetailsActivity


                }
            });
            final TableRow tableRow = (TableRow) view.findViewById(R.id.tablerow);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked==true){
                        int position = getAdapterPosition();
                        Tasks tasks = tasksItems.get(position);
                        DatabaseHandler db = new DatabaseHandler(context);
                        tasks.setTitle(tasks.getTitle());
                        tasks.setDescription(tasks.getDescription());
                        tasks.setIsChecked("true");
                        db.updateTasks(tasks);
                        notifyItemChanged(getAdapterPosition(),tasks);

                        tableRow.setVisibility(View.VISIBLE);

                    }
                    else {
                        int position = getAdapterPosition();
                        Tasks tasks = tasksItems.get(position);
                        DatabaseHandler db = new DatabaseHandler(context);
                        tasks.setTitle(tasks.getTitle());
                        tasks.setDescription(tasks.getDescription());
                        tasks.setIsChecked("false");
                        db.updateTasks(tasks);
                        notifyItemChanged(getAdapterPosition(),tasks);

                        tasksItemName.setPaintFlags(tasksItemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        taskDescription.setPaintFlags(taskDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        tableRow.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.editButton:
                    int position = getAdapterPosition();
                    Tasks tasks = tasksItems.get(position);
                    editItem(tasks);

                    break;
                case R.id.deleteButton:
                    position = getAdapterPosition();
                    tasks = tasksItems.get(position);
                    deleteItem(tasks.getId());

                    break;


            }
        }



        public void deleteItem(final int id) {

            //create an AlertDialog
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

            Button noButton = (Button) view.findViewById(R.id.noButton);
            Button yesButton = (Button) view.findViewById(R.id.yesButton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();


            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //delete the item.
                    DatabaseHandler db = new DatabaseHandler(context);
                    //delete item
                    db.deleteTasks(id);
                    tasksItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();


                }
            });

        }


        public void editItem(final Tasks tasks) {

            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup, null);

            final EditText tasksItem = (EditText) view.findViewById(R.id.taskTitle);
            final EditText taskDescription = (EditText) view.findViewById(R.id.taskDescription);
            final TextView title = (TextView) view.findViewById(R.id.tile);

            title.setText("Edit Tasks");
            Button saveButton = (Button) view.findViewById(R.id.saveButton);


            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseHandler db = new DatabaseHandler(context);

                    //Update item
                    tasks.setTitle(tasksItem.getText().toString());
                    tasks.setDescription(taskDescription.getText().toString());

                    if (!tasksItem.getText().toString().isEmpty()
                            && !taskDescription.getText().toString().isEmpty()) {
                        db.updateTasks(tasks);
                        notifyItemChanged(getAdapterPosition(),tasks);
                    }else {

                    }

                    dialog.dismiss();

                }
            });

        }
    }

}
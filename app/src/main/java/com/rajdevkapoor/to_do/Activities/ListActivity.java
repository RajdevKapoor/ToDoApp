package com.rajdevkapoor.to_do.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.rajdevkapoor.to_do.Data.DatabaseHandler;
import com.rajdevkapoor.to_do.Model.Tasks;
import com.rajdevkapoor.to_do.R;
import com.rajdevkapoor.to_do.UI.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Tasks> tasksList;
    private List<Tasks> listItems;

    private DatabaseHandler db;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private EditText tasksItem;
    private EditText quantity;
    private Button saveButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        db = new DatabaseHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tasksList = new ArrayList<>();
        listItems = new ArrayList<>();

        // Get items from database
        tasksList = db.getAllTasks();

        for (Tasks c : tasksList) {
            Tasks tasks = new Tasks();
            tasks.setTitle(c.getTitle());
            tasks.setDescription("" + c.getDescription());
            tasks.setId(c.getId());
            tasks.setDateItemAdded("Added on: " + c.getDateItemAdded());


            listItems.add(tasks);

        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void createPopupDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        tasksItem = (EditText) view.findViewById(R.id.taskTitle);
        quantity = (EditText) view.findViewById(R.id.taskDescription);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Todo: Save to db
                //Todo: Go to next screen

                if (!tasksItem.getText().toString().isEmpty()
                        && !quantity.getText().toString().isEmpty()) {
                    saveTasksToDB(v);
                }

            }
        });
    }

    private void createPopDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        tasksItem = (EditText) view.findViewById(R.id.taskTitle);
        quantity = (EditText) view.findViewById(R.id.taskDescription);
        saveButton = (Button) view.findViewById(R.id.saveButton);


        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTasksToDB(v);
            }
        });



    }

    private void saveTasksToDB(View v) {

        Tasks tasks = new Tasks();

        String newTasks = tasksItem.getText().toString();
        String newTasksQuantity = quantity.getText().toString();

        tasks.setTitle(newTasks);
        tasks.setDescription(newTasksQuantity);
        tasks.setIsChecked("false");
        //Save to DB
        db.addTask(tasks);

        Snackbar.make(v, "Task Saved!", Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start a new activity
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();
            }
        }, 1200); //  1 second.



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_btn :
                createPopupDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}

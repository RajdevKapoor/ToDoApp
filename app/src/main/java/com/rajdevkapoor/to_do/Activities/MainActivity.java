package com.rajdevkapoor.to_do.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.rajdevkapoor.to_do.Data.DatabaseHandler;
import com.rajdevkapoor.to_do.Model.Tasks;
import com.rajdevkapoor.to_do.R;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText taskTitle;
    private EditText taskDescription;
    private Button saveButton;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=new DatabaseHandler(this);
        byPassActivity();

    }
    private void createPopupDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        taskTitle = (EditText) view.findViewById(R.id.taskTitle);
        taskDescription = (EditText) view.findViewById(R.id.taskDescription);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();



        saveButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                //Todo: Save to db
                //Todo: Go to next screen

                if (!taskTitle.getText().toString().isEmpty()
                        && !taskDescription.getText().toString().isEmpty()) {
                    saveToDB(v);
                }

            }
        });
    }

    private void saveToDB(View v) {
        Tasks tasks = new Tasks();

        String newTasks = taskTitle.getText().toString();
        String newTasksQuantity = taskDescription.getText().toString();

        tasks.setTitle(newTasks);
        tasks.setDescription(newTasksQuantity);
        tasks.setIsChecked("false");

        //Save to DB
        db.addTask(tasks);;

        Snackbar.make(v, "Task Saved!", Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start a new activity
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        }, 1200); //  1 second.

    }
    public void byPassActivity() {
        //Checks if database is empty; if not, then we just
        //go to ListActivity and show all added items

        if (db.getTaskssCount() > 0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }

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

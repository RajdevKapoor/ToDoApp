package com.rajdevkapoor.to_do.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rajdevkapoor.to_do.Model.Tasks;
import com.rajdevkapoor.to_do.Util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context ctx;
    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASK_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_TASK_ITEM + " TEXT,"
                + Constants.KEY_TASK_DESCRIPTION + " TEXT,"
                + Constants.KEY_CHECK + " INTEGER DEFAULT 0,"
                + Constants.KEY_DATE_NAME + " LONG);";

        db.execSQL(CREATE_TASK_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(db);

    }

    public void addTask(Tasks tasks) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_TASK_ITEM, tasks.getTitle());
        values.put(Constants.KEY_TASK_DESCRIPTION, tasks.getDescription());
        values.put(Constants.KEY_CHECK, tasks.getIsChecked());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());

        //Insert the row
        db.insert(Constants.TABLE_NAME, null, values);

        Log.d("Saved!!", "Saved to DB");
        Log.d("val","Hi"+tasks.getIsChecked());

    }


    //Get a Tasks
    public Tasks getTasks(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {Constants.KEY_ID,
                        Constants.KEY_TASK_ITEM, Constants.KEY_TASK_DESCRIPTION, Constants.KEY_CHECK,Constants.KEY_DATE_NAME},
                Constants.KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();


            Tasks tasks = new Tasks();
            tasks.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            tasks.setTitle(cursor.getString(cursor.getColumnIndex(Constants.KEY_TASK_ITEM)));
            tasks.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_TASK_DESCRIPTION)));
            tasks.setIsChecked(cursor.getString(cursor.getColumnIndex(Constants.KEY_CHECK)));
            //Log.d("Task",tasks.getIsChecked());
            //convert timestamp to something readable
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
                    .getTime());

            tasks.setDateItemAdded(formatedDate);



        return tasks;
    }


    public List<Tasks> getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Tasks> tasksList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {
                Constants.KEY_ID, Constants.KEY_TASK_ITEM, Constants.KEY_TASK_DESCRIPTION,Constants.KEY_CHECK,
                Constants.KEY_DATE_NAME}, null, null, null, null, Constants.KEY_DATE_NAME + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Tasks tasks = new Tasks();
                tasks.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                tasks.setTitle(cursor.getString(cursor.getColumnIndex(Constants.KEY_TASK_ITEM)));
                tasks.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_TASK_DESCRIPTION)));
                tasks.setIsChecked(cursor.getString(cursor.getColumnIndex(Constants.KEY_CHECK)));

                //convert timestamp to something readable
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
                        .getTime());

                tasks.setDateItemAdded(formatedDate);

                // Add to the tasksList
                tasksList.add(tasks);

            }while (cursor.moveToNext());
        }

        return tasksList;
    }


    //Updated Tasks
    public int updateTasks(Tasks tasks) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_TASK_ITEM, tasks.getTitle());
        values.put(Constants.KEY_TASK_DESCRIPTION, tasks.getDescription());
        values.put(Constants.KEY_CHECK,tasks.getIsChecked());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());//get system time


        //update row
        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?", new String[] { String.valueOf(tasks.getId())} );
    }


    //Delete Tasks
    public void deleteTasks(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ?",
                new String[] {String.valueOf(id)});

        db.close();

    }


    //Get count
    public int getTaskssCount() {
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }
}

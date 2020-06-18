package com.rajdevkapoor.to_do.Model;

public class Tasks {
    private String title;
    private String description;
    private String dateItemAdded;

    private String isChecked;
    private int id;



    public Tasks() {
    }

    public Tasks(String title, String description, String dateItemAdded, String isChecked, int id) {
        this.title = title;
        this.description = description;
        this.dateItemAdded = dateItemAdded;
        this.isChecked = isChecked;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

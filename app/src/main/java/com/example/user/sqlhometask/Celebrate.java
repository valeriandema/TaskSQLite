package com.example.user.sqlhometask;

public class Celebrate {
    private int _id;
    private String date;
    private String description;

    public Celebrate (){
    }

    public Celebrate (int _id, String date, String description){
        this._id = _id;
        this.date = date;
        this.description = description;
    }

    public Celebrate (String date, String description){
        this.date = date;
        this.description = description;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


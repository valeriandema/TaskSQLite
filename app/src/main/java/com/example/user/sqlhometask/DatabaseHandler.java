package com.example.user.sqlhometask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TABLE_DAYS = "celebrates";
    private static final String DATABASE_NAME = "ImportantDays";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ID = "_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_DESCRIPTION = "description";
    private List<String> arrCelebrates = new ArrayList<String>();

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_DAYS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " DATE,"
                + KEY_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAYS);
        onCreate(db);
    }

    public void addCeleb(Celebrate celeb) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, celeb.getDate());
        values.put(KEY_DESCRIPTION, celeb.getDescription());
        db.insert(TABLE_DAYS, null, values);
        db.close();
    }

    public List<String> getAllCelebrateList () {
        arrCelebrates.clear();
        String[] dates = null;
        String selectQuery = "select date from " + TABLE_DAYS + " group by date;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        dates = new String[cursor.getCount()];
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                dates[i] = "'" + cursor.getString(0) + "'";
                Log.e("TAGGG", cursor.getString(0));
                i++;
            } while (cursor.moveToNext());
        }

        Cursor[] cursor1 = new Cursor[dates.length];
        for (int y = 0; y < dates.length; y++) {
            cursor1[y] = db.rawQuery("select * from " + TABLE_DAYS + " where date = " + dates[y] + ";", null);
            contain(cursor1[y]);
        }
        db.close();
        return arrCelebrates;
    }

    public boolean deleteTitle(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean bol = db.delete(TABLE_DAYS, KEY_ID + "=" + id, null) > 0;
        db.close();
        return bol;
    }

    private void contain(Cursor cursor1) {
        String combine = null;
        String id = null;
        String date = null;
        String description = "";

        if (cursor1.moveToFirst()) {
            do {
                id = cursor1.getString(0);
                date = cursor1.getString(1);
                description += cursor1.getString(2) + "/";
            } while (cursor1.moveToNext());
        }
        combine = id + " " + date + " " + description;
        arrCelebrates.add(combine);
    }

    public void updateRow (int id, String date, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_DAYS +" SET date='" + date + "', description='" +description + "' WHERE _id='" + id + "';");
        db.close();
    }

    public void dropTable () {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_DAYS, null, null);
        db.close();
    }
}

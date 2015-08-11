package com.example.user.sqlhometask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends Activity {
    public final static String EXTRA_MESSAGE = "com.example.user.sqlhometask.MESSAGE";
    private int DIALOG_DATE = 1;
    private int myYear;
    private int myMonth;
    private int myDay;
    private DatabaseHandler db;
    private String[] info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAddCel = null;
        ListView listView = null;

        listView = (ListView) findViewById(R.id.listView);
        btnAddCel = (Button) findViewById(R.id.btnAddCel);

        connectToSQLite();

        getInfo();

        getDate();


        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, info);
        listView.setAdapter(adapter);

        btnAddCel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });


    }

    private void getInfo() {
        List<String> celebrateList = db.getAllCelebrateList();
        Celebrate[] celebrateArr = new Celebrate[celebrateList.size()];
        info = new String[celebrateList.size()];
        int i = 0;
        for (String c : celebrateList) {
            info[i] = c;
            i++;
        }
        /*for (int a = 0; a < info.length; a++) {
            Log.e("TAG2", info[a]);
        }*/
    }

    private void connectToSQLite() {
        db = new DatabaseHandler(this);
        db.dropTable();
        db.addCeleb(new Celebrate("2015-10-03", "Good day"));
        db.addCeleb(new Celebrate("2015-08-13", "Good day"));
        db.addCeleb(new Celebrate("2015-08-14", "Good day8"));
        db.addCeleb(new Celebrate("2015-08-04", "Good day2"));
        db.addCeleb(new Celebrate("2015-07-04", "Good day9"));
        db.addCeleb(new Celebrate("2015-08-05", "Good day3"));
        db.addCeleb(new Celebrate("2015-08-05", "Good day4"));
        db.addCeleb(new Celebrate("2015-08-06", "Good day5"));
        db.addCeleb(new Celebrate("2015-08-06", "Good day6"));
        db.addCeleb(new Celebrate("2015-09-01", "Good day7"));
    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_YEAR, 1);
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, myYear, --myMonth, myDay);
            tpd.getDatePicker().setMinDate(c.getTimeInMillis());
            c.set(Calendar.DAY_OF_YEAR, 366);
            tpd.getDatePicker().setMaxDate(c.getTimeInMillis());
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myYear = year;
            myMonth = monthOfYear;
            myDay = dayOfMonth;
            String message = year + "-" + monthOfYear + "-" + dayOfMonth;
            Intent intent = new Intent(MainActivity.this, AddCelebActivity.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    };

    private void getDate () {
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        myDay = localCalendar.get(Calendar.DATE);
        myMonth = localCalendar.get(Calendar.MONTH) + 1;
        myYear = localCalendar.get(Calendar.YEAR);
    }
}

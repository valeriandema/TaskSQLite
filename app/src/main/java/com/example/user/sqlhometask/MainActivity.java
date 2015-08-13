package com.example.user.sqlhometask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private DatabaseHandler db = null;
    private String[] info;
    private String message;
    private ListView listView = null;
    private MySimpleArrayAdapter adapter = null;
    private final int EDIT = 1;
    private final int DELETE = 2;
    private TextView getTxtView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAddCel = null;
        listView = (ListView) findViewById(R.id.listView);
        btnAddCel = (Button) findViewById(R.id.btnAddCel);

        if (db == null) connectToSQLite();

        getInfo();

        getDate();

        doingListViewAdapter();

        btnAddCel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });


    }

    private void doingListViewAdapter () {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        adapter = new MySimpleArrayAdapter(this, info);
        listView.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        Log.e("GET ACTION", v.toString());
        getTxtView = (TextView) v;
        menu.add(0, EDIT, 0, "EDIT");
        menu.add(0, DELETE, 0, "DELETE");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String[] arrContDesc = getTxtView.getContentDescription().toString().split("=");
        int contentDescription = Integer.valueOf(arrContDesc[0]);

        switch (item.getItemId()) {
            case EDIT:
                Intent intent = new Intent(MainActivity.this, EditCelebActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("ID", contentDescription);
                extras.putString("DATE", arrContDesc[1]);
                extras.putString("DESCRIPTION", getTxtView.getText().toString());
                intent.putExtras(extras);
                startActivityForResult(intent, 2);
                break;
            case DELETE:
                if (db.deleteTitle(contentDescription)) {
                    getInfo();
                    doingListViewAdapter();
                    Toast.makeText(this, "You deleted " +
                            getTxtView.getText().toString() + " notation", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(this, "Your notation wasn't deleted, try again", Toast.LENGTH_SHORT).show();

                break;
        }
        return super.onContextItemSelected(item);
    }

    private void getInfo() {
        info = null;
        List<String> celebrateList = null;
        celebrateList = db.getAllCelebrateList();
        info = new String[celebrateList.size()];
        int i = 0;
        for (String c : celebrateList) {
            info[i] = c;
            i++;
        }
    }

    private void connectToSQLite() {
        db = new DatabaseHandler(this);
        db.dropTable();
        db.addCeleb(new Celebrate("2015-07-04", "Good day9"));
        db.addCeleb(new Celebrate("2015-10-03", "Good day Lorem ipsum dolor sit amet, consectetur adipisicing elit"));
        db.addCeleb(new Celebrate("2015-08-13", "Good day"));
        db.addCeleb(new Celebrate("2015-08-14", "Good day8"));
        db.addCeleb(new Celebrate("2015-08-04", "Good day2"));
        db.addCeleb(new Celebrate("2015-08-05", "Good day3 Lorem ipsum dolor sit amet, consectetur adipisicing elit"));
        db.addCeleb(new Celebrate("2015-08-06", "Good day5"));
        db.addCeleb(new Celebrate("2015-08-05", "Good day4 Lorem ipsum dolor sit amet, consectetur adipisicing elit"));
        db.addCeleb(new Celebrate("2015-08-06", "Good day6"));
        db.addCeleb(new Celebrate("2015-09-01", "Good day7"));
    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_YEAR, 1);
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, myYear, myMonth, myDay);
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
            String month = null, day = null;
            myYear = year;
            myMonth = monthOfYear++;
            myDay = dayOfMonth;

            if (String.valueOf(monthOfYear).length() == 1) {
                month = "0" + String.valueOf(monthOfYear);
            } else {
                month = String.valueOf(monthOfYear);
            }
            if (String.valueOf(dayOfMonth).length() == 1) {
                day = "0" + String.valueOf(dayOfMonth);
            } else {
                day = String.valueOf(dayOfMonth);
            }

            message = year + "-" + month + "-" + day;

            Intent intent = new Intent(MainActivity.this, AddCelebActivity.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivityForResult(intent, 1);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        if (requestCode == 1) {
            String answer = data.getStringExtra(EXTRA_MESSAGE);
            Toast.makeText(this, answer, Toast.LENGTH_SHORT).show();
            db.addCeleb(new Celebrate(message, answer));
            getInfo();
            doingListViewAdapter();
        } else if (requestCode == 2) {
            Bundle extras = data.getExtras();
            int id = extras.getInt(EditCelebActivity.ID_BACK);
            String date = extras.getString(EditCelebActivity.DATE_BACK);
            String description = extras.getString(EditCelebActivity.DESCRIPTION_BACK);
            db.updateRow(id, date, description);
            getInfo();
            doingListViewAdapter();
            Toast.makeText(this, id + " " + date + " " + description, Toast.LENGTH_SHORT).show();
        }
    }

    private void getDate () {
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        myDay = localCalendar.get(Calendar.DATE);
        myMonth = localCalendar.get(Calendar.MONTH);
        myYear = localCalendar.get(Calendar.YEAR);
    }
}

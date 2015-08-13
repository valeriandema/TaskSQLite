package com.example.user.sqlhometask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class EditCelebActivity extends Activity {
    public static final String DESCRIPTION_BACK = "DESC_BACK";
    public static final String ID_BACK = "ID_BACK";
    public static final String DATE_BACK = "DATE_BACK";
    private int DIALOG_DATE = 1;
    private final int RESULT_BACK = 2;
    private String[] splitedDate = null;
    private  TextView txtCurrentDate;
    private int id;
    private EditText editDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_celeb);

        txtCurrentDate = (TextView) findViewById(R.id.txtCurrentDate);
        editDescription = (EditText) findViewById(R.id.editDescription);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        Button btnApply = (Button) findViewById(R.id.btnApply);
        Button btnChangeDate = (Button) findViewById(R.id.btnChangeDate);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        id = extras.getInt("ID");
        final String date = extras.getString("DATE");
        final String description = extras.getString("DESCRIPTION");
        splitedDate = date.split("-");

        Log.e("ID", String.valueOf(id));
        Log.e("DATE", date);
        Log.e("DESCRIPTION", description);

        txtCurrentDate.setText(date);
        editDescription.setText(description);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = editDescription.getText().toString();
                if (date.equals(txtCurrentDate.getText().toString())
                        && description.equals(desc)) {
                    finish();
                } else if (desc.length() > 3) {
                    sendDataToRootIntent();
                }
            }
        });

        btnChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendDataToRootIntent() {
        Intent intentBack = new Intent();
        Bundle extras = new Bundle();
        extras.putInt(ID_BACK, id);
        extras.putString(DATE_BACK, txtCurrentDate.getText().toString());
        extras.putString(DESCRIPTION_BACK, editDescription.getText().toString());
        intentBack.putExtras(extras);
        setResult(RESULT_BACK, intentBack);
        finish();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_YEAR, 1);
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack,
                    Integer.valueOf(splitedDate[0]),
                    Integer.valueOf(splitedDate[1])-1,
                    Integer.valueOf(splitedDate[2]));
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
            String month = String.valueOf(++monthOfYear);
            String day = String.valueOf(dayOfMonth);
            if (month.length() == 1) month = "0" + month;
            if (day.length() == 1) day = "0" + day;
            txtCurrentDate.setText(year + "-" + month + "-" + day);
        }
    };
}

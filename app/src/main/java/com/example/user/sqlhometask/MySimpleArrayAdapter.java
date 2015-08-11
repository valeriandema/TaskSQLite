package com.example.user.sqlhometask;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;

    public MySimpleArrayAdapter(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowAdapter = inflater.inflate(R.layout.row_adapter, parent, false);

        String getDate = values[position].substring(0, values[position].indexOf(' '));
        String getCelebs = values[position].substring(values[position].indexOf(' ') + 1);

        String[] splitMonth = getDate.split("-");
        String month = getMonthForInt(Integer.valueOf(splitMonth[1]));
        String[] splitCelebs = getCelebs.split("/");

        TextView[] txtViews = new TextView[splitCelebs.length];
        LinearLayout linearLayout = (LinearLayout) rowAdapter.findViewById(R.id.linearLayout);

        TextView txtDate = new TextView(rowAdapter.getContext());
        txtDate.setText(month + " " + splitMonth[2]);
        txtDate.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT, 1f));
        txtDate.setPadding(0, 20, 0, 20);
        txtDate.setTextColor(Color.parseColor("#FF0000"));
        linearLayout.addView(txtDate);

        for (int i = 0; i < txtViews.length; i++) {
            txtViews[i] = new TextView(context);
            txtViews[i].setText(splitCelebs[i]);
            txtViews[i].setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            txtViews[i].setPadding(0, 0, 0, 30);
            linearLayout.addView(txtViews[i]);
        }
        return rowAdapter;
    }

    String getMonthForInt(int num) {
        num = num - 1;
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();

        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
}

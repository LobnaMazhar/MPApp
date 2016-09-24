package com.materialplanning.vodafone.mpapp;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lobna on 03-Sep-16.
 */
public class monthlyPhasingAdapter extends ArrayAdapter<monthlyPhasing> {
    Context context;
    ArrayList<monthlyPhasing> monthlyPhasingList;
    ArrayList<monthlyPhasing> updatedMonthlyPhasingList;
    String monthName;

    public monthlyPhasingAdapter(Context context, ArrayList<monthlyPhasing> monthlyPhasingList) {
        super(context, R.layout.monthlyphasing_row, monthlyPhasingList);
        this.context = context;
        this.monthlyPhasingList = monthlyPhasingList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        //LayoutInflater inflater = LayoutInflater.from(getContext());

        View monthlyPhasingListView = inflater.inflate(R.layout.monthlyphasing_row, parent, false);

        final monthlyPhasing monthlyPhasingObject = monthlyPhasingList.get(position);

        // 1 text view feh l etnen 3shan l onClick tsht3'l
        TextView monthNameAndMonthPhaseTextView = (TextView) monthlyPhasingListView.findViewById(R.id.monthNameAndMonthPhaseTextView);
        getMonthName(monthlyPhasingObject.getMonth());
        monthNameAndMonthPhaseTextView.setText(monthName + "\t\t\t\t\t\t\t\t\t\t" + Integer.toString(monthlyPhasingObject.getPhasing()));

        return monthlyPhasingListView;
    }

    public void getMonthName(int monthID) {
        //3mltha b switch 3shan kant btl5bt fl scroll 3shan byb3t data kter w by-recieve 3'lt :/
        switch (monthID){
            case 1:
                monthName = "April";
                break;
            case 2:
                monthName = "May";
                break;
            case 3:
                monthName = "June";
                break;
            case 4:
                monthName = "July";
                break;
            case 5:
                monthName = "August";
                break;
            case 6:
                monthName = "September";
                break;
            case 7:
                monthName = "October";
                break;
            case 8:
                monthName = "November";
                break;
            case 9:
                monthName = "December";
                break;
            case 10:
                monthName = "January";
                break;
            case 11:
                monthName = "February";
                break;
            case 12:
                monthName = "March";
                break;
        }

        /*HashMap<String, String> params = new HashMap<String, String>();
        params.put("monthID", Integer.toString(monthID));

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    JSONObject reader = new JSONObject(result);
                    monthName = reader.getString("monthName");
                } catch (JSONException e) {
                }
            }
        });
        conn.execute(conn.URL + "/getMonthName");*/
    }
}

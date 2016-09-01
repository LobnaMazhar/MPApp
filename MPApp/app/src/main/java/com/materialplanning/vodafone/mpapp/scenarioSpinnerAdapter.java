package com.materialplanning.vodafone.mpapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lobna on 29-Aug-16.
 */
public class scenarioSpinnerAdapter extends ArrayAdapter<scenario> {
    Context context;
    ArrayList<scenario> scenarios;

    public scenarioSpinnerAdapter(Context context, ArrayList<scenario> scenarios) {
        super(context, R.layout.scenariospinner_row, scenarios);
        this.context = context;
        this.scenarios = scenarios;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View scenariosList = inflater.inflate(R.layout.scenariospinner_row, parent, false);

        scenario scenarioObject = scenarios.get(position);
        TextView scenarioNumberSpinnerTextView = (TextView) scenariosList.findViewById(R.id.scenarioNumberSpinnerTextView);
        scenarioNumberSpinnerTextView.setText(Integer.toString(scenarioObject.getScenarioNumber()));

        return scenariosList;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.scenariospinner_row, null);
        }

        TextView scenarioNumberSpinnerTextView = (TextView) convertView.findViewById(R.id.scenarioNumberSpinnerTextView);
        scenarioNumberSpinnerTextView.setText(Integer.toString(scenarios.get(position).getScenarioNumber()));

        return convertView;
    }
}

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
 * Created by Lobna on 16-Aug-16.
 */
public class scenarioAdapter extends ArrayAdapter<scenario> {
    Context context;
    ArrayList<scenario> scenarios;

    public scenarioAdapter(Context context, ArrayList<scenario> scenarios) {
        super(context, R.layout.scenarios_row, scenarios);
        this.context = context;
        this.scenarios = scenarios;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View scenariosList = inflater.inflate(R.layout.scenarios_row, parent, false);

        scenario scenarioObject = scenarios.get(position);

        TextView scenariosListNumberTextView = (TextView) scenariosList.findViewById(R.id.scenariosListNumberTextView);
        scenariosListNumberTextView.setText(Integer.toString(scenarioObject.getScenarioNumber()));

        return scenariosList;
    }
}

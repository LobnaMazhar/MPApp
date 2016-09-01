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
 * Created by Lobna on 28-Aug-16.
 */
public class projectSpinnerAdapter extends ArrayAdapter<project> {
    Context context;
    ArrayList<project> projects;

    public projectSpinnerAdapter(Context context, ArrayList<project> projects) {
        super(context, R.layout.projectspinner_row, projects);
        this.context = context;
        this.projects = projects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View projectsList = inflater.inflate(R.layout.projectspinner_row, parent, false);

        project projectObject = projects.get(position);
        TextView projectNameTextView = (TextView) projectsList.findViewById(R.id.projectNameTextView);
        projectNameTextView.setText(projectObject.getProjectName());

        return projectsList;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.projectspinner_row, null);
        }

        TextView projectNameTextView = (TextView) convertView.findViewById(R.id.projectNameTextView);
        projectNameTextView.setText(projects.get(position).getProjectName());

        return convertView;
    }
}

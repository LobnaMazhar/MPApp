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
public class projectsAdapter extends ArrayAdapter<project> {
    Context context;
    ArrayList<project> projectList;

    public projectsAdapter(Context context, ArrayList<project> projectList) {
        super(context, R.layout.technicalplanslist_row, projectList);
        this.context = context;
        this.projectList = projectList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        //LayoutInflater inflater = LayoutInflater.from(getContext());

        View technicalPlanListView = inflater.inflate(R.layout.technicalplanslist_row, parent, false);

        project projectObject = projectList.get(position);
        TextView projectNameTextView = (TextView) technicalPlanListView.findViewById(R.id.projectNameTextView);
        projectNameTextView.setText(projectObject.getProjectName());

        return technicalPlanListView;
    }
}

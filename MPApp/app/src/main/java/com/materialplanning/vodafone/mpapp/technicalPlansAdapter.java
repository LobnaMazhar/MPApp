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
public class technicalPlansAdapter extends ArrayAdapter<technicalPlans> {
    Context context;
    ArrayList<technicalPlans> technicalPlansList;

    public technicalPlansAdapter(Context context, ArrayList<technicalPlans> technicalPlansList) {
        super(context, R.layout.technicalplanslist_row, technicalPlansList);
        this.context = context;
        this.technicalPlansList = technicalPlansList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        //LayoutInflater inflater = LayoutInflater.from(getContext());

        View technicalPlansListView = inflater.inflate(R.layout.technicalplanslist_row, parent, false);

        technicalPlans technicalPlansObject = technicalPlansList.get(position);
        TextView technicalPlansNameTextView = (TextView) technicalPlansListView.findViewById(R.id.technicalPlansNameTextView);
        technicalPlansNameTextView.setText(technicalPlansObject.getTechnicalPlanName());

        return technicalPlansListView;
    }
}

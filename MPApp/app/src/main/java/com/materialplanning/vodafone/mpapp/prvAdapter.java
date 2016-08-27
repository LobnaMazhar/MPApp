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
 * Created by Lobna on 26-Aug-16.
 */
public class prvAdapter extends ArrayAdapter<prv> {
    Context context; // TAKE CARE ::: CONTEXT IS NOT PRIVATE
    ArrayList<prv> prvs;

    public prvAdapter(Context context, ArrayList<prv> prvs) {
        super(context, R.layout.prvslist_row, prvs);
        this.context = context;
        this.prvs = prvs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        //LayoutInflater inflater = LayoutInflater.from(getContext());

        View prvListView = inflater.inflate(R.layout.prvslist_row, parent, false);

        prv prvObject = prvs.get(position);

        TextView projectNameInPRVTextView = (TextView) prvListView.findViewById(R.id.projectNameInPRVTextView);
        projectNameInPRVTextView.setText(prvObject.getProjectName());

        return prvListView;
    }
}

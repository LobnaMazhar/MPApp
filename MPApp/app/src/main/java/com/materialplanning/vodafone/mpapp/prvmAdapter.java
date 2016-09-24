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
public class prvmAdapter extends ArrayAdapter<prvm> {
    Context context; // TAKE CARE ::: CONTEXT IS NOT PRIVATE
    ArrayList<prvm> prvms;

    public prvmAdapter(Context context, ArrayList<prvm> prvms) {
        super(context, R.layout.prvmslist_row, prvms);
        this.context = context;
        this.prvms = prvms;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        //LayoutInflater inflater = LayoutInflater.from(getContext());

        View prvmListView = inflater.inflate(R.layout.prvmslist_row, parent, false);

        prvm prvmObject = prvms.get(position);

        TextView projectNameInPRVMTextView = (TextView) prvmListView.findViewById(R.id.projectNameInPRVMTextView);
        projectNameInPRVMTextView.setText(prvmObject.getProjectName());

        return prvmListView;
    }
}

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
 * Created by Lobna on 24-Aug-16.
 */
public class regionAdapter extends ArrayAdapter<region> {
    Context context;
    ArrayList<region> regions;

    public regionAdapter(Context context, ArrayList<region> regions) {
        super(context, R.layout.regionspinner_row, regions);
        this.context = context;
        this.regions = regions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View regionsList = inflater.inflate(R.layout.regionspinner_row, parent, false);

        region regionObject = regions.get(position);

        return regionsList;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.regionspinner_row, null);
        }

        TextView regionNameTextView = (TextView) convertView.findViewById(R.id.regionNameTextView);
        regionNameTextView.setText(regions.get(position).getRegionName());

        return convertView;
    }
}

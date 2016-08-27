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
public class vendorAdapter extends ArrayAdapter<vendor> {
    Context context;
    ArrayList<vendor> vendors;

    public vendorAdapter(Context context, ArrayList<vendor> vendors) {
        super(context, R.layout.vendorspinner_row, vendors);
        this.context = context;
        this.vendors = vendors;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View vendorsList = inflater.inflate(R.layout.vendorspinner_row, parent, false);

        vendor vendorObject = vendors.get(position);

        return vendorsList;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.vendorspinner_row, null);
        }

        TextView vendorNameTextView = (TextView) convertView.findViewById(R.id.vendorNameTextView);
        vendorNameTextView.setText(vendors.get(position).getVendorName());

        return convertView;
    }
}

package com.materialplanning.vodafone.mpapp;

/**
 * Created by Lobna on 25-Aug-16.
 */

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unchecked")
public class projectInPRVAdapter extends BaseExpandableListAdapter {

    Context ctx;
    public ArrayList<region> regionsList = new ArrayList<region>();
    public ArrayList<vendor> vendorsList = new ArrayList<vendor>();
    private ArrayList<prv> prvsList = new ArrayList<prv>();

    ArrayList<String> tempChild;
    public LayoutInflater minflater;
    public Activity activity;

    public projectInPRVAdapter(ArrayList<region> regionsList, ArrayList<vendor> vendorsList, ArrayList<prv> prvsList) {
        this.regionsList = regionsList;
        this.vendorsList = vendorsList;
        this.prvsList = prvsList;
    }

    public void setInflater(LayoutInflater mInflater, Activity act) {
        this.minflater = mInflater;
        activity = act;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.prvexpandablelist_childrow, parent, false);
        }

        vendor vendorObject = vendorsList.get(groupPosition);
        TextView vendorNameTextView = (TextView) v.findViewById(R.id.vendorNameExpandableListTextView);
        vendorNameTextView.setText(vendorObject.getVendorName());
        EditText yearTargetEditText = (EditText) v.findViewById(R.id.yearTargetExpandableListEditText);
        yearTargetEditText.setText("2");

        return v;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return vendorsList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return regionsList.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.prvexpandablelist_grouprow, parent, false);
        }

        TextView regionNameTextView = (TextView) v.findViewById(R.id.regionNameExpandableListTextView);
        region regionObject = regionsList.get(groupPosition);
        regionNameTextView.setText(regionObject.getRegionName());

        return v;

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}

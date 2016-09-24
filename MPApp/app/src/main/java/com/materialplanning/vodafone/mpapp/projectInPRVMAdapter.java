package com.materialplanning.vodafone.mpapp;

/**
 * Created by Lobna on 25-Aug-16.
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("unchecked")
public class projectInPRVMAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<region> regions;
    private LayoutInflater inflater;

    public projectInPRVMAdapter(Context context, ArrayList<region> regions){
        this.context = context;
        this.regions = regions;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return regions.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return regions.get(groupPosition).vendors.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return regions.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return regions.get(groupPosition).vendors.get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override // get parent/region row
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.prvmexpandablelist_grouprow, null);
        }

        // SET REGION NAME
        final TextView regionNameExpandableListTextView = (TextView) convertView.findViewById(R.id.regionNameExpandableListTextView);

        region region = (region) getGroup(groupPosition);

        HashMap<String, String> params = new HashMap<>();
        params.put("regionID", Integer.toString(region.getRegionID()));
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    regionNameExpandableListTextView.setText(reader.getString("regionName"));
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/getRegionNameByID");


        return convertView;
    }

    @Override // Get child/vendor row
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.prvmexpandablelist_childrow, null);
        }

        // SET VENDOR NAME
        final int vendorID = ((Pair<Integer, Integer>) getChild(groupPosition, childPosition)).first;
        final TextView vendorNameAndYearTargetExpandableListTextView = (TextView) convertView.findViewById(R.id.vendorNameAndYearTargetExpandableListTextView);

        HashMap<String, String> params = new HashMap<>();
        params.put("vendorID", Integer.toString(vendorID));
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    // Set child/vendor name and year target ,,,, // 3mlahom f one textview together 3shan 3'er kda kant l click listener msh sh3'ala
                    int yearTarget = ((Pair<Integer, Integer>) getChild(groupPosition, childPosition)).second;
                    vendorNameAndYearTargetExpandableListTextView.setText(reader.getString("vendorName") + "\t\t\t\t\t\t\t\t\t\t" + Integer.toString(yearTarget));
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/getVendorNameByID");

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}

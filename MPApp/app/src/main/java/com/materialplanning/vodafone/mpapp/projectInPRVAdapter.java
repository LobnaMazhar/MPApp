package com.materialplanning.vodafone.mpapp;

/**
 * Created by Lobna on 25-Aug-16.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Region;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("unchecked")
public class projectInPRVAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<region> regions;
    private LayoutInflater inflater;

    public projectInPRVAdapter(Context context, ArrayList<region> regions){
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
            convertView = inflater.inflate(R.layout.prvexpandablelist_grouprow, null);
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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.prvexpandablelist_childrow, null);
        }

        // SET VENDOR NAME
        final int vendorID = ((Pair<Integer, Integer>) getChild(groupPosition, childPosition)).first;
        final TextView vendorNameExpandableListTextView = (TextView) convertView.findViewById(R.id.vendorNameExpandableListTextView);

        HashMap<String, String> params = new HashMap<>();
        params.put("vendorID", Integer.toString(vendorID));
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    // Set child/vendor name
                    vendorNameExpandableListTextView.setText(reader.getString("vendorName"));
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/getVendorNameByID");


        // SET YEAR TARGET
        EditText yearTargetExpandableListEditText = (EditText) convertView.findViewById(R.id.yearTargetExpandableListEditText);
        int yearTarget = ((Pair<Integer, Integer>) getChild(groupPosition, childPosition)).second;
        yearTargetExpandableListEditText.setText(Integer.toString(yearTarget));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}

package com.materialplanning.vodafone.mpapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lobna on 16-Aug-16.
 */
public class siteAdapter extends ArrayAdapter<site>{
    Context context;
    ArrayList<site> siteList;

    public siteAdapter(Context context, ArrayList<site> sites) {
        super(context, R.layout.site_row, sites);
        this.context = context;
        this.siteList = sites;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View siteListView = inflater.inflate(R.layout.site_row, parent, false);

        site siteObject = siteList.get(position);

        getMonth(siteObject.getSiteDate(), siteListView);

        TextView siteIDTextView = (TextView) siteListView.findViewById(R.id.siteIDTextView);
        siteIDTextView.setText(siteObject.getSiteID());

        getProjectName(siteObject.getSiteProjectID(), siteListView);

        getRegionNameForSite(siteObject.getSiteID(), siteListView);

        return siteListView;
    }

    public void getMonth(int date, final View siteListView){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("monthID", Integer.toString(date));
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    String month = reader.getString("monthName");
                    TextView siteDateTextView = (TextView) siteListView.findViewById(R.id.siteDateTextView);
                    siteDateTextView.setText(month);
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/getMonthName");
    }

    public void getProjectName(int projectID, final View siteListView){
        HashMap<String, String> params = new HashMap<>();
        params.put("projectID", Integer.toString(projectID));
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    String projectName = reader.getString("projectName");

                    TextView siteTypeTextView = (TextView) siteListView.findViewById(R.id.siteTypeTextView);
                    siteTypeTextView.setText(projectName);

                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/getProjectName");
    }

    public void getRegionNameForSite(String siteID, final View siteListView){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("siteID", siteID);

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
             try {
                 JSONObject reader = new JSONObject(result);
                 String regionName = reader.getString("regionName");

                 TextView siteRegionTextView = (TextView) siteListView.findViewById(R.id.siteRegionTextView);
                 siteRegionTextView.setText(regionName);

             }catch (JSONException e){
             }
            }
        });
        conn.execute(conn.URL + "/getRegionNameForSite");
    }
}

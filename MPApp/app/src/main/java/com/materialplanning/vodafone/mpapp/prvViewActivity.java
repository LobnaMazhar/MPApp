package com.materialplanning.vodafone.mpapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class prvViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prv_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getExtras().getString("projectName"));
        setSupportActionBar(toolbar);

        prepareExpandableListView();
    }

    public void prepareExpandableListView(){
        // get PRVs
        final ArrayList<prv> prvList = new ArrayList<prv>();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("projectID", Integer.toString(getIntent().getExtras().getInt("projectID")));
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);

                    prv prvObject = new prv();
                    prvObject.prvID = reader.getInt("prvID");
                    prvObject.prvProjectID = reader.getInt("prvProjectID");
                    prvObject.prvRegionID = reader.getInt("prvRegionID");
                    prvObject.prvVendorID = reader.getInt("prvVendorID");
                    prvObject.prvYearTarget = reader.getInt("prvYearTarget");
                    prvList.add(prvObject);
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/getPRVs");


        // get regions data
        final ArrayList<region> regionsList = new ArrayList<region>();
        HashMap<String, String> params2 = new HashMap<String, String>();
        Connection conn2 = new Connection(params2, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);

                    region regionObject = new region();
                    regionObject.regionID = reader.getInt("regionID");
                    regionObject.regionName = reader.getString("regionName");
                    regionsList.add(regionObject);
                }catch (JSONException e){
                }
            }
        });
        conn2.execute(conn2.URL + "/getRegions");


        // get vendors data
        final ArrayList<vendor> vendorsList = new ArrayList<vendor>();
        HashMap<String, String> params3 = new HashMap<String, String>();
        Connection conn3 = new Connection(params3, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);

                    vendor vendorObject = new vendor();
                    vendorObject.vendorID = reader.getInt("vendorID");
                    vendorObject.vendorName = reader.getString("vendorName");
                    vendorsList.add(vendorObject);
                }catch (JSONException e){
                }
            }
        });
        conn3.execute(conn3.URL + "/getVendors");


        ExpandableListView prvExpandableListView = (ExpandableListView) findViewById(R.id.prvExpandableListView);
        projectInPRVAdapter prvListAdapter = new projectInPRVAdapter(regionsList, vendorsList, prvList);
        prvExpandableListView.setAdapter(prvListAdapter);
    }

}

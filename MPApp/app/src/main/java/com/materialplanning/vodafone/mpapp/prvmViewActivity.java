package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class prvmViewActivity extends AppCompatActivity {

    int huaweiTotal = 0, ericssonTotal = 0, totalTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prvm_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getExtras().getString("projectName"));
        setSupportActionBar(toolbar);

        prepareExpandableListView();
    }

    public void prepareExpandableListView(){
        final ArrayList<region> regionsList = new ArrayList<>();
        final ArrayList<Integer> foundRegions = new ArrayList<>();

        final ExpandableListView prvmExpandableListView = (ExpandableListView) findViewById(R.id.prvmExpandableListView);


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("projectID", Integer.toString(getIntent().getExtras().getInt("projectID")));
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONArray reader = new JSONArray(result);
                    for(int i=0; i<reader.length(); ++i){
                        JSONObject data = reader.getJSONObject(i);

                        int regionID = data.getInt("prvmRegionID");
                        if(!foundRegions.contains(regionID)){
                            foundRegions.add(regionID);

                            region regionObject = new region();
                            regionObject.regionID = regionID;
                            regionsList.add(regionObject);
                        }

                        int regionIDIndex = foundRegions.indexOf(regionID);
                        region regionObjectAtIndex = regionsList.get(regionIDIndex);
                        int vendorID = data.getInt("prvmVendorID"), yearTarget = data.getInt("prvmYearTarget");
                        regionObjectAtIndex.vendors.add(Pair.create(vendorID, yearTarget));
                        if(vendorID == 1){ // NOTE :: Huawei (in database)
                            huaweiTotal += yearTarget;
                        }else if(vendorID == 2){ // NOTE :: Ericsson (in database)
                            ericssonTotal += yearTarget;
                        }
                    }

                    projectInPRVMAdapter prvmListAdapter = new projectInPRVMAdapter(prvmViewActivity.this, regionsList);
                    prvmExpandableListView.setAdapter(prvmListAdapter);

                    getTotalValues();

                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/getPRVMsByProjectID");

        prvmExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
            //    Toast.makeText(getApplicationContext(), Integer.toString(regionsList.get(groupPosition).vendors.get(childPosition).first) + " ,, " + Integer.toString(regionsList.get(groupPosition).vendors.get(childPosition).second), Toast.LENGTH_LONG).show();

                // Monthly phasing per region
                Intent goToMonthlyPhasing = new Intent(prvmViewActivity.this, monthlyPhasingActivity.class);
                // TODO put extras
                goToMonthlyPhasing.putExtra("projectID", getIntent().getExtras().getInt("projectID"));
                goToMonthlyPhasing.putExtra("projectName", getIntent().getExtras().getString("projectName"));
                goToMonthlyPhasing.putExtra("regionID", regionsList.get(groupPosition).getRegionID());
                goToMonthlyPhasing.putExtra("vendorID", regionsList.get(groupPosition).vendors.get(childPosition).first);
                goToMonthlyPhasing.putExtra("yearTarget", regionsList.get(groupPosition).vendors.get(childPosition).second);
                startActivity(goToMonthlyPhasing);
                finish();

                return false;
            }
        });
    }

    public void getTotalValues(){
        TextView huaweiTotalTextView = (TextView) findViewById(R.id.huaweiTotalTextView);
        huaweiTotalTextView.setText(Integer.toString(huaweiTotal));

        TextView ericssonTotalTextView = (TextView) findViewById(R.id.ericssonTotalTextView);
        ericssonTotalTextView.setText(Integer.toString(ericssonTotal));

        TextView totalTotalTextView = (TextView) findViewById(R.id.totalTotalTextView);
        totalTotal = huaweiTotal + ericssonTotal;
        totalTotalTextView.setText(Integer.toString(totalTotal));
    }

     /*
    //Dot Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dot, menu);
        return true;
    }
     */
}

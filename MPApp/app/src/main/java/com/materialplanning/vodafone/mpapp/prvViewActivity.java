package com.materialplanning.vodafone.mpapp;

import android.graphics.Region;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class prvViewActivity extends AppCompatActivity {

    int huaweiTotal = 0, ericssonTotal = 0, totalTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prv_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getExtras().getString("projectName"));
        setSupportActionBar(toolbar);

        prepareExpandableListView();
   //     getTotalValues();
    }

    public void prepareExpandableListView(){
        // get PRVs
        // TODO h3ml enu y-get unique names ll projects ely fl technical plan w ama ydus 3la wa7d yru7 ygeb l data bta3 which is kol l regions ely tb3 vendor mo3yn b year target
        final ArrayList<region> regionsList = new ArrayList<>();
        final ArrayList<Integer> foundRegions = new ArrayList<>();

        final ExpandableListView prvExpandableListView = (ExpandableListView) findViewById(R.id.prvExpandableListView);


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("projectID", Integer.toString(getIntent().getExtras().getInt("projectID")));
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONArray reader = new JSONArray(result);
                    for(int i=0; i<reader.length(); ++i){
                        JSONObject data = reader.getJSONObject(i);

                        int regionID = data.getInt("prvRegionID");
                        if(!foundRegions.contains(regionID)){
                            foundRegions.add(regionID);

                            region regionObject = new region();
                            regionObject.regionID = regionID;
                            regionsList.add(regionObject);
                        }

                        int regionIDIndex = foundRegions.indexOf(regionID);
                        region regionObjectAtIndex = regionsList.get(regionIDIndex);
                        int vendorID = data.getInt("prvVendorID"), yearTarget = data.getInt("prvYearTarget");
                        regionObjectAtIndex.vendors.add(Pair.create(vendorID, yearTarget));
                        if(vendorID == 1){ // Huawei (in database)
                            huaweiTotal += yearTarget;
                        }else if(vendorID == 2){ // Ericsson (in database)
                            ericssonTotal += yearTarget;
                        }
                    }

                    projectInPRVAdapter prvListAdapter = new projectInPRVAdapter(prvViewActivity.this, regionsList);
                    prvExpandableListView.setAdapter(prvListAdapter);

                    getTotalValues();

                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/getPRVsByProjectID");

        // TODO m7taga on click asln ??
        prvExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), Integer.toString(regionsList.get(groupPosition).vendors.get(childPosition).first) + " ,, " + Integer.toString(regionsList.get(groupPosition).vendors.get(childPosition).second), Toast.LENGTH_LONG).show();
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

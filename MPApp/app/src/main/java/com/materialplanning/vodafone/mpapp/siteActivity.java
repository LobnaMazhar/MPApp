package com.materialplanning.vodafone.mpapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class siteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(getIntent().getExtras().get("rollout/expansion").equals("1"))
            toolbar.setTitle("Rollout");
        else if(getIntent().getExtras().get("rollout/expansion").equals("2"))
            toolbar.setTitle("Expansions");
        setSupportActionBar(toolbar);

        getRollout_Expansions();
    }

    public void getRollout_Expansions(){
        final ArrayList<site> siteList = new ArrayList<site>();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("rollout/expansion", getIntent().getExtras().get("rollout/expansion").toString());
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    final JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); ++i) {
                        JSONObject data = reader.getJSONObject(i);

                        site siteObject = new site();

                        siteObject.siteID = data.getString("siteID");
                        siteObject.siteProjectID = data.getInt("projectID");
                        siteObject.siteDate = data.getInt("date");

                        siteList.add(siteObject);
                    }

                    ArrayAdapter<site> sitesListAdapter = new siteAdapter(siteActivity.this, siteList);
                    // Connect list and adapter
                    ListView siteListView = (ListView) findViewById(R.id.siteListView);
                    siteListView.setAdapter(sitesListAdapter);

                    // TODO hn7tag item click listener ??

                } catch (JSONException e) {
                }
            }
        });
        conn.execute(conn.URL + "/getSites");
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

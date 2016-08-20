package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class rolloutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rollout);

        getRollout();
    }

    public void getRollout(){
        final ArrayList<rollout> rolloutList = new ArrayList<rollout>();
        HashMap<String, String> params = new HashMap<String, String>();
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    final JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); ++i) {
                        JSONObject data = reader.getJSONObject(i);

                        rollout rolloutObject = new rollout();

                      //  rolloutObject.rolloutDate = Date.parse(data.getString("date")); // TODO getDate()
                        rolloutObject.rolloutSiteID = data.getString("siteID");
                        rolloutObject.rolloutRegion = data.getString("region");
                        rolloutObject.rolloutFeederFiber = data.getString("feederFiber");

                        rolloutList.add(rolloutObject);
                    }

                    ArrayAdapter<rollout> rolloutListAdapter = new rolloutAdapter(rolloutActivity.this, rolloutList);
                    // Connect list and adapter
                    ListView rolloutListView = (ListView) findViewById(R.id.rolloutListView);
                    rolloutListView.setAdapter(rolloutListAdapter);

                    // TODO hn7tag item click listener ??

                } catch (JSONException e) {
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/getRollout");
    }

}

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
import java.util.HashMap;

public class technicalPlansActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_plans);

        getTechnicalPlans();
    }

    public void getTechnicalPlans(){
        final ArrayList<technicalPlans> technicalPlansList = new ArrayList<technicalPlans>();
        HashMap<String, String> params = new HashMap<String, String>();
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    final JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); ++i) {
                        JSONObject data = reader.getJSONObject(i);

                        technicalPlans technicalPlansObject = new technicalPlans();

                        technicalPlansObject.technicalPlanName = data.getString("technicalPlanName");

                        technicalPlansList.add(technicalPlansObject);
                    }

                    ArrayAdapter<technicalPlans> technicalPlansListAdapter = new technicalPlansAdapter(technicalPlansActivity.this, technicalPlansList);
                    // Connect list and adapter
                    ListView technicalPlansListView = (ListView) findViewById(R.id.technicalPlansListView);
                    technicalPlansListView.setAdapter(technicalPlansListAdapter);

                    // On item click listener
                    technicalPlansListView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try{

                                        Intent goToTechnicalPlan = new Intent(technicalPlansActivity.this, technicalPlanViewActivity.class);

                                        goToTechnicalPlan.putExtra("technicalPlanID", reader.getJSONObject(position).getInt("technicalPlanID"));
                                        goToTechnicalPlan.putExtra("technicalPlanName", reader.getJSONObject(position).getString("technicalPlanName"));

                                        startActivity(goToTechnicalPlan);

                                    }
                                    catch (JSONException e){}
                                }
                            }
                    );

                } catch (JSONException e) {
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/getTechnicalPlans");
    }

    public void addTechnicalPlan(View view){
        Intent intent = new Intent(this, addTechnicalPlanActivity.class);
        startActivity(intent);
    }

}

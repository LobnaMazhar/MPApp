package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Technical plan");
        setSupportActionBar(toolbar);

        getPRVs();
    }

    public void getPRVs(){
        final ArrayList<prv> prvsList = new ArrayList<prv>();
        HashMap<String, String> params = new HashMap<String, String>();
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    final JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); ++i) {
                        JSONObject data = reader.getJSONObject(i);

                        prv prvObject = new prv();

                        prvObject.prvID = data.getInt("prvID");
                        prvObject.prvProjectID = data.getInt("prvProjectID");
                        prvObject.prvRegionID = data.getInt("prvRegionID");
                        prvObject.prvVendorID = data.getInt("prvVendorID");
                        prvObject.prvYearTarget = data.getInt("prvYearTarget");
                        prvObject.projectName = data.getString("projectName");

                        prvsList.add(prvObject);
                    }

                    ArrayAdapter<prv> prvListAdapter = new prvAdapter(technicalPlansActivity.this, prvsList);
                    // Connect list and adapter
                    ListView projectsInTechnicalPlanListView = (ListView) findViewById(R.id.projectsInTechnicalPlanListView);
                    projectsInTechnicalPlanListView.setAdapter(prvListAdapter);

                    // On item click listener
                    projectsInTechnicalPlanListView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try{
                                        Intent goToPRV = new Intent(technicalPlansActivity.this, prvViewActivity.class);
                                        goToPRV.putExtra("projectID", prvsList.get(position).getPrvProjectID());
                                        goToPRV.putExtra("projectName", prvsList.get(position).getProjectName());
                                        startActivity(goToPRV);
                                    }
                                    catch (Exception e){}
                                }
                            }
                    );
                } catch (JSONException e) {
                }
            }
        });
        conn.execute(conn.URL + "/getPRVs");
    }

    public void addProject(View view) {
        Intent intent = new Intent(technicalPlansActivity.this, addProjectToTechnicalPlanActivity.class);
        startActivity(intent);
    }
}

package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

        getProjectsInPRVs();
    }

    public void getProjectsInPRVs(){
        final ArrayList<project> projectsInPRVsList = new ArrayList<project>();
        HashMap<String, String> params = new HashMap<String, String>();
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    final JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); ++i) {
                        JSONObject data = reader.getJSONObject(i);

                        project projectInPRVObject = new project();

                        projectInPRVObject.projectID = data.getInt("projectID");
                        projectInPRVObject.projectName = data.getString("projectName");

                        projectsInPRVsList.add(projectInPRVObject);
                    }

                    ArrayAdapter<project> projectsInPRVsListAdapter = new projectsAdapter(technicalPlansActivity.this, projectsInPRVsList);
                    // Connect list and adapter
                    ListView projectsInTechnicalPlanListView = (ListView) findViewById(R.id.projectsInTechnicalPlanListView);
                    projectsInTechnicalPlanListView.setAdapter(projectsInPRVsListAdapter);

                    // On item click listener
                    projectsInTechnicalPlanListView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try{
                                        Intent goToPRV = new Intent(technicalPlansActivity.this, prvViewActivity.class);
                                        goToPRV.putExtra("projectID", projectsInPRVsList.get(position).getProjectID());
                                        goToPRV.putExtra("projectName", projectsInPRVsList.get(position).getProjectName());
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
        conn.execute(conn.URL + "/getProjectsInPRVs");
    }

    public void addProject(View view) {
        Intent intent = new Intent(technicalPlansActivity.this, addProjectToTechnicalPlanActivity.class);
        startActivity(intent);
        finish();
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

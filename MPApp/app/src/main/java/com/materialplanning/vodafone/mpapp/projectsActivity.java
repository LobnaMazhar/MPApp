package com.materialplanning.vodafone.mpapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class projectsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Projects");
        setSupportActionBar(toolbar);

        getProjects();
    }

    public void getProjects(){
        final ArrayList<project> projectList = new ArrayList<project>();
        HashMap<String, String> params = new HashMap<String, String>();
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    final JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); ++i) {
                        JSONObject data = reader.getJSONObject(i);

                        project projectObject = new project();

                        projectObject.projectID = data.getInt("projectID");
                        projectObject.projectName = data.getString("projectName");

                        projectList.add(projectObject);
                    }

                    ArrayAdapter<project> projectsListAdapter = new projectsAdapter(projectsActivity.this, projectList);
                    // Connect list and adapter
                    ListView projectsListView = (ListView) findViewById(R.id.projectsListView);
                    projectsListView.setAdapter(projectsListAdapter);

                    projectsListView.setOnTouchListener(new OnSwipeTouchListener(projectsActivity.this, projectsListView){
                        public void onSwipeLeft(int pos) {
                            try{
                                deleteProject(reader.getJSONObject(pos).getInt("projectID"));
                            }catch (JSONException e){
                            }
                        }
                    });

                    // On item click listener
                    projectsListView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try{

                                        Intent goToProject = new Intent(projectsActivity.this, projectViewActivity.class);

                                        goToProject.putExtra("projectID", reader.getJSONObject(position).getInt("projectID"));
                                        goToProject.putExtra("projectName", reader.getJSONObject(position).getString("projectName"));

                                        startActivity(goToProject);
                                    }
                                    catch (JSONException e){}
                                }
                            }
                    );

                } catch (JSONException e) {
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/getProjects");
    }

    public void addProject(View view){
        Intent intent = new Intent(this, addProjectActivity.class);
        startActivity(intent);
    }

    public void deleteProject(final int projectID){
        //Put up the Yes/No message box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete project");
        builder.setMessage("Are you sure?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("projectID", Integer.toString(projectID));
                Connection conn = new Connection(params, new ConnectionPostListener() {
                    @Override
                    public void doSomething(String result) {
                        try {
                            JSONObject reader = new JSONObject(result);
                            if(reader.getBoolean("deleted")){
                                Toast.makeText(projectsActivity.this, "Project has been deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(projectsActivity.this, projectsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }catch (JSONException e){
                            Toast.makeText(projectsActivity.this, "Can't be deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/deleteProject");
            }
        });
        builder.setNegativeButton("No", null);
        builder .show();
    }

}

package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class addProjectActivity extends AppCompatActivity {

    int projectID;
    boolean addScenariosButtonDown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add new project");
        setSupportActionBar(toolbar);
    }

    public void saveProject(View view){
        EditText addProjectNameEditText = (EditText) findViewById(R.id.addProjectNameEditText);
        String projectName = addProjectNameEditText.getText().toString();

        if(!addScenariosButtonDown) {
            saveProject(projectName);
            TextView projectNameErrorTextView = (TextView) findViewById(R.id.projectNameErrorTextView);
            if(!projectNameErrorTextView.getText().toString().equals(""))
                return;
            else{
                getProjectID(projectName);

                Toast.makeText(addProjectActivity.this, "Project is added successfully", Toast.LENGTH_SHORT).show();

                Intent goToProject = new Intent(addProjectActivity.this, projectViewActivity.class);
                goToProject.putExtra("projectID", projectID);
                goToProject.putExtra("projectName", projectName);
                startActivity(goToProject);
            }
        }else{
            Toast.makeText(addProjectActivity.this, "Project is added successfully", Toast.LENGTH_SHORT).show();

            Intent goToProject = new Intent(addProjectActivity.this, projectViewActivity.class);
            goToProject.putExtra("projectID", projectID);
            goToProject.putExtra("projectName", projectName);
            startActivity(goToProject);
            finish();
        }
    }

    public void addScenarios(View view){
        addScenariosButtonDown = true;
        Button addScenariosButton = (Button) findViewById(R.id.addScenariosButton);
        addScenariosButton.setVisibility(View.INVISIBLE);
        ListView addScenariosToProjectListView = (ListView) findViewById(R.id.addScenariosToProjectListView);
        addScenariosToProjectListView.setVisibility(View.VISIBLE);


        EditText addProjectNameEditText = (EditText) findViewById(R.id.addProjectNameEditText);
        String projectName = addProjectNameEditText.getText().toString();

        saveProject(projectName);
        TextView projectNameErrorTextView = (TextView) findViewById(R.id.projectNameErrorTextView);
        if(!projectNameErrorTextView.getText().toString().equals(""))
            return;
        else{
            getProjectID(projectName);
            showScenariosToSelectFrom();
        }
    }

    public void showScenariosToSelectFrom(){
        HashMap<String, String> params = new HashMap<String, String>();
        final ArrayList<scenario> scenariosList = new ArrayList<scenario>();
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    final JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); ++i) {
                        JSONObject data = reader.getJSONObject(i);

                        scenario scenarioObject = new scenario();

                        scenarioObject.scenarioID = data.getInt("scenarioID");
                        scenarioObject.scenarioNumber = data.getInt("scenarioNumber");

                        scenariosList.add(scenarioObject);
                    }

                    ListView addScenariosToProjectListView = (ListView) findViewById(R.id.addScenariosToProjectListView);
                    ArrayAdapter<scenario> scenarioListAdapter = new scenarioAdapter(addProjectActivity.this, scenariosList);
                    // Connect list and adapter
                    addScenariosToProjectListView.setAdapter(scenarioListAdapter);

                    final ArrayList<Integer> selectedScenarios = new ArrayList<Integer>();

                    addScenariosToProjectListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                    addScenariosToProjectListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                        @Override
                        public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
                            if (checked) {
                                selectedScenarios.add(scenariosList.get(position).getScenarioID());
                            }else{
                                int indexToRemove = selectedScenarios.indexOf(scenariosList.get(position).getScenarioID());
                                selectedScenarios.remove(indexToRemove);
                            }
                            actionMode.setTitle(selectedScenarios.size() + " scenarios selected.");
                        }

                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                            MenuInflater inflater = actionMode.getMenuInflater();
                            inflater.inflate(R.menu.context_add, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.add:
                                    addScenariosToProject(selectedScenarios);
                                    actionMode.finish();
                                    return true;
                                default:
                                    return false;
                            }
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode actionMode) {
                        }
                    });

                }catch (JSONException e){
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/getScenarios");
    }

    public void addScenariosToProject(ArrayList<Integer> selectedScenarios){
        for(int i=0; i<selectedScenarios.size(); ++i){
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("scenarioID", Integer.toString(selectedScenarios.get(i)));
            params.put("projectID", Integer.toString(projectID));
            Connection conn = new Connection(params, new ConnectionPostListener() {
                @Override
                public void doSomething(String result) {
                    try{
                        JSONObject reader = new JSONObject(result);
                    }catch (JSONException e){
                    }
                }
            });
            conn.execute(conn.URL + "/addScenarioToProject");
        }
        Toast.makeText(addProjectActivity.this, Integer.toString(selectedScenarios.size()) + " scenarios added.", Toast.LENGTH_LONG).show();
    }

    public void saveProject(String projectName){
        final TextView projectNameErrorTextView = (TextView) findViewById(R.id.projectNameErrorTextView);
        if(projectName.equals("")){
            projectNameErrorTextView.setText("Enter project Name first !!");
        }else{
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("projectName", projectName);

            Connection conn = new Connection(params, new ConnectionPostListener() {
                @Override
                public void doSomething(String result) {
                    try{
                        JSONObject reader = new JSONObject(result);
                        if(reader.getBoolean("added")){
                            projectNameErrorTextView.setText("");
                            return;
                        }
                    }catch (JSONException e){
                        projectNameErrorTextView.setText("Project name already exists !!");
                    }
                }
            });
            conn.execute(conn.URL + "/addProject");
        }
    }

    public void getProjectID(String projectName){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("projectName", projectName);

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    projectID = reader.getInt("projectID");
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/getProjectID");
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

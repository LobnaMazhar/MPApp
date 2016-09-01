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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class projectViewActivity extends AppCompatActivity {

    ArrayList<Integer> scenariosInProjectIDsList = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

        String projectName = getIntent().getExtras().getString("projectName");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(projectName);
        setSupportActionBar(toolbar);

        TextView projectViewNameEditText = (TextView) findViewById(R.id.projectViewNameEditText);
        projectViewNameEditText.setText(projectName);

        getScenariosInProject();
    }

    public void getScenariosInProject(){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("projectID", Integer.toString(getIntent().getExtras().getInt("projectID")));

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
                        scenariosInProjectIDsList.add(data.getInt("scenarioID"));
                    }

                    ArrayAdapter<scenario> scenarioListAdapter = new scenarioAdapter(projectViewActivity.this, scenariosList);
                    // Connect list and adapter
                    ListView scenariosInProjectListView = (ListView) findViewById(R.id.scenariosInProjectListView);
                    scenariosInProjectListView.setAdapter(scenarioListAdapter);


                    // Select items from the list to be (edited(list)=deleted(items))
                    final ArrayList<Integer> selectedScenarios = new ArrayList<Integer>();
                    scenariosInProjectListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                    scenariosInProjectListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
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
                            inflater.inflate(R.menu.context_delete, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.delete:
                                    deleteScenarios(selectedScenarios);
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
                }catch (Exception e){
                }
            }
        });
        conn.execute(conn.URL + "/getScenariosByProjectID");
    }

    public void deleteScenarios(ArrayList<Integer> selectedScenariosToDelete){
        for(int i=0; i<selectedScenariosToDelete.size(); ++i){
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("scenarioID", Integer.toString(selectedScenariosToDelete.get(i)));

            Connection conn = new Connection(params, new ConnectionPostListener() {
                @Override
                public void doSomething(String result) {
                    try{
                        JSONObject reader = new JSONObject(result);
                    }catch (JSONException e){
                    }
                }
            });
            conn.execute(conn.URL + "/deleteScenarioFromProject");
        }
    }

    public void editProject(View view){
        EditText projectViewNameEditText = (EditText) findViewById(R.id.projectViewNameEditText);
        String projectName = projectViewNameEditText.getText().toString();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("projectID", Integer.toString(getIntent().getExtras().getInt("projectID")));
        params.put("projectName", projectName);

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    if(reader.getBoolean("edited"))
                        Toast.makeText(projectViewActivity.this, "Edited", Toast.LENGTH_SHORT).show();
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/editProject");
    }

    public void addScenariosToProject(View view) {
        final ArrayList<scenario> scenariosList = new ArrayList<scenario>();
        HashMap<String, String> params = new HashMap<String, String>();
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    final JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); ++i) {
                        JSONObject data = reader.getJSONObject(i);

                        scenario scenarioObject = new scenario();

                        scenarioObject.scenarioID = data.getInt("scenarioID");
                        scenarioObject.scenarioNumber = data.getInt("scenarioNumber");

                        if (scenariosInProjectIDsList.contains(data.getInt("scenarioID")))
                            continue;
                        else
                            scenariosList.add(scenarioObject);
                    }

                    ListView scenariosInProjectListView = (ListView) findViewById(R.id.scenariosInProjectListView);
                    scenariosInProjectListView.setVisibility(View.INVISIBLE);
                    ListView addScenariosListView = (ListView) findViewById(R.id.addScenariosListView);
                    addScenariosListView.setVisibility(View.VISIBLE);

                    // Connect list and adapter
                    ArrayAdapter<scenario> scenarioListAdapter = new scenarioAdapter(projectViewActivity.this, scenariosList);
                    addScenariosListView.setAdapter(scenarioListAdapter);

                    final ArrayList<Integer> selectedScenarios = new ArrayList<Integer>();
                    addScenariosListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                    addScenariosListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                        @Override
                        public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
                            if (checked) {
                                selectedScenarios.add(scenariosList.get(position).getScenarioID());
                            } else {
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
                } catch (JSONException e) {
                }
            }
        });
        conn.execute(conn.URL + "/getScenarios");
    }

    public void addScenariosToProject(ArrayList<Integer> selectedScenariosToAdd){
        for(int i=0; i<selectedScenariosToAdd.size(); ++i){
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("scenarioID", Integer.toString(selectedScenariosToAdd.get(i)));
            params.put("projectID", Integer.toString(getIntent().getExtras().getInt("projectID")));
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
        Toast.makeText(projectViewActivity.this, Integer.toString(selectedScenariosToAdd.size()) + " scenarios added.", Toast.LENGTH_LONG).show();
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
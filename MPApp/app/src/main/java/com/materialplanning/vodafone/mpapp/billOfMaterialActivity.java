package com.materialplanning.vodafone.mpapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class billOfMaterialActivity extends AppCompatActivity {

    String selectedDate = "";
    int selectedRegionID = 0;

    ArrayList<Integer> selectedProjects = new ArrayList<Integer>();
    boolean addProjectsButtonPressed = false;
    ArrayList<Integer> selectedItems = new ArrayList<Integer>();
    boolean addItemsButtonPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_of_material);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add BOM");
        setSupportActionBar(toolbar);

        prepareRegions();
        prepareMonths();
    }

    public void saveBOM(View view){
        EditText siteIDEditText = (EditText) findViewById(R.id.siteIDEditText);
        String siteID = siteIDEditText.getText().toString();
        if(siteID.isEmpty()){
            Toast.makeText(billOfMaterialActivity.this, "Enter siteID first", Toast.LENGTH_LONG).show();
        }else if(selectedDate.equals("")){
            Toast.makeText(billOfMaterialActivity.this, "Select date", Toast.LENGTH_LONG).show();
        }else if(selectedRegionID == 0) {
            Toast.makeText(billOfMaterialActivity.this, "Select region", Toast.LENGTH_LONG).show();
        }else if(!addProjectsButtonPressed){
            Toast.makeText(billOfMaterialActivity.this, "Add project(s)", Toast.LENGTH_LONG).show();
        }else if(!addItemsButtonPressed){
            Toast.makeText(billOfMaterialActivity.this, "Add item(s)", Toast.LENGTH_LONG).show();
        }else{
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("siteID", siteID);
            params.put("Date", selectedDate);
            params.put("RegionID", Integer.toString(selectedRegionID));

            Connection conn = new Connection(params, new ConnectionPostListener() {
                @Override
                public void doSomething(String result) {
                    try{
                        JSONObject reader = new JSONObject(result);
                        if(reader.getBoolean("added")){
                            int BOMID = reader.getInt("BOMID");
                            addProjects(selectedProjects, BOMID);
                            addItems(selectedItems, BOMID);
                            Toast.makeText(billOfMaterialActivity.this, "BOM added", Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException e){
                    }
                }
            });
            conn.execute(conn.URL + "/addBOM");
        }
    }

    public void prepareRegions(){
        final ArrayList<region> regionsList = new ArrayList<region>();
        HashMap<String, String> params = new HashMap<String, String>();
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONArray reader = new JSONArray(result);
                    for(int i=0; i<reader.length(); ++i){
                        JSONObject data = reader.getJSONObject(i);

                        region regionObject = new region();
                        regionObject.regionID = data.getInt("regionID");
                        regionObject.regionName = data.getString("regionName");
                        regionsList.add(regionObject);
                    }
                }catch (JSONException e){
                }

                Spinner regionSpinner = (Spinner) findViewById(R.id.dateSpinner);
                regionAdapter regionAdapter = new regionAdapter(billOfMaterialActivity.this, regionsList);
                regionSpinner.setAdapter(regionAdapter);

                regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        Toast.makeText(billOfMaterialActivity.this, regionsList.get(position).getRegionName(), Toast.LENGTH_SHORT).show();
                        selectedRegionID = regionsList.get(position).getRegionID();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });
        conn.execute(conn.URL + "/getRegions");
    }

    public void prepareMonths(){
        final ArrayList<String> monthsList = new ArrayList<String>();
        monthsList.add("April");
        monthsList.add("May");
        monthsList.add("June");
        monthsList.add("July");
        monthsList.add("August");
        monthsList.add("September");
        monthsList.add("October");
        monthsList.add("November");
        monthsList.add("December");

        Spinner dateSpinner = (Spinner) findViewById(R.id.dateSpinner);
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(billOfMaterialActivity.this, R.layout.support_simple_spinner_dropdown_item, monthsList);
        dateSpinner.setAdapter(dateAdapter);

        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(billOfMaterialActivity.this, monthsList.get(position), Toast.LENGTH_SHORT).show();
                selectedDate = monthsList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void addProjects(View view){
        // Hide the buttons & ListView
        Button addItemsToBOMButton = (Button) findViewById(R.id.addItemsToBOMButton);
        addItemsToBOMButton.setVisibility(View.INVISIBLE);

        Button addProjectsToBOMButton = (Button) findViewById(R.id.addProjectsToBOMButton);
        addProjectsToBOMButton.setVisibility(View.INVISIBLE);

        ListView addItemsToBOMListView = (ListView) findViewById(R.id.addItemsToBOMListView);
        addItemsToBOMListView.setVisibility(View.INVISIBLE);

        // Show the listView
        ListView addProjectsToBOMListView = (ListView) findViewById(R.id.addProjectsToBOMListView);
        addProjectsToBOMListView.setVisibility(View.VISIBLE);

        showProjectsToSelectFrom();
    }

    public void showProjectsToSelectFrom() {
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

                    ArrayAdapter<project> projectsListAdapter = new projectsAdapter(billOfMaterialActivity.this, projectList);
                    // Connect list and adapter
                    ListView addProjectsToBOMListView = (ListView) findViewById(R.id.addProjectsToBOMListView);
                    addProjectsToBOMListView.setAdapter(projectsListAdapter);

//                    final ArrayList<Integer> selectedProjects = new ArrayList<Integer>();

                    addProjectsToBOMListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                    addProjectsToBOMListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                        @Override
                        public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
                            if (checked) {
                                selectedProjects.add(projectList.get(position).getProjectID());
                            }else{
                                int indexToRemove = selectedProjects.indexOf(projectList.get(position).getProjectID());
                                selectedProjects.remove(indexToRemove);
                            }
                            actionMode.setTitle(selectedProjects.size() + " projects selected.");
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
                                    addProjectsButtonPressed = true;
                                    getDesignBack();
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
        conn.execute(conn.URL + "/getProjects");
    }

    public void addItems(View view){
        // Hide the buttons & ListView
        Button addItemsToBOMButton = (Button) findViewById(R.id.addItemsToBOMButton);
        addItemsToBOMButton.setVisibility(View.INVISIBLE);

        Button addProjectsToBOMButton = (Button) findViewById(R.id.addProjectsToBOMButton);
        addProjectsToBOMButton.setVisibility(View.INVISIBLE);

        ListView addProjectsToBOMListView = (ListView) findViewById(R.id.addProjectsToBOMListView);
        addProjectsToBOMListView.setVisibility(View.INVISIBLE);

        // Show the listView
        ListView addItemsToBOMListView = (ListView) findViewById(R.id.addItemsToBOMListView);
        addItemsToBOMListView.setVisibility(View.VISIBLE);

        showItemsToSelectFrom();
    }

    public void showItemsToSelectFrom() {
        final ArrayList<item> itemList = new ArrayList<item>();
        HashMap<String, String> params = new HashMap<String, String>();
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    final JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); ++i) {
                        JSONObject data = reader.getJSONObject(i);

                        item itemObject = new item();

                        itemObject.itemID = data.getInt("itemID");
                        itemObject.itemEvoCode = data.getString("itemEvoCode");
                        itemObject.itemShortDescription = data.getString("itemShortDescription");
                        itemObject.itemQuantity = data.getInt("itemQuantity");

                        itemList.add(itemObject);
                    }

                    ArrayAdapter<item> itemListAdapter = new itemAdapter(billOfMaterialActivity.this, itemList);
                    // Connect list and adapter
                    ListView addItemsToBOMListView = (ListView) findViewById(R.id.addItemsToBOMListView);
                    addItemsToBOMListView.setAdapter(itemListAdapter);

//                    final ArrayList<Integer> selectedItems = new ArrayList<Integer>();

                    addItemsToBOMListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                    addItemsToBOMListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                        @Override
                        public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
                            if (checked) {
                                selectedItems.add(itemList.get(position).getItemID());
                            }else{
                                int indexToRemove = selectedItems.indexOf(itemList.get(position).getItemID());
                                selectedItems.remove(indexToRemove);
                            }
                            actionMode.setTitle(selectedItems.size() + " items selected.");
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
                                    addItemsButtonPressed = true;
                                    getDesignBack();
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
        conn.execute(conn.URL + "/getItems");
    }

    public void getDesignBack(){
        // Hide the ListViews
        ListView addItemsToBOMListView = (ListView) findViewById(R.id.addItemsToBOMListView);
        addItemsToBOMListView.setVisibility(View.INVISIBLE);

        ListView addProjectsToBOMListView = (ListView) findViewById(R.id.addProjectsToBOMListView);
        addProjectsToBOMListView.setVisibility(View.INVISIBLE);

        // Show the Buttons
        Button addItemsToBOMButton = (Button) findViewById(R.id.addItemsToBOMButton);
        addItemsToBOMButton.setVisibility(View.VISIBLE);

        Button addProjectsToBOMButton = (Button) findViewById(R.id.addProjectsToBOMButton);
        addProjectsToBOMButton.setVisibility(View.VISIBLE);
    }

    public void addProjects(ArrayList<Integer> selectedProjects, int BOMID){
        for(int i=0; i<selectedProjects.size(); ++i){
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("projectID", Integer.toString(selectedProjects.get(i)));
            params.put("BOMID", Integer.toString(BOMID));

            Connection conn = new Connection(params, new ConnectionPostListener() {
                @Override
                public void doSomething(String result) {
                }
            });
            conn.execute(conn.URL + "/addProjectsToBOM");
        }
    }

    public void addItems(ArrayList<Integer> selectedItems, int BOMID){
        for(int i=0; i<selectedItems.size(); ++i){
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("itemID", Integer.toString(selectedItems.get(i)));
            params.put("BOMID", Integer.toString(BOMID));

            Connection conn = new Connection(params, new ConnectionPostListener() {
                @Override
                public void doSomething(String result) {
                }
            });
            conn.execute(conn.URL + "/addItemsToBOM");
        }
    }
}
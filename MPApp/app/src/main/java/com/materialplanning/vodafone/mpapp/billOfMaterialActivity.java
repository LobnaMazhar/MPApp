package com.materialplanning.vodafone.mpapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class billOfMaterialActivity extends AppCompatActivity {

    int selectedDate = -1;
    int selectedRegionID = 0;
    int selectedProjectID = 0;
    int selectedScenarioID = 0;
    int selectedVendorID = 0;

    boolean siteExists, availableStock, siteAndProjectExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_of_material);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add BOM");
        setSupportActionBar(toolbar);

        prepareMonths();
        prepareRegions();
        prepareProjects();
    //    prepareScenarios(); // called after choosing the project
        prepareVendors();
    }

    public void saveBOM(View view){
        EditText siteIDEditText = (EditText) findViewById(R.id.siteIDEditText);
        final String siteID = siteIDEditText.getText().toString();
        if(siteID.isEmpty()){
            Toast.makeText(billOfMaterialActivity.this, "Enter siteID first", Toast.LENGTH_LONG).show();
        }else if(selectedDate == -1){
            Toast.makeText(billOfMaterialActivity.this, "Select date", Toast.LENGTH_LONG).show();
        }else if(selectedRegionID == 0) {
            Toast.makeText(billOfMaterialActivity.this, "Select region", Toast.LENGTH_LONG).show();
        }else if(selectedProjectID == 0){
            Toast.makeText(billOfMaterialActivity.this, "Select project", Toast.LENGTH_LONG).show();
        }else if(selectedScenarioID == 0){
            Toast.makeText(billOfMaterialActivity.this, "Select scenario", Toast.LENGTH_LONG).show();
        }else if(selectedVendorID == 0){
            Toast.makeText(billOfMaterialActivity.this, "Select vendor", Toast.LENGTH_LONG).show();
        }else{
            final HashMap<String, String> params = new HashMap<String, String>();
            params.put("siteID", siteID);
            params.put("date", Integer.toString(selectedDate));
            params.put("regionID", Integer.toString(selectedRegionID));
            params.put("projectID", Integer.toString(selectedProjectID));
            params.put("scenarioID", Integer.toString(selectedScenarioID));
            params.put("vendorID", Integer.toString(selectedVendorID));

            if (siteExists(siteID)) {
                params.put("Rollout/Expansion", "2");

                HashMap<String, String> params2 = new HashMap<>();
                params2.put("siteID", siteID);
                Connection conn2 = new Connection(params2, new ConnectionPostListener() {
                    @Override
                    public void doSomething(String result) {
                        try{
                            JSONObject reader = new JSONObject(result);
                            if(reader.getInt("regionID") != selectedRegionID){
                                TextView regionErrorTextView = (TextView) findViewById(R.id.regionErrorTextView);
                                regionErrorTextView.setText("Expansion site, choose the correct region");
                            }else if(reader.getInt("vendorID") != selectedVendorID){
                                TextView vendorErrorTextView = (TextView) findViewById(R.id.vendorErrorTextView);
                                vendorErrorTextView.setText("Expansion site, choose the correct vendor");
                            }else if(siteAndProjectExists(siteID, selectedProjectID)){
                                TextView projectErrorTextView = (TextView) findViewById(R.id.projectErrorTextView);
                                projectErrorTextView.setText("Expansion site, can't expand for the same project");
                            }else if(!availableStock(selectedScenarioID)){
                                TextView scenarioErrorTextView = (TextView) findViewById(R.id.scenarioErrorTextView);
                                scenarioErrorTextView.setText("No enough stock to release using this scenario");
                            }else{
                                Connection conn = new Connection(params, new ConnectionPostListener() {
                                    @Override
                                    public void doSomething(String result) {
                                        try {
                                            JSONObject reader = new JSONObject(result);
                                            if (reader.getBoolean("added")) {
                                                try{
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(billOfMaterialActivity.this);
                                                    builder.setTitle("Release site");
                                                    builder.setMessage("Site is expanded successfully, send to the vendor to release");

                                                    // Set up the buttons
                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            finish();
                                                        }
                                                    });
                                                    builder.show();
                                                }catch (Exception e) {
                                                }
                                            }
                                        } catch (JSONException e) {
                                        }
                                    }
                                });
                                conn.execute(conn.URL + "/addProjectToSite");
                            }
                        }catch (JSONException e){
                        }
                    }
                });
                conn2.execute(conn2.URL + "/getSiteByID");
            } else {
                params.put("Rollout/Expansion", "1");

                Connection conn = new Connection(params, new ConnectionPostListener() {
                    @Override
                    public void doSomething(String result) {
                        try {
                            JSONObject reader = new JSONObject(result);
                            if (reader.getBoolean("added")) {
                               try{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(billOfMaterialActivity.this);
                                    builder.setTitle("Release site");
                                    builder.setMessage("Site is added successfully, send to the vendor to release");

                                    // Set up the buttons
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                                    builder.show();
                                }catch (Exception e) {
                               }
                            }
                        } catch (JSONException e) {
                        }
                    }
                });
                conn.execute(conn.URL + "/addSite");
            }
        }
    }

    public void prepareMonths(){
        final ArrayList<String> monthsList = new ArrayList<String>();
        monthsList.add("");
        monthsList.add("April");
        monthsList.add("May");
        monthsList.add("June");
        monthsList.add("July");
        monthsList.add("August");
        monthsList.add("September");
        monthsList.add("October");
        monthsList.add("November");
        monthsList.add("December");
        monthsList.add("January");
        monthsList.add("February");
        monthsList.add("March");

        Spinner dateSpinner = (Spinner) findViewById(R.id.dateSpinner);
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(billOfMaterialActivity.this, R.layout.support_simple_spinner_dropdown_item, monthsList);
        dateSpinner.setAdapter(dateAdapter);

        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String monthName = Integer.toString(position) + " " + monthsList.get(position);
                if(!monthName.equals("")) {
                    Toast.makeText(billOfMaterialActivity.this, monthName, Toast.LENGTH_SHORT).show();
                    selectedDate = (position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void prepareRegions(){
        final ArrayList<region> regionsList = new ArrayList<region>();
        region regionObject = new region();
        regionObject.regionName = "";
        regionsList.add(regionObject);

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

                Spinner regionSpinner = (Spinner) findViewById(R.id.regionSpinner);
                regionSpinnerAdapter regionAdapter = new regionSpinnerAdapter(billOfMaterialActivity.this, regionsList);
                regionSpinner.setAdapter(regionAdapter);

                regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        TextView regionErrorTextView = (TextView) findViewById(R.id.regionErrorTextView);
                        regionErrorTextView.setText("");

                        String regionName = regionsList.get(position).getRegionName();
                        if(!regionName.equals("")) {
                            Toast.makeText(billOfMaterialActivity.this, regionName, Toast.LENGTH_SHORT).show();
                            selectedRegionID = regionsList.get(position).getRegionID();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });
        conn.execute(conn.URL + "/getRegions");
    }

    public void prepareProjects(){
        final ArrayList<project> projectsList = new ArrayList<project>();
        project projectObject = new project();
        projectObject.projectName = "";
        projectsList.add(projectObject);

        HashMap<String, String> params = new HashMap<String, String>();
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONArray reader = new JSONArray(result);
                    for(int i=0; i<reader.length(); ++i){
                        JSONObject data = reader.getJSONObject(i);

                        project projectObject = new project();
                        projectObject.projectID = data.getInt("projectID");
                        projectObject.projectName = data.getString("projectName");
                        projectsList.add(projectObject);
                    }
                }catch (JSONException e){
                }

                Spinner projectSpinner = (Spinner) findViewById(R.id.projectSpinner);
                projectSpinnerAdapter projectSpinnerAdapter = new projectSpinnerAdapter(billOfMaterialActivity.this, projectsList);
                projectSpinner.setAdapter(projectSpinnerAdapter);

                projectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        TextView projectErrorTextView = (TextView) findViewById(R.id.projectErrorTextView);
                        projectErrorTextView.setText("");

                        String projectName = projectsList.get(position).getProjectName();
                        if(!projectName.equals("")) {
                            Toast.makeText(billOfMaterialActivity.this, projectName, Toast.LENGTH_SHORT).show();
                            selectedProjectID = projectsList.get(position).getProjectID();
                            prepareScenarios(selectedProjectID);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });
        conn.execute(conn.URL + "/getProjects");
    }

    public void prepareScenarios(int selectedProjectID){
        final ArrayList<scenario> scenariosList = new ArrayList<scenario>();
        scenario scenarioObject = new scenario();
        scenarioObject.scenarioNumber = 0;
        scenariosList.add(scenarioObject);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("projectID", Integer.toString(selectedProjectID));
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONArray reader = new JSONArray(result);
                    for(int i=0; i<reader.length(); ++i){
                        JSONObject data = reader.getJSONObject(i);

                        scenario scenarioObject = new scenario();
                        scenarioObject.scenarioID = data.getInt("scenarioID");
                        scenarioObject.scenarioNumber = data.getInt("scenarioNumber");
                        scenariosList.add(scenarioObject);
                    }
                }catch (JSONException e){
                }

                Spinner scenarioSpinner = (Spinner) findViewById(R.id.scenarioSpinner);
                scenarioSpinnerAdapter scenarioAdapter = new scenarioSpinnerAdapter(billOfMaterialActivity.this, scenariosList);
                scenarioSpinner.setAdapter(scenarioAdapter);

                scenarioSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        TextView scenarioErrorTextView = (TextView) findViewById(R.id.scenarioErrorTextView);
                        scenarioErrorTextView.setText("");

                        int scenarioNumber = scenariosList.get(position).getScenarioNumber();
                        if(scenarioNumber != 0) {
                            Toast.makeText(billOfMaterialActivity.this, Integer.toString(scenarioNumber), Toast.LENGTH_SHORT).show();
                            selectedScenarioID = scenariosList.get(position).getScenarioID();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });
        conn.execute(conn.URL + "/getScenariosByProjectID");
    }

    public void prepareVendors(){
        final ArrayList<vendor> vendorsList = new ArrayList<vendor>();
        vendor vendorObject = new vendor();
        vendorObject.vendorName = "";
        vendorsList.add(vendorObject);

        HashMap<String, String> params = new HashMap<String, String>();
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONArray reader = new JSONArray(result);
                    for(int i=0; i<reader.length(); ++i){
                        JSONObject data = reader.getJSONObject(i);

                        vendor vendorObject = new vendor();
                        vendorObject.vendorID = data.getInt("vendorID");
                        vendorObject.vendorName = data.getString("vendorName");
                        vendorsList.add(vendorObject);
                    }
                }catch (JSONException e){
                }

                Spinner vendorSpinner = (Spinner) findViewById(R.id.vendorSpinner);
                vendorSpinnerAdapter vendorSpinnerAdapter = new vendorSpinnerAdapter(billOfMaterialActivity.this, vendorsList);
                vendorSpinner.setAdapter(vendorSpinnerAdapter);

                vendorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        TextView vendorErrorTextView = (TextView) findViewById(R.id.vendorErrorTextView);
                        vendorErrorTextView.setText("");

                        String vendorName = vendorsList.get(position).getVendorName();
                        if(!vendorName.equals("")) {
                            Toast.makeText(billOfMaterialActivity.this, vendorName, Toast.LENGTH_SHORT).show();
                            selectedVendorID = vendorsList.get(position).getVendorID();
                            prepareScenarios(selectedVendorID);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });
        conn.execute(conn.URL + "/getVendors");
    }

    public boolean siteExists(String siteID){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("siteID", siteID);

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    siteExists = reader.getBoolean("exists");
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/checkIfSiteExists");

        return siteExists;
    }

    public boolean availableStock(int selectedScenarioID){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("scenarioID", Integer.toString(selectedScenarioID));

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    availableStock = reader.getBoolean("available");
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/isAvailableStock");

        return availableStock;
    }

    public boolean siteAndProjectExists(String siteID, int selectedProjectID){
        HashMap<String, String> params = new HashMap<>();
        params.put("siteID", siteID);
        params.put("projectID", Integer.toString(selectedProjectID));

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    siteAndProjectExists = reader.getBoolean("exists");
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/checkSiteAndProjectExists");

     return siteAndProjectExists;
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
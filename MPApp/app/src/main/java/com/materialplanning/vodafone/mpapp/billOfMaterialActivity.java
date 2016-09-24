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
    int enoughPhasingMonthID = 0;

    boolean siteExists, availableStock, siteAndProjectExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_of_material);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add BOM");
        setSupportActionBar(toolbar);

        prepareMonths();
//        prepareRegions(); // called after choosing the project
        prepareProjects();
    //    prepareScenarios(); // called after choosing the project
 //       prepareVendors(); // called after choosing the project and the region
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
                            prepareRegions(selectedProjectID);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });
        conn.execute(conn.URL + "/getProjectsInPRVMs");
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

    public void prepareRegions(final int selectedProjectID){
        final ArrayList<region> regionsList = new ArrayList<region>();
        region regionObject = new region();
        regionObject.regionName = "";
        regionsList.add(regionObject);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("projectID", Integer.toString(selectedProjectID));
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
                            prepareVendors(selectedProjectID, selectedRegionID);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });
        conn.execute(conn.URL + "/getRegionsWithProject");
    }

    public void prepareVendors(int selectedProjectID, int selectedRegionID){
        final ArrayList<vendor> vendorsList = new ArrayList<vendor>();
        vendor vendorObject = new vendor();
        vendorObject.vendorName = "";
        vendorsList.add(vendorObject);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("projectID", Integer.toString(selectedProjectID));
        params.put("regionID", Integer.toString(selectedRegionID));
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
                          //  prepareScenarios(selectedVendorID);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });
        conn.execute(conn.URL + "/getVendorsWithProjectAndRegion");
    }

    public void saveBOM(View view){
        EditText siteIDEditText = (EditText) findViewById(R.id.siteIDEditText);
        String siteID = siteIDEditText.getText().toString();

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
        }else {
            checkEnoughPhasing(selectedProjectID, selectedRegionID, selectedVendorID, selectedDate);
        }
    }

    public void checkEnoughPhasing(int selectedProjectID, int selectedRegionID, int selectedVendorID, int selectedDate){
    //    Toast.makeText(billOfMaterialActivity.this, "Check if there's enough phasing...", Toast.LENGTH_SHORT).show();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("projectID", Integer.toString(selectedProjectID));
        params.put("regionID", Integer.toString(selectedRegionID));
        params.put("vendorID", Integer.toString(selectedVendorID));
        params.put("monthID", Integer.toString(selectedDate));

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    enoughPhasingMonthID = reader.getInt("monthID");
                    if(enoughPhasingMonthID != 0){
                        availableStock(selectedScenarioID);
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(billOfMaterialActivity.this);
                        builder.setTitle("Phasing");
                        builder.setMessage("No more phasing");

                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();
                        return;
                    }
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/checkEnoughPhasing");
    }

    public boolean availableStock(int selectedScenarioID){
   //     Toast.makeText(billOfMaterialActivity.this, "Check if stock is available...", Toast.LENGTH_SHORT).show();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("scenarioID", Integer.toString(selectedScenarioID));

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    availableStock = reader.getBoolean("available");
                    if(availableStock)
                        releaseSite();
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(billOfMaterialActivity.this);
                        builder.setTitle("Stock");
                        builder.setMessage("No stock for releasing");

                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();
                        return;
                    }
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/isAvailableStock");

        return availableStock;
    }

    public void releaseSite() {
        EditText siteIDEditText = (EditText) findViewById(R.id.siteIDEditText);
        final String siteID = siteIDEditText.getText().toString();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("siteID", siteID);
        params.put("date", Integer.toString(selectedDate));
        params.put("regionID", Integer.toString(selectedRegionID));
        params.put("projectID", Integer.toString(selectedProjectID));
        params.put("scenarioID", Integer.toString(selectedScenarioID));
        params.put("vendorID", Integer.toString(selectedVendorID));

        siteExists(siteID, params);
    }

    public boolean siteExists(String siteID, final HashMap<String, String> Rparams){
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("siteID", siteID);

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    siteExists = reader.getBoolean("exists");
                    if(siteExists)
                        checkForExpansion(Rparams);
                    else
                        rollout(Rparams);
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/checkIfSiteExists");

        return siteExists;
    }

    public void rollout (HashMap<String, String> params){
        if(selectedProjectID != 57){ // New site // TODO a3'yr l ID ,,,, da tb3n kalam mo2Qt 3shan l ID msh hyb2a sabt fl DB 3shan byt3'yr tb3 l TP kol sana
            AlertDialog.Builder builder = new AlertDialog.Builder(billOfMaterialActivity.this);
            builder.setTitle("New site");
            builder.setMessage("Can't release a new site with this project.");

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.show();
        }else{
            params.put("Rollout/Expansion", "1");

            Connection conn = new Connection(params, new ConnectionPostListener() {
                @Override
                public void doSomething(String result) {
                    try {
                        JSONObject reader = new JSONObject(result);
                        if (reader.getBoolean("added")) {
                            try {
                                updateItemsStockByScenario(selectedScenarioID);
                                updatePhasing(selectedProjectID, selectedRegionID, selectedVendorID, enoughPhasingMonthID);

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
                            } catch (Exception e) {
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            });
            conn.execute(conn.URL + "/addSite");
        }
    }

    public void checkForExpansion(final HashMap<String, String> params){
        EditText siteIDEditText = (EditText) findViewById(R.id.siteIDEditText);
        final String siteID = siteIDEditText.getText().toString();

        params.put("Rollout/Expansion", "2");

        HashMap<String, String> params2 = new HashMap<>();
        params2.put("siteID", siteID);
        Connection conn2 = new Connection(params2, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    JSONObject reader = new JSONObject(result);
                    if (reader.getInt("regionID") != selectedRegionID) {
                        TextView regionErrorTextView = (TextView) findViewById(R.id.regionErrorTextView);
                        regionErrorTextView.setText("Expansion site, choose the correct region");
                    } else if (reader.getInt("vendorID") != selectedVendorID) {
                        TextView vendorErrorTextView = (TextView) findViewById(R.id.vendorErrorTextView);
                        vendorErrorTextView.setText("Expansion site, choose the correct vendor");
                    } else{
                        siteAndProjectExists(siteID, selectedProjectID, params);
                    }
                } catch (JSONException e) {
                }
            }
        });
        conn2.execute(conn2.URL + "/getSiteByID");
    }

    public boolean siteAndProjectExists(String siteID, int selectedProjectID, final HashMap<String, String> Rparams){
        HashMap<String, String> params = new HashMap<>();
        params.put("siteID", siteID);
        params.put("projectID", Integer.toString(selectedProjectID));

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    siteAndProjectExists = reader.getBoolean("exists");
                    if(siteAndProjectExists){
                        TextView projectErrorTextView = (TextView) findViewById(R.id.projectErrorTextView);
                        projectErrorTextView.setText("Expansion site, can't expand for the same project");
                    }else{
                        expansion(Rparams);
                    }
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/checkSiteAndProjectExists");

     return siteAndProjectExists;
    }

    public void expansion(HashMap<String, String> params){
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    JSONObject reader = new JSONObject(result);
                    if (reader.getBoolean("added")) {
                        try {
                            updateItemsStockByScenario(selectedScenarioID);
                            updatePhasing(selectedProjectID, selectedRegionID, selectedVendorID, enoughPhasingMonthID);

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
                        } catch (Exception e) {
                        }
                    }
                } catch (JSONException e) {
                }
            }
        });
        conn.execute(conn.URL + "/addProjectToSite");
    }

    public void updateItemsStockByScenario(int selectedScenarioID){
        HashMap<String, String> params = new HashMap<>();
        params.put("scenarioID", Integer.toString(selectedScenarioID));
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    if(reader.getBoolean("updated"))
                        Toast.makeText(billOfMaterialActivity.this, "Items list is updated", Toast.LENGTH_SHORT).show();
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/updateItemsStockByScenario");
    }

    public void updatePhasing(int selectedProjectID, int selectedRegionID, int selectedVendorID, int enoughPhasingMonthID){
        HashMap<String, String> params = new HashMap<>();
        params.put("projectID", Integer.toString(selectedProjectID));
        params.put("regionID", Integer.toString(selectedRegionID));
        params.put("vendorID", Integer.toString(selectedVendorID));
        params.put("monthID", Integer.toString(enoughPhasingMonthID));

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    if(reader.getBoolean("updated"))
                        Toast.makeText(billOfMaterialActivity.this, "Month phasing is updated", Toast.LENGTH_SHORT).show();
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/updatePhasing");
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
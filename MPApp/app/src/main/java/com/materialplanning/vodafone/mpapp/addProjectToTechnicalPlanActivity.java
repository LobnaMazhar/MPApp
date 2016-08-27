package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class addProjectToTechnicalPlanActivity extends AppCompatActivity {

    int projectID;
    int selectedRegionID = 0;
    int selectedVendorID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_to_technical_plan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add project to technical plan");
        setSupportActionBar(toolbar);

        prepareRegions();
        prepareVendors();
    }

    public void saveProjectInTechnicalPlan(View view){
        EditText projectNameEditText = (EditText) findViewById(R.id.projectNameEditText);
        String projectName = projectNameEditText.getText().toString();

        EditText yearTargetEditText = (EditText) findViewById(R.id.yearTargetEditText);
        String yearTarget = yearTargetEditText.getText().toString();

        if(projectName.equals("")){
            Toast.makeText(addProjectToTechnicalPlanActivity.this, "Enter project name", Toast.LENGTH_LONG).show();
            return;
        }else if(selectedRegionID == 0){
            Toast.makeText(addProjectToTechnicalPlanActivity.this, "Select region", Toast.LENGTH_LONG).show();
            return;
        }else if(selectedVendorID == 0){
            Toast.makeText(addProjectToTechnicalPlanActivity.this, "Select vendor", Toast.LENGTH_LONG).show();
            return;
        }else{
            getProjectID(projectName);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("projectID",Integer.toString(projectID));
            params.put("regionID",Integer.toString(selectedRegionID));
            params.put("vendorID",Integer.toString(selectedVendorID));
            params.put("yearTarget",yearTarget);

            Connection conn = new Connection(params, new ConnectionPostListener() {
                @Override
                public void doSomething(String result) {
                    try{
                        JSONObject reader = new JSONObject(result);
                        if(reader.getBoolean("added")) {
                            Toast.makeText(addProjectToTechnicalPlanActivity.this, "Project added", Toast.LENGTH_SHORT).show();

                            Intent goToTechnicalPlans = new Intent(addProjectToTechnicalPlanActivity.this, technicalPlansActivity.class);
                            startActivity(goToTechnicalPlans);
                        }
                    }catch (JSONException e){
                        Toast.makeText(addProjectToTechnicalPlanActivity.this, "Project name doesn't exist !!", Toast.LENGTH_LONG).show();
                    }
                }
            });
            conn.execute(conn.URL + "/addProjectToTechnicalPlan");
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
                regionAdapter regionAdapter = new regionAdapter(addProjectToTechnicalPlanActivity.this, regionsList);
                regionSpinner.setAdapter(regionAdapter);

                regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        Toast.makeText(addProjectToTechnicalPlanActivity.this, regionsList.get(position).getRegionName(), Toast.LENGTH_SHORT).show();
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

    public void prepareVendors(){
        final ArrayList<vendor> vendorsList = new ArrayList<vendor>();
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
                vendorAdapter vendorAdapter = new vendorAdapter(addProjectToTechnicalPlanActivity.this, vendorsList);
                vendorSpinner.setAdapter(vendorAdapter);

                vendorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        Toast.makeText(addProjectToTechnicalPlanActivity.this, vendorsList.get(position).getVendorName(), Toast.LENGTH_SHORT).show();
                        selectedVendorID = vendorsList.get(position).getVendorID();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });
        conn.execute(conn.URL + "/getVendors");
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
}

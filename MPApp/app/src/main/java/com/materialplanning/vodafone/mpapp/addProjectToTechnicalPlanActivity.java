package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

    int selectedProjectID = 0;
    int selectedRegionID = 0;
    int selectedVendorID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_to_technical_plan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add project to technical plan");
        setSupportActionBar(toolbar);

        prepareProjects();
        prepareRegions();
        prepareVendors();
    }

    public void saveProjectInTechnicalPlan(View view){

        EditText yearTargetEditText = (EditText) findViewById(R.id.yearTargetEditText);
        String yearTarget = yearTargetEditText.getText().toString();

        if(selectedProjectID == 0){
            Toast.makeText(addProjectToTechnicalPlanActivity.this, "Select project", Toast.LENGTH_LONG).show();
            return;
        }else if(selectedRegionID == 0){
            Toast.makeText(addProjectToTechnicalPlanActivity.this, "Select region", Toast.LENGTH_LONG).show();
            return;
        }else if(selectedVendorID == 0){
            Toast.makeText(addProjectToTechnicalPlanActivity.this, "Select vendor", Toast.LENGTH_LONG).show();
            return;
        }else{
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("projectID",Integer.toString(selectedProjectID));
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
                    }
                }
            });
            conn.execute(conn.URL + "/addProjectToTechnicalPlan");
        }
    }

    public void prepareProjects(){
        final ArrayList<project> projectsList = new ArrayList<project>();
        project projectObject = new project();
        //  projectObject.projectID = Integer.parseInt("");
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
                projectSpinnerAdapter projectAdapter = new projectSpinnerAdapter(addProjectToTechnicalPlanActivity.this, projectsList);
                projectSpinner.setAdapter(projectAdapter);

                projectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        String projectName = projectsList.get(position).getProjectName();
                        if(!projectName.equals("")) {
                            Toast.makeText(addProjectToTechnicalPlanActivity.this, projectName, Toast.LENGTH_SHORT).show();
                            selectedProjectID = projectsList.get(position).getProjectID();
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

    public void prepareRegions(){
        final ArrayList<region> regionsList = new ArrayList<region>();
        region regionObject = new region();
      //  regionObject.regionID = Integer.parseInt("");
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
                regionSpinnerAdapter regionAdapter = new regionSpinnerAdapter(addProjectToTechnicalPlanActivity.this, regionsList);
                regionSpinner.setAdapter(regionAdapter);

                regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        String regionName = regionsList.get(position).getRegionName();
                        if(!regionName.equals("")) {
                            Toast.makeText(addProjectToTechnicalPlanActivity.this, regionName, Toast.LENGTH_SHORT).show();
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

    public void prepareVendors(){
        final ArrayList<vendor> vendorsList = new ArrayList<vendor>();
        vendor vendorObject = new vendor();
 //       vendorObject.vendorID = Integer.parseInt("");
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
                vendorSpinnerAdapter vendorAdapter = new vendorSpinnerAdapter(addProjectToTechnicalPlanActivity.this, vendorsList);
                vendorSpinner.setAdapter(vendorAdapter);

                vendorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        String vendorName = vendorsList.get(position).getVendorName();
                        if(!vendorName.equals("")){
                            Toast.makeText(addProjectToTechnicalPlanActivity.this, vendorName, Toast.LENGTH_SHORT).show();
                            selectedVendorID = vendorsList.get(position).getVendorID();
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

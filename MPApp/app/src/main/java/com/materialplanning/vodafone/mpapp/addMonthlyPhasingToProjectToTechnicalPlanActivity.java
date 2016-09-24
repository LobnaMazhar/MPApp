package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class addMonthlyPhasingToProjectToTechnicalPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monthly_phasing_to_project_to_technical_plan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Setup monthly phasing");
        setSupportActionBar(toolbar);
    }

    public void savePRVM(View view){
        ArrayList<String> monthlyPhasingList = new ArrayList<>();
        int totalMonthlyPhasing = 0;

        EditText aprilPhaseEditText = (EditText) findViewById(R.id.aprilPhaseEditText);
        String aprilPhase = aprilPhaseEditText.getText().toString();
        monthlyPhasingList.add(aprilPhase);
        totalMonthlyPhasing += Integer.parseInt(aprilPhase);

        EditText mayPhaseEditText = (EditText) findViewById(R.id.mayPhaseEditText);
        String mayPhase = mayPhaseEditText.getText().toString();
        monthlyPhasingList.add(mayPhase);
        totalMonthlyPhasing += Integer.parseInt(mayPhase);

        EditText junePhaseEditText = (EditText) findViewById(R.id.junePhaseEditText);
        String junePhase = junePhaseEditText.getText().toString();
        monthlyPhasingList.add(junePhase);
        totalMonthlyPhasing += Integer.parseInt(junePhase);

        EditText julyPhaseEditText = (EditText) findViewById(R.id.julyPhaseEditText);
        String julyPhase = julyPhaseEditText.getText().toString();
        monthlyPhasingList.add(julyPhase);
        totalMonthlyPhasing += Integer.parseInt(julyPhase);

        EditText augustPhaseEditText = (EditText) findViewById(R.id.augustPhaseEditText);
        String augustPhase = augustPhaseEditText.getText().toString();
        monthlyPhasingList.add(augustPhase);
        totalMonthlyPhasing += Integer.parseInt(augustPhase);

        EditText septemberPhaseEditText = (EditText) findViewById(R.id.septemberPhaseEditText);
        String septemberPhase = septemberPhaseEditText.getText().toString();
        monthlyPhasingList.add(septemberPhase);
        totalMonthlyPhasing += Integer.parseInt(septemberPhase);

        EditText octoberPhaseEditText = (EditText) findViewById(R.id.octoberPhaseEditText);
        String octoberPhase = octoberPhaseEditText.getText().toString();
        monthlyPhasingList.add(octoberPhase);
        totalMonthlyPhasing += Integer.parseInt(octoberPhase);

        EditText novemberPhaseEditText = (EditText) findViewById(R.id.novemberPhaseEditText);
        String novemberPhase = novemberPhaseEditText.getText().toString();
        monthlyPhasingList.add(novemberPhase);
        totalMonthlyPhasing += Integer.parseInt(novemberPhase);

        EditText decemberPhaseEditText = (EditText) findViewById(R.id.decemberPhaseEditText);
        String decemberPhase = decemberPhaseEditText.getText().toString();
        monthlyPhasingList.add(decemberPhase);
        totalMonthlyPhasing += Integer.parseInt(decemberPhase);

        EditText januaryPhaseEditText = (EditText) findViewById(R.id.januaryPhaseEditText);
        String januaryPhase = januaryPhaseEditText.getText().toString();
        monthlyPhasingList.add(januaryPhase);
        totalMonthlyPhasing += Integer.parseInt(januaryPhase);

        EditText februaryPhaseEditText = (EditText) findViewById(R.id.februaryPhaseEditText);
        String februaryPhase = februaryPhaseEditText.getText().toString();
        monthlyPhasingList.add(februaryPhase);
        totalMonthlyPhasing += Integer.parseInt(februaryPhase);

        EditText marchPhaseEditText = (EditText) findViewById(R.id.marchPhaseEditText);
        String marchPhase = marchPhaseEditText.getText().toString();
        monthlyPhasingList.add(marchPhase);
        totalMonthlyPhasing += Integer.parseInt(marchPhase);


        String yearTarget = getIntent().getExtras().getString("yearTarget");
       // Toast.makeText(addMonthlyPhasingToProjectToTechnicalPlanActivity.this, "Checking conditions ...", Toast.LENGTH_LONG).show();
        if(totalMonthlyPhasing > Integer.parseInt(yearTarget)){
            Toast.makeText(addMonthlyPhasingToProjectToTechnicalPlanActivity.this, "ERROR total monthly phasing exceeds year target.", Toast.LENGTH_LONG).show();
            return;
        }else if(totalMonthlyPhasing < Integer.parseInt(yearTarget)){
            Toast.makeText(addMonthlyPhasingToProjectToTechnicalPlanActivity.this, "ERROR total monthly phasing is less than year target.", Toast.LENGTH_LONG).show();
            return;
        }else {
            for (int i = 0; i < monthlyPhasingList.size(); ++i) {
                HashMap<String, String> params = new HashMap<>();
                params.put("projectID", Integer.toString(getIntent().getExtras().getInt("projectID")));
                params.put("regionID", Integer.toString(getIntent().getExtras().getInt("regionID")));
                params.put("vendorID", Integer.toString(getIntent().getExtras().getInt("vendorID")));
                params.put("yearTarget", yearTarget);
                params.put("monthID", Integer.toString((i + 1)));
                params.put("phasing", monthlyPhasingList.get(i));

                Connection conn = new Connection(params, new ConnectionPostListener() {
                    @Override
                    public void doSomething(String result) {
                        try {
                            JSONObject reader = new JSONObject(result);
                            if (reader.getBoolean("added")){
                            }
                        } catch (JSONException e) {
                        }
                    }
                });
                conn.execute(conn.URL + "/addPRVM");
            }
            Toast.makeText(addMonthlyPhasingToProjectToTechnicalPlanActivity.this, "Project was added successfully", Toast.LENGTH_SHORT).show();

            Intent goToTechnicalPlans = new Intent(addMonthlyPhasingToProjectToTechnicalPlanActivity.this, technicalPlansActivity.class);
            startActivity(goToTechnicalPlans);
            finish();
        }
    }

}

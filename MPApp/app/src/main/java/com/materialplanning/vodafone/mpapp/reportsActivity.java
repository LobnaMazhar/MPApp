package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class reportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Reports");
        setSupportActionBar(toolbar);

        getReports();
    }

    public void getReports(){
        final String[] reportsList = getResources().getStringArray(R.array.reports);

        ListView reportsListView = (ListView) findViewById(R.id.reportsListView);
        final ArrayAdapter<String> reportsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reportsList);
        reportsListView.setAdapter(reportsAdapter);

        // TODO on click
        reportsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(reportsList[position].equals("Technical Plan")){
                    Intent goToTechnicalPlan = new Intent(reportsActivity.this, technicalPlansActivity.class);
                    startActivity(goToTechnicalPlan);
                }else if(reportsList[position].equals("New Rollout")){
                    Intent goToRollout = new Intent(reportsActivity.this, siteActivity.class);
                    goToRollout.putExtra("rollout/expansion", "1");
                    startActivity(goToRollout);
                }else if(reportsList[position].equals("Expansions")){
                    Intent goToExpansions = new Intent(reportsActivity.this, siteActivity.class);
                    goToExpansions.putExtra("rollout/expansion", "2");
                    startActivity(goToExpansions);
                }else if(reportsList[position].equals("Monthly phasing per project")){
                    Intent goToMonthlyPhasingPerProject = new Intent(reportsActivity.this, monthlyPhasingPerProjectActivity.class);
                    startActivity(goToMonthlyPhasingPerProject);
                }
            }
        });
    }

    public void getBOM(View view){
        Intent goToBOM = new Intent(reportsActivity.this, billOfMaterialActivity.class);
        startActivity(goToBOM);
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

package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class technicalPlanViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_plan_view);

        TextView technicalPlanViewNameTextView = (TextView) findViewById(R.id.technicalPlanViewNameTextView);
        technicalPlanViewNameTextView.setText(getIntent().getExtras().getString("technicalPlanName"));

        getScenariosInTechnicalPlan();
    }

    public void getScenariosInTechnicalPlan(){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("technicalPlanID", Integer.toString(getIntent().getExtras().getInt("technicalPlanID")));

        final ArrayList<scenario> scenariosList = new ArrayList<scenario>();
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    final JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); ++i) {
                        JSONObject data = reader.getJSONObject(i);

                        scenario scenarioObject = new scenario();

                        scenarioObject.scenarioNumber = data.getInt("scenarioNumber");

                        scenariosList.add(scenarioObject);
                    }

                    ArrayAdapter<scenario> scenarioListAdapter = new scenarioAdapter(technicalPlanViewActivity.this, scenariosList);
                    // Connect list and adapter
                    ListView scenariosInTechnicalPlanListView = (ListView) findViewById(R.id.scenariosInTechnicalPlanListView);
                    scenariosInTechnicalPlanListView.setAdapter(scenarioListAdapter);


                    scenariosInTechnicalPlanListView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try{

                                        Intent goToScenario = new Intent(technicalPlanViewActivity.this, scenarioViewActivity.class);

                                        goToScenario.putExtra("scenarioID", reader.getJSONObject(position).getInt("scenarioID"));
                                        goToScenario.putExtra("scenarioNumber", reader.getJSONObject(position).getInt("scenarioNumber"));

                                        startActivity(goToScenario);
                                    }
                                    catch (JSONException e){}
                                }
                            }
                    );
                }catch (Exception e){
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/getScenariosInTechnicalPlan");
    }

}

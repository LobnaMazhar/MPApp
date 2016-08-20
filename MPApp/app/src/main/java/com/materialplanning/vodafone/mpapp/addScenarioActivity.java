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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class addScenarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scenario);
    }

    public void saveScenario(View view){
        HashMap<String, String> params = new HashMap<String, String>();

        final EditText addScenarioNumberEditText = (EditText) findViewById(R.id.addScenarioNumberEditText);
        String scenarioNumber = addScenarioNumberEditText.getText().toString();
        if(scenarioNumber.isEmpty()){
            Toast.makeText(addScenarioActivity.this, "Please specify the scenario number", Toast.LENGTH_LONG).show();
            return;
        }

        checkScenarioNumber(scenarioNumber);
        TextView errorNumberAlreadyExistsTextView = (TextView) findViewById(R.id.errorNumberAlreadyExistsTextView);
        if(errorNumberAlreadyExistsTextView.getText().equals("Scenario number already exists !!"))
            return;


        params.put("scenarioNumber", scenarioNumber);

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    JSONArray reader = new JSONArray(result);
                    if(reader.getBoolean(1))
                        Toast.makeText(addScenarioActivity.this, "A new scenario is added", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/addScenario");
    }

    public void addItemsToScenario (View view){
        EditText addScenarioNumberEditText = (EditText) findViewById(R.id.addScenarioNumberEditText);
        String scenarioNumber = addScenarioNumberEditText.getText().toString();
        if(scenarioNumber.isEmpty()){
            Toast.makeText(addScenarioActivity.this, "Please specify the scenario number first", Toast.LENGTH_LONG).show();
            return;
        }

        checkScenarioNumber(scenarioNumber);
        TextView errorNumberAlreadyExistsTextView = (TextView) findViewById(R.id.errorNumberAlreadyExistsTextView);
        if(errorNumberAlreadyExistsTextView.getText().equals("Scenario number already exists !!"))
            return;


        Intent goToaddItemsToScenario = new Intent(addScenarioActivity.this, addItemsToScenarioActivity.class);
        goToaddItemsToScenario.putExtra("scenarioNumber", scenarioNumber);
        startActivity(goToaddItemsToScenario);
    }

    public void checkScenarioNumber(String scenarioNumber){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("scenarioNumber", scenarioNumber);

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    if(reader.getBoolean("exists")){
                        TextView errorNumberAlreadyExistsTextView = (TextView) findViewById(R.id.errorNumberAlreadyExistsTextView);
                        errorNumberAlreadyExistsTextView.setText("Scenario number already exists !!");
                    }
                }catch(JSONException e){
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/checkScenarioNumber");
    }

}

package com.materialplanning.vodafone.mpapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class scenariosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenarios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Scenarios");
        setSupportActionBar(toolbar);

        getScenarios();
    }

    public void getScenarios(){
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

                        scenariosList.add(scenarioObject);
                    }

                    ArrayAdapter<scenario> scenarioListAdapter = new scenarioAdapter(scenariosActivity.this, scenariosList);
                    // Connect list and adapter
                    ListView scenariosListView = (ListView) findViewById(R.id.scenariosListView);
                    scenariosListView.setAdapter(scenarioListAdapter);

                    scenariosListView.setOnTouchListener(new OnSwipeTouchListener(scenariosActivity.this, scenariosListView){
                        public void onSwipeLeft(int pos) {
                            try{
                                deleteScenario(reader.getJSONObject(pos).getInt("scenarioID"));
                            }catch(JSONException e){
                            }
                        }
                    });

                    scenariosListView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try{
                                        Intent goToScenario = new Intent(scenariosActivity.this, scenarioViewActivity.class);

                                        goToScenario.putExtra("scenarioID", reader.getJSONObject(position).getInt("scenarioID"));
                                        goToScenario.putExtra("scenarioNumber", reader.getJSONObject(position).getInt("scenarioNumber"));

                                        startActivity(goToScenario);
                                    }
                                    catch (JSONException e){}
                                }
                            }
                    );
                } catch (JSONException e) {
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/getScenarios");
    }

    public void deleteScenario(final int scenarioID){
        //Put up the Yes/No message box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete scenario");
        builder.setMessage("Are you sure?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("scenarioID", Integer.toString(scenarioID));
                Connection conn = new Connection(params, new ConnectionPostListener() {
                    @Override
                    public void doSomething(String result) {
                        try {
                            JSONObject reader = new JSONObject(result);
                            if(reader.getBoolean("deleted")){
                                Toast.makeText(scenariosActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(scenariosActivity.this, scenariosActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }catch (JSONException e){
                        }
                    }
                });
                conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/deleteScenario");
            }
        });
        builder.setNegativeButton("No", null);
        builder .show();
    }

    public void addScenario(View view){
        Intent intent = new Intent(this, addScenarioActivity.class);
        startActivity(intent);
    }
}

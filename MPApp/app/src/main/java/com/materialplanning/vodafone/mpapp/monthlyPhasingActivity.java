package com.materialplanning.vodafone.mpapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class monthlyPhasingActivity extends AppCompatActivity {

    ArrayList<monthlyPhasing> monthlyPhasingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_phasing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getExtras().getString("projectName"));
        setSupportActionBar(toolbar);

        getYearTarget();
        prepareMonthlyPhasing();
    }

    public void getYearTarget(){
        EditText yearTargetEditText = (EditText) findViewById(R.id.yearTargetEditText);
        yearTargetEditText.setText(Integer.toString(getIntent().getExtras().getInt("yearTarget")));
    }

    public void prepareMonthlyPhasing(){
        monthlyPhasingList = new ArrayList<monthlyPhasing>();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("projectID", Integer.toString(getIntent().getExtras().getInt("projectID")));
        params.put("regionID", Integer.toString(getIntent().getExtras().getInt("regionID")));
        params.put("vendorID", Integer.toString(getIntent().getExtras().getInt("vendorID")));

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONArray reader = new JSONArray(result);
                    for(int i=0; i<reader.length(); ++i){
                        JSONObject data = reader.getJSONObject(i);

                        monthlyPhasing monthlyPhasingObject = new monthlyPhasing();
                        monthlyPhasingObject.month = data.getInt("monthID");
                        monthlyPhasingObject.phasing = data.getInt("monthPhase");
                        monthlyPhasingList.add(monthlyPhasingObject);
                    }

                    ListView monthlyPhasingListView = (ListView) findViewById(R.id.monthlyPhasingListView);
                    ArrayAdapter<monthlyPhasing> monthlyPhasingAdapter = new monthlyPhasingAdapter(monthlyPhasingActivity.this, monthlyPhasingList);
                    monthlyPhasingListView.setAdapter(monthlyPhasingAdapter);

                    monthlyPhasingListView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                    try {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(monthlyPhasingActivity.this);
                                        builder.setTitle("Monthly phasing");
                                        builder.setMessage("Specify Project's monthly phasing");

                                        // Set up the input
                                        final EditText input = new EditText(monthlyPhasingActivity.this);
                                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                                        builder.setView(input);

                                        // Set up the buttons
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(monthlyPhasingActivity.this, input.getText().toString(), Toast.LENGTH_LONG).show();
                                                HashMap<String, String> params = new HashMap<String, String>();
                                                params.put("projectID", Integer.toString(getIntent().getExtras().getInt("projectID")));
                                                params.put("regionID", Integer.toString(getIntent().getExtras().getInt("regionID")));
                                                params.put("vendorID", Integer.toString(getIntent().getExtras().getInt("vendorID")));
                                                params.put("monthID", Integer.toString(monthlyPhasingList.get(position).getMonth()));
                                                params.put("monthPhase", input.getText().toString());

                                                Connection connection = new Connection(params, new ConnectionPostListener() {
                                                    @Override
                                                    public void doSomething(String result) {
                                                        try{
                                                            JSONObject reader = new JSONObject(result);
                                                            if(reader.getBoolean("edited")) {
                                                                Toast.makeText(monthlyPhasingActivity.this, "Phasing is edited", Toast.LENGTH_SHORT).show();
                                                     //           monthlyPhasingList.get(position).phasing = Integer.parseInt(input.getText().toString());
                                                                prepareMonthlyPhasing();
                                                            }
                                                        }catch (JSONException e){
                                                        }
                                                    }
                                                });
                                                connection.execute(connection.URL + "/editMonthlyPhasing");
                                            }
                                        });
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        builder.show();
                                    } catch (Exception e) {
                                    }
                                }
                            });

                }catch(JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/getPhasing");
    }

    public void saveTarget(View view){
        EditText yearTargetEditText = (EditText) findViewById(R.id.yearTargetEditText);
        String yearTarget = yearTargetEditText.getText().toString();

        int totalMonthPhasing = 0;
        for(int i=0; i<monthlyPhasingList.size(); ++i){
            totalMonthPhasing += monthlyPhasingList.get(i).getPhasing();
            if(totalMonthPhasing > Integer.parseInt(yearTarget)){
                Toast.makeText(monthlyPhasingActivity.this, "ERROR total monthly phasing exceeds year target.", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if(totalMonthPhasing < Integer.parseInt(yearTarget)){
            Toast.makeText(monthlyPhasingActivity.this, "ERROR total monthly phasing is less than year target.", Toast.LENGTH_LONG).show();
            return;
        }else if(totalMonthPhasing == Integer.parseInt(yearTarget)){
            HashMap<String, String> params = new HashMap<>();
            params.put("projectID", Integer.toString(getIntent().getExtras().getInt("projectID")));
            params.put("regionID", Integer.toString(getIntent().getExtras().getInt("regionID")));
            params.put("vendorID", Integer.toString(getIntent().getExtras().getInt("vendorID")));
            params.put("yearTarget", yearTarget);

            Connection conn = new Connection(params, new ConnectionPostListener() {
                @Override
                public void doSomething(String result) {
                    try{
                        JSONObject reader = new JSONObject(result);
                        if(reader.getBoolean("edited")) {
                            Toast.makeText(monthlyPhasingActivity.this, "Year target is edited", Toast.LENGTH_SHORT).show();

                            Intent goToPRVM = new Intent(monthlyPhasingActivity.this, prvmViewActivity.class);
                            goToPRVM.putExtra("projectID", getIntent().getExtras().getInt("projectID"));
                            goToPRVM.putExtra("projectName", getIntent().getExtras().getString("projectName"));
                            startActivity(goToPRVM);

                            finish();
                        }

                    }catch (JSONException e){
                    }
                }
            });
            conn.execute(conn.URL + "/editYearTarget");
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(monthlyPhasingActivity.this, "Save first !!", Toast.LENGTH_SHORT).show();
        return;
    }
}

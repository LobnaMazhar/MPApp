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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class addScenarioActivity extends AppCompatActivity {

    ArrayList<Integer> selectedItemsIDs;
    int scenarioID = 0; // used in getting scenarioID of the recently created scenario using scenario Number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scenario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add new scenario");
        setSupportActionBar(toolbar);

        selectedItemsIDs = new ArrayList<Integer>();
    }

    public void saveScenario(View view) {
        HashMap<String, String> params = new HashMap<String, String>();

        final EditText addScenarioNumberEditText = (EditText) findViewById(R.id.addScenarioNumberEditText);
        String scenarioNumber = addScenarioNumberEditText.getText().toString();
        if (scenarioNumber.isEmpty()) {
            Toast.makeText(addScenarioActivity.this, "Please specify the scenario number", Toast.LENGTH_LONG).show();
            return;
        }

        TextView errorNumberAlreadyExistsTextView = (TextView) findViewById(R.id.errorNumberAlreadyExistsTextView);
        errorNumberAlreadyExistsTextView.setText("");
        checkScenarioNumber(scenarioNumber);
        if (errorNumberAlreadyExistsTextView.getText().equals("Scenario number already exists !!"))
            return;
        else{
            params.put("scenarioNumber", scenarioNumber);

            Connection conn = new Connection(params, new ConnectionPostListener() {
                @Override
                public void doSomething(String result) {
                    try {
                        JSONObject reader = new JSONObject(result);
                    } catch (JSONException e) {
                    }
                }
            });
            conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/addScenario");


            // Get scenario ID to use while inserting items to scenario
            getScenarioID(scenarioNumber);


            // Add items to the scenario using scenarioID got from the prev fn call
            addItemsToScenario();
            selectedItemsIDs.clear();

            Toast.makeText(addScenarioActivity.this, "A new scenario is added", Toast.LENGTH_SHORT).show();
        }
    }

    public void addItemsToScenario(View view) {

        EditText addScenarioNumberEditText = (EditText) findViewById(R.id.addScenarioNumberEditText);
        String scenarioNumber = addScenarioNumberEditText.getText().toString();
        if (scenarioNumber.isEmpty()) {
            Toast.makeText(addScenarioActivity.this, "Please specify the scenario number first", Toast.LENGTH_LONG).show();
            return;
        }

        TextView errorNumberAlreadyExistsTextView = (TextView) findViewById(R.id.errorNumberAlreadyExistsTextView);
        errorNumberAlreadyExistsTextView.setText("");
        checkScenarioNumber(scenarioNumber);
        if (errorNumberAlreadyExistsTextView.getText().equals("Scenario number already exists !!"))
            return;

        // Hide the button
        Button addItemsButton = (Button) findViewById(R.id.addItemsButton);
        addItemsButton.setVisibility(View.INVISIBLE);
        // Show the listView
        ListView addItemsToScenarioItemsListView = (ListView) findViewById(R.id.addItemsToScenarioItemsListView);
        addItemsToScenarioItemsListView.setVisibility(View.VISIBLE);

        showItemsToSelectFrom(selectedItemsIDs);

       /* Intent goToaddItemsToScenario = new Intent(addScenarioActivity.this, addItemsToScenarioActivity.class);
        goToaddItemsToScenario.putExtra("scenarioNumber", scenarioNumber);
        startActivity(goToaddItemsToScenario); */
    }

    public void checkScenarioNumber(String scenarioNumber) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("scenarioNumber", scenarioNumber);

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    JSONObject reader = new JSONObject(result);
                    if (reader.getBoolean("exists")) {
                        TextView errorNumberAlreadyExistsTextView = (TextView) findViewById(R.id.errorNumberAlreadyExistsTextView);
                        errorNumberAlreadyExistsTextView.setText("Scenario number already exists !!");
                    }
                } catch (JSONException e) {
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/checkScenarioNumber");
    }

    public void showItemsToSelectFrom(final ArrayList<Integer> selectedItemsIDs) {
        final ArrayList<item> itemList = new ArrayList<item>();
        HashMap<String, String> params = new HashMap<String, String>();
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    final JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); ++i) {
                        JSONObject data = reader.getJSONObject(i);

                        item itemObject = new item();

                        itemObject.itemID = data.getInt("itemID");
                        itemObject.itemEvoCode = data.getString("itemEvoCode");
                        itemObject.itemShortDescription = data.getString("itemShortDescription");
                        itemObject.itemQuantity = data.getInt("itemQuantity");

                        itemList.add(itemObject);
                    }

                    ArrayAdapter<item> itemListAdapter = new itemAdapter(addScenarioActivity.this, itemList);
                    // Connect list and adapter
                    ListView addItemsToScenarioItemsListView = (ListView) findViewById(R.id.addItemsToScenarioItemsListView);
                    addItemsToScenarioItemsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    addItemsToScenarioItemsListView.setItemsCanFocus(false);
                    addItemsToScenarioItemsListView.setAdapter(itemListAdapter);

                    addItemsToScenarioItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            view.setSelected(true); // 3shan l selected items ybanu ,, TODO hal s7 kda wla a
                            try {
                                int selectedItemID = reader.getJSONObject(position).getInt("itemID");
                                if (selectedItemsIDs.contains(selectedItemID))
                                    selectedItemsIDs.remove(selectedItemID);
                                else
                                    selectedItemsIDs.add(selectedItemID);
                            } catch (JSONException e) {
                            }
                        }
                    });
                } catch (JSONException e) {
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/getItems");
    }

    public void getScenarioID(String scenarioNumber){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("scenarioNumber", scenarioNumber);
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    JSONObject reader = new JSONObject(result);
                    scenarioID = reader.getInt("scenarioID");
                } catch (JSONException e) {
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/getScenarioID");
    }

    public void addItemsToScenario() {
        HashMap<String, String> params = new HashMap<String, String>();
        for(int i=0; i<selectedItemsIDs.size(); ++i){
            params.clear();

            params.put("itemID", selectedItemsIDs.get(i).toString());
            params.put("scenarioID", Integer.toString(scenarioID));

            Connection conn = new Connection(params, new ConnectionPostListener() {
                @Override
                public void doSomething(String result) {
                    try {
                        JSONObject reader = new JSONObject(result);
                    } catch (JSONException e) {
                    }
                }
            });
            conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/addItemsToScenario");
        }
    }
}

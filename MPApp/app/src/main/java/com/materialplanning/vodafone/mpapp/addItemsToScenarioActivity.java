package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

public class addItemsToScenarioActivity extends AppCompatActivity {

    ArrayList<Integer> selectedItemsIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items_to_scenario);

        selectedItemsIDs = new ArrayList<Integer>();
        showItemsToSelectFrom(selectedItemsIDs);

        ListView addItemsToScenarioItemsListView = (ListView) findViewById(R.id.addItemsToScenarioItemsListView);
        addItemsToScenarioItemsListView.setVisibility(View.VISIBLE);
    }

    public void showItemsToSelectFrom(final ArrayList<Integer> selectedItemsIDs){
        {
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

                        ArrayAdapter<item> itemListAdapter = new itemAdapter(addItemsToScenarioActivity.this, itemList);
                        // Connect list and adapter
                        ListView addItemsToScenarioItemsListView = (ListView) findViewById(R.id.addItemsToScenarioItemsListView);
                        addItemsToScenarioItemsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        addItemsToScenarioItemsListView.setAdapter(itemListAdapter);

                        addItemsToScenarioItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                view.setSelected(true); // 3shan l selected items ybanu ,, TODO hal s7 kda wla a
                                try{
                                    int selectedItemID = reader.getJSONObject(position).getInt("itemID");
                                    if(selectedItemsIDs.contains(selectedItemID))
                                        selectedItemsIDs.remove(selectedItemID);
                                    else
                                        selectedItemsIDs.add(selectedItemID);
                                }catch (JSONException e){
                                }
                            }
                        });
                    } catch (JSONException e) {
                    }
                }
            });
            conn.execute(conn.URL + "/getItems");
        }
    }

    public void addSelectedItems(View view){
        HashMap<String, String> params = new HashMap<String, String>();
        final String scenarioNumber = getIntent().getExtras().getString("scenarioNumber");
        params.put("scenarioNumber", scenarioNumber);
        params.put("selectedItemsIDsList", selectedItemsIDs.toString());

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    if(reader.getBoolean("added")) {
                        Toast.makeText(addItemsToScenarioActivity.this, "Selected items are added to scenario #" + scenarioNumber + " successfully", Toast.LENGTH_LONG).show();
                        finish(); // TODO kda tamam wla a 3shan y5ls da w yrg3 ll ablu lw7du
                    }
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/addItemsToScenario");
    }

    //Dot Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dot, menu);
        return true;
    }
}

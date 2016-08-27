package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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
    boolean addItemsButtonDown = false;

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
        final EditText addScenarioNumberEditText = (EditText) findViewById(R.id.addScenarioNumberEditText);
        final String scenarioNumber = addScenarioNumberEditText.getText().toString();

        if (!addItemsButtonDown) {
            if (scenarioNumber.isEmpty()) {
                Toast.makeText(addScenarioActivity.this, "Please specify the scenario number", Toast.LENGTH_LONG).show();
                return;
            }

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("scenarioNumber", scenarioNumber);

            Connection conn = new Connection(params, new ConnectionPostListener() {
                @Override
                public void doSomething(String result) {
                    try {
                        JSONObject reader = new JSONObject(result);
                        if(reader.getBoolean("added")){
                            Toast.makeText(addScenarioActivity.this, "A new scenario is added", Toast.LENGTH_SHORT).show();

                            getScenarioID(scenarioNumber);

                            Intent intent = new Intent(addScenarioActivity.this, scenarioViewActivity.class);
                            intent.putExtra("scenarioID", Integer.toString(scenarioID));
                            intent.putExtra("scenarioNumber", scenarioNumber);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        TextView errorNumberAlreadyExistsTextView = (TextView) findViewById(R.id.errorNumberAlreadyExistsTextView);
                        errorNumberAlreadyExistsTextView.setText("Scenario number already exists !!");
                    }
                }
            });
            conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/addScenario");
        }else{
            Toast.makeText(addScenarioActivity.this, "A new scenario is added", Toast.LENGTH_SHORT).show();

            getScenarioID(scenarioNumber);

            Intent intent = new Intent(addScenarioActivity.this, scenarioViewActivity.class);
            intent.putExtra("scenarioID", Integer.toString(scenarioID));
            intent.putExtra("scenarioNumber", scenarioNumber);
            startActivity(intent);
            finish();
        }
    }

    public void addItemsToScenario(View view) {
        addItemsButtonDown = true;

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
        else {
            // Hide the button
            Button addItemsButton = (Button) findViewById(R.id.addItemsButton);
            addItemsButton.setVisibility(View.INVISIBLE);
            // Show the listView
            ListView addItemsToScenarioItemsListView = (ListView) findViewById(R.id.addItemsToScenarioItemsListView);
            addItemsToScenarioItemsListView.setVisibility(View.VISIBLE);

            addScenario(scenarioNumber);
            getScenarioID(scenarioNumber);

            showItemsToSelectFrom(selectedItemsIDs);
        }
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
        conn.execute(conn.URL + "/checkScenarioNumber");
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
                    addItemsToScenarioItemsListView.setAdapter(itemListAdapter);

                    final ArrayList<Integer> selectedItems = new ArrayList<Integer>();

                    addItemsToScenarioItemsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                    addItemsToScenarioItemsListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                        @Override
                        public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
                            if (checked) {
                                selectedItems.add(itemList.get(position).getItemID());
                            }else{
                                int indexToRemove = selectedItems.indexOf(itemList.get(position).getItemID());
                                selectedItems.remove(indexToRemove);
                            }
                            actionMode.setTitle(selectedItems.size() + " items selected.");
                        }

                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                            MenuInflater inflater = actionMode.getMenuInflater();
                            inflater.inflate(R.menu.context_add, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.add:
                                    addItemsToScenario(selectedItems);
                                    actionMode.finish();
                                    return true;
                                default:
                                    return false;
                            }
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode actionMode) {
                        }
                    });
                } catch (JSONException e) {
                }
            }
        });
        conn.execute(conn.URL + "/getItems");
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
        conn.execute(conn.URL + "/getScenarioID");
    }

    public void addItemsToScenario(final ArrayList<Integer> selectedItemsToAdd) {
        for(int i=0; i<selectedItemsToAdd.size(); ++i) {
            HashMap<String, String> params = new HashMap<String, String>();

            params.put("itemID", Integer.toString(selectedItemsToAdd.get(i)));
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
            conn.execute(conn.URL + "/addItemToScenario");
        }
        EditText addScenarioNumberEditText = (EditText) findViewById(R.id.addScenarioNumberEditText);
        String scenarioNumber = addScenarioNumberEditText.getText().toString();
        Toast.makeText(addScenarioActivity.this, Integer.toString(selectedItemsToAdd.size()) + " items added to scenario number " + scenarioNumber, Toast.LENGTH_LONG).show();
    }

    public void addScenario(String scenarioNumber){
        HashMap<String, String> params = new HashMap<String, String>();
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
        conn.execute(conn.URL + "/addScenario");
    }
}

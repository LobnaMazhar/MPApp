package com.materialplanning.vodafone.mpapp;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.Layout;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class scenarioViewActivity extends AppCompatActivity {
    int projectID = 0;
    ArrayList<Integer> selectedItemsIDs;
    ArrayList<Integer> itemsInScenariosIDsList = new ArrayList<Integer>();
    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getExtras().getString("scenarioNumber"));
        setSupportActionBar(toolbar);

        getScenarioNumber();
        getProjectName();
        getItemsInScenario();

        selectedItemsIDs = new ArrayList<Integer>();
    }

    public void getScenarioNumber(){
        EditText scenarioNumberEditText = (EditText) findViewById(R.id.scenarioNumberEditText);
        scenarioNumberEditText.setText(getIntent().getExtras().getString("scenarioNumber"));
    }

    public void getProjectID(){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("scenarioID", Integer.toString(getIntent().getExtras().getInt("scenarioID")));

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    projectID = reader.getInt("projectID");
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/getProjectID");
    }

    public void getProjectName(){
        getProjectID();
        if(projectID == 0){
            TextView scenarioProjectNameTextView = (TextView) findViewById(R.id.scenarioProjectNameTextView);
            scenarioProjectNameTextView.setText("NA");
            scenarioProjectNameTextView.setTextColor(getResources().getColor(R.color.vodafoneRed));
         //   scenarioProjectNameEdiText.setTextColor(60202);
        }else{
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("projectID", Integer.toString(projectID));

            Connection conn = new Connection(params, new ConnectionPostListener() {
                @Override
                public void doSomething(String result) {
                    try{
                        JSONObject reader = new JSONObject(result);

                        TextView scenarioProjectNameTextView = (TextView) findViewById(R.id.scenarioProjectNameTextView);
                        scenarioProjectNameTextView.setText(reader.getString("projectName"));
                    }catch (JSONException e){
                    }
                }
            });
            conn.execute(conn.URL + "/getProjectName");
        }
    }

    public void getItemsInScenario() {
        final ArrayList<itemInScenario> itemsInScenariosList = new ArrayList<itemInScenario>();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("scenarioID", Integer.toString(getIntent().getExtras().getInt("scenarioID")));
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    final JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); ++i) {
                        JSONObject data = reader.getJSONObject(i);

                        itemInScenario itemInScenarioObject = new itemInScenario();

                        itemInScenarioObject.itemInScenarioID = data.getInt("itemInScenarioID");
                        itemInScenarioObject.itemInScenarioItemID = data.getInt("itemInScenarioItemID");
                        itemInScenarioObject.itemInScenarioScenarioID = data.getInt("itemInScenarioScenarioID");
                        itemInScenarioObject.itemInScenarioItemQuantity = data.getInt("itemInScenarioItemQuantity");
                        itemInScenarioObject.itemInScenarioItemShortDescription = data.getString("itemInScenarioItemShortDescription");

                        itemsInScenariosList.add(itemInScenarioObject);
                        itemsInScenariosIDsList.add(data.getInt("itemInScenarioItemID")); // 3shan ynf3 y3ml check 3la l IDs elly tzhr fl addNewItems
                    }

                    ArrayAdapter<itemInScenario> itemsInScenariosAdapterListAdapter = new itemsInScenariosAdapter(scenarioViewActivity.this, itemsInScenariosList);
                    // Connect list and adapter
                    final ListView itemsInScenarioListView = (ListView) findViewById(R.id.itemsInScenarioListView);
                    itemsInScenarioListView.setAdapter(itemsInScenariosAdapterListAdapter);


                    // Select items from the list to be (edited(list)=deleted(items))
                    final ArrayList<Integer> selectedItems = new ArrayList<Integer>();
                    itemsInScenarioListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                    itemsInScenarioListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                        @Override
                        public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
                            if (checked) {
                                selectedItems.add(itemsInScenariosList.get(position).getitemInScenarioItemID());
                            }else{
                                int indexToRemove = selectedItems.indexOf(itemsInScenariosList.get(position).getitemInScenarioItemID());
                                selectedItems.remove(indexToRemove);
                            }
                            actionMode.setTitle(selectedItems.size() + " items selected.");
                        }

                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                            MenuInflater inflater = actionMode.getMenuInflater();
                            inflater.inflate(R.menu.context_delete, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.delete:
                                    deleteItems(selectedItems);
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


                    itemsInScenarioListView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                    try {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(scenarioViewActivity.this);
                                        builder.setTitle("Item quantity");
                                        builder.setMessage("Specify item's quantity");

                                        // Set up the input
                                        final EditText input = new EditText(scenarioViewActivity.this);
                                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                                        builder.setView(input);

                                        // Set up the buttons
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(scenarioViewActivity.this, input.getText().toString(), Toast.LENGTH_LONG).show();
                                                HashMap<String, String> params = new HashMap<String, String>();
                                                params.put("ItemQuantity", input.getText().toString());
                                                params.put("itemInScenarioID",Integer.toString(itemsInScenariosList.get(position).getitemInScenarioID()));

                                                Connection connection = new Connection(params, new ConnectionPostListener() {
                                                    @Override
                                                    public void doSomething(String result) {
                                                        try{
                                                            JSONObject reader = new JSONObject(result);
                                                            if(reader.getBoolean("edited"))
                                                                Toast.makeText(scenarioViewActivity.this, "Quantity is edited", Toast.LENGTH_SHORT).show();
                                                        }catch (JSONException e){
                                                        }
                                                    }
                                                });
                                                connection.execute(connection.URL + "/editItemQuantityInScenario");
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
                } catch (JSONException e) {
                }
            }
        });
        conn.execute(conn.URL + "/getItemsInScenario");
    }

    public void editScenario(View view){
        HashMap<String,String> params = new HashMap<String, String>();

        String scenarioID = Integer.toString(getIntent().getExtras().getInt("scenarioID"));
        params.put("scenarioID", scenarioID);

        EditText scenarioNumberEditText = (EditText) findViewById(R.id.scenarioNumberEditText);
        String scenarioNumber = scenarioNumberEditText.getText().toString();
        params.put("scenarioNumber", scenarioNumber);

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                }catch (JSONException e){
                    Toast.makeText(scenarioViewActivity.this, "Scenario number already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
        conn.execute(conn.URL + "/editScenario");


        // Add items to the scenario using scenarioID sent with the intent
        //addItemsToScenario();


        Toast.makeText(scenarioViewActivity.this, "Scenario has been edited", Toast.LENGTH_SHORT).show();
    }

    public void deleteItems(final ArrayList<Integer> selectedItemsToDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete item(s) from scenario");
        builder.setMessage("Are you sure you want to delete " + selectedItemsToDelete.size() + " items?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                for(int i=0; i<selectedItemsToDelete.size(); ++i) {
                    // Remove items checked to be deleted from the list of the items in the scenarios so they can appear back while choosing items to add
                    int indexToRemove = itemsInScenariosIDsList.indexOf(selectedItemsToDelete.get(i));
                    itemsInScenariosIDsList.remove(indexToRemove);

                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("itemID", selectedItemsToDelete.get(i).toString());
                    params.put("scenarioID", Integer.toString(getIntent().getExtras().getInt("scenarioID")));

                    Connection conn = new Connection(params, new ConnectionPostListener() {
                        @Override
                        public void doSomething(String result) {
                            try {
                                JSONObject reader = new JSONObject(result);
                            } catch (JSONException e) {
                            }
                        }
                    });
                    conn.execute(conn.URL + "/deleteItemFromScenario");
                }
                Toast.makeText(scenarioViewActivity.this, selectedItemsToDelete.size() + " items deleted.", Toast.LENGTH_LONG).show();

                Intent goToScenario = new Intent(scenarioViewActivity.this, scenarioViewActivity.class);
                goToScenario.putExtra("scenarioID", getIntent().getExtras().getInt("scenarioID"));
                goToScenario.putExtra("scenarioNumber", Integer.toString(getIntent().getExtras().getInt("scenarioNumber")));
                startActivity(goToScenario);
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    public void addItems(View view){
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

                        if(itemsInScenariosIDsList.contains(data.getInt("itemID")))
                            continue;
                        else
                            itemList.add(itemObject);
                    }

                    ListView itemsInScenarioListView = (ListView) findViewById(R.id.itemsInScenarioListView);
                    itemsInScenarioListView.setVisibility(View.INVISIBLE);
                    ListView addItemsToScenarioItemsListView = (ListView) findViewById(R.id.addItemsToScenarioItemsListView);
                    addItemsToScenarioItemsListView.setVisibility(View.VISIBLE);

                    // Connect list and adapter
                    ArrayAdapter<item> itemListAdapter = new itemAdapter(scenarioViewActivity.this, itemList);
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

    public void addItemsToScenario(final ArrayList<Integer> selectedItemsToAdd) {
        for(int i=0; i<selectedItemsToAdd.size(); ++i) {
            HashMap<String, String> params = new HashMap<String, String>();

            params.put("itemID", Integer.toString(selectedItemsToAdd.get(i)));
            params.put("scenarioID", Integer.toString(getIntent().getExtras().getInt("scenarioID")));

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
            Toast.makeText(scenarioViewActivity.this, Integer.toString(selectedItemsToAdd.size()) + " items added to scenario number " + Integer.toString(getIntent().getExtras().getInt("scenarioNumber")), Toast.LENGTH_LONG).show();

            Intent intent = new Intent(scenarioViewActivity.this, scenarioViewActivity.class);
            intent.putExtra("scenarioID", getIntent().getExtras().getInt("scenarioID"));
            intent.putExtra("scenarioNumber", getIntent().getExtras().getInt("scenarioNumber"));
            startActivity(intent);
            finish();
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
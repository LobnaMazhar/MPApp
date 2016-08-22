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
import java.util.StringTokenizer;

public class scenarioViewActivity extends AppCompatActivity {

    int projectID = 0;
    ArrayList<Integer> selectedItemsIDs;
    final ArrayList<itemInScenario> itemsInScenariosList = new ArrayList<itemInScenario>();
    ArrayList<Integer> itemsInScenariosIDsList = new ArrayList<Integer>();
    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_view);

        getScenarioNumber();
        getProjectName();
        getItemsInScenario();

        selectedItemsIDs = new ArrayList<Integer>();
    }

    public void getScenarioNumber(){
        EditText scenarioNumberEditText = (EditText) findViewById(R.id.scenarioNumberEditText);
        scenarioNumberEditText.setText(Integer.toString(getIntent().getExtras().getInt("scenarioNumber")));
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
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/getProjectID");
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
            conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/getProjectName");
        }
    }

    public void getItemsInScenario() {
        HashMap<String, String> params = new HashMap<String, String>();
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

                        itemsInScenariosList.add(itemInScenarioObject);
                        itemsInScenariosIDsList.add(data.getInt("itemInScenarioItemID")); // 3shan ynf3 y3ml check 3la l IDs elly tzhr fl addNewItems
                    }

                    ArrayAdapter<itemInScenario> itemsInScenariosAdapterListAdapter = new itemsInScenariosAdapter(scenarioViewActivity.this, itemsInScenariosList);
                    // Connect list and adapter
                    final ListView itemsInScenarioListView = (ListView) findViewById(R.id.itemsInScenarioListView);
                    itemsInScenarioListView.setAdapter(itemsInScenariosAdapterListAdapter);

                    // On item click listener
                 /*   itemsInScenarioListView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try {
                                        HashMap<String, String> params = new HashMap<String, String>();
                                        params.put("itemID", Integer.toString(reader.getJSONObject(position).getInt("itemInScenarioItemID")));

                                        Connection conn = new Connection(params, new ConnectionPostListener() {
                                            @Override
                                            public void doSomething(String result) {
                                                try{
                                                    JSONObject reader = new JSONObject(result);

                                                    Intent goToItem = new Intent(scenarioViewActivity.this, itemViewActivity.class);

                                                    goToItem.putExtra("itemID", reader.getInt("itemID"));
                                                    goToItem.putExtra("itemEvoCode", reader.getString("itemEvoCode"));
                                                    goToItem.putExtra("itemShortDescription", reader.getString("itemShortDescription"));
                                                    goToItem.putExtra("itemQuantity", reader.getInt("itemQuantity"));

                                                    startActivity(goToItem);
                                                }catch(JSONException e){
                                                }
                                            }
                                        });
                                        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/getItem");
                                    } catch (Exception e) {
                                    }
                                }
                            }
                    ); */


                    // Select items from the list to be (edited(list)=deleted(items))
                    final ArrayList<Integer> selectedItems = new ArrayList<Integer>();
                    itemsInScenarioListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                    itemsInScenarioListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                        @Override
                        public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
                            if (checked) {
                                selectedItems.add(itemsInScenariosList.get(position).getitemInScenarioItemID());
                                actionMode.setTitle(selectedItems.size() + " items selected.");
                            }

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
                                case R.id.deleteItemsFromScenarioItemID:
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
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                                                // TODO lw l toast tl3 hna yb2a hndh service b2a w azbt fn editQ
                                            }
                                        });
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        builder.show();
                                        //TODO on click yft7 dialog box fe l evocode w esm l item w l Q bt3tha hna w mmkn y3dlu b2a w fe save btn
                                        // TODO kda ams7 onClick ely fo2
                                    } catch (Exception e) {
                                    }
                                }
                            });
                } catch (JSONException e) {
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/getItemsInScenario");
    }

    public void editScenario(View view){
        // TODO y-edit l quantity bta3t item mo3yn
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
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/editScenario");


        // Add items to the scenario using scenarioID sent with the intent
        addItemsToScenario();


        Toast.makeText(scenarioViewActivity.this, "Scenario has been edited", Toast.LENGTH_SHORT).show();
    }

    public void deleteItems(final ArrayList<Integer> selectedItemsToDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete item from scenario");
        builder.setMessage("Are you sure?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("selectedItemsIDs", selectedItemsToDelete.toString());

                Connection conn = new Connection(params, new ConnectionPostListener() {
                    @Override
                    public void doSomething(String result) {
                        try {
                            JSONObject reader = new JSONObject(result);
                            if (reader.getBoolean("deleted"))
                                Toast.makeText(scenarioViewActivity.this, selectedItemsToDelete.size() + " items deleted.", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                        }
                    }
                });
                conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/deleteItemsFromScenario");
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

                    ArrayAdapter<item> itemListAdapter = new itemAdapter(scenarioViewActivity.this, itemList);
                    // Connect list and adapter
                    ListView itemsInScenarioListView = (ListView) findViewById(R.id.itemsInScenarioListView);
                    itemsInScenarioListView.setVisibility(View.INVISIBLE);
                    ListView addItemsToScenarioItemsListView = (ListView) findViewById(R.id.addItemsToScenarioItemsListView);
                    addItemsToScenarioItemsListView.setVisibility(View.VISIBLE);
                    addItemsToScenarioItemsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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

    public void addItemsToScenario() {
        HashMap<String, String> params = new HashMap<String, String>();
        for(int i=0; i<selectedItemsIDs.size(); ++i){
            params.clear();

            params.put("itemID", selectedItemsIDs.get(i).toString());
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
            conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/addItemsToScenario");
        }
    }
}
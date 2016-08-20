package com.materialplanning.vodafone.mpapp;

import android.app.ListActivity;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class scenarioViewActivity extends AppCompatActivity {

    final ArrayList<item> itemList = new ArrayList<item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_view);

        getScenarioNumber();
        getProjectName();
        getItemsInScenario();
    }

    public void getScenarioNumber(){
        EditText scenarioNumberEditText = (EditText) findViewById(R.id.scenarioNumberEditText);
        scenarioNumberEditText.setText(Integer.toString(getIntent().getExtras().getInt("scenarioNumber")));
    }

    public void getProjectName(){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("scenarioID", Integer.toString(getIntent().getExtras().getInt("scenarioID")));

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);

                    EditText scenarioProjectNameEdiText = (EditText) findViewById(R.id.scenarioProjectNameEdiText);
                    scenarioProjectNameEdiText.setText(reader.getString("projectName"));
                }catch (JSONException e){
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/getProjectName");
    }

    public void getItemsInScenario(){
        {
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

                        ArrayAdapter<item> itemListAdapter = new itemAdapter(scenarioViewActivity.this, itemList);
                        // Connect list and adapter
                        ListView itemsInScenarioListView = (ListView) findViewById(R.id.itemsInScenarioListView);
                        itemsInScenarioListView.setAdapter(itemListAdapter);

                        // On item click listener
                        itemsInScenarioListView.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        try{
                                            Intent goToItem = new Intent(scenarioViewActivity.this, itemViewActivity.class);

                                            goToItem.putExtra("itemID", reader.getJSONObject(position).getInt("itemID"));
                                            goToItem.putExtra("itemEvoCode", reader.getJSONObject(position).getString("itemEvoCode"));
                                            goToItem.putExtra("itemShortDescription", reader.getJSONObject(position).getString("itemShortDescription"));
                                            goToItem.putExtra("itemQuantity", reader.getJSONObject(position).getInt("itemQuantity"));

                                            startActivity(goToItem);
                                        }
                                        catch (Exception e){
                                        }
                                    }
                                }
                        );


                        // Select items from the list to be (edited(list)=deleted(items))
                        final ArrayList<Integer> selectedItems = new ArrayList<Integer>();
                        itemsInScenarioListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                        itemsInScenarioListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                            @Override
                            public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
                                if (checked) {
                                    selectedItems.add(itemList.get(position).getItemID());
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
                                switch (menuItem.getItemId()){
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
                                        try{
                                            //TODO on click yft7 dialog box fe l evocode w esm l item w l Q bt3tha hna w mmkn y3dlu b2a w fe save btn
                                        }
                                        catch (Exception e){}
                                    }
                                });
                    } catch (JSONException e) {
                    }
                }
            });
            conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/getItemsInScenario");
        }
    }

    public void editScenario(View view){
        // TODO y-edit l quantity bta3t item mo3yn
        HashMap<String,String> params = new HashMap<String, String>();

        String scenarioID = Integer.toString(getIntent().getExtras().getInt("scenarioID"));
        params.put("scenarioID", scenarioID);

        EditText scenarioNumberEditText = (EditText) findViewById(R.id.scenarioNumberEditText);
        String scenarioNumber = scenarioNumberEditText.getText().toString();
        params.put("scenarioNumber", scenarioNumber);

        EditText scenarioProjectNameEdiText = (EditText) findViewById(R.id.scenarioProjectNameEdiText);
        String scenarioProjectName = scenarioProjectNameEdiText.getText().toString();
        params.put("projectName", scenarioProjectName);

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    if(reader.getBoolean("edited"))
                        Toast.makeText(scenarioViewActivity.this, "Scenario has been edited", Toast.LENGTH_SHORT).show();
                }catch (JSONException e){
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/editScenario");
    }

    public void deleteItems(final ArrayList<Integer> selectedItemsToDelete){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("selectedItemsIDs", selectedItemsToDelete.toString());

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    if(reader.getBoolean("deleted"))
                        Toast.makeText(scenarioViewActivity.this, selectedItemsToDelete.size() + " items deleted.", Toast.LENGTH_LONG).show();
                }catch (JSONException e){
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/deleteItemsFromScenario");
    }
}
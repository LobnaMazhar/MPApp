package com.materialplanning.vodafone.mpapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class itemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Items");
        setSupportActionBar(toolbar);

        getItems();
    }

    public void getItems(){
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

                    ArrayAdapter<item> itemListAdapter = new itemAdapter(itemsActivity.this, itemList);
                    // Connect list and adapter
                    ListView itemsList = (ListView) findViewById(R.id.itemsListView);
                    itemsList.setAdapter(itemListAdapter);

                    itemsList.setOnTouchListener(new OnSwipeTouchListener(itemsActivity.this,itemsList){
                        public void onSwipeLeft(int pos) {
                            try{
                                deleteItem(reader.getJSONObject(pos).getInt("itemID"), reader.getJSONObject(pos).getString("itemShortDescription"));
                            }catch (JSONException e){
                            }
                        }
                    });

                    // On item click listener
                    itemsList.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try{
                                        Intent goToItem = new Intent(itemsActivity.this, itemViewActivity.class);

                                        goToItem.putExtra("itemID", reader.getJSONObject(position).getInt("itemID"));
                                        goToItem.putExtra("itemEvoCode", reader.getJSONObject(position).getString("itemEvoCode"));
                                        goToItem.putExtra("itemShortDescription", reader.getJSONObject(position).getString("itemShortDescription"));
                                        goToItem.putExtra("itemQuantity", Integer.toString(reader.getJSONObject(position).getInt("itemQuantity")));

                                        startActivity(goToItem);
                                    }
                                    catch (JSONException e){}
                                }
                            }
                    );
                } catch (JSONException e) {
                }
            }
        });
        conn.execute(conn.URL + "/getItems");
    }

    public void deleteItem(final int itemID, String itemShortDescription){
        //Put up the Yes/No message box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete item");
        builder.setMessage("Are you sure you want to delete item " + itemShortDescription + " ?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("itemID", Integer.toString(itemID));
                Connection conn = new Connection(params, new ConnectionPostListener() {
                    @Override
                    public void doSomething(String result) {
                        try {
                            JSONObject reader = new JSONObject(result);
                            if(reader.getBoolean("deleted")){
                                Toast.makeText(itemsActivity.this, "Item has been deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(itemsActivity.this, itemsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }catch (JSONException e){
                            Toast.makeText(itemsActivity.this, "Can't be deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                conn.execute(conn.URL + "/deleteItem");
            }
        });
        builder.setNegativeButton("No", null);
        builder .show();
    }

    public void addItem(View view){
        Intent intent = new Intent(this, addItemActivity.class);
        startActivity(intent);
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

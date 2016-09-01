package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class addItemActivity extends AppCompatActivity {

    int itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add new item");
        setSupportActionBar(toolbar);
    }

    public void saveItem(View view){
        HashMap<String, String> params = new HashMap<String, String>();

        final TextView addItemEvoCodeTextView = (TextView) findViewById(R.id.addItemEvoCodeTextView);
        String evoCode = addItemEvoCodeTextView.getText().toString();
        params.put("itemEvoCode",evoCode);

        final TextView addItemShortDescriptionTextView = (TextView) findViewById(R.id.addItemShortDescriptionTextView);
        final String shortDescription = addItemShortDescriptionTextView.getText().toString();
        if(shortDescription.isEmpty()){
            Toast.makeText(this, "Enter the description", Toast.LENGTH_LONG).show();
            return;
        }
        params.put("itemShortDescription", shortDescription);

        final TextView addItemQuantityTextView = (TextView) findViewById(R.id.addItemQuantityTextView);
        String quantity = addItemQuantityTextView.getText().toString();
        if(quantity.isEmpty())
            quantity = "0";
        params.put("itemQuantity", quantity);

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    final JSONObject reader = new JSONObject(result);
                    if(reader.getBoolean("added")) {
                        Toast.makeText(addItemActivity.this, "Item is added successfully", Toast.LENGTH_SHORT).show();

                        Intent goToItem = new Intent(addItemActivity.this, itemViewActivity.class);
                        getItemID(shortDescription);
                        goToItem.putExtra("itemID", itemID);
                        goToItem.putExtra("itemEvoCode", addItemEvoCodeTextView.getText().toString());
                        goToItem.putExtra("itemShortDescription", addItemShortDescriptionTextView.getText().toString());
                        goToItem.putExtra("itemQuantity", addItemQuantityTextView.getText().toString());

                        startActivity(goToItem);
                        finish();
                    }
                    else
                        Toast.makeText(addItemActivity.this, "Item already exists", Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Toast.makeText(addItemActivity.this, "Item already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
        conn.execute(conn.URL + "/addItem");
    }

    public void getItemID(String itemShortDescription){
        HashMap<String, String> params = new HashMap<>();
        params.put("shortDescription", itemShortDescription);
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    itemID = reader.getInt("itemID");
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/getItemIDByShortDescription");
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

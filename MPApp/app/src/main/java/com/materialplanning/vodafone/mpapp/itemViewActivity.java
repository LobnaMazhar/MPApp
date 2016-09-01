package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class itemViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getExtras().getString("itemEvoCode"));
        setSupportActionBar(toolbar);

        TextView itemEvoCodeEditText = (TextView) findViewById(R.id.itemEvoCodeEditText);
        itemEvoCodeEditText.setText(getIntent().getExtras().getString("itemEvoCode"));

        TextView itemShortDescriptionEditText = (TextView) findViewById(R.id.itemShortDescriptionEditText);
        itemShortDescriptionEditText.setText(getIntent().getExtras().getString("itemShortDescription"));

        TextView itemQuantityEditText = (TextView) findViewById(R.id.itemQuantityEditText);
        itemQuantityEditText.setText(getIntent().getExtras().getString("itemQuantity"));
    }

    /*
    public void editItem(View view){
        HashMap<String, String> params = new HashMap<String, String>();

        String itemID = Integer.toString(getIntent().getExtras().getInt("itemID"));
        params.put("itemID", itemID);

        TextView itemEvoCodeTextView = (TextView) findViewById(R.id.itemEvoCodeTextView);
        String itemEvoCode = itemEvoCodeTextView.getText().toString();
        params.put("itemEvoCode", itemEvoCode);

        TextView itemShortDescriptionTextView = (TextView) findViewById(R.id.itemShortDescriptionTextView);
        String itemShortDescription = itemShortDescriptionTextView.getText().toString();
        params.put("itemShortDescription", itemShortDescription);

        TextView itemQuantityTextView = (TextView) findViewById(R.id.itemQuantityTextView);
        String itemQuantity = itemQuantityTextView.getText().toString();
        params.put("itemQuantity", itemQuantity);


        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    JSONObject reader = new JSONObject(result);
                    if(reader.getBoolean("Edited")){
                        Intent intent = new Intent(itemViewActivity.this, itemViewActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }catch (JSONException e){
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/editItem");
    }
    */

    /*
    public void deleteItem(View view){
        //Put up the Yes/No message box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete item");
        builder.setMessage("Are you sure?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("itemID", Integer.toString(getIntent().getExtras().getInt("itemID")));
                Connection conn = new Connection(params, new ConnectionPostListener() {
                    @Override
                    public void doSomething(String result) {
                        try {
                            JSONObject reader = new JSONObject(result);
                            if(reader.getBoolean("deleted")){
                                Toast.makeText(itemViewActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(itemViewActivity.this, itemsActivity.class);
                                startActivity(intent);
                            }
                        }catch (JSONException e){
                        }
                    }
                });
                conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/deleteItem");
            }
        });
        builder.setNegativeButton("No", null);
        builder .show();
    }
    */

    public void saveEditedItem(View view){
        HashMap<String, String> params = new HashMap<String, String>();

        String itemID = Integer.toString(getIntent().getExtras().getInt("itemID"));
        params.put("itemID", itemID);

        TextView itemEvoCodeEditText = (TextView) findViewById(R.id.itemEvoCodeEditText);
        final String itemEvoCode = itemEvoCodeEditText.getText().toString();
        params.put("itemEvoCode", itemEvoCode);

        TextView itemShortDescriptionEditText = (TextView) findViewById(R.id.itemShortDescriptionEditText);
        final String itemShortDescription = itemShortDescriptionEditText.getText().toString();
        params.put("itemShortDescription", itemShortDescription);

        TextView itemQuantityEditText = (TextView) findViewById(R.id.itemQuantityEditText);
        final String itemQuantity = itemQuantityEditText.getText().toString();
        params.put("itemQuantity", itemQuantity);


        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    JSONObject reader = new JSONObject(result);
                    if(reader.getBoolean("edited")){
                        Toast.makeText(itemViewActivity.this, "Item is edited", Toast.LENGTH_SHORT).show();

                        Intent goToItem = new Intent(itemViewActivity.this, itemViewActivity.class);

                        goToItem.putExtra("itemID", getIntent().getExtras().getInt("itemID"));
                        goToItem.putExtra("itemEvoCode", itemEvoCode);
                        goToItem.putExtra("itemShortDescription", itemShortDescription);
                        goToItem.putExtra("itemQuantity", itemQuantity);

                        startActivity(goToItem);
                        finish();
                    }
                }catch (JSONException e){
                    Toast.makeText(itemViewActivity.this, "Edit is invalid, same data exists", Toast.LENGTH_LONG).show();
                }
            }
        });
        conn.execute(conn.URL + "/editItem");
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

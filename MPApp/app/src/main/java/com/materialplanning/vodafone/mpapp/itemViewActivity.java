package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
        setSupportActionBar(toolbar);

        TextView itemEvoCodeTextView = (TextView) findViewById(R.id.itemEvoCodeTextView);
        itemEvoCodeTextView.setText(savedInstanceState.getString("itemEvoCode"));

        TextView itemShortDescriptionTextView = (TextView) findViewById(R.id.itemShortDescriptionTextView);
        itemShortDescriptionTextView.setText(savedInstanceState.getString("itemShortDescription"));

        TextView itemQuantityTextView = (TextView) findViewById(R.id.itemQuantityTextView);
        itemQuantityTextView.setText(savedInstanceState.getInt("itemQuantity"));
    }

    public void editItem(View view){
        Toast.makeText(this, "EDIT NOW", Toast.LENGTH_SHORT).show();
        // TODO write the code
    }

    public void deleteItem(View view){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("itemID", Integer.toString(getIntent().getExtras().getInt("itemID")));
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    JSONObject reader = new JSONObject(result);
                    if(reader.getBoolean("deleted")){
                        Intent intent = new Intent(itemViewActivity.this, itemsActivity.class);
                        startActivity(intent);
                    }
                }catch (JSONException e){
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/deleteItem");
    }

}

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
import android.widget.ListAdapter;
import android.widget.ListView;

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

                    // TODO onItemClickListener
                    itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                            try {
                                Intent intent = new Intent(itemsActivity.this, itemViewActivity.class);

                                intent.putExtra("itemID", reader.getJSONObject(position).getInt("itemID"));
                                intent.putExtra("itemEvoCode", reader.getJSONObject(position).getString("itemEvoCode"));
                                intent.putExtra("itemShortDescription", reader.getJSONObject(position).getString("itemShortDescription"));
                                intent.putExtra("itemQuantity", reader.getJSONObject(position).getInt("itemQuantity"));

                                startActivity(intent);
                            }catch (JSONException e){
                            }
                        }
                    });

                } catch (JSONException e) {
                }
            }
        });
        conn.execute("http://mpapp-radionetwork.rhcloud.com/MPApp/rest/getItems");
    }


}

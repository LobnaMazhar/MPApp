package com.materialplanning.vodafone.mpapp;

import android.app.Activity;
import android.content.Context;
import android.drm.DrmStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lobna on 22-Aug-16.
 */
public class addItemToScenarioAdapter extends ArrayAdapter<itemInScenario> {
    Context context; // TAKE CARE ::: CONTEXT IS NOT PRIVATE
    ArrayList<itemInScenario> itemInScenarioList;

    public addItemToScenarioAdapter(Context context, ArrayList<itemInScenario> itemInScenarioList) {
        super(context, R.layout.additemstoscenariolist_row, itemInScenarioList);
        this.context = context;
        this.itemInScenarioList = itemInScenarioList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        //LayoutInflater inflater = LayoutInflater.from(getContext());

        View itemInScenarioListView = inflater.inflate(R.layout.additemstoscenariolist_row, parent, false);

        itemInScenario itemInScenarioObject = itemInScenarioList.get(position);

        final TextView addItemsToScenarioItemShortDescriptionTextView = (TextView) itemInScenarioListView.findViewById(R.id.addItemsToScenarioItemShortDescriptionTextView);
        int itemID = itemInScenarioObject.getitemInScenarioItemID();

        HashMap<String, String > params = new HashMap<String, String>();
        params.put("itemID", Integer.toString(itemID));
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);

                    addItemsToScenarioItemShortDescriptionTextView.setText(reader.getString("itemShortDescription"));

                }catch(JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/getItemShortDescription");

        return itemInScenarioListView;
    }

    public void getItemShortDescription(int itemID){

    }
}

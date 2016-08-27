package com.materialplanning.vodafone.mpapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lobna on 21-Aug-16.
 */
public class itemsInScenariosAdapter extends ArrayAdapter<itemInScenario> {
    Context context;
    ArrayList<itemInScenario> itemsInScenario;

    public itemsInScenariosAdapter(Context context, ArrayList<itemInScenario> itemsInScenario) {
        super(context, R.layout.itemsinscenarioslist_row, itemsInScenario);
        this.context = context;
        this.itemsInScenario = itemsInScenario;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        //LayoutInflater inflater = LayoutInflater.from(getContext());

        View itemsInScenarioListView = inflater.inflate(R.layout.itemsinscenarioslist_row, parent, false);

        itemInScenario itemInScenarioObject = itemsInScenario.get(position);
        TextView itemInScenarioItemShortDescriptionTextView = (TextView) itemsInScenarioListView.findViewById(R.id.itemInScenarioItemShortDescriptionTextView);
        itemInScenarioItemShortDescriptionTextView.setText(itemInScenarioObject.getitemInScenarioItemShortDescription());
        TextView itemInScenarioItemQuantityTextView = (TextView) itemsInScenarioListView.findViewById(R.id.itemInScenarioItemQuantityTextView);
        itemInScenarioItemQuantityTextView.setText(Integer.toString(itemInScenarioObject.getitemInScenarioItemQuantity()));

        return itemsInScenarioListView;
    }
}

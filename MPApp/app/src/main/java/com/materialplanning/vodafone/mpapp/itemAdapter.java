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
 * Created by Lobna on 12-Aug-16.
 */
public class itemAdapter extends ArrayAdapter<item> {
    Context context; // TAKE CARE ::: CONTEXT IS NOT PRIVATE
    ArrayList<item> items;

    public itemAdapter(Context context, ArrayList<item> items) {
        super(context, R.layout.itemslist_row, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        //LayoutInflater inflater = LayoutInflater.from(getContext());

        View itemListView = inflater.inflate(R.layout.itemslist_row, parent, false);

        item itemObject = items.get(position);
        TextView itemListItemEvoCodeTextView = (TextView) itemListView.findViewById(R.id.itemListItemEvoCodeTextView);
        itemListItemEvoCodeTextView.setText(itemObject.getItemEvoCode());
        TextView itemListItemQuantityTextView = (TextView) itemListView.findViewById(R.id.itemListItemQuantityTextView);
        itemListItemQuantityTextView.setText(Integer.toString(itemObject.getItemQuantity()));

        return itemListView;
    }
}

package com.materialplanning.vodafone.mpapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by Lobna on 14-Aug-16.
 */
public class fileArrayAdapter extends ArrayAdapter<option> {

    private Context c;
    private int id;
    private List<option> items;

    public fileArrayAdapter(Context context, int textViewResourceId,
                            List<option> objects) {
        super(context, textViewResourceId, objects);
        c = context;
        id = textViewResourceId;
        items = objects;
    }
    public option getItem(int i)
    {
        return items.get(i);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }
        final option o = items.get(position);
        if (o != null) {
            TextView dirTextView = (TextView) v.findViewById(R.id.dirTextView);
            TextView fileTextView = (TextView) v.findViewById(R.id.fileTextView);

            if(dirTextView != null)
                dirTextView.setText(o.getName());
            if(fileTextView != null)
                fileTextView.setText(o.getData());

        }
        return v;
    }
}

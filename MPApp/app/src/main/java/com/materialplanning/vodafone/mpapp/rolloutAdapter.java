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
 * Created by Lobna on 16-Aug-16.
 */
public class rolloutAdapter extends ArrayAdapter<rollout>{
    Context context;
    ArrayList<rollout> rolloutList;

    public rolloutAdapter(Context context, ArrayList<rollout> rollouts) {
        super(context, R.layout.rollout_row, rollouts);
        this.context = context;
        this.rolloutList = rollouts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View rolloutListView = inflater.inflate(R.layout.rollout_row, parent, false);

        rollout rolloutObject = rolloutList.get(position);

        TextView rolloutDateTextView = (TextView) rolloutListView.findViewById(R.id.rolloutDateTextView);
        rolloutDateTextView.setText(rolloutObject.getRolloutDate().toString());

        TextView rolloutSiteIDTextView = (TextView) rolloutListView.findViewById(R.id.rolloutSiteIDTextView);
        rolloutSiteIDTextView.setText(rolloutObject.getRolloutSiteID());

        TextView rolloutRegionTextView = (TextView) rolloutListView.findViewById(R.id.rolloutRegionTextView);
        rolloutRegionTextView.setText(rolloutObject.getRolloutRegion());

        TextView rolloutFeederFiberTextView = (TextView) rolloutListView.findViewById(R.id.rolloutFeederFiberTextView);
        rolloutFeederFiberTextView.setText(rolloutObject.getRolloutFeederFiber());

        return rolloutListView;
    }
}

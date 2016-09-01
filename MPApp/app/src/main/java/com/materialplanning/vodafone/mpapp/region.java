package com.materialplanning.vodafone.mpapp;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Lobna on 24-Aug-16.
 */
public class region {
    int regionID;
    String regionName;

    public ArrayList<Pair<Integer, Integer>> vendors = new ArrayList<>();

    public int getRegionID(){
        return regionID;
    }

    public String getRegionName(){
        return regionName;
    }
}

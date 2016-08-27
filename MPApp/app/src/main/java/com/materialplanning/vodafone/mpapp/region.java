package com.materialplanning.vodafone.mpapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Lobna on 24-Aug-16.
 */
public class region {
    int regionID;
    String regionName;

    private List<vendor> vendorList = new ArrayList<vendor>();

    public int getRegionID(){
        return regionID;
    }

    public String getRegionName(){
        return regionName;
    }
}

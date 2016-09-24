package com.materialplanning.vodafone.mpapp;

/**
 * Created by Lobna on 25-Aug-16.
 */
public class prvm {
    int prvmID;
    int prvmProjectID;
    int prvmRegionID;
    int prvmVendorID;
    int prvmYearTarget;
    String projectName;
    String prvmRegionName;
    String prvmVendorName;

    public int getPrvmProjectID(){
        return prvmProjectID;
    }

    public String getProjectName(){
        return projectName;
    }

    public int getPrvmRegionID(){
        return prvmRegionID;
    }

    public int getPrvmVendorID(){
        return prvmVendorID;
    }

    public int getPrvmYearTarget(){
        return prvmYearTarget;
    }
}

package com.materialplanning.vodafone.mpapp;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Lobna on 16-Aug-16.
 */
public class site {
    String siteID;
    int siteRegionID;
    int siteVendorID;

    int siteProjectID;
    int siteDate;

    String siteMonthName;
    String siteProjectName;
    String siteRegionName;

    public String getSiteID(){
        return siteID;
    }

    public int getSiteRegionID(){
        return siteRegionID;
    }

    public int getSiteVendorID(){
        return siteVendorID;
    }

    public int getSiteProjectID() {
        return siteProjectID;
    }

    public int getSiteDate(){
        return siteDate;
    }

    public String getSiteMonthName(){
        return siteMonthName;
    }

    public String getSiteProjectName(){
        return siteProjectName;
    }

    public String getSiteRegionName(){
        return siteRegionName;
    }
}

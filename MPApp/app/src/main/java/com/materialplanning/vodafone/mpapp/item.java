package com.materialplanning.vodafone.mpapp;

/**
 * Created by Lobna on 11-Aug-16.
 */
public class item {
    int itemID;
    String itemEvoCode;
    String itemShortDescription;
    int itemQuantity;

    public item(){
    }

    public int getItemID(){
        return itemID;
    }

    public String getItemEvoCode(){
        return itemEvoCode;
    }

    public int getItemQuantity(){
        return itemQuantity;
    }

    public String getItemShortDescription(){
        return itemShortDescription;
    }
}

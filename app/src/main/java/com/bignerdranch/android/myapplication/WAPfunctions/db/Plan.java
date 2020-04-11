package com.bignerdranch.android.myapplication.WAPfunctions.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class Plan extends DataSupport {
    private int id;
    private String body;
    private String remindTime;
    private String remind;
    private String type;
//    private String placeReminded;


    public String getRemindPlaceNumber() {
        return remindPlaceNumber;
    }

    public void setRemindPlaceNumber(String remindPlaceNumber) {
        this.remindPlaceNumber = remindPlaceNumber;
    }

    private String remindPlaceNumber;

    public String getRemindPlace() {
        return remindPlace;
    }

    public void setRemindPlace(String remindPlace) {
        this.remindPlace = remindPlace;
    }

    private String remindPlace;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

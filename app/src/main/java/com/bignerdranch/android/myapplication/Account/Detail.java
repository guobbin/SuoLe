package com.bignerdranch.android.myapplication.Account;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/9/15/015.
 */

public class Detail extends DataSupport {


    private int id;
    private String mTitle;
    private double mFee;
    private boolean mIncome;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mWeek;
    private int mWeekOfMonth;
    public int getWeekOfMonth() {return mWeekOfMonth;}

    public void setWeekOfMonth(int weekOfMonth) {mWeekOfMonth = weekOfMonth;}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getWeek() {return mWeek;}
    public void setWeek(int week) {mWeek = week;}
    public boolean isIncome() {
        return mIncome;
    }
    public void setIncome(boolean income) {mIncome = income;}
    public int getYear() {
        return mYear;
    }
    public void setYear(int year) {
        mYear = year;
    }
    public int getMonth() {
        return mMonth;
    }
    public void setMonth(int month) {
        mMonth = month;
    }
    public int getDay() {
        return mDay;
    }
    public void setDay(int day) {
        mDay = day;
    }
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }
    public double getFee() {
        return mFee;
    }
    public void setFee(double fee) {
        mFee = fee;
    }

}

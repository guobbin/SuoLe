package com.bignerdranch.android.myapplication;

/**
 * Created by T540p on 2017/7/28.
 */

public class News {
    private String newsTitle;   //新闻标题
    private String newsUrl;     //新闻链接地址
    private String newsTime;    //新闻时间与来源
    private String ptoUrl;

    public News(String newsTitle, String newsUrl, String newsTime,String ptoUrl) {
        this.newsTitle = newsTitle;
        this.newsUrl = newsUrl;
        this.ptoUrl=ptoUrl;
        this.newsTime = newsTime;
    }

    public String getPtoUrl() {
        return ptoUrl;
    }

    public void setPtoUrl(String ptoUrl) {
        this.ptoUrl = ptoUrl;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
}

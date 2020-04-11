package com.bignerdranch.android.myapplication;

import cn.bmob.v3.BmobObject;

/**
 * Created by T540p on 2017/11/7.
 */

public class Comment extends BmobObject {
    private String MingleNo;
    private String Observer;
    private String mContent;
    private String Author;
    private String ObPhoto;
    private String mmTime;

    public Comment(){}

    public Comment(String MingleNo,String Observer,String ObPhoto,String mComment,String Author,String mmTime){
        this.Author=Author;
        this.mContent=mComment;
        this.MingleNo=MingleNo;
        this.ObPhoto=ObPhoto;
        this.Observer=Observer;
        this.mmTime=mmTime;
    }
    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getMingleNo() {
        return MingleNo;
    }

    public void setMingleNo(String mingleNo) {
        MingleNo = mingleNo;
    }

    public String getObserver() {
        return Observer;
    }

    public void setObserver(String observer) {
        Observer = observer;
    }

    public String getObPhoto() {
        return ObPhoto;
    }

    public void setObPhoto(String obPhoto) {
        ObPhoto = obPhoto;
    }

    public String getMmTime() {
        return mmTime;
    }

    public void setMmTime(String mmTime) {
        this.mmTime = mmTime;
    }
}



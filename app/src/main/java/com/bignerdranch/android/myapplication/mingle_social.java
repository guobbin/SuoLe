package com.bignerdranch.android.myapplication;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by T540p on 2017/8/15.
 */

public class mingle_social extends BmobObject implements Serializable {

    public String Writer;
    private String Text;
    private String Good;
    private String WriterPersonalIcon;
    private String mingleId;
    private String mTime;

    public mingle_social(){}

    public mingle_social(String mingleId,String Writer,String Text,String Good,String WriterPersonalIcon,String Time){
        this.Writer=Writer;
        this.Text=Text;
        this.Good=Good;
        this.WriterPersonalIcon=WriterPersonalIcon;
        this.mingleId=mingleId;
        this.mTime=Time;
    }

    public String getGood() {
        return Good;
    }

    public void setGood(String good) {
        Good = good;
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        Writer = writer;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getWriterPersonalIcon() {
        return WriterPersonalIcon;
    }

    public void setWriterPersonalIcon(String writerPersonalIcon) {
        WriterPersonalIcon = writerPersonalIcon;
    }

    public String getMingleId() {
        return mingleId;
    }

    public void setMingleId(String mingleId) {
        this.mingleId = mingleId;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }
}
package com.bignerdranch.android.myapplication;

import org.litepal.crud.DataSupport;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by T540p on 2017/8/1.
 */

public class User extends BmobObject {
    private String userName;
    private String password;
    private String phoneNumber;
    private BmobFile personalIcon;
    private String petName;
    private String birthday;
    private String sex;
    private String school;
    private String academy;
    private String major;
    private String grade;

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword(){
        return password;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public BmobFile getPersonalIcon(){
        return personalIcon;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public void setUserName(String userName){
        this.userName=userName;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }

    public void setPersonalIcon(BmobFile personalIcon){
        this.personalIcon=personalIcon;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}

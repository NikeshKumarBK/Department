package com.example.user.department;

/**
 * Created by User on 4/6/2016.
 */
public class RowItem {
    private String assign,date,subCode,subName;

    public RowItem(String assign,String subCode,String subName, String date) {
        this.assign = assign;
        this.subCode = subCode;
        this.subName = subName;
        this.date = date;
    }


    public String getAssign() {
        return assign;
    }

    public void setAssign(String assign) {
        this.assign = assign;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }



}
package com.example.nd_excel.Bean;

import java.io.Serializable;
import java.util.ArrayList;

public class MixBean implements Serializable {
    private int type;//text=1 photo=2 table=3

    //1
    private String textStr;

    public MixBean(int type, String textStr) {
        this.type = type;
        this.textStr = textStr;
    }
    //onle datatype

    //2
    private String photoStr;

    public MixBean(int type, String textStr, String photoStr) {
        this.type = type;
        this.textStr = textStr;
        this.photoStr = photoStr;
    }

    //3
    private ArrayList<ArrayList<String>>lists;

    public MixBean(int type, String textStr, String photoStr, ArrayList<ArrayList<String>> lists) {
        this.type = type;
        this.textStr = textStr;
        this.photoStr = photoStr;
        this.lists = lists;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTextStr() {
        return textStr;
    }

    public void setTextStr(String textStr) {
        this.textStr = textStr;
    }

    public String getPhotoStr() {
        return photoStr;
    }

    public void setPhotoStr(String photoStr) {
        this.photoStr = photoStr;
    }

    public ArrayList<ArrayList<String>> getLists() {
        return lists;
    }

    public void setLists(ArrayList<ArrayList<String>> lists) {
        this.lists = lists;
    }
}

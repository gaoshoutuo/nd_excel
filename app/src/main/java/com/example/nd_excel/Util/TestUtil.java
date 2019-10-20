package com.example.nd_excel.Util;

import org.json.JSONObject;

import java.util.ArrayList;

public class TestUtil {//测试方法包
    //mainactivity 的menu arraylist 的测试
    public static ArrayList<String> makeArray(){
        ArrayList list=new ArrayList();
        for (int i=0;i<20;i++){
            list.add("测试列表"+i);


        }
        return list;
    }

}

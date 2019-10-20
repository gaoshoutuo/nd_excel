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

    //解析excel xml  解析器包含关系
    public static void parseExcelXml_1(){

    }

    //解析excel xml  解析excel中各表xml数据
    public static void parseExcelXml_2(){

    }

    //解析excel xml  解析excel中workbook与表名xml的映射
    public static void parseExcelXml_3(){

    }

    //解析excel xml  解析excel中sharedString的索引数据
    public static void parseExcelXml_4(){

    }

}

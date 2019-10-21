package com.example.nd_excel.Util;

import android.content.res.Resources;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

public class TestUtil {//测试方法包
    //mainactivity 的menu arraylist 的测试
    public static ArrayList<String> makeArray(){
        ArrayList list=new ArrayList();
        for (int i=0;i<20;i++){
            list.add("测试列表"+i);
        }
        return list;
    }

    //解析excel xml  解析器表1包含关系
    public static HashMap<String, String> parseExcelXml_1(String filename){
        //String []in=new String[9999];
        HashMap<String,String>map=new HashMap<>();
        try {
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=factory.newPullParser();
            //xmlPullParser.setInput(new StringReader(xmlData));
            try {
                InputStream inputStream=App.getInstance().getResources().getAssets().open(filename);
                xmlPullParser.setInput(inputStream,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //int i=0;
          /*  try {
                for (int u=0;u<20;u++){
                    xmlPullParser.next();
                    String nodename=xmlPullParser.getName();
                    //String key=xmlPullParser.getAttributeValue(null,"r");
                    //String value=xmlPullParser.nextText();
                    Log.e("西溪湿地","--"+nodename+"--");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            //StringBuilder sb=new StringBuilder();

            //有些只有c 没有v 所以直接跳过了
            while ( xmlPullParser.getEventType()!=xmlPullParser.END_DOCUMENT){//我服了 跟文件格式有关 我得多打很多空格才解决问题
                //String nodename=xmlPullParser.getName();
                //Log.e("nodename",".........."+xmlPullParser.getName()+"..........");
                if ("c".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.START_TAG){
                    //in[Integer.parseInt(xmlPullParser.getAttributeValue(null,"r"))]=xmlPullParser.nextText();//类似哈希表
                    String key=xmlPullParser.getAttributeValue(null,"r");
                    //String value=xmlPullParser.nextText();
                    //if (value!=null)map.put(key,value);
                    xmlPullParser.next();
                    if ("v".equals(xmlPullParser.getName())&& xmlPullParser.getEventType()==xmlPullParser.START_TAG){
                        String value=xmlPullParser.nextText();
                        //Log.e("西溪湿地","-------------"+key+"-"+value+"-----");
                        if (value!=null)map.put(key,value);
                    }
                }
                if ("sheetData".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.END_TAG){

                    return map;//类似哈希表
                }
                xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    //解析excel xml  解析器表1 超链接关系
    public static void parseExcelXml_2(){

    }



    //解析excel xml  解析excel中workbook与表名xml的映射
    //坑爹点 带冒号的是namespace
    public static HashMap<String, String> parseExcelXml_3(String filename){
        HashMap<String,String>map=new HashMap<>();
        try {
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=factory.newPullParser();
            try {
                InputStream inputStream=App.getInstance().getResources().getAssets().open(filename);
                xmlPullParser.setInput(inputStream,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }

            while ( xmlPullParser.getEventType()!=xmlPullParser.END_DOCUMENT){//我服了 跟文件格式有关 我得多打很多空格才解决问题
                if ("sheet".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.START_TAG){
                    //String id=xmlPullParser.getAttributeValue("r","id");
                    String id=xmlPullParser.getAttributeValue(2);
                    id=id.substring(3);
                    String name=xmlPullParser.getAttributeValue(null,"name");
                    Log.e("---",id+name+"1--1");
                    map.put(id,name);//id 1 2 3 可以变 具体看情况 界面怎么跳 按照上面对
                }
                if ("sheets".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.END_TAG){
                    return map;//类似哈希表
                }
                xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    //解析excel xml  解析excel中sharedString的索引数据
    //坑爹点 一个单元个有si 但是会有很多不同格式的t si应该只对一个映射 所以t要拼起来
    public static HashMap<String, String> parseExcelXml_4(String filename){
        HashMap<String,String>map=new HashMap<>();
        try {
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=factory.newPullParser();
            try {
                InputStream inputStream=App.getInstance().getResources().getAssets().open(filename);
                xmlPullParser.setInput(inputStream,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            int i=-1;
            StringBuffer value=new StringBuffer();
            while ( xmlPullParser.getEventType()!=xmlPullParser.END_DOCUMENT){//我服了 跟文件格式有关 我得多打很多空格才解决问题
                if ("si".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.START_TAG){
                    xmlPullParser.next();
                }
                if ("t".equals(xmlPullParser.getName())&& xmlPullParser.getEventType()==xmlPullParser.START_TAG){
                   //String newValue=xmlPullParser.nextText()+value;
                    value.append(xmlPullParser.nextText());
                }
                if ("si".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.END_TAG){
                    i++;map.put(i+"",value.toString());value=new StringBuffer();
                }
                if ("sst".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.END_TAG){
                    return map;
                }
                xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    //解析excel xml  解析excel中各表xml数据
    public static void parseExcelXml_5(){

    }

    //如表一解析 所有xml sheet数据到 一个总的hashmap<sheet_x,hashmap> 再将这个hashmap解析成json 配合 shaString
    public static void parseAllSheet(){
        HashMap<String,HashMap<String,String>>bigmap=new HashMap<>();
        long l1=System.currentTimeMillis();
        for (int i=1;i<=625;i++){
            HashMap<String,String>map=parseExcelXml_1("sheet"+i+".xml");
            Log.e("完成进度","完成第"+i+"分");//有点不敢尝试计算量可能有点恐怖
            bigmap.put("sheet"+i,map);
        }
        long l2=System.currentTimeMillis();
        Log.e("完成进度",(l2-l1)/1000+"秒");

    }
    public static String initXmlStr(String filename){
        String xmlstr= getFileString(App.getInstance().getResources(), filename);
       // Log.e("map0",xmlstr);
        return xmlstr;
    }

    public static String getFileString(Resources resources, String filename){
        StringBuilder sb= new StringBuilder();
        try {
            InputStream is = resources.getAssets()
                    .open(filename);

            int len=0;
            byte []bytes=new byte[8000];

            while ((len=is.read(bytes))!=-1){
                sb.append(new String(bytes,0,len));
            }
            is.close();

            //Bitmap bm = BitmapFactory.decodeStream(bis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    //String 切割机

}

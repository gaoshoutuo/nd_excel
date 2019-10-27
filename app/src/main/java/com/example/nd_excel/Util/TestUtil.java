package com.example.nd_excel.Util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.example.nd_excel.Activity.TestActivity;
import com.example.nd_excel.Adapter.TableAdapter;
import com.example.nd_excel.MainActivity;
import com.example.nd_excel.R;
import com.kelin.scrollablepanel.library.ScrollablePanel;
import com.leon.lfilepickerlibrary.LFilePicker;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class TestUtil {//测试方法包  /storage/emulated/0
    //mainactivity 的menu arraylist 的测试
    public static String path="/storage/emulated/0";
    public static ArrayList<String> makeArray(){
        ArrayList list=new ArrayList();
        for (int i=0;i<20;i++){
            list.add("测试列表"+i);
        }
        return list;
    }

    //解析excel sheet xml 单元格名称与index关系
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

    //解析excel xml  解析器表1 超链接关系 E553 3.1.2.1
    public static HashMap<String, String> parseExcelXml_2(String filename){
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
            int p=2;
           // map.put("目录",1+"");
          while ( xmlPullParser.getEventType()!=xmlPullParser.END_DOCUMENT){//我服了 跟文件格式有关 我得多打很多空格才解决问题
                if ("hyperlink".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.START_TAG){
                    String key=xmlPullParser.getAttributeValue(null,"ref");
                    if (key.contains(":")){
                        key=key.substring(0,key.indexOf(":"));
                    }
                    String value=xmlPullParser.getAttributeValue(null,"location");
                    //String valueD=xmlPullParser.getAttributeValue(null,"display");
                    value=value.substring(value.indexOf("'")+1,value.lastIndexOf("'"));
                    //Log.e("3,3,3,3",p+"--"+key+"---"+value);
                    map.put(value,key);
                    p++;

                }
                if ("worksheet".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.END_TAG){
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
                    //Log.e("---",id+"--"+name+"--");
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
                    i++;map.put(i+"",value.toString());
                    //Log.e("xml4_shared",i+"--"+value.toString());
                    value=new StringBuffer();
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

    //解析excel xml  解析excel中各表worksheet xml数据
    public static void parseExcelXml_5(){

    }
    public static int imageId=1;
    //解析drawingx 文件中的image以及cell 以及图片内的单元格  sheet x->draw x-> image x y z->
    public static HashMap<String, String> parseExcelXml_6(int drawFileId, HashMap<String,String>map){
        //HashMap<String,String>map=new HashMap<>();
        StringBuffer stringBuffer=new StringBuffer();
        try {
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=factory.newPullParser();
            try {
                InputStream inputStream=App.getInstance().getResources().getAssets().open("drawings/drawing"+drawFileId+".xml");//drawing340.xml
                xmlPullParser.setInput(inputStream,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }

            //奇葩矫正规则

            if (drawFileId==59){
                stringBuffer.append("image").append(75).append("|");
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==102){
                stringBuffer.append("image").append(191).append("|");
                stringBuffer.append("image").append(192).append("|");
                stringBuffer.append("image").append(193).append("|");
                stringBuffer.append("image").append(194).append("|");
                stringBuffer.append("image").append(195).append("|");
                imageId=196;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==107){
                stringBuffer.append("image").append(203).append("|");
                stringBuffer.append("image").append(204).append("|");
                imageId=205;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==108){
                stringBuffer.append("image").append(206).append("|");
                stringBuffer.append("image").append(207).append("|");
                imageId=208;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==110){
                stringBuffer.append("image").append(205).append("|");
                imageId=208;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==111){
                stringBuffer.append("image").append(208).append("|");
                stringBuffer.append("image").append(209).append("|");
                imageId=210;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==113){
                stringBuffer.append("image").append(211).append("|");
                stringBuffer.append("image").append(212).append("|");
                imageId=213;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==125){
                stringBuffer.append("image").append(224).append("|");
                stringBuffer.append("image").append(225).append("|");
                imageId=226;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==127){
                stringBuffer.append("image").append(227).append("|");

                imageId=228;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==128){
                stringBuffer.append("image").append(228).append("|");
                stringBuffer.append("image").append(229).append("|");
                imageId=230;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==129){
                stringBuffer.append("image").append(230).append("|");
                imageId=231;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==169){
                stringBuffer.append("image").append(316).append("|");
                stringBuffer.append("image").append(315).append("|");
                imageId=317;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==171){
                stringBuffer.append("image").append(322).append("|");
                stringBuffer.append("image").append(323).append("|");
                stringBuffer.append("image").append(324).append("|");
                stringBuffer.append("image").append(325).append("|");
                stringBuffer.append("image").append(326).append("|");
                imageId=327;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==175){
                stringBuffer.append("image").append(339).append("|");
                imageId=340;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==176){
                stringBuffer.append("image").append(340).append("|");
                imageId=341;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==186){
                stringBuffer.append("image").append(353).append("|");
                imageId=354;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==187){
                stringBuffer.append("image").append(353).append("|");
                imageId=354;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==188){
                stringBuffer.append("image").append(354).append("|");
                imageId=354;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==189){
                stringBuffer.append("image").append(354).append("|");
                imageId=354;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==190){
                stringBuffer.append("image").append(355).append("|");
                imageId=354;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==191){
                stringBuffer.append("image").append(355).append("|");
                imageId=356;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==192){
                stringBuffer.append("image").append(355).append("|");
                imageId=356;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==193){
                stringBuffer.append("image").append(355).append("|");
                imageId=356;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==204){
                stringBuffer.append("image").append(366).append("|");
                imageId=367;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==217){
                stringBuffer.append("image").append(382).append("|");
                stringBuffer.append("image").append(383).append("|");
                imageId=384;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==220){
                stringBuffer.append("image").append(387).append("|");
                imageId=388;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==224){
                stringBuffer.append("image").append(392).append("|");
                stringBuffer.append("image").append(393).append("|");
                stringBuffer.append("image").append(366).append("|");
                imageId=394;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==251){
                stringBuffer.append("image").append(436).append("|");
                stringBuffer.append("image").append(439).append("|");
                imageId=440;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==255){
                stringBuffer.append("image").append(436).append("|");
                stringBuffer.append("image").append(449).append("|");
                stringBuffer.append("image").append(450).append("|");
                imageId=451;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==259){
                stringBuffer.append("image").append(460).append("|");
                stringBuffer.append("image").append(469).append("|");
                stringBuffer.append("image").append(470).append("|");
                stringBuffer.append("image").append(471).append("|");
                imageId=472;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==336){
                stringBuffer.append("image").append(629).append("|");
                stringBuffer.append("image").append(630).append("|");
                imageId=631;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==337){
                stringBuffer.append("image").append(611).append("|");
                imageId=631;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }else if (drawFileId==345){
                stringBuffer.append("image").append(642).append("|");
                stringBuffer.append("image").append(641).append("|");
                imageId=643;
                map.put(drawFileId+"",stringBuffer.toString());
                return map;//类似哈希表
            }





            while ( xmlPullParser.getEventType()!=xmlPullParser.END_DOCUMENT){
                //解析图片
               /* if ("xdr".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.START_TAG
                        &&xmlPullParser.getAttributeNamespace(0).equals("pic")){*/
                    //xmlPullParser.next();
                  /*  while (!("xdr".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.START_TAG
                            &&xmlPullParser.getAttributeNamespace(0).equals("cNvPr"))){
                        xmlPullParser.next();
                    }*/
               // Log.e("desc",xmlPullParser.getName()+"--");

                if (("xdr:pic".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.START_TAG
                        )){
                    xmlPullParser.next();
                    //Log.e("111",xmlPullParser.getName()+"--");
                    if (("xdr:nvPicPr".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.START_TAG
                    )){
                        xmlPullParser.next();
                       // Log.e("222",xmlPullParser.getName()+"--");
                        if (("xdr:cNvPr".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.START_TAG
                        )){
                           // Log.e("desc",xmlPullParser.getAttributeValue(null,"id")+"--");//只有这样才能找到图片
                            //map.put(drawFileId)

                            stringBuffer.append("image").append(imageId).append("|");//contains image 就行了
                            imageId++;
                        }
                    }

                }

                //}
                //解析图片内单元格
                if (("a:t".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.START_TAG
                )){
                    stringBuffer.append(xmlPullParser.nextText()).append("|");
                }

                if ("xdr:wsDr".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.END_TAG){
                    map.put(drawFileId+"",stringBuffer.toString());
                    //Log.e("desc",stringBuffer.toString());
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

    //解析图片的draw 在worksheet 中分布 1-625 有无draw
    public static ArrayList<Integer> parseExcelXml_7(ArrayList<Integer>list,int sheetId){

        try {
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=factory.newPullParser();
            try {
                InputStream inputStream=App.getInstance().getResources().getAssets().open("worksheets/sheet"+sheetId+".xml");
                xmlPullParser.setInput(inputStream,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            while ( xmlPullParser.getEventType()!=xmlPullParser.END_DOCUMENT){//我服了 跟文件格式有关 我得多打很多空格才解决问题
                if ("drawing".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.START_TAG){
                    list.add(sheetId);
                    return list;
                    //Log.e("draw",sheetId+"");
                }

                //sheetData 也不是万能的
                if ("worksheet".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.END_TAG){
                    return list;//类似哈希表
                }
                xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    //解析excel xml  表1 包含关系
    public static HashMap<String, String> parseExcelXml_8(String filename){
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
            StringBuffer bufferB=new StringBuffer();
            StringBuffer bufferC=new StringBuffer();
            StringBuffer bufferD=new StringBuffer();
            StringBuffer bufferE=new StringBuffer();
            StringBuffer bufferF=new StringBuffer();
            while ( xmlPullParser.getEventType()!=xmlPullParser.END_DOCUMENT){//我服了 跟文件格式有关 我得多打很多空格才解决问题
                if ("c".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.START_TAG){
                String key=xmlPullParser.getAttributeValue(null,"r");
                    xmlPullParser.next();
                    if ("v".equals(xmlPullParser.getName())&& xmlPullParser.getEventType()==xmlPullParser.START_TAG){
                        switch (key.substring(0,1)){
                            case "B":
                                bufferB.append(key).append("|");
                                break;

                            case "C":
                                bufferC.append(key).append("|");
                                break;

                            case "D":
                                bufferD.append(key).append("|");
                                break;

                            case "E":
                                bufferE.append(key).append("|");
                                break;

                            case "F":
                                bufferF.append(key).append("|");
                                break;

                                default:break;
                        }
                    }
                }
                if ("sheetData".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.END_TAG){
                    map.put("B",bufferB.toString());
                    map.put("C",bufferC.toString());
                    map.put("D",bufferD.toString());
                    map.put("E",bufferE.toString());
                    map.put("F",bufferF.toString());
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

    //如表一解析 所有xml sheet数据到 一个总的hashmap<sheet_x,hashmap> 再将这个hashmap解析成json 配合 shaString
    public static void parseAllSheet(){
        HashMap<String,HashMap<String,String>>bigmap=new HashMap<>();
        long l1=System.currentTimeMillis();
        JSONObject json=new JSONObject();
        try {


        for (int i=1;i<=625;i++){
            HashMap<String,String>map=parseExcelXml_1("worksheets/"+"sheet"+i+".xml");
            //bigmap.put("sheet"+i,map);
            JSONObject sheetJson=new JSONObject();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key=(String)entry.getKey();
                String value=(String)entry.getValue();
                sheetJson.put(key,value);
                }
                json.put("sheet"+i+".xml",sheetJson.toString());
            Log.e("完成进度","完成第"+i+"分");//有点不敢尝试计算量可能有点恐怖
        }


        }catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.e("完成进度",json.toString());//有点不敢尝试计算量可能有点恐怖
        String filename=path+"/tuo/"+jsonFileNames[0];
        Log.e("完成进度",filename);
        wirteFileToE(filename,json.toString());
        long l2=System.currentTimeMillis();
        Log.e("完成进度",(l2-l1)/1000+"秒");

    }

    //迭代解析所以drawing xml
    public static HashMap<String,String> parseDrawXml(){

        HashMap<String,String>map=new HashMap<>();
        ArrayList<Integer>list=new ArrayList<>();
        for (int i=1;i<=345;i++){
            parseExcelXml_6(i,map);
        }
        for (int i=1;i<=625;i++){
            parseExcelXml_7(list,i);
        }
        int t=0;
        JSONObject json=new JSONObject();
        //String []strValue=new String[346];
        try {
        for (int i=1;i<=345;i++){
            String value=map.get(i+"");
            String sheetName="sheet"+list.get(i-1)+".xml";//不对
            json.put(sheetName,value);
            Log.e("完成进度",i+"--"+value+"--"+sheetName);//image599|image600|--sheet547.xml
        }



      /*  for (Map.Entry<String, String> entry : map.entrySet()) {
            String key=(String)entry.getKey();
            String value=(String)entry.getValue();
            String sheetName="sheet"+list.get(t)+".xml";
            t++;
            Log.e("完成进度",key+"--"+value+"--"+sheetName);
                json.put(sheetName,value);
        }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String filename=path+"/tuo/"+jsonFileNames[1];//image
       // Log.e("完成进度",filename);
        //Log.e("完成进度",json.toString());
        wirteFileToE(filename,json.toString());
        //Log.e("list_map",list.size()+"---"+map.size());

        return map;
    }

    //解析表一的前后布局成JSON
    public static void makeJsonMenu(){
        long l1=System.currentTimeMillis();
        HashMap map=parseExcelXml_8("sheet888.xml");
        String str[]={"B","C","D","E","F"};//对就是这种对象的命名我已经找到办法了去年11月结果搁置忘记
        String strB[]=((String)map.get(str[0])).split("\\|");
        String strC[]=((String)map.get(str[1])).split("\\|");
        String strD[]=((String)map.get(str[2])).split("\\|");
        String strE[]=((String)map.get(str[3])).split("\\|");
        String strF[]=((String)map.get(str[4])).split("\\|");
        JSONObject obj=new JSONObject();

        makeJsonMenuPart_1(strB,strC,obj);serZeroStrsIndex();
        makeJsonMenuPart_1(strC,strD,obj);serZeroStrsIndex();//224
        makeJsonMenuPart_1(strD,strE,obj);serZeroStrsIndex();//319
        makeJsonMenuPart_1(strE,strF,obj);serZeroStrsIndex();//143

        String filename=path+"/tuo/"+jsonFileNames[2];
        Log.e("完成进度",filename);
        wirteFileToE(filename,obj.toString());
        long l2=System.currentTimeMillis();
        Log.e("完成进度",(l2-l1)/1000+"秒");

        Log.e("json",obj.toString());//


    }
    //输入数组 以及字符 求出关系 最后一个必然包含所有制
    public static int sStrsIndex=0;
    public static void serZeroStrsIndex(){
        sStrsIndex=0;
    }
    public static JSONObject makeJsonMenuPart_1(String [] fStrs,String [] sStrs,JSONObject obj){
        StringBuffer buffer=new StringBuffer();

        for (int i=0;i<fStrs.length;i++){
            Log.e("json",i+"-"+fStrs.length+"-"+sStrsIndex+"-"+sStrs.length);
            if (i+1==fStrs.length){
                for (int k=sStrsIndex;k<sStrs.length;k++){
                    buffer.append(sStrs[k]).append("|");
                    try {
                        obj.put(fStrs[i],buffer.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                sStrsIndex=0;
                return obj;
            }

            for (int j=0;j<sStrs.length&&sStrsIndex<sStrs.length;j++){
                String str1=fStrs[i];
                String str2=fStrs[i+1];
                String str3=sStrs[sStrsIndex];
                int index1= Integer.parseInt(str1.substring(1));
                int index2= Integer.parseInt(str2.substring(1));
                int index3= Integer.parseInt(str3.substring(1));

                if (index3>=index1&&index3<index2){
                    //Log.e("json",index1+"--"+index2+"--"+index3+"--");
                    buffer.append(str3).append("|");
                    sStrsIndex++;
                }else {
                    try {
                        String abc=buffer.toString();
                        obj.put(str1,abc);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    buffer=new StringBuffer();
                    break;
                }
            }

        }
        return obj;
    }



    //对齐  xml与章节的关系
    public static void alignXmlNum(/*HashMap<String,String>map1,HashMap<String,String>map2*/){
        //String []mapStr1=new String[630];
        //String []mapStr2=new String[630];
        JSONObject json=new JSONObject();
        long l1=System.currentTimeMillis();
        try {
        HashMap<String,String>map1=TestUtil.parseExcelXml_2("sheet888.xml");//600多 只算超链接
        HashMap<String,String>map2=TestUtil.parseExcelXml_3("workbook.xml");//625 是xml都算
             Log.e("sheet"+".xml",map1.size()+"");
        for (int i=2;i<=map2.size();i++){
            //map1.get(i)
            String value2=map2.get(i+"");
            if (map1.get(value2)==null||map1.get(value2).equals("")){
                continue;
            }else {
                //json.put("sheet"+i+".xml",value2);//不应该这样做 反过来
                json.put(value2,"sheet"+i+".xml");
                //Log.e("sheet"+i+".xml",value2);
            }
        }
        json.put("sheet"+1+".xml","目录");
        //String filename=path+"/tuo/"+jsonFileNames[7];
            String filename=path+"/tuo/"+jsonFileNames[8];
        Log.e("完成进度",filename);
        wirteFileToE(filename,json.toString());
        long l2=System.currentTimeMillis();
        Log.e("完成进度",(l2-l1)/1000+"秒");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // xml2 E554 3.1.2,3 make
    public static void makeHypJsonToE(){
        //jsonFileNames[9];
        long l1=System.currentTimeMillis();
        try {
        JSONObject json=new JSONObject();
        HashMap<String,String>map=TestUtil.parseExcelXml_2("sheet888.xml");//582 只算超链接
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key=(String)entry.getKey();
            String value=(String)entry.getValue();
            //Log.e("完成进度",key+"--"+value+"--"+sheetName);
                json.put(value,key);
        }
            String filename=path+"/tuo/"+jsonFileNames[10];
            Log.e("完成进度",filename+map.size());
            wirteFileToE(filename,json.toString());
            long l2=System.currentTimeMillis();
            Log.e("完成进度",(l2-l1)+"ms");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //shared_json make
    public static void makeSharedStringJsonToE(){
        long l1=System.currentTimeMillis();
        HashMap map=TestUtil.parseExcelXml_4("sharedStrings.xml");
        JSONObject json=new JSONObject();
        try {
        for (int i=0;i<map.size();i++){
            json.put(i+"",map.get(i+""));
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String filename=path+"/tuo/"+jsonFileNames[6];
        Log.e("完成进度",filename);
        wirteFileToE(filename,json.toString());
        long l2=System.currentTimeMillis();
        Log.e("完成进度",(l2-l1)/1000+"秒");

    }

    //
    public static void makeImageNameToE(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long l1=System.currentTimeMillis();
                JSONObject json=new JSONObject();
                ConcurrentLinkedQueue<String> clq=TestUtil.zipFileList(TestUtil.path + "/Download/知识点细化0924.zip");
                String abc=null;
               /* String testStr[]="xl/media/image596.jpeg".split("/");
                String testStr2[]=testStr[testStr.length-1].split("\\.");
                Log.e("aaa",testStr[testStr.length-1]+testStr2[0]+testStr2[1]);*/
                while ((abc=clq.poll())!=null){
                    //es.submit(r1);
                    Log.e("aaaaa","----"+abc+"----");
                    String strImg[]=abc.split("/");
                    String littleStr=strImg[strImg.length-1];
                    String []changeStr=littleStr.split("\\.");
                    if (littleStr.contains("wmf")||littleStr.contains("emf")){
                        littleStr=changeStr[0]+"."+"png";
                    }
                    try {
                        json.put(changeStr[0],littleStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                String filename=path+"/tuo/"+jsonFileNames[11];
                Log.e("完成进度",filename);
                wirteFileToE(filename,json.toString());
                long l2=System.currentTimeMillis();
                Log.e("完成进度",(l2-l1)/1000+"秒");
            }
        }).start();
    }

    //Menu_json make
   /* public static void makeJsonToE(){

    }*/

   // about mu ui
    public static void photoUtil(PhotoView view,String filePath){// cacu height and width
        Bitmap bitmap= null;
        try {
            bitmap = BitmapFactory.decodeStream(App.getInstance().getResources().getAssets().open(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        double sh=bitmap.getWidth()/bitmap.getHeight();
        Log.e("width",bitmap.getWidth()+"--"+bitmap.getHeight());
        if (bitmap.getWidth()*bitmap.getHeight()>15000)
        view.getLayoutParams().height= (int) (1080/sh);//1080? 1280?
        else{
            view.getLayoutParams().height= bitmap.getHeight();
            view.getLayoutParams().width=bitmap.getWidth();
        }
        view.setImageBitmap(bitmap); view.enable();
    }

    //textUtil
    public static void TextUtil(TextView view,String textStr){
        int mag=textStr.length()/10 +1;
        view.getLayoutParams().height=view.getLayoutParams().height *mag;
    }

    //tableUtil TestPannelAdapter
    public static void tableUtil(ScrollablePanel view, ArrayList<ArrayList<String>>lists,Activity activity){
        TableAdapter tableAdapter = new TableAdapter(lists,activity);
        //ScrollablePanel scrollablePanel = (ScrollablePanel) findViewById(R.id.scrollable_panel);
        view.setPanelAdapter(tableAdapter);
    }

    //make seq 1 2
    public static void makeSeqTextJsonToE(){
        long l1=System.currentTimeMillis();
        JSONObject json=new JSONObject();
        JSONObject hypJson=JsonConCap.getHyp_2_Json();
        Iterator<String> iterator1 =hypJson.keys();
        while(iterator1.hasNext()){
            String key1 = iterator1.next();
            String value1 = hypJson.optString(key1);
            makeSeqPart1(key1,value1,json);
        }

        String filename=path+"/tuo/"+jsonFileNames[14];
        Log.e("完成进度",filename);
        wirteFileToE(filename,json.toString());
        long l2=System.currentTimeMillis();
        Log.e("完成进度",(l2-l1)/1000+"秒");
    }

    public static void makeSeqImageJsonToE(){
        long l1=System.currentTimeMillis();

        JSONObject json=new JSONObject();
       /* JSONObject imageJson=JsonConCap.getImageJson();
        JSONObject imageNameJson=JsonConCap.getImageNameJson();
        JSONObject hypJson=JsonConCap.getHyp_2_Json();

        Iterator<String> iterator =hypJson.keys();
        while (iterator.hasNext()){
            String key=iterator.next();
            String value=hypJson.optString(key);
        }


        Iterator<String> iterator1 =imageJson.keys();
        while (iterator1.hasNext()){
            StringBuffer buffer=new StringBuffer();
            String key=iterator1.next();
            String value=imageJson.optString(key);
            String imageStr[]=value.split("\\|");
            for (int i=0;i<imageStr.length;i++){
                String readImageName=imageNameJson.optString(imageStr[i]);
                if (readImageName!=null)buffer.append(readImageName).append("|");
            }
            try {
                json.put(value,buffer.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }*/
        JSONObject imageJson=JsonConCap.getImageJson();
        JSONObject imageNameJson=JsonConCap.getImageNameJson();
        JSONObject hypJson=JsonConCap.getHyp_2_Json();
        JSONObject xmlChapterJson=JsonConCap.getXmlChapter_2_Json();
        Iterator<String>iterator=hypJson.keys();
        while (iterator.hasNext()){
            StringBuffer buffer=new StringBuffer();
            String key=iterator.next();
            String value=hypJson.optString(key);
            if (value==null||"".equals(value))continue;
            String xmlData=xmlChapterJson.optString(value);
            if (xmlData==null||"".equals(xmlData))continue;
            String imageStr=imageJson.optString(xmlData);
            String imageArr[]=imageStr.split("\\|");
            for (int i=0;i<imageArr.length;i++){
                String readImageName=imageNameJson.optString(imageArr[i]);
                if (readImageName!=null)buffer.append(readImageName).append("|");
            }
            try {
                Log.e(key,buffer.toString());
                json.put(key,buffer.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        String filename=path+"/tuo/"+jsonFileNames[13];
        Log.e("完成进度",filename);
        wirteFileToE(filename,json.toString());
        long l2=System.currentTimeMillis();
        Log.e("完成进度",(l2-l1)/1000+"秒");
    }

    public static void makeSeqPart1(String key,String value,JSONObject json) {
        StringBuffer buffer1=new StringBuffer();
        try {
        JSONObject sharedStringJson=JsonConCap.getSharedStringJson();
        JSONObject cacuJson=JsonConCap.getCacuJson();
        JSONObject xmlChapterJson=JsonConCap.getXmlChapter_2_Json();
        String xmlData=xmlChapterJson.optString(value);
            JSONObject sheetJson=null;

        if (xmlData!=null||!xmlData.equals("")){

            //Log.e("-----","--"+value+"--");
            String sheetData=cacuJson.optString(xmlData);
            if (sheetData==null||"".equals(sheetData))return;
            sheetJson=new JSONObject(sheetData);
        }else {
            return;
        }
        //limit null

            String indexs=makeSeqPart2(sheetJson);
        if (indexs==null||"".equals(indexs))return;
            String index[]=indexs.split("\\|");
            for (int i=0;i<index.length;i++){
                String sharedValue=sharedStringJson.optString(index[i]);
                buffer1.append(sharedValue).append("|");
            }
            json.put(key,buffer1.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String makeSeqPart2(JSONObject json) {
       StringBuffer buffer1=new StringBuffer();
        StringBuffer buffer2=new StringBuffer();
            Iterator<String> iterator1 =json.keys();
            while(iterator1.hasNext()){
                String key1 = iterator1.next();
                String value1 = json.optString(key1);
                buffer1.append(value1).append("|");
            }


            String buffStr1[]=buffer1.toString().split("\\|");
        if (buffStr1.length==0)return null;
                    int[] integerArr=new int[buffStr1.length];
                    for (int i=0;i<buffStr1.length;i++){

                        if (buffStr1[i]==null||"".equals(buffStr1[i])){
                            Log.e("-----","--"+buffer1.toString()+"--");
                            continue;
                        }
                        integerArr[i]=Integer.parseInt(buffStr1[i]);

                    }
        Arrays.sort(integerArr);
        for (int i=0;i<buffStr1.length;i++){
            buffer2.append(integerArr[i]+"").append("|");//
        }

        return buffer2.toString();
    }

        //1 2 text  34 image
        public static void makeSeqPart3(String key,String value,JSONObject json) {
            StringBuffer buffer1=new StringBuffer();
            try {
                JSONObject sharedStringJson=JsonConCap.getSharedStringJson();
                JSONObject cacuJson=JsonConCap.getCacuJson();
                JSONObject xmlChapterJson=JsonConCap.getXmlChapter_2_Json();
                String xmlData=xmlChapterJson.optString(value);
                JSONObject sheetJson=null;
                if (xmlData!=null||!xmlData.equals("")){

                    //Log.e("-----","--"+value+"--");
                    String sheetData=cacuJson.optString(xmlData);
                    if (sheetData==null||"".equals(sheetData))return;
                    sheetJson=new JSONObject(sheetData);
                }else {
                    return;
                }
                //limit null

                String indexs=makeSeqPart4(sheetJson);
                if (indexs==null||"".equals(indexs))return;
                String index[]=indexs.split("\\|");
                for (int i=0;i<index.length;i++){
                    String sharedValue=sharedStringJson.optString(index[i]);
                    buffer1.append(sharedValue).append("|");
                }
                json.put(key,buffer1.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    public static String makeSeqPart4(JSONObject json) {
        StringBuffer buffer1=new StringBuffer();
        StringBuffer buffer2=new StringBuffer();
        Iterator<String> iterator1 =json.keys();
        while(iterator1.hasNext()){
            String key1 = iterator1.next();
            String value1 = json.optString(key1);
            buffer1.append(value1).append("|");
        }


        String buffStr1[]=buffer1.toString().split("\\|");
        if (buffStr1.length==0)return null;
        int[] integerArr=new int[buffStr1.length];
        for (int i=0;i<buffStr1.length;i++){

            if (buffStr1[i]==null||"".equals(buffStr1[i])){
                Log.e("-----","--"+buffer1.toString()+"--");
                continue;
            }
            integerArr[i]=Integer.parseInt(buffStr1[i]);

        }
        Arrays.sort(integerArr);
        for (int i=0;i<buffStr1.length;i++){
            buffer2.append(integerArr[i]+"").append("|");//
        }

        return buffer2.toString();
    }

    static String[]jsonFileNames=new String[]{
      "cacu_1.json",
            "image_1.json",
            "menu_1.json",
            "q_a_1.json",
            "pre_say_1.json",
            "sequential_aeeangement_1.json",
            "shared_string_1.json",
            "xml_chapter_1.json",
            "xml_chapter_2.json",
            "hyp.json",
            "hyp_1.json",
            "image_name_1.json",//index 11
            "text_seq.json",
            "image_sep.json",
            "text_seq_1.json"//index 14
    };

    /**
     * cacu.json_1.json 所有表个个单元格对应映射
     *
     * sequential_aeeangement_1.json 总秩序
     * image
     * @param filename
     * @return
     */

    //讲parseXml 形成的json存储到本地某个文件夹下
    public static String readFileInE(String filename){
        //String floder=Environment.getExternalStorageDirectory().getPath();
        //String flleNameReal=floder+"/"+filename;
        StringBuffer buffer=new StringBuffer();
        //File file=new File(filename);
        try {

            //FileReader fr= new FileReader(filename);
            //FileInputStream fis=new FileInputStream(filename);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(filename), "UTF-8");
            BufferedReader reader = new BufferedReader(isr);

            //BufferedReader reader=new BufferedReader(new FileReader(filename));
            //byte []fileByte=new byte[8000];
            //int len=0;
            String str=null;
            while ((str=reader.readLine())!=null){
                buffer.append(str);
            }
           // Log.e("buffer",buffer.toString()+"===");
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();

    }

    public static JSONObject readJsonInAsset(String jsonName){
        JSONObject json=null;
        StringBuffer buffer=new StringBuffer();
        try {
            InputStream is=App.getInstance().getResources().getAssets().open(jsonName);
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String str=null;
            while ((str=reader.readLine())!=null){
                buffer.append(str);
            }
            // Log.e("buffer",buffer.toString()+"===");
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            json=new JSONObject(buffer.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    // 解压文件该目录下所有文件列表 多线程的 生产者消费者
    public static ConcurrentLinkedQueue zipFileList(String zippath){
        ConcurrentLinkedQueue<String> clq=new ConcurrentLinkedQueue();
        try {
            File file = new File(zippath);
            ZipInputStream zipInput = new ZipInputStream(new FileInputStream(file));
            ZipEntry entry = null;
            while((entry = zipInput.getNextEntry()) != null){
                if (entry.isDirectory()){
                    continue;
                }
                else {
                    if (entry.getName().contains("media"))
                    clq.add(entry.getName());
                    //Log.e("解压缩" , entry.getName() + "文件");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clq;
    }

    // multThread 线程版本的解压
    public static void zipContraMultiFileMultThread(String zippath ,String outzippath,String filePath){
        try {
            File file = new File(zippath);
            File outFile = null;
            ZipFile zipFile = new ZipFile(file);
            InputStream input = null;
            OutputStream output = null;
                //System.out.println("解压缩" + entry.getName() + "文件");
                Log.e("解压缩" , filePath + "文件");
                outFile = new File(outzippath + File.separator + filePath);
                    if(!outFile.getParentFile().exists()){
                        outFile.getParentFile().mkdirs();//坑爹点又被我碰到了
                    }
                    if(!outFile.exists()){
                        outFile.createNewFile();
                    }
                    //entry=
            ZipEntry entry=new ZipEntry(filePath);
                input = zipFile.getInputStream(entry);
                output = new FileOutputStream(outFile);
                int temp = 0;
                while((temp = input.read()) != -1){
                    output.write(temp);
                }
                input.close();
                output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //android unzip

   /* private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 8, TimeUnit.SECONDS,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), new ThreadPoolExecutor.AbortPolicy());*/

    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 4, 200,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), new ThreadPoolExecutor.AbortPolicy());

    public static void ZipContraMultiFile(String zippath ,String outzippath){
        try {
            File file = new File(zippath);
            File outFile = null;
            ZipFile zipFile = new ZipFile(file);
            ZipInputStream zipInput = new ZipInputStream(new FileInputStream(file));
            ZipEntry entry = null;
            InputStream input = null;
            OutputStream output = null;
            while((entry = zipInput.getNextEntry()) != null){
                //System.out.println("解压缩" + entry.getName() + "文件");
                Log.e("解压缩" , entry.getName() + "文件");
                outFile = new File(outzippath + File.separator + entry.getName());
                Log.e("解压缩" , outFile.getPath() + "文件");
                if (entry.isDirectory()){
                    if(!outFile.getParentFile().exists()){
                        outFile.getParentFile().mkdirs();//坑爹点又被我碰到了
                    }
                    continue;
                }
                else {
                    if(!outFile.getParentFile().exists()){
                        outFile.getParentFile().mkdir();//坑爹点又被我碰到了
                    }
                    if(!outFile.exists()){
                        outFile.createNewFile();
                    }
                }

                /*if(!outFile.getParentFile().exists()){
                    outFile.getParentFile().mkdir();//坑爹点又被我碰到了
                    continue;
                }*/

               /* if(!outFile.exists()){
                    outFile.createNewFile();
                }*/
                input = zipFile.getInputStream(entry);
                output = new FileOutputStream(outFile);
                int temp = 0;
                while((temp = input.read()) != -1){
                    output.write(temp);
                }
                input.close();
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Unzip the file by keyword.
     *
     * @param zipFile The ZIP file.
     * @param destDir The destination directory.
     * @param keyword The keyboard.
     * @return the unzipped files
     * @throws IOException if unzip unsuccessfully
     */


    public static List<File> unzipFileByKeyword(final File zipFile, final File destDir, final String keyword) throws IOException {
        if (zipFile == null || destDir == null) return null;
        List<File> files = new ArrayList<>();
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<?> entries = zip.entries();
        try {
            if (isSpace(keyword)) {
                while (entries.hasMoreElements()) {
                    ZipEntry entry = ((ZipEntry) entries.nextElement());
                    String entryName = entry.getName();
                    if (entryName.contains("../")) {
                        Log.e("ZipUtils", "it's dangerous!");//防止被利用漏洞恶意修改文件
                        return files;
                    }
                    if (!unzipChildFile(destDir, files, zip, entry)) return files;
                }
            } else {
                while (entries.hasMoreElements()) {
                    ZipEntry entry = ((ZipEntry) entries.nextElement());
                    String entryName = entry.getName();
                    if (entryName.contains("../")) {
                        Log.e("ZipUtils", "it's dangerous!");
                        return files;
                    }
                    if (entryName.contains(keyword)) {
                        if (!unzipChildFile(destDir, files, zip, entry)) return files;
                    }
                }
            }
        } finally {
            zip.close();
        }
        return files;
    }

    private static boolean isSpace(String keyword) {
        return false;
    }

    private static boolean unzipChildFile(final File destDir,final List<File> files,final ZipFile zip,final ZipEntry entry)
            throws IOException {
        File file = new File(destDir, entry.getName());
        files.add(file);
        if (entry.isDirectory()) {
            return createOrExistsDir(file);//创建文件夹
        } else {
            if (!createOrExistsFile(file)) return false;
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(zip.getInputStream(entry));
                out = new BufferedOutputStream(new FileOutputStream(file));
                byte buffer[] = new byte[8000];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
        }
        return true;
    }

    private static boolean createOrExistsFile(File file) {
        return false;
    }

    private static boolean createOrExistsDir(File file) {
        return false;
    }


    //讲parseXml 形成的json存储到本地某个文件夹下
    public static void wirteFileToE(String filename,String str){
        try {
        File file=new File(filename);
        if (!file.exists()){
            file.createNewFile();
        }
            FileOutputStream fos=new FileOutputStream(file);
            BufferedWriter bWriter=new BufferedWriter(new OutputStreamWriter(fos,"utf-8"));
            bWriter.write(str);
            bWriter.flush();
            bWriter.close();
            Log.e("111","成功");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void fileSelect(int REQUESTCODE_FROM_ACTIVITY, Activity activity,int fileSize){
        //REQUESTCODE_FROM_ACTIVITY = 1000;
        LFilePicker lFilePicker=new LFilePicker();
        lFilePicker.withActivity(activity)
                .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                .withStartPath("/storage/emulated/0")
                .withIsGreater(true)//big more zhan
                .withFileSize(fileSize)
                .withMutilyMode(true)
                .start();
    }

    //从asset中读取资产
    public static String readAssetFile(String filename){
        return null;
    }

    //给asset写入资产
    public static String writeAssetFile(String filename,JSONObject obj){
        try {
            InputStream inputStream=App.getInstance().getResources().getAssets().open(filename);
            //OutputStream outputStream=App.getInstance().getResources().getAssets().没有这个方法
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

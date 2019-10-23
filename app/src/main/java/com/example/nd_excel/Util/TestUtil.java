package com.example.nd_excel.Util;

import android.content.res.Resources;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    public static int imageId=1;
    //解析drawingx 文件中的image以及cell 以及图片内的单元格  sheet x->draw x-> image x y z->
    public static HashMap<String, String> parseExcelXml_6(int drawFileId, HashMap<String,String>map){
        //HashMap<String,String>map=new HashMap<>();
        StringBuffer stringBuffer=new StringBuffer();
        try {
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=factory.newPullParser();
            try {
                InputStream inputStream=App.getInstance().getResources().getAssets().open("drawing"+drawFileId+".xml");//drawing340.xml
                xmlPullParser.setInput(inputStream,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
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
                    Log.e("desc",stringBuffer.toString());
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

    //解析图片的draw 分布
    public static ArrayList<Integer> parseExcelXml_7(ArrayList<Integer>list,int sheetId){

        try {
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=factory.newPullParser();
            try {
                InputStream inputStream=App.getInstance().getResources().getAssets().open("sheet"+sheetId+".xml");
                xmlPullParser.setInput(inputStream,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            while ( xmlPullParser.getEventType()!=xmlPullParser.END_DOCUMENT){//我服了 跟文件格式有关 我得多打很多空格才解决问题
                if ("drawing".equals(xmlPullParser.getName())&&xmlPullParser.getEventType()==xmlPullParser.START_TAG){
                    list.add(sheetId);
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
        for (int i=1;i<=625;i++){
            HashMap<String,String>map=parseExcelXml_1("sheet"+i+".xml");
            Log.e("完成进度","完成第"+i+"分");//有点不敢尝试计算量可能有点恐怖
            bigmap.put("sheet"+i,map);
        }
        long l2=System.currentTimeMillis();
        Log.e("完成进度",(l2-l1)/1000+"秒");

    }

    //迭代解析所以drawing xml
    public static HashMap<String,String> parseDrawXml(){
        HashMap<String,String>map=new HashMap<>();
        for (int i=1;i<=345;i++){
            parseExcelXml_6(i,map);
        }
        return map;
    }

    //解析表一的前后布局成JSON
    public static void makeJsonMenu(){
        HashMap map=parseExcelXml_8("sheet888.xml");
        String str[]={"B","C","D","E","F"};//对就是这种对象的命名我已经找到办法了去年11月结果搁置忘记
        String strB[]=((String)map.get(str[0])).split("\\|");
        String strC[]=((String)map.get(str[1])).split("\\|");
        String strD[]=((String)map.get(str[2])).split("\\|");
        String strE[]=((String)map.get(str[3])).split("\\|");
        String strF[]=((String)map.get(str[4])).split("\\|");
        JSONObject obj=new JSONObject();

        makeJsonMenuPart_1(strB,strC,obj);
        makeJsonMenuPart_1(strC,strD,obj);//224
        makeJsonMenuPart_1(strD,strE,obj);//319
        serZeroStrsIndex();
        makeJsonMenuPart_1(strE,strF,obj);//143
        serZeroStrsIndex();
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

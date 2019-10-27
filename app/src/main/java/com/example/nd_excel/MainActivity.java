package com.example.nd_excel;




import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nd_excel.Activity.BaseActivity;
import com.example.nd_excel.Activity.TestActivity;
import com.example.nd_excel.Util.App;
import com.example.nd_excel.Util.FileTestUtil;
import com.example.nd_excel.Util.JsonConCap;
import com.example.nd_excel.Util.TestUtil;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipFile;


public class MainActivity extends BaseActivity {
    static int REQUESTCODE_FROM_ACTIVITY=-1;
    TextView poT;
    ImageView back;
    ImageView backMenu;
    Stack<ArrayList<String>> stack=new Stack<>();

    Stack<ArrayList<String>> stack2=new Stack<>();
    ViewHolderAdapter menuAdapter;
    //ArrayList<String>list=TestUtil.makeArray();
    ArrayList<String>list;
    ArrayList<String>menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initList();
       Log.e("ht",spStr.length+"");

        RecyclerView recyclerView=f(R.id.manu_menu);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        menuAdapter=new ViewHolderAdapter(R.layout.item_mainactivity,MainActivity.this, list);
        recyclerView.setAdapter(menuAdapter);
        poT=f(R.id.para_text);back=f(R.id.back);backMenu=f(R.id.back_menu);
        back.setOnClickListener(backListener);backMenu.setOnClickListener(backMenuListener);
        poT.setText("当前位置:"+"目录");

    }

    private String[]menuIndexStr={"B3","B208","B273",
            "B335","B399","B480","B513","B530","B552"};
    private String []spStr=null;
    private void initList(){
        try {
        list=new ArrayList<>();
        //JsonConCap.getInstance().get
        JsonConCap.getInstance();
        JSONObject cacuJson=JsonConCap.getCacuJson();
            JSONObject sheetJson=new JSONObject(cacuJson.getString("sheet1.xml"));
        JSONObject sharedStringJson=JsonConCap.getSharedStringJson();
        spStr=new String[menuIndexStr.length];

        for (int i=0;i<menuIndexStr.length;i++){
                String index=sheetJson.getString(menuIndexStr[i]);
                list.add(sharedStringJson.getString(index));
            spStr[i]=menuIndexStr[i];
        }
        //why array cant ref spStr=menuIndexStr;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        menuList=new ArrayList<>();
        ArrayList<String> kk=new ArrayList<>();
        kk.addAll(list);
        menuList.addAll(kk);
    }
    private void pushList(){
        ArrayList<String>list2=new ArrayList<String>();
        list2.addAll(MainActivity.this.list);
        stack.push(list2);
    }

    private void popList(){
        ArrayList<String>popList=stack.pop();
        list.clear();
        list.addAll(popList);
        menuAdapter.notifyDataSetChanged();
    }


    private void pushSpStr(){
        ArrayList<String>spStrList=new ArrayList<>();
        for (int i=0;i<spStr.length;i++){
            spStrList.add(spStr[i]);
        }
        stack2.push(spStrList);
    }

    private void popSpStr(){

        ArrayList<String>spStrList=stack2.pop();
        spStr=new String[spStrList.size()];
        for (int i=0;i<spStrList.size();i++){
            spStr[i]=spStrList.get(i);
        }
    }

    private void reMakeListOpen(String value){//value2
        try {
        list.clear();
        JSONObject  cacuJson=JsonConCap.getCacuJson();
            JSONObject  sheetJson=new JSONObject(cacuJson.getString("sheet1.xml"));//delete many code
        spStr=value.split("\\|");
        ArrayList <String>newList=new ArrayList();
        for (int i=0;i<spStr.length;i++){
            String index=sheetJson.getString(spStr[i]);
            newList.add(JsonConCap.getSharedStringJson().getString(index));
        }
            list.addAll(newList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void reMakeListJump(String value,JSONObject sheetJson){//value2
        try {
            list.clear();
            JSONObject cacuJson=JsonConCap.getCacuJson();
            JSONObject sharedStringJson=JsonConCap.getSharedStringJson();
            spStr=value.split("\\|");
            ArrayList <String>newList=new ArrayList();
            for (int i=0;i<spStr.length;i++){
                String index=sheetJson.getString(spStr[i]);
                newList.add(sharedStringJson.getString(index));
            }
            list.addAll(newList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void reMakeListJump2(String tag){
        JSONObject textSeqJson=JsonConCap.getTextSeq_2_Json();
        JSONObject imageSeqJson=JsonConCap.getTextSeq_2_Json();
        String  textStr=textSeqJson.optString(tag);
        String []textArray=textStr.split("\\|");
        ArrayList<String>dataList=new ArrayList<>();
        ArrayList<String>typeList=new ArrayList<>();
        for (int i=0;i<textArray.length;i++){
            if (textArray[i]==null||"".equals(textArray[i])){
                continue;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {
                //If it is a file selection mode, you need to get the path collection of all the files selected
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);//Constant.RESULT_INFO == "paths"
                //List<String> list = data.getStringArrayListExtra("paths");
                //Toast.makeText(getApplicationContext(), "selected " + list.size() , Toast.LENGTH_SHORT).show();
                //If it is a folder selection mode, you need to get the folder path of your choice
                String path = data.getStringExtra("path");

                //Toast.makeText(getApplicationContext(), "The selected path is:" + path, Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "The selected path is:" + list.get(0), Toast.LENGTH_LONG).show();
                String readStr=TestUtil.readFileInE(list.get(0));
                TestUtil.wirteFileToE(path+"/"+"1234.txt",readStr);
                Log.e("111",path);
                Log.e("111",readStr);
            }
        }
    }

    // using handler?
    public void changed(ArrayList<String> list,ViewHolderAdapter adapter,String tag){
        setPotText(tag);
       /* ArrayList<String>popList=stack.pop();
        list.clear();
        list.addAll(popList);
        menuAdapter.notifyDataSetChanged();*/
    }

    public void setPotText(final String tag){
        //poT.setText(tag);
        poT.post(new Runnable() {
            @Override
            public void run() {
                poT.setText("当前目录:"+tag);
            }
        });
    }

    public View.OnClickListener backListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changed(null,null,"133");
            if (stack.empty()){
                return;
            }
            popList();
            popSpStr();
        }
    };

    public View.OnClickListener backMenuListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            while (!stack.empty()){
                stack.pop();
            }
            while (!stack2.empty()){
                stack2.pop();
            }
            list.clear();
            list.addAll(menuList);
            menuAdapter.notifyDataSetChanged();
            spStr=new String[menuIndexStr.length];
            for (int i=0;i<menuIndexStr.length;i++){
                spStr[i]=menuIndexStr[i];
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
     /*   ArrayList<String>popList=stack.pop();
        list.clear();
        list.addAll(popList);
        menuAdapter.notifyDataSetChanged();*/
    }

    class ViewHolderAdapter extends RecyclerView.Adapter<ViewHolderAdapter.ViewHolder> {
         public int layoutId;
         public Activity activity;
         public ArrayList<String> list;
         //public View view;


         //test


         public ViewHolderAdapter(int layoutId, Activity activity, ArrayList<String> list) {
             this.layoutId = layoutId;
             this.activity = activity;
             this.list = list;
         }

         //menu按钮的监听
         public View.OnClickListener listener = new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //String tag=(String)v.getTag();
                 String tag = ((String) v.getTag());
                 Log.e("112233", tag + "111");
                 try {
                     String temp1=JsonConCap.getHyp_2_Json().optString(tag);//opt
                     Log.e("changed",temp1+"--");
                     String menuValue=JsonConCap.getMenuJson().getString(tag);
                     if (temp1==null||"".equals(temp1)){
                         pushList();
                         pushSpStr();
                         reMakeListOpen(menuValue);
                         menuAdapter.notifyDataSetChanged();
                         //ArrayList<String>listNew=new ArrayList<>();
                         //listNew.addAll(list);
                         //stack.push(listNew);
                         Log.e("changed","------------------");
                     }else {//hyp jump
                         /*String chapter=JsonConCap.getHyp_2_Json().optString(temp1);
                         JSONObject sheetJson=new JSONObject(JsonConCap.getCacuJson().optString(chapter));
                         reMakeListJump(menuValue,sheetJson);*/

                     }
                     switch (tag){
                         case "B3":
                             //TestUtil.makeSeqTextJsonToE();

                             //TestUtil.makeSeqImageJsonToE();
                             break;
                     }

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
               /*  try{
                     switch (JsonConCap.getMenuJson().getString(tag)){
                         case "123":
                             break;

                             default:break;
                     }
                 }catch (JSONException e){
                     e.printStackTrace();
                 }*/
             }
         };

         //
         public void rab(String tag){//
             switch (tag) {//此处可以变量对变量吗

                 case "测试列表0":
                     //Toast.makeText(MainActivity.this,"测试列表1",Toast.LENGTH_SHORT).show();
                     long l1 = System.currentTimeMillis();

                     HashMap map0 = TestUtil.parseExcelXml_1("sheet888.xml");

                     long l2 = System.currentTimeMillis();
                     Log.e("map0", (String) map0.get("D3") + "---" + (l2 - l1) + "ms");
                     break;
                 case "测试列表1":
                     Toast.makeText(MainActivity.this, "测试列表1", Toast.LENGTH_SHORT).show();
                     break;

                 case "测试列表2":
                     long l3 = System.currentTimeMillis();

                     HashMap map2 = TestUtil.parseExcelXml_4("sharedStrings.xml");

                     long l4 = System.currentTimeMillis();
                     Log.e("map0", map2.size() + "---" + (l4 - l3) + "ms");
                     break;

                 case "测试列表3":
                     long l5 = System.currentTimeMillis();

                     HashMap map3 = TestUtil.parseExcelXml_3("workbook.xml");

                     long l6 = System.currentTimeMillis();
                     Log.e("map0", (String) map3.get("270") + "---" + (l6 - l5) + "ms");
                     break;

                 case "测试列表4":
                     //Toast.makeText(MainActivity.this,"测试列表1",Toast.LENGTH_SHORT).show();
                     break;


                 case "测试列表5":
                     // Toast.makeText(MainActivity.this,"测试列表1",Toast.LENGTH_SHORT).show();
                     break;


                 case "测试列表6":
                     //Toast.makeText(MainActivity.this,"测试列表1",Toast.LENGTH_SHORT).show();
                     long l11 = System.currentTimeMillis();

                     //HashMap map6=TestUtil.parseExcelXml_6("drawing340.xml");
                     HashMap map6 = TestUtil.parseExcelXml_6(340, new HashMap<String, String>());
                     long l12 = System.currentTimeMillis();
                     Log.e("map0", "---" + (l12 - l11) + "ms");
                     break;


                 case "测试列表7":
                     //Toast.makeText(MainActivity.this,"测试列表1",Toast.LENGTH_SHORT).show();
                     long l13 = System.currentTimeMillis();

                     //HashMap map6=TestUtil.parseExcelXml_6("drawing340.xml");
                     ArrayList list7 = TestUtil.parseExcelXml_7(new ArrayList<Integer>(), 1);
                     long l14 = System.currentTimeMillis();

                     if (list7.size() != 0)
                         Log.e("map0", "---" + (l14 - l13) + "ms" + list7.get(0));
                     else Log.e("map0", "这个是良民没有图片");
                     break;

                 case "测试列表8":
                     //Toast.makeText(MainActivity.this,"测试列表1",Toast.LENGTH_SHORT).show();
                     long l15 = System.currentTimeMillis();

                     HashMap map8 = TestUtil.parseExcelXml_8("sheet888.xml");

                     String[] str8D = ((String) map8.get("B")).split("\\|");//需要加一个转义太坑了
                     //for (int i=0;i<str8D.length;i++){
                     Log.e("map0", (String) map8.get("D") + "---");
                     Log.e("map0", (String) map8.get("E") + "---");
                     //}

                     //Log.e("map0",(String)map8.get("B")+"---"+str8D.length);
                     //Log.e("map0",(String)map8.get("C")+"---");
                     //Log.e("map0",(String)map8.get("D")+"---");
                     //Log.e("map0",(String)map8.get("E")+"---");
                     long l16 = System.currentTimeMillis();
                     //Log.e("map0",(String)map8.get("F")+"---"+(l16-l15)+"ms");
                     break;

                 case "测试列表9":
                     //Toast.makeText(MainActivity.this,"测试列表1",Toast.LENGTH_SHORT).show();
                     long l17 = System.currentTimeMillis();

                     TestUtil.makeJsonMenu();
                     long l18 = System.currentTimeMillis();
                     Log.e("map0", (l18 - l17) + "ms");
                     break;

                 case "测试列表10":
                     Intent intent10 = new Intent(MainActivity.this, TestActivity.class);
                     MainActivity.this.startActivity(intent10);
                     //Toast.makeText(MainActivity.this,"测试列表1",Toast.LENGTH_SHORT).show();
                     break;

                 case "测试列表11":
                       /* Intent intent11= (Intent) FileTestUtil.
                                openFile((App.getInstance().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)).getPath());
                        MainActivity.this.startActivity(intent11);*/
                     //int REQUESTCODE_FROM_ACTIVITY = 1000;
                      /*  REQUESTCODE_FROM_ACTIVITY = 1000;
                        LFilePicker lFilePicker=new LFilePicker();
                        lFilePicker.withActivity(MainActivity.this)
                                .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                                .withStartPath("/storage/emulated/0")
                                .withIsGreater(true)//big more zhan
                                .withFileSize(500 * 1024*100)
                                .start();*/
                     //TestUtil.fileSelect(REQUESTCODE_FROM_ACTIVITY,MainActivity.this,500*1024*100);

                     //TestUtil.fileSelect(1000,MainActivity.this,0);
                     //REQUESTCODE_FROM_ACTIVITY=1000;
                     break;

                 case "测试列表12"://拼最后的json文件
                     //TestUtil.parseAllSheet();
                     /*   long l23=System.currentTimeMillis();
                        TestUtil.parseDrawXml();
                        long l24=System.currentTimeMillis();
                        Log.e("map0",(l24-l23)+"ms");*/
                     //TestUtil.parseExcelXml_2("sheet888.xml");//600多 只算超链接
                     //TestUtil.parseExcelXml_3("workbook.xml");//625 是xml都算

                     //TestUtil.makeSharedStringJsonToE();

                     //TestUtil.alignXmlNum();

                     //TestUtil.makeJsonMenu();
                     //TestUtil.parseDrawXml();
                     //TestUtil.makeHypJsonToE();

                         /*long l23=System.currentTimeMillis();

                                 //TestUtil.ZipContraMultiFile(TestUtil.path + "/tuo2/test1.zip", TestUtil.path + "/tuo2");
                                 final ConcurrentLinkedQueue<String> clq=TestUtil.zipFileList(TestUtil.path + "/tuo2/test1.zip");
                                 //ExecutorService es = Executors.newFixedThreadPool(10);

                                 ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 4, 200,
                                         TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), new ThreadPoolExecutor.DiscardPolicy());

                                 String abc=null;
                                while ((abc=clq.poll())!=null){
                                   threadPool.execute(new Task(abc));
                                    //es.submit(r1);
                                  Log.e("aaaaa",abc);
                                }*/
                     //TestUtil.zipFileList(TestUtil.path + "/tuo2/test1.zip");
                                /* Message msg=handler.obtainMessage();
                                 msg.what=10086;
                                 msg.sendToTarget();*/

                         /*long l24=System.currentTimeMillis();
                         Log.e("time",l24-l23+"");*/

                     //TestUtil.makeImageNameToE();
                       /*  long l23=System.currentTimeMillis();
                         JsonConCap jsonConCap=JsonConCap.getInstance();
                         long l24=System.currentTimeMillis();
                         Log.e("time",l24-l23+"");*/
                     break;

                 case "测试列表13":

                     break;

                 default:
                     break;
             }
         }//功成身退

         class Task implements Runnable{
             public String filepath;

             public Task(String filepath) {
                 this.filepath = filepath;
             }

             public String getFilepath() {
                 return filepath;
             }

             public void setFilepath(String filepath) {
                 this.filepath = filepath;
             }

             @Override
             public void run() {
                 TestUtil.zipContraMultiFileMultThread(TestUtil.path + "/tuo2/test1.zip",
                         TestUtil.path + "/tuo2",filepath);

             }
         }

         private Handler handler = new Handler() {
             @Override
             public void handleMessage(Message msg) {
                 super.handleMessage(msg);
                 switch (msg.what){
                     case 10086:
                         Toast.makeText(MainActivity.this,"完成",Toast.LENGTH_LONG).show();
                         break;

                         default:break;
                 }
             }
         };


         @NonNull
         @Override
         public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
             View view= LayoutInflater.from(activity).inflate(layoutId,null,false);
             //view.setTag(list.get(i));
             //Log.e("112233",list.get(i)+"111");
             //view.setOnClickListener(listener);
             ViewHolder viewHolder=new ViewHolder(view);
             return viewHolder;
         }

         @Override
         public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {//textview cant gei tag in view
                String menuName=list.get(i);
                viewHolder.textView.setText(menuName);
             viewHolder.textView.setTag(spStr[i]);
                viewHolder.textView.setOnClickListener(listener);
             //viewHolder.itemView.setOnClickListener(listener);
         }

         @Override
         public int getItemCount() {
             return list.size();
         }

         class ViewHolder extends RecyclerView.ViewHolder{
            TextView textView;
            String position;
             public ViewHolder(@NonNull View itemView) {
                 super(itemView);
                 textView=itemView.findViewById(R.id.menu_text);
             }

             public ViewHolder(@NonNull View itemView, TextView textView, String position) {
                 super(itemView);
                 this.textView = textView;
                 this.position = position;
             }
         }

     }
}

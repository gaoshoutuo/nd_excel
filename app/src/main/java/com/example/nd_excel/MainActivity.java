package com.example.nd_excel;




import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import android.os.Bundle;

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
import com.example.nd_excel.Util.TestUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;


public class MainActivity extends BaseActivity {
    TextView poT;
    ImageView back;
    ImageView backMenu;
    Stack<String[]> stack=new Stack<>();
    ViewHolderAdapter menuAdapter;
    ArrayList<String>list=TestUtil.makeArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView=f(R.id.manu_menu);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        menuAdapter=new ViewHolderAdapter(R.layout.item_mainactivity,MainActivity.this, list);
        recyclerView.setAdapter(menuAdapter);
        poT=f(R.id.para_text);back=f(R.id.back);backMenu=f(R.id.back_menu);
        back.setOnClickListener(backListener);backMenu.setOnClickListener(backMenuListener);
        poT.setText("当前位置:"+"目录");
    }
    // using handler?
    public void changed(ArrayList<String> list,ViewHolderAdapter adapter,String tag){
        setPotText(tag);

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
        }
    };

    public View.OnClickListener backMenuListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

     class ViewHolderAdapter extends RecyclerView.Adapter<ViewHolderAdapter.ViewHolder>{
        public int layoutId;
        public Activity activity;
        public ArrayList<String>list=new ArrayList<>();
        //public View view;


         //test



         public ViewHolderAdapter(int layoutId, Activity activity, ArrayList<String> list) {
             this.layoutId = layoutId;
             this.activity = activity;
             this.list = list;
         }

         //menu按钮的监听
         public View.OnClickListener listener=new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                //String tag=(String)v.getTag();
                 String tag=((TextView)v).getText().toString();
                 Log.e("112233",tag+"111");
                switch (tag){//此处可以变量对变量吗

                    case "测试列表0":
                        //Toast.makeText(MainActivity.this,"测试列表1",Toast.LENGTH_SHORT).show();
                        long l1=System.currentTimeMillis();

                        HashMap map0=TestUtil.parseExcelXml_1("sheet888.xml");

                        long l2=System.currentTimeMillis();
                        Log.e("map0",(String)map0.get("D3")+"---"+(l2-l1)+"ms");
                        break;
                    case "测试列表1":
                        Toast.makeText(MainActivity.this,"测试列表1",Toast.LENGTH_SHORT).show();
                        break;

                    case "测试列表2":
                        long l3=System.currentTimeMillis();

                        HashMap map2=TestUtil.parseExcelXml_4("sharedStrings.xml");

                        long l4=System.currentTimeMillis();
                        Log.e("map0",(String)map2.get("540")+"---"+(l4-l3)+"ms");
                        break;

                    case "测试列表3":
                        long l5=System.currentTimeMillis();

                        HashMap map3=TestUtil.parseExcelXml_3("workbook.xml");

                        long l6=System.currentTimeMillis();
                        Log.e("map0",(String)map3.get("270")+"---"+(l6-l5)+"ms");
                       break;

                    case "测试列表4":
                        //Toast.makeText(MainActivity.this,"测试列表1",Toast.LENGTH_SHORT).show();
                        break;


                    case "测试列表5":
                       // Toast.makeText(MainActivity.this,"测试列表1",Toast.LENGTH_SHORT).show();
                        break;


                    case "测试列表6":
                        //Toast.makeText(MainActivity.this,"测试列表1",Toast.LENGTH_SHORT).show();
                        long l11=System.currentTimeMillis();

                        //HashMap map6=TestUtil.parseExcelXml_6("drawing340.xml");
                        HashMap map6=TestUtil.parseExcelXml_6(340,new HashMap<String, String>());
                        long l12=System.currentTimeMillis();
                        Log.e("map0","---"+(l12-l11)+"ms");
                        break;


                    case "测试列表7":
                        //Toast.makeText(MainActivity.this,"测试列表1",Toast.LENGTH_SHORT).show();
                        long l13=System.currentTimeMillis();

                        //HashMap map6=TestUtil.parseExcelXml_6("drawing340.xml");
                        ArrayList list7=TestUtil.parseExcelXml_7(new ArrayList<Integer>(),1);
                        long l14=System.currentTimeMillis();

                        if (list7.size()!=0) Log.e("map0","---"+(l14-l13)+"ms"+list7.get(0));
                        else Log.e("map0","这个是良民没有图片");
                        break;

                    case "测试列表8":
                        //Toast.makeText(MainActivity.this,"测试列表1",Toast.LENGTH_SHORT).show();
                        long l15=System.currentTimeMillis();

                        HashMap map8=TestUtil.parseExcelXml_8("sheet888.xml");

                        String[]str8D= ((String)map8.get("B")).split("\\|");//需要加一个转义太坑了
                        //for (int i=0;i<str8D.length;i++){
                            Log.e("map0",(String)map8.get("D")+"---");
                        Log.e("map0",(String)map8.get("E")+"---");
                        //}

                        //Log.e("map0",(String)map8.get("B")+"---"+str8D.length);
                        //Log.e("map0",(String)map8.get("C")+"---");
                        //Log.e("map0",(String)map8.get("D")+"---");
                        //Log.e("map0",(String)map8.get("E")+"---");
                        long l16=System.currentTimeMillis();
                        //Log.e("map0",(String)map8.get("F")+"---"+(l16-l15)+"ms");
                        break;

                    case "测试列表9":
                        //Toast.makeText(MainActivity.this,"测试列表1",Toast.LENGTH_SHORT).show();
                        long l17=System.currentTimeMillis();

                        TestUtil.makeJsonMenu();
                        long l18=System.currentTimeMillis();
                        Log.e("map0",(l18-l17)+"ms");
                        break;

                    case "测试列表10":
                        Intent intent10=new Intent(MainActivity.this, TestActivity.class);
                        MainActivity.this.startActivity(intent10);
                        //Toast.makeText(MainActivity.this,"测试列表1",Toast.LENGTH_SHORT).show();
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
             view.setOnClickListener(listener);
             ViewHolder viewHolder=new ViewHolder(view);
             return viewHolder;
         }

         @Override
         public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
                String menuName=list.get(i);
                viewHolder.textView.setText(menuName);
                viewHolder.textView.setOnClickListener(listener);

         }

         @Override
         public int getItemCount() {
             return list.size();
         }

         class ViewHolder extends RecyclerView.ViewHolder{
            TextView textView;
             public ViewHolder(@NonNull View itemView) {
                 super(itemView);
                 textView=itemView.findViewById(R.id.menu_text);
             }
         }

     }
}

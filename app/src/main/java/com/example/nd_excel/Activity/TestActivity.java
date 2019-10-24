package com.example.nd_excel.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.example.nd_excel.R;
import com.kelin.scrollablepanel.library.PanelAdapter;
import com.kelin.scrollablepanel.library.ScrollablePanel;

import java.io.IOException;
import java.util.ArrayList;

public class TestActivity extends BaseActivity {
    private ViewPager viewPager;
    private ArrayList<View> pageview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //viewPagerSet();
        //一个做校长的为人
        //makePannelAdapter();

    }

    private void makePannelAdapter(){
        TestPannelAdapter testPanelAdapter = new TestPannelAdapter(makeCellList(),TestActivity.this);
        ScrollablePanel scrollablePanel = (ScrollablePanel) findViewById(R.id.scrollable_panel);
        scrollablePanel.setPanelAdapter(testPanelAdapter);
    }

    private ArrayList<ArrayList<String>> makeCellList(){
        ArrayList<ArrayList<String>>lists=new ArrayList<>();
        for (int i=0;i<20;i++){
            ArrayList<String>list=new ArrayList<>();
            for (int j=0;j<9;j++){
                list.add(i+"-"+j);
            }
            lists.add(list);
        }
        return lists;
    }

    //testPannelAdapter
    public class TestPannelAdapter extends PanelAdapter{
        private ArrayList<ArrayList<String>>lists;
        private Activity activity;

        public TestPannelAdapter() {
        }

        public TestPannelAdapter(ArrayList<ArrayList<String>> lists, Activity activity) {
            this.lists = lists;
            this.activity = activity;
        }

        @Override
        public int getRowCount() {
            return lists.size();
        }

        @Override
        public int getColumnCount() {
            return lists.get(0).size();
        }

        @Override
        public int getItemViewType(int row, int column) {
            return super.getItemViewType(row, column);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column) {
            ((PannelViewHolder)holder).textView.setText((String)(lists.get(row).get(column)));

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(TestActivity.this).inflate(R.layout.item_tablecell,null);
            PannelViewHolder viewHolder=new PannelViewHolder(view);
            return viewHolder;
        }

        private class PannelViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;
            public PannelViewHolder(@NonNull View itemView) {
                super(itemView);
                textView=itemView.findViewById(R.id.pannel_text);
            }
        }
    }


    void viewPagerSet(){
        viewPager = (ViewPager) findViewById(R.id.test_viewpager);

        //查找布局文件用LayoutInflater.inflate
        LayoutInflater inflater =getLayoutInflater();
        View view1 = inflater.inflate(R.layout.item_test_image, null);
        View view2 = inflater.inflate(R.layout.item_test_image, null);
        View view3 = inflater.inflate(R.layout.item_test_image, null);
      /*  ImageView imageView1=view1.findViewById(R.id.test_image_item);
        ImageView imageView2=view1.findViewById(R.id.test_image_item);
        ImageView imageView3=view1.findViewById(R.id.test_image_item);

        try {
            imageView1.setImageBitmap(BitmapFactory.decodeStream(getResources().getAssets().open("image1.png")));
            imageView2.setImageBitmap(BitmapFactory.decodeStream(getResources().getAssets().open("image2.png")));
            imageView3.setImageBitmap(BitmapFactory.decodeStream(getResources().getAssets().open("image1.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        PhotoView photoView1=view1.findViewById(R.id.test_image_item);
        PhotoView photoView2=view2.findViewById(R.id.test_image_item);
        PhotoView photoView3=view3.findViewById(R.id.test_image_item);
        try {
            //photoView1.setMaxHeight(300);  我找到调整大小的办法了

            //Log.e("width",photoView1.getLayoutParams().width+"");//-1 我惊呆了

            Bitmap bitmap1=BitmapFactory.decodeStream(getResources().getAssets().open("image1.png"));
            double sh=bitmap1.getWidth()/bitmap1.getHeight();
            Log.e("width",bitmap1.getWidth()+"--"+bitmap1.getHeight());
            photoView1.getLayoutParams().height= (int) (1080/sh);//1080? 1280?
            photoView1.setImageBitmap(bitmap1); photoView1.enable();
            photoView2.setImageBitmap(BitmapFactory.decodeStream(getResources().getAssets().open("image2.png"))); photoView2.enable();
            photoView3.setImageBitmap(BitmapFactory.decodeStream(getResources().getAssets().open("image3.png"))); photoView3.enable();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将view装入数组
        pageview =new ArrayList<View>();
        pageview.add(view1);
        pageview.add(view2);
        pageview.add(view3);


        //数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter(){

            @Override
            //获取当前窗体界面数
            public int getCount() {
                // TODO Auto-generated method stub
                return pageview.size();
            }

            @Override
            //断是否由对象生成界面
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0==arg1;
            }
            //是从ViewGroup中移出当前View
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(pageview.get(arg1));
            }

            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1){
                ((ViewPager)arg0).addView(pageview.get(arg1));
                return pageview.get(arg1);
            }


        };

        //绑定适配器
        viewPager.setAdapter(mPagerAdapter);
    }



}

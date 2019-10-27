package com.example.nd_excel.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.example.nd_excel.Bean.MixBean;
import com.example.nd_excel.MainActivity;
import com.example.nd_excel.R;
import com.example.nd_excel.Util.FinalValue;
import com.example.nd_excel.Util.TestUtil;
import com.kelin.scrollablepanel.library.ScrollablePanel;

import java.util.ArrayList;
import java.util.HashMap;

public class ManuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {//好像没什么用 manu有main代替
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manu);
        Intent intent=getIntent();
        RecyclerView recyclerView=f(R.id.page_recy);
        LinearLayoutManager lm=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        ManuAdapter adapter=new ManuAdapter(ManuActivity.this,intent.getIntExtra("latoutid",0),
                getTypeList(intent),getDataList(intent));
        recyclerView.setAdapter(adapter);
    }
    //ArrayList<Integer>typeList;//text image
    //ArrayList<MixBean>list;
    private ArrayList<Integer>getTypeList(Intent intent){
        Bundle args = intent.getBundleExtra("bundle");
        ArrayList<Integer> object = (ArrayList<Integer>) args.getSerializable("typelist");
        return object;
    }

    private ArrayList<MixBean>getDataList(Intent intent){
        Bundle args = intent.getBundleExtra("bundle");
        ArrayList<MixBean> object = (ArrayList<MixBean>) args.getSerializable("datalist");
        return object;
    }

    /** sent
     * ArrayList<Object> object = new ArrayList<Object>();//object类必须implement Serializable
     * Intent intent = new Intent(Current.class, Transfer.class);
     * Bundle args = new Bundle();
     * args.putSerializable("ARRAYLIST",(Serializable)object);
     * intent.putExtra("BUNDLE",args);
     * startActivity(intent);
     * ————————————————
     * 版权声明：本文为CSDN博主「濯君」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
     * 原文链接：https://blog.csdn.net/zzldm/article/details/52523259
     */

    /** get
     * Intent intent = getIntent();
     * Bundle args = intent.getBundleExtra("BUNDLE");
     * ArrayList<Object> object = (ArrayList<Object>) args.getSerializable("ARRAYLIST");
     * ————————————————
     * 版权声明：本文为CSDN博主「濯君」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
     * 原文链接：https://blog.csdn.net/zzldm/article/details/52523259
     */

    class ManuAdapter extends RecyclerView.Adapter<ManuAdapter.MixViewHolder>{
        Activity activity;
        int layoutId;
        ArrayList<Integer>typeList;//text image
        ArrayList<MixBean>list;
        //HashMap<String,>
        int pIndex=0;

        public ManuAdapter(Activity activity, int layoutId, ArrayList<Integer> typeList, ArrayList<MixBean> list) {
            this.activity = activity;
            this.layoutId = layoutId;
            this.typeList = typeList;
            this.list = list;
        }

        @NonNull
        @Override
        public MixViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view=LayoutInflater.from(activity).inflate(layoutId,null);
            int type=0;
            if (typeList!=null){
                type=typeList.get(pIndex);
            }
           /* if (index==FinalValue.TYPEOFPHOTO){

            }else if (index==FinalValue.TYPEOFTEXT){

            }else if (index==FinalValue.TYPETABLE){

            }*/
            ;
            MixViewHolder holder=new MixViewHolder(view,type);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MixViewHolder viewHolder, int i) {
            viewHolder=(MixViewHolder)viewHolder;
            MixBean mb=list.get(i);
            if (mb.getType()==FinalValue.TYPEOFPHOTO){
                TestUtil.photoUtil(viewHolder.photoView,mb.getPhotoStr());
            }else if (mb.getType()==FinalValue.TYPEOFTEXT){
                TestUtil.TextUtil(viewHolder.textView,mb.getTextStr());
            }else if (mb.getType()==FinalValue.TYPETABLE){
                TestUtil.tableUtil(viewHolder.scrollablePanel,mb.getLists(), ManuActivity.this);
            }

            pIndex=i+1;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class TextViewHolder extends RecyclerView.ViewHolder {
            int textNum;
            TextView[] textViewArray;
            TextView textView;
            public TextViewHolder(@NonNull View itemView) {
                super(itemView);
                textView=itemView.findViewById(R.id.major_text);
            }

            public TextViewHolder(@NonNull View itemView, int textNum) {
                super(itemView);
                this.textNum = textNum;
                textViewArray=new TextView[textNum];
                for (int i=0;i<textNum;i++){
                    textViewArray[i]=itemView.findViewById(R.id.major_text);
                }
            }
            //ViewPager viewPager;

        }

        class PhotoViewHolder extends RecyclerView.ViewHolder {
            int photoNum;
            PhotoView[] photoViewArray;

            PhotoView photoView;
            public PhotoViewHolder(@NonNull View itemView) {
                super(itemView);
                photoView=itemView.findViewById(R.id.test_image_item);
            }

            public PhotoViewHolder(@NonNull View itemView, int photoNum) {
                super(itemView);
                this.photoNum = photoNum;
                this.photoViewArray = photoViewArray;
                photoViewArray=new PhotoView[photoNum];
                for (int i=0;i<photoNum;i++){
                    photoViewArray[i]=itemView.findViewById(R.id.test_image_item);
                }
            }
            //ViewPager viewPager;

        }

        class MixViewHolder extends RecyclerView.ViewHolder {
            ScrollablePanel scrollablePanel;
            PhotoView photoView;
            TextView textView;
            int type;// 1=text  2=photo
            public MixViewHolder(@NonNull View itemView) {
                super(itemView);
                //itemView.findViewById(R.id.major_text);
            }

            public MixViewHolder(@NonNull View itemView, int type) {
                super(itemView);
                this.type = type;
                if (type== FinalValue.TYPEOFPHOTO){
                    photoView=itemView.findViewById(R.id.test_image_item);
                }else if (type==FinalValue.TYPEOFTEXT){
                    textView=itemView.findViewById(R.id.major_text);
                }else if (type==FinalValue.TYPETABLE){
                    scrollablePanel=itemView.findViewById(R.id.scrollable_panel);
                }
            }
        }

    }
}

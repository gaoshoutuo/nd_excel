package com.example.nd_excel.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.nd_excel.R;

public class ManuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {//好像没什么用 manu有main代替
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manu);
        RecyclerView recyclerView=f(R.id.page_recy);
        LinearLayoutManager lm=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
    }

    class ManuAdapter extends RecyclerView.Adapter<ManuAdapter.ViewHolder>{


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}

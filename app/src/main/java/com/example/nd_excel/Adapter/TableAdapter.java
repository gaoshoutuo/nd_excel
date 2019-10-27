package com.example.nd_excel.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nd_excel.Activity.TestActivity;
import com.example.nd_excel.R;
import com.example.nd_excel.Util.App;
import com.kelin.scrollablepanel.library.PanelAdapter;

import java.util.ArrayList;


public class TableAdapter extends PanelAdapter {
    private ArrayList<ArrayList<String>>lists;
    private Activity activity;

    public TableAdapter() {
    }

    public TableAdapter(ArrayList<ArrayList<String>> lists, Activity activity) {
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
        ((TableAdapter.PannelViewHolder)holder).textView.setText((String)(lists.get(row).get(column)));

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(App.getInstance()).inflate(R.layout.item_tablecell,null);
        TableAdapter.PannelViewHolder viewHolder=new TableAdapter.PannelViewHolder(view);
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
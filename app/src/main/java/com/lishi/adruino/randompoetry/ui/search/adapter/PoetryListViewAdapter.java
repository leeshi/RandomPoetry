package com.lishi.adruino.randompoetry.ui.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.lishi.adruino.randompoetry.R;
import com.lishi.adruino.randompoetry.item.PoetryItem;

import java.util.List;

public class PoetryListViewAdapter extends BaseAdapter {
    private List<PoetryItem> itemList;
    private LayoutInflater mInflater;  //加载自定义布局


    public PoetryListViewAdapter(Context context, List<PoetryItem> list){
        this.itemList = list;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public Object getItem(int position){
        return itemList.get(position);
    }
    @Override
    public int getCount(){
        return itemList.size();
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup viewGroup){
        View viewPoetry = this.mInflater.inflate(R.layout.search_poetry_item_layout, null);
        //获取item对象
        PoetryItem item = this.itemList.get(position);
        //获取自定义布局View对象
        TextView poetryView = viewPoetry.findViewById(R.id.poetryView);
        TextView poetView = viewPoetry.findViewById(R.id.poetView);
        TextView titleView = viewPoetry.findViewById(R.id.titleView);
        //添加数据
        poetryView.setText(item.getContent());
        poetView.setText(item.getPoet());
        titleView.setText(item.getTitle());

        return viewPoetry;
    }

    public void update(List<PoetryItem> list){
        this.itemList = list;
    }



}

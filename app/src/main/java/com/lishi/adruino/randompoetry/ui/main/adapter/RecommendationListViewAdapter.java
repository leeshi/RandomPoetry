package com.lishi.adruino.randompoetry.ui.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lishi.adruino.randompoetry.R;
import com.lishi.adruino.randompoetry.item.PoetryItem;

import java.util.List;

public class RecommendationListViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;  //加载自定义布局
    private List<PoetryItem> listPoetryItem;

    public RecommendationListViewAdapter(Context context,List<PoetryItem> list){
        listPoetryItem = list;
        mInflater = LayoutInflater.from(context);
    }

    public void update(List<PoetryItem> list){
        //TODO 添加储存，同一天的推荐就不用多次加载了
        this.listPoetryItem = list;
    }

    @Override
    public int getCount() {
        return listPoetryItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listPoetryItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //初始化单个view
        View viewPoetry = this.mInflater.inflate(R.layout.recommendation_item,null);

        PoetryItem poetryItem = listPoetryItem.get(position);

        TextView poetryView = viewPoetry.findViewById(R.id.poetry);
        TextView sourceView = viewPoetry.findViewById(R.id.source);
        //set数据
        poetryView.setText(poetryItem.getContent());
        sourceView.setText(poetryItem.getPoet() + "《" + poetryItem.getTitle()+"》");

        return viewPoetry;
    }
}

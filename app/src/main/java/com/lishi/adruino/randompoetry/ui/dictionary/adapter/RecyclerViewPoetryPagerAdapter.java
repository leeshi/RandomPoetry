package com.lishi.adruino.randompoetry.ui.dictionary.adapter;


import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.lishi.adruino.randompoetry.R;
import com.lishi.adruino.randompoetry.item.PoetryItem;

import java.util.ArrayList;
import java.util.List;

/*
 * RecyclerViewPoetryPagerAdapter用于根据文本数据进行TextView的更新操作
 * 每个Fragment对应一个Adapter，因此我们只需要在对应的View中调用更新页面的方法
 * 实际上，并没有必要对数据进行更新，因为文本信息从一开始就注定是不变的，isn't it?
 * 还是说需要保存这些Fragment，以便下一次可以直接通过更新数据调用！！
 * Yes
 * **一个Presenter对应多个Adapter**
 */
public class RecyclerViewPoetryPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<Object> contents;
    private final int mode;
    //每一个CardView对应一个TextView
    List<TextView> listTextView = new ArrayList<>();
    public RecyclerViewPoetryPagerAdapter(List<Object> contents,int mode) {
        this.contents = contents;
        this.mode = mode;
    }
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate CardView
        View view = null;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_cardview, parent, false);

        //获取每个CardView的TextView并保存起来
        TextView tv = view.findViewById(R.id.firstTextView);
        TextView tv1 = view.findViewById(R.id.secondTextView);
        TextView tv2 = view.findViewById(R.id.thirdTextView);

        listTextView.add(tv);
        listTextView.add(tv1);
        listTextView.add(tv2);

        return new RecyclerView.ViewHolder(view) {
        };

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    /*
     * 对数据进行更新，利用textView.setText方法，从这个角度来讲，不必使用notifyDataChanged
     * 必须对数据进行对齐
     * @param
     *        List<String>
     * @return void
     */
    public void update(List<Object> data){
        if(mode == 2){
            PoetryItem poetryItem = (PoetryItem) data.get(0);
            TextView first = listTextView.get(0);
            TextView second = listTextView.get(1);
            TextView third = listTextView.get(2);

            first.setGravity(Gravity.CENTER);
            first.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            first.setTextSize(20);
            //first.setTextColor(Col);
            first.setText(poetryItem.mTitle);


            second.setGravity(Gravity.CENTER);
            second.setText(poetryItem.mPoet);
            third.setGravity(Gravity.CENTER);
            third.setText(poetryItem.mContent);

            listTextView.get(3).setText(data.get(1).toString());
            //隐藏
            listTextView.get(4).setVisibility(View.GONE);
            listTextView.get(5).setVisibility(View.GONE);
        }else if(mode == 1){
            listTextView.get(0).setText(data.get(0).toString());
            //隐藏不显示的tv，防止出现空白
            listTextView.get(1).setVisibility(View.GONE);
            listTextView.get(2).setVisibility(View.GONE);
        }else{
            for (int i = 0; i < data.size(); i++) {
                PoetryItem poetryItem = (PoetryItem) data.get(i);

                TextView first = listTextView.get(i*3);
                TextView second = listTextView.get(i*3 + 1);
                TextView third = listTextView.get(i*3 + 2);
                //加粗等字体处理
                first.setGravity(Gravity.CENTER);
                first.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                first.setTextSize(20);
                first.setText(poetryItem.mTitle);

                second.setGravity(Gravity.CENTER);
                second.setText(poetryItem.mPoet);

                third.setGravity(Gravity.CENTER);
                third.setText(poetryItem.mContent);
            }
        }
    }

}
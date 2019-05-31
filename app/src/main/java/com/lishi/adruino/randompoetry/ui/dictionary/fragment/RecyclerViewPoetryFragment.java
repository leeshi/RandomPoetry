package com.lishi.adruino.randompoetry.ui.dictionary.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.lishi.adruino.randompoetry.R;
import com.lishi.adruino.randompoetry.ui.dictionary.adapter.RecyclerViewPoetryPagerAdapter;
import com.lishi.adruino.randompoetry.ui.dictionary.view.DetailedPoetryView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewPoetryFragment extends Fragment implements DetailedPoetryView {
    public static Fragment newInstance(){return  new RecyclerViewPoetryFragment();}
    final List<Object> items = new ArrayList<>();
    private RecyclerViewPoetryPagerAdapter mViewPagerAdapter;

    RecyclerView mRecyclerView;

    @Override
    public void onStop(){
        super.onStop();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        //通过外部传参来设置item数量，然后获取各个item的textView
        int ITEMS = getArguments().getInt("ITEMS");
        for (int i=0;i<ITEMS;i++){
            items.add(new Object());
        }

        mViewPagerAdapter = new RecyclerViewPoetryPagerAdapter(items,ITEMS);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setAdapter(mViewPagerAdapter);
    }

    @Override
    public void showFailedError() {
        //TODO
    }

    @Override
    public void toPoetryFragment(List<Object> listData) {
        mViewPagerAdapter.update(listData);
    }

    @Override
    public void hideLoading() {
        //TODO
    }

    @Override
    public void showLoading() {
        //TODO
    }

    @Override
    public void clearView() {
        //TODO
    }

    @Override
    public void getMode() {
        //TODO
    }
}

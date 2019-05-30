package com.lishi.adruino.randompoetry.ui.search.presenter;


import android.os.Handler;

import com.lishi.adruino.randompoetry.item.PoetryItem;
import com.lishi.adruino.randompoetry.model.OnLoadListener;
import com.lishi.adruino.randompoetry.ui.search.model.PoetryCrawler;
import com.lishi.adruino.randompoetry.ui.search.view.SearchPoetryView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchPoetryPresenter {
    private SearchPoetryView searchPoetryView;
    private PoetryCrawler poetryCrawler;
    private Handler mHandler = new Handler();

    private List<PoetryItem> listAllPoetryItem = new ArrayList<>();

    public SearchPoetryPresenter(SearchPoetryView searchPoetryView){
        this.poetryCrawler = new PoetryCrawler();
        this.searchPoetryView = searchPoetryView;
    }

    public void search(){
        searchPoetryView.showLoading();
        poetryCrawler.search(new OnLoadListener(){
            @Override
            public void loadSuccess(Object list) {
                //如果是第一次搜索就把之前保存的信息清空
                if(!searchPoetryView.ifSearched()) {
                    listAllPoetryItem.clear();
                }

                searchPoetryView.setSearched();

                listAllPoetryItem.addAll((List)list);
                mHandler.post(()->{
                    searchPoetryView.hideLoading();
                    searchPoetryView.toMainActivity(listAllPoetryItem);
                });
            }

            @Override
            public void loadFailed() {
                mHandler.post(()->{
                    searchPoetryView.hideLoading();
                    searchPoetryView.showFailedError();
                });
            }

            @Override
            public void loadOver(){
                mHandler.post(()-> searchPoetryView.hideLoading());
            }

            @Override
            public Object getLoadOption() {
                Map<String,Object> options = new HashMap<>();
                options.put("mode",searchPoetryView.getMode());
                options.put("searched",searchPoetryView.ifSearched());
                options.put("content",searchPoetryView.getContent());
                return options;
            }
        });
    }

    public void clear(){
        searchPoetryView.clearPoetry();
    }
}
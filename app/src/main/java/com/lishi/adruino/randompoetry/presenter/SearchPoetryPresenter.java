package com.lishi.adruino.randompoetry.presenter;


import android.os.Handler;

import com.lishi.adruino.randompoetry.item.PoetryItem;
import com.lishi.adruino.randompoetry.model.OnLoadListener;
import com.lishi.adruino.randompoetry.model.PoetryCrawler;
import com.lishi.adruino.randompoetry.view.SearchPoetryView;

import java.util.ArrayList;
import java.util.List;

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
        poetryCrawler.SearchPoetry(searchPoetryView.getMode(),searchPoetryView.ifSearched(), searchPoetryView.getContent(), new OnLoadListener(){
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
                listAllPoetryItem.clear();
                mHandler.post(()->{
                    searchPoetryView.hideLoading();
                    searchPoetryView.toMainActivity(listAllPoetryItem);
                });
            }

            @Override
            public Object getLoadOption() {
                return null;
            }
        });
    }

    public void clear(){
        searchPoetryView.clearPoetry();
    }
}
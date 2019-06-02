package com.lishi.adruino.randompoetry.ui.search.presenter;


import android.os.Handler;
import android.widget.Toast;

import com.lishi.adruino.randompoetry.item.PoetryItem;
import com.lishi.adruino.randompoetry.model.OnLoadListener;
import com.lishi.adruino.randompoetry.presenter.Presenter;
import com.lishi.adruino.randompoetry.ui.search.activity.SearchActivity;
import com.lishi.adruino.randompoetry.ui.search.model.SearchingCrawlerImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchingPresenterImpl implements Presenter {
    private SearchActivity searchPoetryView;
    private SearchingCrawlerImpl poetryCrawler;
    private Handler mHandler = new Handler();

    private List<PoetryItem> listAllPoetryItem = new ArrayList<>();

    public SearchingPresenterImpl(SearchActivity searchPoetryView){
        this.poetryCrawler = new SearchingCrawlerImpl();
        this.searchPoetryView = searchPoetryView;
    }


    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onProcess(String loadOption) {
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
                mHandler.post(()-> {
                    Toast.makeText(searchPoetryView,"没有更多了(+_+)?",Toast.LENGTH_SHORT).show();
                    searchPoetryView.hideLoading();
                });
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
}
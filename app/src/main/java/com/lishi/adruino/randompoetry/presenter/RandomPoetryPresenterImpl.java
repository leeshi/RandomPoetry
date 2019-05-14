package com.lishi.adruino.randompoetry.presenter;

import android.os.Handler;

import com.lishi.adruino.randompoetry.item.PoetryItem;
import com.lishi.adruino.randompoetry.model.Crawler;
import com.lishi.adruino.randompoetry.model.OnLoadListener;
import com.lishi.adruino.randompoetry.view.RecommendationView;

import java.util.List;

public class RandomPoetryPresenterImpl implements RandomPoetryPresenter{
    private RecommendationView mRecommendationView;
    private Crawler mCrawler;
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(RecommendationView recommendationView,Crawler crawler) {
        this.mRecommendationView = recommendationView;
        this.mCrawler = crawler;
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
    public void onProcess() {
        mRecommendationView.showLoading();
        mCrawler.search(new OnLoadListener() {
            @Override
            public void loadSuccess(List<PoetryItem> listData) {
                mHandler.post(()->{
                    mRecommendationView.hideLoading();
                    mRecommendationView.toMainActivity(listData);
                });
            }

            @Override
            public void loadFailed() {
                mHandler.post(()->{
                    mRecommendationView.hideLoading();
                    mRecommendationView.showFailedError();
                });
            }

            @Override
            public void loadOver() {

            }
        });
    }
}

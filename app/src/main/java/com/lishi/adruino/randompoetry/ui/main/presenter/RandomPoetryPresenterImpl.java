package com.lishi.adruino.randompoetry.ui.main.presenter;

import android.os.Handler;

import com.lishi.adruino.randompoetry.model.Crawler;
import com.lishi.adruino.randompoetry.model.OnLoadListener;
import com.lishi.adruino.randompoetry.presenter.Presenter;
import com.lishi.adruino.randompoetry.ui.main.view.RecommendationView;

public class RandomPoetryPresenterImpl implements Presenter {
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
    public void onProcess(String loadOption) {
        mRecommendationView.showLoading();
        mCrawler.search(new OnLoadListener() {
            @Override
            public void loadSuccess(Object data) {
                mHandler.post(()->{
                    mRecommendationView.hideLoading();
                    mRecommendationView.toMainActivity(data);
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

            @Override
            public Object getLoadOption() {
                return loadOption;
            }
        });
    }
}

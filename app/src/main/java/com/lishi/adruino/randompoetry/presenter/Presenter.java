package com.lishi.adruino.randompoetry.presenter;

import com.lishi.adruino.randompoetry.model.Crawler;
import com.lishi.adruino.randompoetry.view.RecommendationView;

//可以跟词典使用同一个presenter
public interface Presenter {
    void onCreate(RecommendationView recommendationView, Crawler crawler);
    void onResume();
    void onDestroy();
    void onItemClick(int position);
    void onProcess(String loadOption);
}

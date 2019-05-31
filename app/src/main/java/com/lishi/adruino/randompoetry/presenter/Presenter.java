package com.lishi.adruino.randompoetry.presenter;

import com.lishi.adruino.randompoetry.model.Crawler;
import com.lishi.adruino.randompoetry.ui.main.view.RecommendationView;

//可以跟词典使用同一个presenter
public interface Presenter {
    void onResume();
    void onDestroy();
    void onItemClick(int position);
    void onProcess(String loadOption);
}

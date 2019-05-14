package com.lishi.adruino.randompoetry.view;

import com.lishi.adruino.randompoetry.item.PoetryItem;

import java.util.List;

public interface RecommendationView {
    void showLoading();
    void showFailedError();
    void hideLoading();
    void loadSuccess();
    void toMainActivity(List<PoetryItem> listData);
}

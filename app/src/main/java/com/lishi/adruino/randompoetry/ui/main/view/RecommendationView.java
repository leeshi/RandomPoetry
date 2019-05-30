package com.lishi.adruino.randompoetry.ui.main.view;

public interface RecommendationView {
    void showLoading();
    void showFailedError();
    void hideLoading();
    void loadSuccess();
    void toMainActivity(Object data);
}

package com.lishi.adruino.randompoetry.ui.dictionary.presenter;

public interface DetailedPoetryPresenter {
    void onCreate(String serial);
    void onResume();
    void onDestroy();
    void onItemClick(int position);
}

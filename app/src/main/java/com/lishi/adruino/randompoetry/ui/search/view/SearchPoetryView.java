package com.lishi.adruino.randompoetry.ui.search.view;


import com.lishi.adruino.randompoetry.item.PoetryItem;

import java.util.List;

public interface SearchPoetryView {
    int getMode();
    boolean ifSearched();
    void setSearched();
    String getContent();

    void clearPoetry();
    void showLoading();
    void hideLoading();
    void toMainActivity(List<PoetryItem> list);
    void showFailedError();
}
package com.lishi.adruino.randompoetry.ui.search.view;


import com.lishi.adruino.randompoetry.item.PoetryItem;

import java.util.List;

public interface SearchingView {
    int getMode();
    boolean ifSearched();
    void setSearched();
    String getContent();

    void showLoading();
    void hideLoading();
    void toMainActivity(List<PoetryItem> list);
    void showFailedError();
}
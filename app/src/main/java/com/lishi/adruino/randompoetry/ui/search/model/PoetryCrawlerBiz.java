package com.lishi.adruino.randompoetry.ui.search.model;

import com.lishi.adruino.randompoetry.model.OnLoadListener;

public interface PoetryCrawlerBiz {
    void SearchPoetry(int mode, boolean ifSearched, String content, OnLoadListener searchListener);
}
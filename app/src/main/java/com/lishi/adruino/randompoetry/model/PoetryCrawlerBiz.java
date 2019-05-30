package com.lishi.adruino.randompoetry.model;

public interface PoetryCrawlerBiz {
    void SearchPoetry(int mode, boolean ifSearched, String content, OnLoadListener searchListener);
}
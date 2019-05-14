package com.lishi.adruino.randompoetry.model;

import com.lishi.adruino.randompoetry.item.PoetryItem;

import java.util.List;

public interface OnLoadListener {
    void loadSuccess(List<PoetryItem> listData);
    void loadFailed();
    void loadOver();
}

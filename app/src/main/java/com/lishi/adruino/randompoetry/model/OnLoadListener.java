package com.lishi.adruino.randompoetry.model;

public interface OnLoadListener {
    void loadSuccess(Object data);
    void loadFailed();
    void loadOver();
    Object getLoadOption();
}

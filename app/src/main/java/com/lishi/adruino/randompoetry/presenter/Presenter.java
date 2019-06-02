package com.lishi.adruino.randompoetry.presenter;

//可以跟词典使用同一个presenter
public interface Presenter {
    void onResume();
    void onDestroy();
    void onItemClick(int position);
    void onProcess(String loadOption);
}

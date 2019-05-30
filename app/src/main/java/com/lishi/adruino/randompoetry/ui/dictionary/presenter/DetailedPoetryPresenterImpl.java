package com.lishi.adruino.randompoetry.ui.dictionary.presenter;

import android.os.Handler;

import com.lishi.adruino.randompoetry.ui.dictionary.activity.DictionaryActivity;
import com.lishi.adruino.randompoetry.item.PoetryItem;
import com.lishi.adruino.randompoetry.model.Crawler;
import com.lishi.adruino.randompoetry.model.OnLoadListener;
import com.lishi.adruino.randompoetry.ui.dictionary.view.DetailedPoetryView;

import java.util.ArrayList;
import java.util.List;

/*
 * 此处，一个Presenter对应多个View，在对象构造时，将所有View都保存下来
 */
public class DetailedPoetryPresenterImpl implements DetailedPoetryPresenter {
    List<DetailedPoetryView> listAllFragmentView;
    DictionaryActivity mainActivity;
    Crawler DetailCrawler;
    private Handler mHandler = new Handler();


    public DetailedPoetryPresenterImpl(DictionaryActivity mainActivity, List<DetailedPoetryView> listAllFragmentView, Crawler DetailCrawler){
        this.listAllFragmentView = listAllFragmentView;
        this.DetailCrawler = DetailCrawler;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(String serial){
        //TODO 显示正在加载
        DetailCrawler.search(new OnLoadListener() {
            @Override
            public void loadOver() {
                //TODO
            }

            @Override
            public Object getLoadOption(){
                return serial;
            }

            @Override
            public void loadFailed() {
                //TODO 使用另一个View进行操作，需要把Fragment都设置为GONE
            }

            /*
             * 此处一定要控制好每个TextView所对应的内容
             * 顺序为 title,poet,time,contson,fanyi,shangxi,background
             * 可以考虑使用HashMap使得关系更加清晰
             */
            @Override
            public void loadSuccess(Object data) {
                //TODO 强转
                List<Object> listData = (ArrayList)data;
                //更新main title
                mHandler.post(()->{
                    PoetryItem poetryItem = (PoetryItem) listData.get(0);
                    System.out.println(poetryItem);
                    mainActivity.toMainActivity(poetryItem.mTitle);
                });

                for(int i = 0;i < listAllFragmentView.size();i++){
                    switch (i){
                        //TODO 修改这里的列表长度以适应每一个Fragment
                        case 0:
                            mHandler.post(()-> {
                                    listAllFragmentView.get(0).toPoetryFragment(listData.subList(0,2));
                            });
                            continue;
                        case 1:
                            mHandler.post(()-> {
                                listAllFragmentView.get(1).toPoetryFragment(listData.subList(2, 3));
                            });
                            continue;
                        case 2:
                            mHandler.post(()-> {
                                listAllFragmentView.get(2).toPoetryFragment((listData.subList(3, 4)));
                            });
                            continue;
                        case 3:
                            mHandler.post(()->{
                                //一共三个推荐
                                listAllFragmentView.get(3).toPoetryFragment(listData.subList(4,7));
                            });
                            continue;
                            //....more
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        //TODO 在恢复以后进行的工作
    }

    @Override
    public void onDestroy() {
        //TODO
    }

    @Override
    public void onItemClick(int position) {
        //TODO 获取点击到的信息
    }
}

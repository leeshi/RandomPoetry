package com.lishi.adruino.randompoetry.ui.dictionary.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.lishi.adruino.randompoetry.R;
import com.lishi.adruino.randompoetry.ui.dictionary.adapter.PoetryPagerAdapter;
import com.lishi.adruino.randompoetry.ui.dictionary.model.DetailCrawlerImpl;
import com.lishi.adruino.randompoetry.ui.dictionary.presenter.DetailedPoetryPresenter;
import com.lishi.adruino.randompoetry.ui.dictionary.presenter.DetailedPoetryPresenterImpl;
import com.lishi.adruino.randompoetry.ui.dictionary.view.MainPageView;


import java.util.HashMap;
import java.util.Map;


public class DictionaryActivity extends AppCompatActivity implements MainPageView {
    private MaterialViewPager mViewPager;
    Map<Integer,Integer> mapCardPerPage = new HashMap<>();
    static final int TAPS = 5;
    //Presenter
    DetailedPoetryPresenter myPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        mapCardPerPage.put(0,1);

        //添加监听
        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://upload.art.ifeng.com/2018/0621/1529553560153.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "http://file.763g.com/tu/d/file/p/2015-05-21/pyollcav1m59446.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "https://images.shobserver.com/news/690_390/2017/7/14/c09bd564-ae0f-4c8b-90fe-06ece42c9486.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://img01.1sucai.com/170618/330772-1F61QFU045.jpg");
                    case 4:
                        return HeaderDesign.fromColorResAndUrl(
                            R.color.lime,
                            "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }
                return null;
            }
        });

        //添加toolbar
        Toolbar toolbar = mViewPager.getToolbar();

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }

        PoetryPagerAdapter poetryPagerAdapter = new PoetryPagerAdapter(getSupportFragmentManager());
        mViewPager.getViewPager().setAdapter(poetryPagerAdapter);
        //更新主标题
        //默认加载所有Fragment，以便Presenter更新数据
        mViewPager.getViewPager().setOffscreenPageLimit(5);

        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
        myPresenter = new DetailedPoetryPresenterImpl(this,poetryPagerAdapter.getFragmentViewList(),new DetailCrawlerImpl());
        myPresenter.onCreate("ed3504a25e60");
    }

    /*
    先将数据传送给MainActivity，进行页面加载动画显示，再去更新Fragment
     */
    @Override
    public void toMainActivity(String mainTitle) {
        TextView logo = mViewPager.findViewById(R.id.logo_white);
        logo.setText(mainTitle);
    }
}

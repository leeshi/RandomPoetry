package com.lishi.adruino.randompoetry.ui.dictionary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.lishi.adruino.randompoetry.R;
import com.lishi.adruino.randompoetry.presenter.Presenter;
import com.lishi.adruino.randompoetry.ui.dictionary.adapter.PoetryPagerAdapter;
import com.lishi.adruino.randompoetry.ui.dictionary.model.DetailCrawlerImpl;
import com.lishi.adruino.randompoetry.ui.dictionary.presenter.DetailPoetryPresenterImpl;
import com.lishi.adruino.randompoetry.ui.dictionary.view.MainPageView;
import com.lishi.adruino.randompoetry.utils.ImageUrlUtils;


import java.util.HashMap;
import java.util.Map;


public class DictionaryActivity extends AppCompatActivity implements MainPageView {
    private MaterialViewPager mViewPager;
    private Map<Integer,Integer> mapCardPerPage = new HashMap<>();

    private Presenter myPresenter;

    private String serial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        //获取参数
        Intent intent = this.getIntent();
        serial = (String)intent.getStringExtra("serial");
        //初始化ViewPager
        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        mapCardPerPage.put(0,1);

        String[] Urls = ImageUrlUtils.getUrls();
        //添加监听
        mViewPager.setMaterialViewPagerListener((page)->{
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue, Urls[page]);
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green, Urls[page]);
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan, Urls[page]);
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red, Urls[page]);
                }
                return null;
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

        //返回按钮
        mViewPager.getToolbar().setNavigationOnClickListener((view)->{
            DictionaryActivity.this.finish();
        });

        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
        myPresenter = new DetailPoetryPresenterImpl(this,poetryPagerAdapter.getFragmentViewList(),new DetailCrawlerImpl());
        if(serial != null){
            myPresenter.onProcess(serial);
        }
    }

    /*
    先将数据传送给MainActivity，进行页面加载动画显示，再去更新Fragment
     */
    @Override
    public void toMainActivity(String mainTitle) {
        TextView logo = mViewPager.findViewById(R.id.logo_white);
        logo.setText(mainTitle);
    }
    @Override
    public void showFailedError(){
        Toast.makeText(this, "加载失败T.T请检查网络", Toast.LENGTH_SHORT).show();
    }
}

package com.lishi.adruino.randompoetry.ui.search.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.lishi.adruino.randompoetry.R;
import com.lishi.adruino.randompoetry.ui.search.adapter.PoetryListViewAdapter;
import com.lishi.adruino.randompoetry.item.PoetryItem;
import com.lishi.adruino.randompoetry.ui.search.presenter.SearchPoetryPresenter;
import com.lishi.adruino.randompoetry.ui.search.view.SearchPoetryView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchPoetryView {
    private ListView mPoetryListView;
    private View footerListView;
    private km.lmy.searchview.SearchView mSearchView;
    private SearchView mSearchBut;
    private Toolbar activityToolbar;
    private Button buttonSearchOpetion;

    private PoetryListViewAdapter mPoetryListViewAdapter;

    private SearchPoetryPresenter mSearchPoetryPresenter = new SearchPoetryPresenter(this);


    private String SearchContent;
    private boolean searched;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_search_poetry);


        //init toolbar
        this.activityToolbar = findViewById(R.id.activity_toolbar);
        this.activityToolbar.setTitle(this.getTitle());
        this.activityToolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(activityToolbar);

        //init button
        buttonSearchOpetion = findViewById(R.id.search_option_button);
        buttonSearchOpetion.setOnClickListener((v)-> {
            mode++;
            switch (mode%3){
                case 1:
                    buttonSearchOpetion.setText("作者");
                    return;
                case 2:
                    buttonSearchOpetion.setText("题目");
                    return;
                case 0:
                    buttonSearchOpetion.setText("模糊");
                    return;
            }
        });

        //init ListView
        initListView();

        //init SearchView
        initSearchView();
    }


    public void initSearchView(){
        this.mSearchView = findViewById(R.id.searchView);
        List<String> HistoryList = new ArrayList<>();
        this.mSearchView.setNewHistoryList(HistoryList);


        this.mSearchView.setOnSearchActionListener((v)->{
            SearchContent = this.mSearchView.getEditTextView().getText().toString();
            this.mSearchView.addOneHistory(SearchContent);
            searched = false;
            this.mSearchPoetryPresenter.search();
            this.mSearchView.getEditTextView().setText("");
            this.mSearchView.close();
        });

        //隐藏按钮
        this.mSearchView.setOnSearchBackIconClickListener((v)->{
            this.mSearchView.close();
        });

        //init button
        this.mSearchBut = findViewById(R.id.search);
        this.mSearchBut.setOnSearchClickListener((v)->{
            this.mSearchBut.onActionViewCollapsed();
            this.mSearchView.open();
        });
    }

    public void initListView(){
        this.mPoetryListView = (ListView) findViewById(R.id.PoetryListView);
        this.mPoetryListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        boolean toBottom = view.getLastVisiblePosition() == view.getCount() - 1;
                        if (toBottom) {
                            Log.d("ListView","到达底部");
                            mSearchPoetryPresenter.search();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        footerListView = this.getLayoutInflater().inflate(R.layout.litview_footview, null);
        ProgressBar footProgressBar = (ProgressBar) footerListView.findViewById(R.id.listview_footview_progressBar);
        //TODO 添加刷新样式
        this.mPoetryListView.addFooterView(footerListView);

        this.mPoetryListView.setFooterDividersEnabled(false);
        this.mPoetryListView.setHeaderDividersEnabled(false);

        footerListView.setVisibility(View.GONE);

        this.mPoetryListViewAdapter = new PoetryListViewAdapter(this,new ArrayList<>());
        this.mPoetryListView.setAdapter(this.mPoetryListViewAdapter);
    }


    /*
     * 实现OnSearchListener接口
     */
    @Override
    public void clearPoetry(){
        //TODO
    }

    @Override
    public int getMode(){
        return mode % 3;
    }

    @Override
    public boolean ifSearched(){
        return searched;
    }

    @Override
    public void setSearched(){
        searched = true;
    }

    @Override
    public String getContent(){
        //TODO
        //获取信息
        //测试而已
        //return this.SearchContent;
        return SearchContent;
    }

    @Override
    public void showFailedError(){
        Toast.makeText(this,"failed to load poetry",Toast.LENGTH_LONG);
    }

    @Override
    public void showLoading(){
        //TODO
        footerListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading(){
        //TODO
        footerListView.setVisibility(View.GONE);
    }

    //将诗句传送到主界面
    @Override
    public void toMainActivity(List<PoetryItem> list){
        //TODO
        this.mPoetryListViewAdapter.update(list);
        this.mPoetryListViewAdapter.notifyDataSetChanged();
    }
}


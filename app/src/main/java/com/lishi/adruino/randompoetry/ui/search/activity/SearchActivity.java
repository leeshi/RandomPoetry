package com.lishi.adruino.randompoetry.ui.search.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.lishi.adruino.randompoetry.R;
import com.lishi.adruino.randompoetry.ui.dictionary.activity.DictionaryActivity;
import com.lishi.adruino.randompoetry.ui.search.adapter.PoetryListViewAdapter;
import com.lishi.adruino.randompoetry.item.PoetryItem;
import com.lishi.adruino.randompoetry.ui.search.presenter.SearchingPresenterImpl;
import com.lishi.adruino.randompoetry.ui.search.view.SearchingView;
import com.lishi.adruino.randompoetry.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchingView {
    private ListView mPoetryListView;
    private View footerListView;
    private km.lmy.searchview.SearchView mSearchView;
    private SearchView mSearchBut;
    private Toolbar activityToolbar;
    private Button buttonSearchOption;

    private PoetryListViewAdapter mPoetryListViewAdapter;

    private SearchingPresenterImpl mSearchPoetryPresenter = new SearchingPresenterImpl(this);


    private String SearchContent;
    private boolean searched;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_poetry);

        //init toolBar and button
        initButTool();
        //init ListView
        initListView();
        //init SearchView
        initSearchView();

        //是否有数据传入
        //TODO 设置为模式
        Intent intent = getIntent();
        String sentence = intent.getStringExtra("sentence");
        if(sentence != null){
            mSearchPoetryPresenter.onProcess(sentence);
        }

    }

    private void initButTool(){
        //init toolbar
        this.activityToolbar = findViewById(R.id.activity_toolbar);
        this.activityToolbar.setTitle(this.getTitle());
        this.activityToolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(activityToolbar);

        //init button
        buttonSearchOption = findViewById(R.id.search_option_button);
        buttonSearchOption.setOnClickListener((v)-> {
            mode++;
            switch (mode%3){
                case 1:
                    buttonSearchOption.setText("作者");
                    return;
                case 2:
                    buttonSearchOption.setText("题目");
                    return;
                case 0:
                    buttonSearchOption.setText("模糊");
                    return;
            }
        });
    }

    private void initSearchView(){
        this.mSearchView = findViewById(R.id.searchView);
        List<String> HistoryList = new ArrayList<>();
        this.mSearchView.setNewHistoryList(HistoryList);


        this.mSearchView.setOnSearchActionListener((v)->{
            mPoetryListView.setSelection(0);

            SearchContent = this.mSearchView.getEditTextView().getText().toString();
            //输入内容检查
            if(SearchContent.isEmpty()){
                Toast.makeText(this,"空时什么呢（。＾▽＾）",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!StringUtils.checkLegality(SearchContent)){
                Toast.makeText(this,"请输入中文字符（。＾▽＾）",Toast.LENGTH_SHORT).show();
                return;
            }

            this.mSearchView.addOneHistory(SearchContent);
            searched = false;
            this.mSearchPoetryPresenter.onProcess(null);
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

    private void initListView(){
        this.mPoetryListView = (ListView) findViewById(R.id.PoetryListView);
        this.mPoetryListView.setOnItemClickListener((parent,view,position,id)->{
            //启动详情页
            Intent intent = new Intent();
            intent.setClass(this,DictionaryActivity.class);
            intent.putExtra("mode","serial");
            PoetryItem selectedItem = (PoetryItem)mPoetryListViewAdapter.getItem(position);
            intent.putExtra("serial",selectedItem.serial);
            //test
            System.out.println(selectedItem.mTitle+":"+selectedItem.serial);
            startActivity(intent);
        });
        this.mPoetryListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        boolean toBottom = view.getLastVisiblePosition() == view.getCount() - 1;
                        if (toBottom) {
                            Log.d("ListView","到达底部");
                            mSearchPoetryPresenter.onProcess(null);
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
        Intent intent = getIntent();
        String content = intent.getStringExtra("sentence");
        intent.removeExtra("sentence");
        if(content == null) {
            return SearchContent;
        }else{
            return content;
        }
    }

    @Override
    public void showFailedError(){
        Toast.makeText(this,"加载失败＞﹏＜请重试",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(){
        footerListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading(){
        footerListView.setVisibility(View.GONE);
        //Toast.makeText(this,"加载完成(●ˇ∀ˇ●)",Toast.LENGTH_SHORT).show();
    }

    //将诗句传送到主界面
    @Override
    public void toMainActivity(List<PoetryItem> list){
        //TODO
        this.mPoetryListViewAdapter.update(list);
        this.mPoetryListViewAdapter.notifyDataSetChanged();
    }
}


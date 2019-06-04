package com.lishi.adruino.randompoetry.ui.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lishi.adruino.randompoetry.R;
import com.lishi.adruino.randompoetry.ui.main.adapter.RecommendationListViewAdapter;
import com.lishi.adruino.randompoetry.item.PoetryItem;
import com.lishi.adruino.randompoetry.ui.main.model.MainCrawlerImpl;
import com.lishi.adruino.randompoetry.presenter.Presenter;
import com.lishi.adruino.randompoetry.ui.main.presenter.MainPresenterImpl;
import com.lishi.adruino.randompoetry.ui.main.view.RecommendationView;
import com.lishi.adruino.randompoetry.ui.search.activity.SearchActivity;
import com.lishi.adruino.randompoetry.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity implements RecommendationView {
    private ListView recommListView;
    private RecommendationListViewAdapter mRecommendationListViewAdapter;

    private TextView sentenceTextView;
    private TextView fromTextView;

    //上下联
    private me.grantland.widget.AutofitTextView firstTextView;
    private me.grantland.widget.AutofitTextView secondTextView;
    private TextInputEditText inputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*-----------------------悬浮按钮---------------------*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((v) -> {
            Intent intent = new Intent();
            intent.setClass(ScrollingActivity.this,SearchActivity.class);
            startActivity(intent);
        });

        /*------------------------ListView-------------------*/
        mRecommendationListViewAdapter = new RecommendationListViewAdapter(this,new ArrayList<>());
        recommListView = findViewById(R.id.recommendations);
        recommListView.setAdapter(mRecommendationListViewAdapter);
        //点击监听
        recommListView.setOnItemClickListener((AdapterView<?> arg0, View view, int position,
                                               long arg3)->{
            PoetryItem poetryItem = (PoetryItem) mRecommendationListViewAdapter.getItem(position);
            //添加启动参数
            Intent intent = new Intent();
            intent.putExtra("sentence",poetryItem.mContent);
            intent.setClass(this,SearchActivity.class);
            startActivity(intent);
        });

        /*--------------------presenter------------------*/
        Presenter randomPoetryPresenter = new MainPresenterImpl(this,new MainCrawlerImpl());
        randomPoetryPresenter.onProcess(null);

        /*--------------------InputTextView--------------*/
        inputEditText = findViewById(R.id.couplet);
        inputEditText.setOnEditorActionListener((v,actionID,keyEvent)->{
            String couplet = inputEditText.getText().toString();
            //进行内容合法性检查
            if(StringUtils.checkLegality(couplet)) {
                inputEditText.clearFocus();
                inputEditText.setText("");
                randomPoetryPresenter.onProcess(couplet);
                firstTextView.setText("上联：" + couplet);
                secondTextView.setText("");
            }else{
                Toast.makeText(ScrollingActivity.this,"只允许输入中文字符",Toast.LENGTH_SHORT).show();
            }
            return false;
        });

        secondTextView = findViewById(R.id.first_couplet);
        firstTextView = findViewById(R.id.second_couplet);

        /*--------------------句子------------------------*/
        fromTextView = findViewById(R.id.from);
        sentenceTextView = findViewById(R.id.sentence);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(this,SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading() {
        Toast.makeText(this,"加载中",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFailedError() {
        Toast.makeText(this,"加载失败≡(▔﹏▔)≡",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideLoading() {
        Toast.makeText(this,"加载结束(●'◡'●)",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadSuccess() {

    }

    @Override
    public void toMainActivity(Object data) {
        //TODO 进行类型检查
        if(List.class.isInstance(data)) {
            List<PoetryItem> listData = (List<PoetryItem>) data;
            mRecommendationListViewAdapter.update(listData.subList(0, listData.size() - 1));
            mRecommendationListViewAdapter.notifyDataSetChanged();
            fromTextView.setText("————" + listData.get(listData.size() - 1).getPoet());
            sentenceTextView.setText(listData.get(listData.size() - 1).getContent());
        }else if(String.class.isInstance(data)){
            secondTextView.setText("下联：" + (String)data);
        }
    }
}

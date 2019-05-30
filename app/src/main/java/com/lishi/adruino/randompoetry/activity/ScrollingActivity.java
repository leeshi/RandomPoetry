package com.lishi.adruino.randompoetry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.lishi.adruino.randompoetry.adapter.RecommendationListViewAdapter;
import com.lishi.adruino.randompoetry.item.PoetryItem;
import com.lishi.adruino.randompoetry.model.CrawlerImpl;
import com.lishi.adruino.randompoetry.presenter.Presenter;
import com.lishi.adruino.randompoetry.presenter.RandomPoetryPresenterImpl;
import com.lishi.adruino.randompoetry.view.RecommendationView;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ScrollingActivity.this,DictionaryActivity.class);
                startActivity(intent);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        fromTextView = findViewById(R.id.from);
        sentenceTextView = findViewById(R.id.sentence);

        mRecommendationListViewAdapter = new RecommendationListViewAdapter(this,new ArrayList<PoetryItem>());
        recommListView = findViewById(R.id.recommendations);
        recommListView.setAdapter(mRecommendationListViewAdapter);
        Presenter randomPoetryPresenter = new RandomPoetryPresenterImpl();
        randomPoetryPresenter.onCreate(this,new CrawlerImpl());
        randomPoetryPresenter.onProcess(null);
        //设置上下联
        inputEditText = findViewById(R.id.couplet);
        inputEditText.setOnEditorActionListener((v,actionID,keyEvent)->{
            //TODO 先进行输入内容的检查
            String couplet = inputEditText.getText().toString();
            inputEditText.clearFocus();
            inputEditText.setText("");
            randomPoetryPresenter.onProcess(couplet);
            firstTextView.setText(couplet);
            return false;
        });

        secondTextView = findViewById(R.id.first_couplet);
        firstTextView = findViewById(R.id.second_couplet);


        //点击监听
        //TODO 启动别的页面
        recommListView.setOnItemClickListener((AdapterView<?> arg0, View arg1, int arg2,
                                               long arg3)->{
            PoetryItem poetryItem = (PoetryItem) recommListView.getItemAtPosition(arg2);
            Toast.makeText(ScrollingActivity.this,poetryItem.toString(),Toast.LENGTH_LONG).show();
        });
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading() {
        Toast.makeText(this,"Loading",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFailedError() {
        Toast.makeText(this,"Failed to connect!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideLoading() {
        Toast.makeText(this,"Loading finish",Toast.LENGTH_SHORT).show();
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
            secondTextView.setText((String)data);
        }
    }
}

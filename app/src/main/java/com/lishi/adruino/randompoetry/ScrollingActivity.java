package com.lishi.adruino.randompoetry;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.lishi.adruino.randompoetry.adapter.RecommendationListViewAdapter;
import com.lishi.adruino.randompoetry.item.PoetryItem;
import com.lishi.adruino.randompoetry.model.RecommendationsCrawlerImpl;
import com.lishi.adruino.randompoetry.presenter.RandomPoetryPresenter;
import com.lishi.adruino.randompoetry.presenter.RandomPoetryPresenterImpl;
import com.lishi.adruino.randompoetry.view.RecommendationView;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity implements RecommendationView {
    private ListView recommListView;
    private RecommendationListViewAdapter mRecommendationListViewAdapter;

    private TextView sentenceTextView;
    private TextView fromTextView;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fromTextView = findViewById(R.id.from);
        sentenceTextView = findViewById(R.id.sentence);

        mRecommendationListViewAdapter = new RecommendationListViewAdapter(this,new ArrayList<PoetryItem>());
        recommListView = findViewById(R.id.recommendations);
        recommListView.setAdapter(mRecommendationListViewAdapter);

        RandomPoetryPresenter randomPoetryPresenter = new RandomPoetryPresenterImpl();
        randomPoetryPresenter.onCreate(this,new RecommendationsCrawlerImpl());
        randomPoetryPresenter.onProcess();
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

    }

    @Override
    public void showFailedError() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void loadSuccess() {

    }

    @Override
    public void toMainActivity(List<PoetryItem> listData) {
        mRecommendationListViewAdapter.update(listData.subList(0,listData.size() - 1));
        mRecommendationListViewAdapter.notifyDataSetChanged();
        fromTextView.setText( "--" + listData.get(listData.size() - 1).getPoet());
        sentenceTextView.setText(listData.get(listData.size() - 1).getContent());
    }
}

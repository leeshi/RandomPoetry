package com.lishi.adruino.randompoetry.model;

import com.google.gson.Gson;
import com.lishi.adruino.randompoetry.item.PoetryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class RecommendationsCrawlerImpl implements Crawler{
    private final String tarUrl = "https://api.gushi.ci/all.json";

    @Override
    public void search(OnLoadListener onLoadListener) {
        new Thread(()->{
            System.out.println();
            try{
                Connection conn = Jsoup.connect(tarUrl).ignoreContentType(true).header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
                Document doc = Jsoup.parse(conn.get().html());

                ArrayList<PoetryItem> listData = new ArrayList<>();

                JSONObject json = new JSONObject(doc.text());

                listData.add(new PoetryItem(json.getString("content"),json.getString("origin"),json.getString("author")));

                onLoadListener.loadSuccess(listData);
            }catch (IOException e){
                e.printStackTrace();
                onLoadListener.loadFailed();
            } catch (JSONException e) {
                e.printStackTrace();
                onLoadListener.loadFailed();
            }
        }).start();
    }
}

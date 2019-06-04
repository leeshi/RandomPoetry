package com.lishi.adruino.randompoetry.ui.main.model;

import com.lishi.adruino.randompoetry.item.PoetryItem;
import com.lishi.adruino.randompoetry.model.Crawler;
import com.lishi.adruino.randompoetry.model.OnLoadListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class MainCrawlerImpl implements Crawler {
    private final String tarUrl = "http://10.122.195.45:8888";

    @Override
    public void search(OnLoadListener onLoadListener) {
        new Thread(()->{
            try{
                if(onLoadListener.getLoadOption() == null) {
                    Connection conn = Jsoup.connect(tarUrl).ignoreContentType(true).header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36").timeout(3000);
                    Document doc = Jsoup.parse(conn.get().html());

                    ArrayList<PoetryItem> listData = new ArrayList<>();

                    JSONObject out_json = new JSONObject(doc.text());
                    JSONArray jsons = out_json.getJSONArray("data");
                    for (int i = 0; i < jsons.length(); i++) {
                        JSONObject json = jsons.getJSONObject(i);
                        listData.add(new PoetryItem(json.getString("content"), json.getString("origin"), json.getString("author")));
                    }

                    conn = Jsoup.connect("https://v1.hitokoto.cn/").ignoreContentType(true);
                    doc = Jsoup.parse(conn.get().html());
                    JSONObject json = new JSONObject(doc.text());
                    listData.add(new PoetryItem(json.getString("hitokoto"), json.getString("from"), json.getString("creator")));

                    onLoadListener.loadSuccess(listData);
                }
                else{
                    Connection conn = Jsoup.connect(tarUrl).ignoreContentType(true).timeout(3000);
                    String couplet = (String)onLoadListener.getLoadOption();
                    Document doc = Jsoup.parse(conn.data("couplet",couplet).post().html());

                    JSONObject jsonObject = new JSONObject(doc.text());
                    onLoadListener.loadSuccess(jsonObject.getString("next_couplet"));
                }
            }catch (IOException e){
                e.printStackTrace();
                onLoadListener.loadFailed();
            } catch (JSONException e) {
                e.printStackTrace();
                onLoadListener.loadFailed();
            }finally {
                onLoadListener.loadOver();
            }
        }).start();
    }
}

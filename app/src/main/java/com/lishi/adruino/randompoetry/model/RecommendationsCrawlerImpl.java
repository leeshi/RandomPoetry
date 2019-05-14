package com.lishi.adruino.randompoetry.model;

import com.lishi.adruino.randompoetry.item.PoetryItem;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class RecommendationsCrawlerImpl implements Crawler{
    private final String tarUrl = "https://www.gushiwen.org/";

    @Override
    public void search(OnLoadListener onLoadListener) {
        new Thread(()->{
            System.out.println();
            try{
                Connection conn = Jsoup.connect(tarUrl).header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
                Document doc = Jsoup.parse(conn.get().html());
                ArrayList<PoetryItem> listData = new ArrayList<>();

                Elements sons = doc.getElementsByClass("sons");

                for(int i = 0;i < 10;i++){
                    Element son = sons.get(i);
                    Element contson = son.getElementsByClass("contson").get(0);
                    Element source = son.getElementsByAttributeValue("class","source").get(0);
                    Element title = son.getElementsByTag("b").get(0);
                    String AfterRe = contson.html().replaceAll("<br>","").replaceAll("</?[^>]+>","").replaceAll("\\(.*?\\)","");
                    listData.add(new PoetryItem(AfterRe,title.text(),source.text()));
                }

                onLoadListener.loadSuccess(listData);
            }catch (IOException e){
                e.printStackTrace();
                onLoadListener.loadFailed();
            }
        }).start();
    }
}

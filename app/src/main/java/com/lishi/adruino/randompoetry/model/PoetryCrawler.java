package com.lishi.adruino.randompoetry.model;

import android.util.Log;

import com.lishi.adruino.randompoetry.item.PoetryItem;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PoetryCrawler implements PoetryCrawlerBiz{
    private final String mPoetryWebUrl = "https://so.gushiwen.org/search.aspx?";
    private String mSearchType = "title";
    private int PageCount = 0;                  //当前搜索内容的总页数
    private int NowCount = 1;                   //当前搜索到的页数


    /*
     * 1-> author
     * 2-> title
     */
    @Override
    public void SearchPoetry(int searchMode,boolean searched, String content, final OnLoadListener searchListener){
        new Thread( () ->{
            String TarUrl;
            if(searchMode == 1) {
                TarUrl = this.mPoetryWebUrl + "type=author" + "&page=" + NowCount + "&value=" + content;
            }
            else if(searchMode == 2){
                TarUrl = this.mPoetryWebUrl + "title="+"&page=" + NowCount +"&value="+content;
            }
            else {
                TarUrl = this.mPoetryWebUrl + "&page=" + NowCount + "&value=" + content;
            }


            //test
            System.out.println("ifSearched: "+searched);

            if(!searched)
                NowCount = 1;

            Log.d("CrawlerTargetUrl",TarUrl);
            try {
                Connection conn = Jsoup.connect(TarUrl).timeout(3000);
                Document doc = Jsoup.parse(conn.get().html());
                Elements ListDiv = doc.getElementsByAttributeValue("class","sons");

                List<PoetryItem> list = new ArrayList<>();

                Elements pageCountElement = doc.getElementsByAttributeValue("id", "sumPage");
                if(!pageCountElement.isEmpty()) {
                    if(NowCount == 1){
                        PageCount = Integer.parseInt(pageCountElement.get(0).text());
                        System.out.println("sumPage: "+PageCount);
                    }
                    else if(NowCount > PageCount) {
                        searchListener.loadOver();      //如果已经搜索到所有页面，结束所有搜索
                        return;
                    }
                }
                //空集合，搜索不到内容
                else{
                    searchListener.loadFailed();
                    return;
                }

                NowCount++;
                for(Element element:ListDiv){
                    Element contson = element.getElementsByAttributeValue("class","contson").get(0);
                    Element source = element.getElementsByAttributeValue("class","source").get(0);
                    Element title = element.getElementsByTag("b").get(0);
                    String AfterRe = contson.html().replaceAll("<br>","").replaceAll("</?[^>]+>","").replaceAll("\\(.*?\\)","");
                    list.add(new PoetryItem(AfterRe,title.text(),source.text()));
                }
                    searchListener.loadSuccess(list);
            }
            catch(IOException e){
                e.printStackTrace();
                searchListener.loadFailed();
            }

        }).start();
    }
}

package com.lishi.adruino.randompoetry.ui.search.model;

import android.util.Log;

import com.lishi.adruino.randompoetry.item.PoetryItem;
import com.lishi.adruino.randompoetry.model.Crawler;
import com.lishi.adruino.randompoetry.model.OnLoadListener;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PoetryCrawler implements Crawler {
    private final String mPoetryWebUrl = "https://so.gushiwen.org/search.aspx?";
    private int PageCount = 0;                  //当前搜索内容的总页数
    private int NowCount = 1;                   //当前搜索到的页数


    /*
     * 1-> author
     * 2-> title
     */
    @Override
    public void search(final OnLoadListener searchListener){
        new Thread( () ->{
            //初始化搜索选项
            Map<String,Object> options = (Map<String,Object>)searchListener.getLoadOption();
            boolean searched = (Boolean) options.get("searched");
            int searchMode = (Integer)options.get("mode");
            String content = (String)options.get("content");

            if(content.isEmpty()){
                searchListener.loadOver();
                return;
            }

            String TarUrl;
            if(!searched)
                NowCount = 1;

            if(searchMode == 1) {
                TarUrl = this.mPoetryWebUrl + "type=author" + "&page=" + NowCount + "&value=" + content;
            }
            else if(searchMode == 2){
                TarUrl = this.mPoetryWebUrl + "title="+"&page=" + NowCount +"&value="+content;
            }
            else {
                TarUrl = this.mPoetryWebUrl + "&page=" + NowCount + "&value=" + content;
            }


            Log.d("CrawlerTargetUrl",TarUrl);
            try {
                Connection conn = Jsoup.connect(TarUrl).timeout(3000);
                Document doc = Jsoup.parse(conn.get().html());
                Elements ListDiv = doc.getElementsByAttributeValue("class","sons");

                //爬取不到任何诗词
                if(ListDiv.isEmpty()){
                    System.out.println("内容加载结束");
                    searchListener.loadOver();
                    return;
                }

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

                for(Element element:ListDiv){
                    Element contson = element.getElementsByAttributeValue("class","contson").get(0);
                    Element source = element.getElementsByAttributeValue("class","source").get(0);
                    Element title = element.getElementsByTag("b").get(0);
                    String AfterRe = contson.html().replaceAll("<br>","")
                            .replaceAll("</?[^>]+>","")
                            .replaceAll("\\(.*?\\)","");

                    String serial = contson.id().replaceAll("contson","");

                    list.add(new PoetryItem(AfterRe,title.text(),source.text(),serial));
                }
                searchListener.loadSuccess(list);
                //成功更新数据后再更新count，防止爬取出错
                NowCount++;
            }
            catch(IOException e){
                e.printStackTrace();
                searchListener.loadFailed();
            }

        }).start();
    }
}

package com.lishi.adruino.randompoetry.ui.dictionary.model;

import android.util.Log;

import com.lishi.adruino.randompoetry.item.PoetryItem;
import com.lishi.adruino.randompoetry.model.Crawler;
import com.lishi.adruino.randompoetry.model.OnLoadListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailCrawlerImpl implements Crawler {
    @Override
    public void search(final OnLoadListener onLoadListener) {
        //新建一个线程
        new Thread(() -> {
            //TODO 要确保不会出现别的数据类型
            String serial = (String)onLoadListener.getLoadOption();
            String url = "https://so.gushiwen.org/shiwenv_"+ serial + ".aspx";
            System.out.println(url);
            Log.d("Crawler","访问 "+url);
            try {
                Document doc = Jsoup.connect(url).header("User-Agent", "chrome").get();
                //sons列表
                Elements sons = doc.getElementsByClass("sons");
                //古诗文内容
                Element contson = doc.getElementsByAttributeValue("class", "contson").get(0);
                //获取题目
                String AfterRe = contson.html().replaceAll("<br>","").replaceAll("</?[^>]+>","").replaceAll("\\(.*?\\)","");

                String title = doc.getElementsByTag("h1").get(0).text();
                //String title = doc.getElementsByAttribute("h1").get(0).text();
                //获取朝代与作者
                Element sourceElement = doc.getElementsByAttributeValue("class", "source").get(0);
                String source = sourceElement.getAllElements().get(0).text();

                //获取注释和赏析，逻辑比较复杂
                //有时候，翻译会有两个部分，需要加以区分

                StringBuilder shangxiSB = new StringBuilder();
                StringBuilder fanyiSB = new StringBuilder();
                String backGround = new String();

                Elements sonElements = doc.getElementsByAttributeValue("class", "sons");
                String baseUrl = "https://so.gushiwen.org/shiwen2017/ajax";
                for (Element element : sonElements) {
                    if (element.getElementsByTag("h2").text().contains("背景")) {
                        backGround = element.getElementsByAttributeValue("class", "contyishang").get(0).text();
                        continue;
                    } else if (element.getElementsByTag("h2").text().contains("译文") && !element.text().contains("展开")) {//提取不需要展开的信息
                        fanyiSB.append(element.getElementsByAttributeValue("class", "contyishang").get(0).text());
                        continue;
                    } else if (element.getElementsByTag("h2").text().contains("赏析") && !element.text().contains("展开")) {//提取不需要展开的信息
                        shangxiSB.append(element.getElementsByAttributeValue("class", "contyishang").get(0).text());
                        continue;
                    }


                    String id = element.id();
                    //使用正则表达式提取id
                    String number = id.replaceAll("[^0-9]", "");
                    //使用正则表达式提取模式
                    //包括翻译，赏析等等
                    //必须排除不需要展开的情况
                    String mode = id.replaceAll("[^A-Za-z]", "");
                    //contains "quan"是播放js，与内容无关
                    if (!number.isEmpty() && id.contains("fanyi") && !id.contains("quan")) {
                        String newUrl = baseUrl + mode + ".aspx?id=" + number;
                        //添加索引，不然会被封
                        Document fanyiDoc = Jsoup.connect(newUrl).header("User-Agent", "chrome").referrer(url).cookie("ASP.NET_SessionId", "bqodoezoibimiwe4arvepsip").get();
                        System.out.println(newUrl);
                        //逐个抓取标签p的文字
                        Element fanyiItem = fanyiDoc.getElementsByAttributeValue("class", "contyishang").get(0);
                        for(Element p:fanyiItem.getElementsByTag("p")){
                            fanyiSB.append(p.text() + '\n');
                        }
                    } else if (!number.isEmpty() && id.contains("shangxi") && !id.contains("quan")) {

                        String newUrl = baseUrl + mode + ".aspx?id=" + number;
                        Document shangxiDoc = Jsoup.connect(newUrl).header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                                .header("referer",url).get();
                        System.out.println(newUrl);
                        Element shangxiElement = shangxiDoc.getElementsByClass("contyishang").get(0);
                        for(Element p:shangxiElement.getElementsByTag("p")){
                            shangxiSB.append(p.text() + '\n');
                        }
                    }
                }

                Log.d("DetailCrawlerImpl", "successfully fetch poetry data");

                //必须注意，这里的list包含了所有信息
                //包括推荐
                List<PoetryItem> listPoetry = getRecommendation(sons);

                List<Object> listData = new ArrayList<>();

                //当前的诗词单项
                PoetryItem currentPoetry = new PoetryItem(AfterRe,title,source,serial);

                listData.add(currentPoetry);
                listData.add(backGround);
                //背景与诗词显示在同一个卡片内
                listData.add(fanyiSB.toString());
                listData.add(shangxiSB.toString());
                listData.addAll(listPoetry);
                onLoadListener.loadSuccess(listData);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("DetailCrawlerImpl", "failed to load");
                onLoadListener.loadFailed();
            } finally {
                onLoadListener.loadOver();
            }
        }).start();
    }

    /*
     * 搜索所有的推荐诗词
     * @return List<String>
     * @param Elements
     * 按照title,source,content,serial的顺序排列
     */
    private List<PoetryItem> getRecommendation(Elements sons){
        List<PoetryItem> listPoetry = new ArrayList<>();

        //后三个sons element是推荐的诗词内容
        int size = sons.size();
        //用循环获取元素
        for(int i = size - 1;i > size - 4;i--){
            Element element = sons.get(i);
            //诗词的主题，包含内容和id
            Element contson = element.getElementsByAttributeValue("class","contson").get(0);
            //获取serial
            String serial = contson.id().replace("contson","");
            String AfterRe = contson.html().replaceAll("<br>","").replaceAll("</?[^>]+>","").replaceAll("\\(.*?\\)","");
            //第一个p元素是题目
            String title = element.getElementsByTag("p").get(0).text();

            String source = element.getElementsByAttributeValue("class","source").get(0).text();

            PoetryItem poetryItem = new PoetryItem(AfterRe,title,source,serial);
            //TODO 使用串号需要新建对象,暂时没有想到使用对象同时又便于复用Fragment的方法
            //TODO 可以使用新的Fragment
            listPoetry.add(poetryItem);
        }

        return listPoetry;
    }
}

package com.lishi.adruino.randompoetry.item;

public class PoetryItem {
    private String mContent;
    private String mTitle;
    private String mPoet;

    public PoetryItem(String content,String title,String poet){
        this.mContent = content;
        this.mPoet = poet;
        this.mTitle = title;
    }

    /*getter
     */
    public String getContent(){
        return this.mContent;
    }
    public String getTitle(){
        return this.mTitle;
    }
    public String getPoet(){
        return this.mPoet;
    }
}


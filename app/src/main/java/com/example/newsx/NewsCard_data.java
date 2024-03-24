package com.example.newsx;

public class NewsCard_data {
    String title,content,urltoimage,url,datetime,source;

    public NewsCard_data(String title,String content,String urltoimage,String url,String datetime, String source){
        this.title = title;
        this.content=content;
        this.url=url;
        this.urltoimage =urltoimage;
        this.datetime = datetime;
        this.source=source;
    }
}

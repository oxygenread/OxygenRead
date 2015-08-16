package com.dragonlz.oxygenread.UI.Model;

/**
 * Created by ASUS on 2015/8/11.
 */
public class News {

       private String title;
       private int newsImage;

    public News(String title,int newsImage){
        this.title= title;
        this.newsImage = newsImage;
    }

    public String getTitle() {
        return title;
    }

    public int getNewsImage() {
        return newsImage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNewsImage(int newsImage) {
        this.newsImage = newsImage;
    }
}

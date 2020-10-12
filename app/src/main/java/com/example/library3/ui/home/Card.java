package com.example.library3.ui.home;

public class Card {


    String name;
    String auther;
    String book_url;
    String thumbnail;

    public String getBook_url() {
        return book_url;
    }

    public void setBook_url(String book_url) {
        this.book_url = book_url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Card() {
    }
    public Card(String name,String auther,String book_url,String thumbnail)
    {
        this.name=name;
        this.auther=auther;
        this.book_url=book_url;
        this.thumbnail=thumbnail;
    }
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther=auther;
    }
}

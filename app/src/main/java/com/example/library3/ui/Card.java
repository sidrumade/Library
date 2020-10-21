package com.example.library3.ui;

public class Card {


    String name;
    String author;
    String book_url;
    String thumbnail;
    String book_type;

    public String getBook_type() {
        return book_type;
    }

    public void setBook_type(String book_type) {
        this.book_type = book_type;
    }

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
    public Card(String name,String author,String book_url,String thumbnail,String book_type)
    {
        this.name=name;
        this.author=author;
        this.book_url=book_url;
        this.thumbnail=thumbnail;
        this.book_type=book_type;
    }
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author=author;
    }
}

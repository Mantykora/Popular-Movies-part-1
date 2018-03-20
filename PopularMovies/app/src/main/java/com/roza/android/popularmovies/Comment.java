package com.roza.android.popularmovies;

/**
 * Created by hiddenpik on 17.03.2018.
 */

public class Comment {
    private String content;
    private String author;

    public Comment() {}

    public Comment(String content, String author) {
        this.content = content;
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

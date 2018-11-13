package com.nashlincoln.openlibrary.net;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Doc {
    @SerializedName("cover_i")
    private int coverId;

    @SerializedName("has_fulltext")
    private boolean hasFulltext;

    @SerializedName("title")
    private String title;

    @SerializedName("author_name")
    private List<String> authorName;

    @SerializedName("first_publish_year")
    private int firstPublishYear;

    @SerializedName("isbn")
    private List<String> isbn;

    @SerializedName("author_key")
    private List<String> authorKey;

    @SerializedName("publisher")
    private List<String> publisher;

    public int getCoverId() {
        return coverId;
    }

    public boolean hasFulltext() {
        return hasFulltext;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthorName() {
        return authorName;
    }

    public int getFirstPublishYear() {
        return firstPublishYear;
    }

    public String getIsbn() {
        return isbn != null && isbn.size() > 0 ? isbn.get(0) : null;
    }

    public List<String> getAuthorKey() {
        return authorKey;
    }

    public List<String> getPublisher() {
        return publisher;
    }
}

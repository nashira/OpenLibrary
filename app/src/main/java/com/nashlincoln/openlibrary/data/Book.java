package com.nashlincoln.openlibrary.data;

import com.nashlincoln.openlibrary.view.SearchResult;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Book {

    @PrimaryKey
    @NonNull
    private String key;

    @NonNull
    private Date addedAt;

    @Nullable
    private Date completedAt;

    SearchResult searchResult;

    public Book(SearchResult searchResult) {
        this.searchResult = searchResult;
        this.key = searchResult.getIsbn();
        this.addedAt = new Date();
    }

    public static Book fromSearchResult(SearchResult searchResult) {
        return new Book(searchResult);
    }

    @NonNull
    public String getKey() {
        return key;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }

    @NonNull
    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(@NonNull Date addedAt) {
        this.addedAt = addedAt;
    }

    @Nullable
    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(@Nullable Date completedAt) {
        this.completedAt = completedAt;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public boolean isComplete() {
        return getCompletedAt() != null;
    }
}

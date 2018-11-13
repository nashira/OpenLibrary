package com.nashlincoln.openlibrary.view;

import android.text.TextUtils;

import com.nashlincoln.openlibrary.net.Doc;
import com.nashlincoln.openlibrary.net.SearchResponse;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {

    private final List<String> authorName;
    private final int coverId;
    private final int firstPublishYear;
    private final String title;
    private final List<String> publisher;
    private final String isbn;
    private final boolean hasFulltext;

    public SearchResult(Doc doc) {
        authorName = doc.getAuthorName();
        coverId = doc.getCoverId();
        firstPublishYear = doc.getFirstPublishYear();
        title = doc.getTitle();
        publisher = doc.getPublisher();
        isbn = doc.getIsbn();
        hasFulltext = doc.hasFulltext();
    }

    public String getAuthorName() {
        if (authorName == null) {
            return null;
        }
        return TextUtils.join(", ", authorName);
    }

    public int getCoverId() {
        return coverId;
    }

    public int getFirstPublishYear() {
        return firstPublishYear;
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        if (publisher == null) {
            return null;
        }
        return TextUtils.join(", ", publisher);
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean hasFulltext() {
        return hasFulltext;
    }

    public static List<SearchResult> fromSearchResponse(SearchResponse searchResponse) {
        List<SearchResult> searchResults = new ArrayList<>();
        for (Doc doc : searchResponse.getDocs()) {
            searchResults.add(new SearchResult(doc));
        }
        return searchResults;
    }
}

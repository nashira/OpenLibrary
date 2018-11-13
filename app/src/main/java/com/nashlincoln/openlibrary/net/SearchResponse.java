package com.nashlincoln.openlibrary.net;

import java.util.List;

public class SearchResponse {
    private int start;
    private int num_found;
    List<Doc> docs;

    public List<Doc> getDocs() {
        return docs;
    }
}

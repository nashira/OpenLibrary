package com.nashlincoln.openlibrary.net;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenLibraryClient {
    @GET("search.json")
    Observable<SearchResponse> search(@Query("q") String query);

    @GET("search.json")
    Observable<SearchResponse> searchByTitle(@Query("title") String query);

    @GET("search.json")
    Observable<SearchResponse> searchByAuthor(@Query("author") String query);

//    Observable<>
}

package com.nashlincoln.openlibrary.data;

import com.google.gson.Gson;
import com.nashlincoln.openlibrary.view.SearchResult;

import java.util.Date;

import androidx.room.TypeConverter;

public class OpenLibraryTypeConverters {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static long fromDate(Date date) {
        return date == null ? 0 : date.getTime();
    }

    @TypeConverter
    public static Date fromLong(long time) {
        return time == 0 ? null : new Date(time);
    }

    @TypeConverter
    public static String fromSearchResult(SearchResult searchResult) {
        return gson.toJson(searchResult);
    }

    @TypeConverter
    public static SearchResult fromString(String string) {
        return gson.fromJson(string, SearchResult.class);
    }
}

package com.nashlincoln.openlibrary.view;

import android.annotation.SuppressLint;
import android.util.Log;

import com.nashlincoln.openlibrary.net.OpenLibraryClient;
import com.nashlincoln.openlibrary.net.SearchResponse;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends ViewModel {

    private static final String TAG = SearchViewModel.class.getSimpleName();
    private final OpenLibraryClient client;
    private String query;
    private MutableLiveData<List<SearchResult>> searchResultsLiveData;
    private CompositeDisposable disposables;
    private SearchType searchType;

    @Inject
    public SearchViewModel(OpenLibraryClient client) {
        this.client = client;
        searchType = SearchType.EVERYTHING;
        searchResultsLiveData = new MutableLiveData<>();
        disposables = new CompositeDisposable();
    }

    public LiveData<List<SearchResult>> getSearchResults() {
        return searchResultsLiveData;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
        if (query != null) {
            performSearch();
        }
    }

    public void search(String query) {
        this.query = query;
        performSearch();
    }

    @SuppressLint("CheckResult")
    private void performSearch() {
        // cancel any in-progress searches
        disposables.clear();
        Observable<SearchResponse> observable;
        switch (searchType) {
            case TITLE:
                observable = client.searchByTitle(query);
                break;
            case AUTHOR:
                observable = client.searchByAuthor(query);
                break;
            default:
            case EVERYTHING:
                observable = client.search(query);
                break;
        }

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::toSearchResults)
                .subscribe(
                        searchResultsLiveData::setValue,
                        throwable -> {
                            Log.w(TAG, "search error", throwable);
                            searchResultsLiveData.setValue(null);
                        },
                        () -> {
                            Log.d(TAG, "search complete");
                        },
                        disposables::add);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    private List<SearchResult> toSearchResults(SearchResponse searchResponse) {
        return SearchResult.fromSearchResponse(searchResponse);
    }

    public SearchType getSearchType() {
        return searchType;
    }

    enum SearchType {
        EVERYTHING,
        TITLE,
        AUTHOR
    }
}

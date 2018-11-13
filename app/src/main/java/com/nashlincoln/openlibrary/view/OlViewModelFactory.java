package com.nashlincoln.openlibrary.view;

import android.app.Application;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class OlViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Provider<SearchViewModel> searchModelProvider;
    private final Provider<BookListViewModel> bookListViewModelProvider;

    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link AndroidViewModel}
     */
    @Inject
    public OlViewModelFactory(@NonNull Application application,
                              Provider<SearchViewModel> searchViewModelProvider,
                              Provider<BookListViewModel> bookListViewModelProvider) {
        super(application);
        this.searchModelProvider = searchViewModelProvider;
        this.bookListViewModelProvider = bookListViewModelProvider;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == SearchViewModel.class) {
            return (T) searchModelProvider.get();
        } else if (modelClass == BookListViewModel.class) {
            return (T) bookListViewModelProvider.get();
        }
        return super.create(modelClass);
    }
}

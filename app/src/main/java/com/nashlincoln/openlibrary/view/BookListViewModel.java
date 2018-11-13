package com.nashlincoln.openlibrary.view;

import com.nashlincoln.openlibrary.data.Book;
import com.nashlincoln.openlibrary.data.OpenLibraryLogic;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BookListViewModel extends ViewModel {
    private final OpenLibraryLogic logic;
    private MutableLiveData<List<Book>> booksLiveData;
    private Disposable disposable;

    @Inject
    public BookListViewModel(OpenLibraryLogic logic) {
        this.logic = logic;
        booksLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Book>> getBooks() {
        return booksLiveData;
    }

    public void init() {
        disposable = logic.getBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(booksLiveData::setValue,
                        throwable -> {
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}

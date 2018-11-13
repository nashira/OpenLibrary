package com.nashlincoln.openlibrary.data;

import android.support.v4.media.MediaBrowserCompat;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Dao
public abstract class OpenLibraryLogic {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(Book book);

    @Update
    public abstract int update(Book book);

    public Completable addBook(Book book) {
        return Completable.fromAction(() -> insert(book));
    }

    public Completable completeBook(Book book) {
        return Completable.fromAction(() -> {
            book.setCompletedAt(new Date());
            update(book);
        });

    }

    @Query("SELECT * FROM Book")
    public abstract Flowable<List<Book>> getBooks();
}

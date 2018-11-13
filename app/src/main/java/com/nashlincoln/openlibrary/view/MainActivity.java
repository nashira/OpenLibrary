package com.nashlincoln.openlibrary.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nashlincoln.openlibrary.R;
import com.nashlincoln.openlibrary.data.Book;
import com.nashlincoln.openlibrary.data.OpenLibraryLogic;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    OlViewModelFactory viewModelFactory;

    @Inject
    Picasso picasso;

    @Inject
    OpenLibraryLogic logic;

    private BookListViewModel viewModel;
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(BookListViewModel.class);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter();
        recyclerView.setAdapter(bookAdapter);
        viewModel.getBooks().observe(this, bookAdapter::setBookList);
        viewModel.init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {
        private List<Book> books;

        @NonNull
        @Override
        public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BookViewHolder(
                    getLayoutInflater().inflate(
                            R.layout.view_book,
                            parent,
                            false));
        }

        @Override
        public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
            holder.bindView(books.get(position));
        }

        @Override
        public int getItemCount() {
            return books == null ? 0 : books.size();
        }

        public void setBookList(List<Book> bookList) {
            books = bookList;
            Collections.sort(books, (a, b) -> {
                // multi-faceted sort
                // first by completed ASC
                int aa = a.isComplete() ? 1 : 0;
                int bb = b.isComplete() ? 1 : 0;
                int c = aa - bb;
                // then by [added|completed] date DESC
                if (c == 0) {
                    if (a.isComplete()) {
                        return b.getCompletedAt().compareTo(a.getCompletedAt());
                    } else {
                        return b.getAddedAt().compareTo(a.getAddedAt());
                    }
                }
                return c;
            });
            notifyDataSetChanged();
        }
    }

    private class BookViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = BookViewHolder.class.getSimpleName();
        private final String COVER_URL = "http://covers.openlibrary.org/b/id/%s-M.jpg";
        private final ImageView imageCover;
        private final ImageView imageComplete;
        private final TextView textTitle;
        private final TextView textAuthor;
        private final TextView textYear;
        private final TextView textPublisher;
        private final View itemView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageCover = itemView.findViewById(R.id.image);
            imageComplete = itemView.findViewById(R.id.fulltext);
            textTitle = itemView.findViewById(R.id.title);
            textAuthor = itemView.findViewById(R.id.author);
            textYear = itemView.findViewById(R.id.year);
            textPublisher = itemView.findViewById(R.id.publisher);
        }

        public void bindView(Book book) {
            SearchResult searchResult = book.getSearchResult();
            String format = String.format(Locale.US, COVER_URL, searchResult.getCoverId());
            Log.d(TAG, format);
            picasso.load(format)
                    .into(imageCover);

            imageComplete.setVisibility(book.isComplete() ? View.VISIBLE : View.GONE);
            textTitle.setText(searchResult.getTitle());
            textAuthor.setText(searchResult.getAuthorName());
            textPublisher.setText(searchResult.getPublisher());
            if (book.isComplete()) {
                textYear.setText(
                        String.format(Locale.US, "Completed - %s", book.getCompletedAt().toString()));
            } else {
                textYear.setText(
                        String.format(Locale.US, "Added - %s", book.getAddedAt().toString()));
            }
            itemView.setOnClickListener(v -> {
                logic.completeBook(book)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            });
        }
    }
}

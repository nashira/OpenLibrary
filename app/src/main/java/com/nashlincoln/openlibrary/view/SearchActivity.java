package com.nashlincoln.openlibrary.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nashlincoln.openlibrary.R;
import com.nashlincoln.openlibrary.data.Book;
import com.nashlincoln.openlibrary.data.OpenLibraryLogic;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    SearchViewModel searchViewModel;
    private SearchAdapter searchAdapter;

    @Inject
    OlViewModelFactory viewModelFactory;

    @Inject
    Picasso picasso;

    @Inject
    OpenLibraryLogic logic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SearchViewModel.class);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new SearchAdapter();
        recyclerView.setAdapter(searchAdapter);
        searchViewModel.getSearchResults()
                .observe(this, this::setSearchResults);
    }

    private void setSearchResults(List<SearchResult> searchResults) {
        hideKeyboard();
        searchAdapter.setSearchResults(searchResults);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private class SearchAdapter extends RecyclerView.Adapter {

        public static final int VIEW_TYPE_SEARCH_INPUT = 0;
        public static final int VIEW_TYPE_SEARCH_RESULT = 1;
        private List<SearchResult> searchResults;

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_SEARCH_INPUT: {
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.view_search_input, parent, false);
                    return new SearchInputViewHolder(v);
                }

                case VIEW_TYPE_SEARCH_RESULT:
                default: {
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.view_search_result, parent, false);
                    return new SearchResultViewHolder(v);
                }
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof SearchInputViewHolder) {
                ((SearchInputViewHolder)holder).bindView();
            } else if (holder instanceof SearchResultViewHolder) {
                ((SearchResultViewHolder) holder).bindView(searchResults.get(position - 1));
            }
        }

        @Override
        public int getItemCount() {
            return 1 + (searchResults == null ? 0 : searchResults.size());
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return VIEW_TYPE_SEARCH_INPUT;
            }
            return VIEW_TYPE_SEARCH_RESULT;
        }

        public void setSearchResults(List<SearchResult> searchResults) {
            this.searchResults = searchResults;
            notifyDataSetChanged();
        }
    }

    private class SearchInputViewHolder extends RecyclerView.ViewHolder {
        private final RadioButton radioEverything;
        private final RadioButton radioTitle;
        private final RadioButton radioAuthor;
        private final EditText editText;

        public SearchInputViewHolder(@NonNull View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.edit_text);
            radioEverything = itemView.findViewById(R.id.radio_everything);
            radioTitle = itemView.findViewById(R.id.radio_title);
            radioAuthor = itemView.findViewById(R.id.radio_author);

            radioEverything.setOnClickListener(v -> {
                searchViewModel.setSearchType(SearchViewModel.SearchType.EVERYTHING);
            });

            radioTitle.setOnClickListener(v -> {
                searchViewModel.setSearchType(SearchViewModel.SearchType.TITLE);
            });

            radioAuthor.setOnClickListener(v -> {
                searchViewModel.setSearchType(SearchViewModel.SearchType.AUTHOR);
            });

            editText.setOnEditorActionListener((v, actionId, event) -> {
                if (event != null) {
                    searchViewModel.search(editText.getText().toString().trim());
                    return true;
                }
                return false;
            });
        }


        public void bindView() {
            SearchViewModel.SearchType searchType = searchViewModel.getSearchType();
            switch (searchType) {

                case EVERYTHING:
                    radioEverything.setChecked(true);
                    break;
                case TITLE:
                    radioTitle.setChecked(true);
                    break;
                case AUTHOR:
                    radioAuthor.setChecked(true);
                    break;
            }
        }
    }

    private class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private final String COVER_URL = "http://covers.openlibrary.org/b/id/%s-M.jpg";
        private final String TAG = SearchResultViewHolder.class.getSimpleName();
        private final ImageView imageCover;
        private final ImageView imageFullText;
        private final TextView textTitle;
        private final TextView textAuthor;
        private final TextView textYear;
        private final TextView textPublisher;
        private final View itemView;

        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageCover = itemView.findViewById(R.id.image);
            imageFullText = itemView.findViewById(R.id.fulltext);
            textTitle = itemView.findViewById(R.id.title);
            textAuthor = itemView.findViewById(R.id.author);
            textYear = itemView.findViewById(R.id.year);
            textPublisher = itemView.findViewById(R.id.publisher);
        }

        public void bindView(SearchResult searchResult) {
            String format = String.format(Locale.US, COVER_URL, searchResult.getCoverId());
            Log.d(TAG, format);
            picasso.load(format)
                    .into(imageCover);

            imageFullText.setVisibility(searchResult.hasFulltext() ? View.VISIBLE : View.GONE);
            textTitle.setText(searchResult.getTitle());
            textAuthor.setText(searchResult.getAuthorName());
            textPublisher.setText(searchResult.getPublisher());
            textYear.setText(String.valueOf(searchResult.getFirstPublishYear()));
            itemView.setOnClickListener(v -> {

                logic.addBook(Book.fromSearchResult(searchResult))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(SearchActivity.this::finish);
            });
        }
    }
}

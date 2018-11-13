package com.nashlincoln.openlibrary.dagger;

import android.app.Application;

import com.nashlincoln.openlibrary.view.MainActivity;
import com.nashlincoln.openlibrary.data.OpenLibraryDatabase;
import com.nashlincoln.openlibrary.data.OpenLibraryLogic;
import com.nashlincoln.openlibrary.net.OpenLibraryClient;
import com.nashlincoln.openlibrary.view.SearchActivity;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class AppModule {

    public static final String BASE_URL = "https://openlibrary.org";

    @ActivityScope
    @ContributesAndroidInjector(modules = {})
    abstract MainActivity contributeMainActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {})
    abstract SearchActivity contributeSearchActivity();

    @Provides
    @Singleton
    static OpenLibraryClient provideOpenLibraryClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(OpenLibraryClient.class);
    }

    @Provides
    @Singleton
    static Picasso providePicasso(Application application) {
        OkHttp3Downloader downloader = new OkHttp3Downloader(application);
        return new Picasso.Builder(application).downloader(downloader).build();
    }

    @Provides
    @Singleton
    static OpenLibraryDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, OpenLibraryDatabase.class, "db")
                .build();
    }

    @Provides
    @Singleton
    static OpenLibraryLogic provideOpenLibraryLogic(OpenLibraryDatabase openLibraryDatabase) {
        return openLibraryDatabase.libraryLogic();
    }
}
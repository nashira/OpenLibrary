package com.nashlincoln.openlibrary.dagger;

import android.app.Application;

import com.nashlincoln.openlibrary.OpenLibraryApp;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class})
public interface AppComponent {

    void inject(OpenLibraryApp openLibraryApp);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
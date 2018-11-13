package com.nashlincoln.openlibrary.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
        version = OpenLibraryDatabase.VERSION,
        entities = {
                Book.class
        },
        exportSchema = false
)
@TypeConverters({OpenLibraryTypeConverters.class})
public abstract class OpenLibraryDatabase extends RoomDatabase {
    static final int VERSION = 1;

    public abstract OpenLibraryLogic libraryLogic();
}

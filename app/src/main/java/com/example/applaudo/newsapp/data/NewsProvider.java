package com.example.applaudo.newsapp.data;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import static com.example.applaudo.newsapp.data.NewsContract.*;

public class NewsProvider extends ContentProvider {

    private final String LOG_TAG = NewsProvider.class.getSimpleName();

    private static final int NEWS = 100;

    private static final int NEWSLATER = 200;

    private static final int CATEGORY = 300;

    private NewsDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        //News
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_NEWS,NEWS);

        //NewsLater
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_NEWSLATER,NEWSLATER);

        //Category
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_CATEGORY,CATEGORY);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new NewsDbHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case NEWS:
                getContext().getContentResolver().notifyChange(uri,null);
                return insertNews(uri,values);
        }

        throw new UnsupportedOperationException("Not yet implemented");
    }

    //Helper method to insert
    private Uri insertNews(Uri uri, ContentValues values){
        //Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        Long id = database.insert(NewsEntry.TABLE_NAME,null,values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for: " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri,id);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }
}

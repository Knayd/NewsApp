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

    private NewsDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        //News
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_NEWS, NEWS);

        //NewsLater
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_NEWSLATER, NEWSLATER);
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
                getContext().getContentResolver().notifyChange(uri, null);
                return insertNews(uri, values, NewsEntry.TABLE_NAME);

            case NEWSLATER:
                getContext().getContentResolver().notifyChange(uri, null);
                return insertNews(uri, values, NewsLaterEntry.TABLE_NAME);
        }

        throw new UnsupportedOperationException("Not yet implemented");
    }

    //Helper method to insert, this is used for both 'news' and 'newslater' table
    private Uri insertNews(Uri uri, ContentValues values, final String tableName) {
        //Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        Long id = database.insert(tableName, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for: " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor = null;
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case NEWS:
                cursor = database.query(NewsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            case NEWSLATER:
                cursor = database.query(NewsLaterEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
        }
        return cursor;

    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case NEWSLATER:
                SQLiteDatabase database = mDbHelper.getWritableDatabase();
                return database.delete(NewsLaterEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

    }

    @Override
    public String getType(Uri uri) {
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

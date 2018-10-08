package com.example.applaudo.newsapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.applaudo.newsapp.data.NewsContract.*;


public class NewsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "news.db";
    private static final int VERSION = 1;

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Runs when the database is first acceded

        String SQL_CREATE_NEWS_TABLE ="CREATE TABLE " + NewsEntry.TABLE_NAME+ "("
                +NewsEntry.COLUMN_NEWS_ID + " TEXT PRIMARY KEY NOT NULL,"
                +NewsEntry.COLUMN_NEWS_HEADLINE + " TEXT,"
                +NewsEntry.COLUMN_NEWS_BODYTEXT + " TEXT,"
                +NewsEntry.COLUMN_NEWS_THUMBNAIL + " TEXT,"
                +NewsEntry.COLUMN_NEWS_SECTION + " TEXT,"
                +NewsEntry.COLUMN_NEWS_WEBSITE + " TEXT,"
                +NewsEntry.COLUMN_NEWS_CATEGORY + " INTEGER)";

        String SQL_CREATE_NEWSLATER_TABLE ="CREATE TABLE " + NewsLaterEntry.TABLE_NAME+ "("
                +NewsLaterEntry.COLUMN_NEWSLATER_ID + " TEXT PRIMARY KEY NOT NULL,"
                +NewsLaterEntry.COLUMN_NEWSLATER_HEADLINE + " TEXT,"
                +NewsLaterEntry.COLUMN_NEWSLATER_BODYTEXT + " TEXT,"
                +NewsLaterEntry.COLUMN_NEWSLATER_THUMBNAIL + " TEXT,"
                +NewsLaterEntry.COLUMN_NEWSLATER_SECTION + " TEXT,"
                +NewsLaterEntry.COLUMN_NEWSLATER_WEBSITE + " TEXT,"
                +NewsLaterEntry.COLUMN_NEWSLATER_CATEGORY + " INTEGER)";

        String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + CategoryEntry.TABLE_NAME+ "("
                +CategoryEntry.COLUMN_CATEGORY_ID+ " INTEGER PRIMARY KEY NOT NULL,"
                +CategoryEntry.COLUMN_CATEGORY_NAME+ " TEXT)";


        db.execSQL(SQL_CREATE_NEWS_TABLE);
        db.execSQL(SQL_CREATE_NEWSLATER_TABLE);
        db.execSQL(SQL_CREATE_CATEGORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

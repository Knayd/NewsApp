package com.example.applaudo.newsapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class NewsContract {


    //Uri
    public static final String CONTENT_AUTHORITY = "com.example.applaudo.newsapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    //Constants for every table
    public static final String PATH_NEWS = "news";
    public static final String PATH_NEWSLATER = "newslater";
    public static final String PATH_CATEGORY = "category";


    public static final class NewsEntry implements BaseColumns{
        //CREATE TABLE news (id TEXT PRIMARY KEY NOT NULL, headline TEXT, bodytext TEXT, section TEXT, thumbnail TEXT, website TEXT);

        //Content Uri for the news table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_NEWS);

        //Table news
        public static final String TABLE_NAME = "news";

        public static final String COLUMN_NEWS_ID = "id";
        public static final String COLUMN_NEWS_HEADLINE = "headline";
        public static final String COLUMN_NEWS_BODYTEXT = "bodytext";
        public static final String COLUMN_NEWS_SECTION = "section";
        public static final String COLUMN_NEWS_THUMBNAIL = "thumbnail";
        public static final String COLUMN_NEWS_WEBSITE = "website";
        public static final String COLUMN_NEWS_CATEGORY = "category";

    }

    public static final class NewsLaterEntry implements BaseColumns{

        //CREATE TABLE newslater (id TEXT PRIMARY KEY NOT NULL, headline TEXT, bodytext TEXT, section TEXT, thumbnail TEXT, website TEXT, category INTEGER);

        //Content Uri for the newslater table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_NEWSLATER);

        //Table newslater
        public static final String TABLE_NAME = "newslater";

        public static final String COLUMN_NEWSLATER_ID = "id";
        public static final String COLUMN_NEWSLATER_HEADLINE = "headline";
        public static final String COLUMN_NEWSLATER_BODYTEXT = "bodytext";
        public static final String COLUMN_NEWSLATER_SECTION = "section";
        public static final String COLUMN_NEWSLATER_THUMBNAIL = "thumbnail";
        public static final String COLUMN_NEWSLATER_WEBSITE = "website";
        public static final String COLUMN_NEWSLATER_CATEGORY = "category";

    }

    public static final class CategoryEntry implements BaseColumns{

        //CREATE TABLE category (id INTEGER  PRIMARY KEY AUTOINCREMENT, name TEXT);

        //Content Uri for the category table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_CATEGORY);

        //Table category
        public static final String TABLE_NAME = "category";

        public static final String COLUMN_CATEGORY_ID = "id";
        public static final String COLUMN_CATEGORY_NAME = "name";

    }


}

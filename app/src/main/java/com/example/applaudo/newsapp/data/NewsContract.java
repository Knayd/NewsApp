package com.example.applaudo.newsapp.data;

import android.provider.BaseColumns;

public final class NewsContract {


    public static final class NewsEntry implements BaseColumns{
        //CREATE TABLE news (id TEXT PRIMARY KEY NOT NULL, headline TEXT, bodytext TEXT, section TEXT, thumbnail TEXT, website TEXT);

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

        //Table category
        public static final String TABLE_NAME = "category";

        public static final String COLUMN_CATEGORY_ID = "id";
        public static final String COLUMN_CATEGORY_NAME = "name";

    }


}

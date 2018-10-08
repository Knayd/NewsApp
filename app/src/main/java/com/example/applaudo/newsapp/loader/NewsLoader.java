package com.example.applaudo.newsapp.loader;

import android.content.Context;
import android.content.CursorLoader;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.CursorAdapter;
import android.widget.Toast;

import com.example.applaudo.newsapp.R;
import com.example.applaudo.newsapp.data.NewsContract;
import com.example.applaudo.newsapp.models.News;
import com.example.applaudo.newsapp.query.QueryUtils;

import java.util.ArrayList;

import static com.example.applaudo.newsapp.data.NewsContract.*;

public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {

    //Query string
    private String mUrl;
    private int mCategory;

    public NewsLoader(@NonNull Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    //Custom constructor to retrieve the category type from the Fragment
    public NewsLoader(Context context, String mUrl, int mCategory){
        super(context);
        this.mUrl = mUrl;
        this.mCategory = mCategory;
    }

    //Required to trigger the loadInBackground method
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<News> loadInBackground() {
        //This is where the list is gonna be filled
        ArrayList<News> newsList;

        if (mUrl == null) {
            //If the url is null, I know it is because there is no connection
            //So, here's where the data from the database will be fetched

            String selection = "category=?";
            String[] args = {String.valueOf(mCategory)};

            Cursor cursor = getContext().getContentResolver().query(NewsEntry.CONTENT_URI,null,selection,args,null);

            return transformCursorToList(cursor);

        } else {
            newsList = QueryUtils.fetchNewsData(mUrl);
        }

        return newsList;
    }

    //Helper method to transform insert the cursor data into an ArrayList
    private ArrayList<News> transformCursorToList(Cursor cursor){
        ArrayList<News> newsList = new ArrayList<>();

        int idColumnIndex = cursor.getColumnIndex(NewsEntry.COLUMN_NEWS_ID);
        int headlineColumnIndex = cursor.getColumnIndex(NewsEntry.COLUMN_NEWS_HEADLINE);
        int bodyTextColumnIndex = cursor.getColumnIndex(NewsEntry.COLUMN_NEWS_BODYTEXT);
        int sectionColumnIndex = cursor.getColumnIndex(NewsEntry.COLUMN_NEWS_SECTION);
        int thumbnailColumnIndex = cursor.getColumnIndex(NewsEntry.COLUMN_NEWS_THUMBNAIL);
        int webSiteColumnIndex = cursor.getColumnIndex(NewsEntry.COLUMN_NEWS_WEBSITE);

        while(cursor.moveToNext()){

            String id = cursor.getString(idColumnIndex);
            String bodyText = cursor.getString(bodyTextColumnIndex);
            String headline = cursor.getString(headlineColumnIndex);
            String section = cursor.getString(sectionColumnIndex);
            String thumbnail = cursor.getString(thumbnailColumnIndex);
            String webSite = cursor.getString(webSiteColumnIndex);

            newsList.add(new News(headline,bodyText,section,thumbnail,webSite,id));
        }

        return newsList;

    }
}

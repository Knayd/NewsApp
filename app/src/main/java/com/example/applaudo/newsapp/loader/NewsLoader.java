package com.example.applaudo.newsapp.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.applaudo.newsapp.models.News;
import com.example.applaudo.newsapp.query.QueryUtils;

import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {
    //Query string
    private String mUrl;

    public NewsLoader(@NonNull Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    //Required to trigger the loadInBackground method
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<News> loadInBackground() {
        //This is where the list is gonna be loaded
        if (mUrl == null) {
            return null;
        }
        ArrayList<News> newsList = QueryUtils.fetchNewsData(mUrl);

        return newsList;
    }
}

package com.example.applaudo.newsapp.loader;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.Toast;

import com.example.applaudo.newsapp.R;
import com.example.applaudo.newsapp.models.News;
import com.example.applaudo.newsapp.query.QueryUtils;

import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {

    //Query string
    private String mUrl;
    private String mNoConnectionMsg;

    public NewsLoader(@NonNull Context context, String mUrl) {
        super(context);
        //I use the context of the activity to get the resources.
        this.mNoConnectionMsg = context.getResources().getString(R.string.str_no_results);
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

        //Since this entire method will only be executed when a request is made,
        //and since there's just two possible outcomes (empty/non-empty JSON)
        //I can add in here an object which will hold the "no results" message
        //Which will be sent to the adapter
        if (newsList.size() == 0) {
            newsList.add(new News("","", mNoConnectionMsg,"","",""));
        }

        return newsList;
    }
}

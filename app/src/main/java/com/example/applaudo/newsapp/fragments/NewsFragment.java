package com.example.applaudo.newsapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.applaudo.newsapp.R;
import com.example.applaudo.newsapp.adapter.NewsAdapter;
import com.example.applaudo.newsapp.loader.NewsLoader;
import com.example.applaudo.newsapp.main.NewsDetailsActivity;
import com.example.applaudo.newsapp.models.News;
import com.example.applaudo.newsapp.query.Query;

import java.util.ArrayList;

public class NewsFragment extends Fragment implements NewsAdapter.OnNewsClicked, LoaderManager.LoaderCallbacks<ArrayList<News>> {

    NewsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_news,container, false);

        RecyclerView rv = v.findViewById(R.id.fragment_recycler);


        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        //The contract for the interface
        mAdapter = new NewsAdapter(this);
        rv.setAdapter(mAdapter);
        rv.setLayoutManager(llm);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(1,null,this);

        return v;

    }

    //Implementation of the interface
    @Override
    public void onNewsClicked(String headline, String bodyText, String section, String thumbnail, String website) {
        //Here I send the data to the DetailsActivity
        Intent intent = NewsDetailsActivity.getInstance(getContext(), headline, bodyText, section, thumbnail, website);
        startActivity(intent);
    }


    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
        //Create a new loader from the given URL

        //Retrieve the tab position from the PagerAdapter
        Bundle bundle = getArguments();
        if (bundle != null) {
            int tabPosition = bundle.getInt("tabPosition");
            //Return different loaders with different URL's based on tab position
            switch (tabPosition) {
                case 0:
                    return new NewsLoader(getContext(), Query.QUERY_SPORTS);
                case 1:
                    return new NewsLoader(getContext(), Query.QUERY_POLITICS);
                case 2:
                    return new NewsLoader(getContext(), Query.QUERY_SOCIAL);
                default:
                    return new NewsLoader(getContext(), Query.QUERY_MUSIC);
            }
        } else {
            //It shouldn't ever get in here, but it was required because of the "if" statement
            return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> data) {
        //Here sets the data for the recycler
        mAdapter.setmNewsList(data);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        Toast.makeText(getContext(), "Reset", Toast.LENGTH_SHORT).show();
    }
}

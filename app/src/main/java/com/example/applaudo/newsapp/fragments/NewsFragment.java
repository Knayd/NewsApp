package com.example.applaudo.newsapp.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.applaudo.newsapp.R;
import com.example.applaudo.newsapp.adapter.NewsAdapter;
import com.example.applaudo.newsapp.data.NewsContract;
import com.example.applaudo.newsapp.data.NewsDbHelper;
import com.example.applaudo.newsapp.loader.NewsLoader;
import com.example.applaudo.newsapp.main.MainActivity;
import com.example.applaudo.newsapp.main.NewsDetailsActivity;
import com.example.applaudo.newsapp.models.News;
import com.example.applaudo.newsapp.query.Query;

import java.util.ArrayList;

public class NewsFragment extends Fragment implements NewsAdapter.OnNewsClicked, LoaderManager.LoaderCallbacks<ArrayList<News>> {

    public static String LOADER_SEARCH_ARGS = "LOADER_SEARCH_ARGS";

    private static final int LOADER_MAIN_ID =1;
    private static final int LOADER_CURSOR_ID = 2;

    NewsAdapter mAdapter;
    Handler handler = new Handler();


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_news);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(final String newText) {
                //To avoid stacking the runnables
                handler.removeCallbacksAndMessages(null);
                //To delay the search
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doSearch(newText);
                    }
                },1000);
                return false;
            }
        });
    }

    //Helper method to do the searching
    private void doSearch(String searchTerm){
        LoaderManager loaderManager = getLoaderManager();

        //This to avoid doing the search on empty text
        if(!TextUtils.isEmpty(searchTerm)){
            //Stores the text and sends it to the onCreateLoader method
            Bundle loaderData = new Bundle();
            loaderData.putString(LOADER_SEARCH_ARGS,searchTerm);
            //Restarts the current loader and sends the search params
            loaderManager.restartLoader(LOADER_MAIN_ID,loaderData,NewsFragment.this);
        }
        else {
            //To reload the data when the text is empty
            loaderManager.restartLoader(LOADER_MAIN_ID,null,NewsFragment.this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_news,container, false);

        //Checks for internet connection
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() )
        {
            RecyclerView rv = v.findViewById(R.id.fragment_recycler);

            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            //Enables the options search bar for the Fragment
            setHasOptionsMenu(true);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_MAIN_ID,null,this);
            Toast.makeText(getContext(), "Connected", Toast.LENGTH_SHORT).show();
            //The contract for the interface
            mAdapter = new NewsAdapter(this);
            rv.setAdapter(mAdapter);
            rv.setLayoutManager(llm);
        } else {
            TextView noConnection = v.findViewById(R.id.fragment_no_connection_msj);
            noConnection.setVisibility(View.VISIBLE);
        }


        NewsDbHelper helper = new NewsDbHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NewsContract.NewsEntry.COLUMN_NEWS_ID,"ID");
        values.put(NewsContract.NewsEntry.COLUMN_NEWS_HEADLINE,"BodyText");
        values.put(NewsContract.NewsEntry.COLUMN_NEWS_BODYTEXT,"BodyText");
        values.put(NewsContract.NewsEntry.COLUMN_NEWS_SECTION,"BodyText");
        values.put(NewsContract.NewsEntry.COLUMN_NEWS_THUMBNAIL,"BodyText");
        values.put(NewsContract.NewsEntry.COLUMN_NEWS_WEBSITE,"BodyText");

        long id = db.insert(NewsContract.NewsEntry.TABLE_NAME,null,values);

        Toast.makeText(getContext(),String.valueOf(id), Toast.LENGTH_SHORT).show();

        return v;

    }

    //Implementation of the interface
    @Override
    public void onNewsClicked(String headline, String bodyText, String section, String thumbnail, String website, String id) {
        //Here I send the data to the DetailsActivity
        Intent intent = NewsDetailsActivity.getInstance(getContext(), headline, bodyText, section, thumbnail, website, id);
        startActivity(intent);
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
        //Create a new loader from the given URL's

        //Retrieve the tab position from the PagerAdapter
        if(id==LOADER_MAIN_ID) {
            Bundle bundle = getArguments();
            //Checks if nothing was send from the menu
            if (args == null) {
                int tabPosition = bundle.getInt(MainActivity.TAB);
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
                //Retrieving the search term sent from the SearchView
                String searchTerm = args.getString(LOADER_SEARCH_ARGS);
                //Building the URL to do the search
                String QUERY_SEARCH = Query.BASE_URL + searchTerm + Query.API_KEY;

                return new NewsLoader(getContext(), QUERY_SEARCH);
            }
        } else {

            return null; //TODO: Block for the CursorLoader
        }


    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> data){
        //Here sets the data for the recycler
        mAdapter.setmNewsList(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        Toast.makeText(getContext(), "Reset", Toast.LENGTH_SHORT).show();
    }



}

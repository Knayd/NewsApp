package com.example.applaudo.newsapp.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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

import static com.example.applaudo.newsapp.data.NewsContract.*;

public class NewsFragment extends Fragment implements NewsAdapter.OnNewsClicked, LoaderManager.LoaderCallbacks<ArrayList<News>> {

    public static String LOADER_SEARCH_ARGS = "LOADER_SEARCH_ARGS";

    private static final int LOADER_MAIN_ID =1;
    private static final int LOADER_CURSOR_ID = 2;

    private NewsAdapter mAdapter;
    private Handler mHandler = new Handler();


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
                mHandler.removeCallbacksAndMessages(null);
                //To delay the search
                mHandler.postDelayed(new Runnable() {
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

        LoaderManager loaderManager = getLoaderManager();

        RecyclerView rv = v.findViewById(R.id.fragment_recycler);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        //Checks for internet connection
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() )
        {
            //Enables the options search bar for the Fragment
            setHasOptionsMenu(true);

            loaderManager.initLoader(LOADER_MAIN_ID,null,this);

        } else {
            loaderManager.initLoader(LOADER_CURSOR_ID,null,this);
        }

        //The contract for the interface
        mAdapter = new NewsAdapter(this);
        rv.setAdapter(mAdapter);
        rv.setLayoutManager(llm);

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
        Bundle bundle = getArguments();
        int tabPosition = bundle.getInt(MainActivity.TAB);
        if(id==LOADER_MAIN_ID) {

            //Checks if nothing was send from the menu
            if (args == null) {
                //Return different loaders with different URL's based on tab position
                switch (tabPosition) {
                    case 0:
                        return new NewsLoader(getContext(), Query.QUERY_SPORTS);
                    case 1:
                        return new NewsLoader(getContext(), Query.QUERY_POLITICS);
                    case 2:
                        return new NewsLoader(getContext(), Query.QUERY_SOCIAL);
                    case 3:
                        return new NewsLoader(getContext(), Query.QUERY_MUSIC);
                        default:
                            //This is the case for the Read later tab
                        return new NewsLoader(getContext(),null,tabPosition);

                }
            } else {
                //Retrieving the search term sent from the SearchView
                String searchTerm = args.getString(LOADER_SEARCH_ARGS);
                //Building the URL to do the search
                String QUERY_SEARCH = Query.BASE_URL + searchTerm + Query.API_KEY;

                return new NewsLoader(getContext(), QUERY_SEARCH);
            }
        } else {
            //To load the data from the database
            return new NewsLoader(getContext(),null,tabPosition);
        }


    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> data){
        //Here sets the data for the recycler
        mAdapter.setmNewsList(data);
        //Inserting the data in the database
        if (data.size()!=0) {
            insertNewsList(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
    }



    //Helper method to insert the data in the database
    private void insertNewsList(ArrayList<News> data){

        Bundle bundle = getArguments();
        int tabPosition = bundle.getInt(MainActivity.TAB);

        for (int i = 0; i < data.size() ; i++) {
            ContentValues values = new ContentValues();
            //Checks if the field doesn't exist before doing the insert
            if(!fieldExists(data.get(i).getId())){
                values.put(NewsEntry.COLUMN_NEWS_ID,data.get(i).getId());
                values.put(NewsEntry.COLUMN_NEWS_HEADLINE,data.get(i).getHeadline());
                values.put(NewsEntry.COLUMN_NEWS_BODYTEXT,data.get(i).getBodyText());
                values.put(NewsEntry.COLUMN_NEWS_SECTION,data.get(i).getSection());
                values.put(NewsEntry.COLUMN_NEWS_THUMBNAIL,data.get(i).getThumbnail());
                values.put(NewsEntry.COLUMN_NEWS_WEBSITE,data.get(i).getWebSite());
                //Category is based on the tab position
                values.put(NewsEntry.COLUMN_NEWS_CATEGORY,tabPosition);

                Uri newUri = getContext().getContentResolver().insert(NewsEntry.CONTENT_URI,values);

            }

        }

    }

    //Helper method to check if the field already exists in the database
    private boolean fieldExists(String id){

        NewsDbHelper mDbHelper = new NewsDbHelper(getContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String projection[] = {
                NewsEntry.COLUMN_NEWS_ID
        };

        String selection = "id=?";
        String[] args = {id};

        Cursor news =  db.query(NewsEntry.TABLE_NAME,projection,selection,args,null,null,null);

        //This is so I can close the cursor
        int checkCursor = news.getCount();
        news.close();

        return checkCursor != 0;
    }

}

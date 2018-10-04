package com.example.applaudo.newsapp.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.applaudo.newsapp.R;
import com.example.applaudo.newsapp.fragments.NewsDetailsFragment;

public class NewsDetailsActivity extends AppCompatActivity {

    private final static String EXT_DETAILS_HEADLINE = "EXT_DETAILS_HEADLINE";
    private final static String EXT_DETAILS_BODYTEXT = "EXT_DETAILS_BODYTEXT";
    private final static String EXT_DETAILS_SECTION = "EXT_DETAILS_SECTION";
    private final static String EXT_DETAILS_THUMBNAIL = "EXT_DETAILS_THUMBNAIL";
    private final static String EXT_DETAILS_WEBSITE = "EXT_DETAILS_WEBSITE";
    private final static String EXT_DETAILS_ID = "EXT_DETAILS_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        //Bundle to pack and send the data to the fragment
        Bundle itemData = getIntent().getExtras();

        //Creating the new fragment when the activity is created.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Setting the item data to the fragment
        NewsDetailsFragment fragment = new NewsDetailsFragment();
        fragment.setArguments(itemData);
        fragmentTransaction.add(R.id.details_layout, fragment);
        fragmentTransaction.commit();
    }


    public static Intent getInstance(Context context, String headline, String bodyText, String section, String thumbnail, String website, String id) {

        Intent intent = new Intent(context,NewsDetailsActivity.class);
        intent.putExtra(EXT_DETAILS_HEADLINE, headline);
        intent.putExtra(EXT_DETAILS_BODYTEXT, bodyText);
        intent.putExtra(EXT_DETAILS_SECTION, section);
        intent.putExtra(EXT_DETAILS_THUMBNAIL, thumbnail);
        intent.putExtra(EXT_DETAILS_WEBSITE, website);
        intent.putExtra(EXT_DETAILS_ID, id);
        return intent;
    }
}

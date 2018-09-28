package com.example.applaudo.newsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.applaudo.newsapp.R;


public class NewsDetailsFragment extends Fragment {

    private final static String EXT_DETAILS_HEADLINE = "EXT_DETAILS_HEADLINE";
    private final static String EXT_DETAILS_BODYTEXT = "EXT_DETAILS_BODYTEXT";
    private final static String EXT_DETAILS_SECTION = "EXT_DETAILS_SECTION";
    private final static String EXT_DETAILS_THUMBNAIL = "EXT_DETAILS_THUMBNAIL";
    private final static String EXT_DETAILS_WEBSITE = "EXT_DETAILS_WEBSITE";

    private TextView mHeadLine, mBodyText, mSection, mThumbnail, mWebsite;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_details,container,false);

        //Unpacking the data sent from the activity
        Bundle bundle = getArguments();
        if (bundle != null) {
            //Setting the values to every view
            mHeadLine = v.findViewById(R.id.details_fragment_headline);
            mBodyText = v.findViewById(R.id.details_fragment_bodyText);
            mSection = v.findViewById(R.id.details_fragment_section);
            mThumbnail = v.findViewById(R.id.details_fragment_thumbnail);
            mWebsite = v.findViewById(R.id.details_fragment_website);

            mHeadLine.setText(bundle.getString(EXT_DETAILS_HEADLINE));
            mBodyText.setText(bundle.getString(EXT_DETAILS_BODYTEXT));
            mSection.setText(bundle.getString(EXT_DETAILS_SECTION));
            mThumbnail.setText(bundle.getString(EXT_DETAILS_THUMBNAIL));
            mWebsite.setText(bundle.getString(EXT_DETAILS_WEBSITE));
        }

        return v;
    }
}

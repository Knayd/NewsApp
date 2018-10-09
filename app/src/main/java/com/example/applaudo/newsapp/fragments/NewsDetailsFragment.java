package com.example.applaudo.newsapp.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.applaudo.newsapp.R;
import com.example.applaudo.newsapp.data.NewsDbHelper;
import com.example.applaudo.newsapp.models.News;

import static com.example.applaudo.newsapp.data.NewsContract.*;


public class NewsDetailsFragment extends Fragment implements View.OnClickListener {

    private final static String EXT_DETAILS_HEADLINE = "EXT_DETAILS_HEADLINE";
    private final static String EXT_DETAILS_BODYTEXT = "EXT_DETAILS_BODYTEXT";
    private final static String EXT_DETAILS_SECTION = "EXT_DETAILS_SECTION";
    private final static String EXT_DETAILS_THUMBNAIL = "EXT_DETAILS_THUMBNAIL";
    private final static String EXT_DETAILS_WEBSITE = "EXT_DETAILS_WEBSITE";
    private final static String EXT_DETAILS_ID = "EXT_DETAILS_ID";


    private TextView mHeadLine, mBodyText, mSection, mThumbnail, mWebsite;
    private Button mAddReadLater, mRemoveReadLater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_details, container, false);

        //Unpacking the data sent from the activity
        Bundle bundle = getArguments();
        if (bundle != null) {
            //Setting the values to every view
            mHeadLine = v.findViewById(R.id.details_fragment_headline);
            mBodyText = v.findViewById(R.id.details_fragment_bodyText);
            mSection = v.findViewById(R.id.details_fragment_section);
            mThumbnail = v.findViewById(R.id.details_fragment_thumbnail);
            mWebsite = v.findViewById(R.id.details_fragment_website);

            mAddReadLater = v.findViewById(R.id.details_fragment_btn_add_read_later);
            mRemoveReadLater = v.findViewById(R.id.details_fragment_btn_remove_read_later);

            mHeadLine.setText(bundle.getString(EXT_DETAILS_HEADLINE));
            mBodyText.setText(bundle.getString(EXT_DETAILS_BODYTEXT));
            mSection.setText(bundle.getString(EXT_DETAILS_SECTION));
            mThumbnail.setText(bundle.getString(EXT_DETAILS_THUMBNAIL));
            mWebsite.setText(bundle.getString(EXT_DETAILS_WEBSITE));

            //To open the new's page when the URL is clicked
            mWebsite.setOnClickListener(this);

            //To add/remove from read later
            mAddReadLater.setOnClickListener(this);
            mRemoveReadLater.setOnClickListener(this);

            mAddReadLater.setText(getResources().getString(R.string.str_add_read_later));
            mRemoveReadLater.setText(getResources().getString(R.string.str_remove_read_later));

            //To hide the buttons if the news exists in in "Read me later"
            if (!fieldExistsInNewsLaterTable(bundle.getString(EXT_DETAILS_ID))) {
                mAddReadLater.setVisibility(View.VISIBLE);
            } else {
                mRemoveReadLater.setVisibility(View.VISIBLE);
            }

        }

        return v;
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = getArguments();

        if (bundle != null) {
            switch (view.getId()) {
                case R.id.details_fragment_website:
                    String url = mWebsite.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    break;
                case R.id.details_fragment_btn_add_read_later:
                    mAddReadLater.setVisibility(View.GONE);
                    mRemoveReadLater.setVisibility(View.VISIBLE);
                    insertNewsLater(getNewsObject(bundle));
                    break;
                case R.id.details_fragment_btn_remove_read_later:
                    mAddReadLater.setVisibility(View.VISIBLE);
                    mRemoveReadLater.setVisibility(View.GONE);
                    deleteNewsLater(bundle.getString(EXT_DETAILS_ID));
                    break;
            }
        }
    }

    //Helper method to check if the field already exists in the newslater database
    private boolean fieldExistsInNewsLaterTable(String id) {

        NewsDbHelper mDbHelper = new NewsDbHelper(getContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String projection[] = {
                NewsLaterEntry.COLUMN_NEWSLATER_ID
        };

        String selection = "id=?";
        String[] args = {id};

        Cursor news = db.query(NewsLaterEntry.TABLE_NAME, projection, selection, args, null, null, null);

        //This is so I can close the cursor
        int checkCursor = news.getCount();
        news.close();

        return checkCursor != 0;
    }

    private void insertNewsLater(News news) {

        ContentValues values = new ContentValues();
        //Checks if the field doesn't exist before doing the insert
        if (!fieldExistsInNewsLaterTable(news.getId())) {
            values.put(NewsLaterEntry.COLUMN_NEWSLATER_ID, news.getId());
            values.put(NewsEntry.COLUMN_NEWS_HEADLINE, news.getHeadline());
            values.put(NewsLaterEntry.COLUMN_NEWSLATER_BODYTEXT, news.getBodyText());
            values.put(NewsLaterEntry.COLUMN_NEWSLATER_SECTION, news.getSection());
            values.put(NewsLaterEntry.COLUMN_NEWSLATER_THUMBNAIL, news.getThumbnail());
            values.put(NewsLaterEntry.COLUMN_NEWSLATER_WEBSITE, news.getWebSite());

            Uri newUri = getContext().getContentResolver().insert(NewsLaterEntry.CONTENT_URI, values);

        }

    }

    private void deleteNewsLater(String id) {
        if (fieldExistsInNewsLaterTable(id)) {

            String where = NewsLaterEntry.COLUMN_NEWSLATER_ID + "=?";
            String[] args = {id};

            int rowsDeleted = getContext().getContentResolver().delete(NewsLaterEntry.CONTENT_URI, where, args);

        }
    }

    private News getNewsObject(Bundle bundle) {
        News news = new News(
                bundle.getString(EXT_DETAILS_HEADLINE),
                bundle.getString(EXT_DETAILS_BODYTEXT),
                bundle.getString(EXT_DETAILS_SECTION),
                bundle.getString(EXT_DETAILS_THUMBNAIL),
                bundle.getString(EXT_DETAILS_WEBSITE),
                bundle.getString(EXT_DETAILS_ID)
        );

        return news;

    }
}

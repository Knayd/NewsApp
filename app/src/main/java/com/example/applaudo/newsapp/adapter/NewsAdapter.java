package com.example.applaudo.newsapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.applaudo.newsapp.R;
import com.example.applaudo.newsapp.models.News;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<News> mNewsList;
    private OnNewsClicked mCallback;

    //Interface to open the details
    public interface OnNewsClicked {
        void onNewsClicked(String headline, String bodyText, String section, String thumbnail, String website);
    }


    public NewsAdapter(ArrayList<News> mNewsList, OnNewsClicked mCallback) {
        this.mNewsList = mNewsList;
        this.mCallback = mCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_news_item_list, parent, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                NewsViewHolder nv = (NewsViewHolder) holder;
                nv.bindView(mNewsList,position);
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

     class NewsViewHolder extends RecyclerView.ViewHolder {
        private TextView mHeadLine, mSection;
        private ImageView mThumbnail;
        private View mLayout;

        NewsViewHolder(View itemView) {
            super(itemView);
            mHeadLine = itemView.findViewById(R.id.recycler_headline);
            mSection = itemView.findViewById(R.id.recycler_section);
            mThumbnail = itemView.findViewById(R.id.recycler_thumbnail);
            mLayout = itemView.findViewById(R.id.recycler_item_layout);
        }

        //Sets the data to the view
        void bindView(final ArrayList<News> list, final int position) {
            mHeadLine.setText(list.get(position).getmHeadline());
            mSection.setText(list.get(position).getmSection());
            mThumbnail.setImageResource(R.mipmap.ic_launcher_round); //TODO: just a placeholder

            //Listens when an item is clicked on the recycler and sends the data of the current item
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onNewsClicked(
                            list.get(position).getmHeadline(),
                            list.get(position).getmBodyText(),
                            list.get(position).getmSection(),
                            list.get(position).getmThumbnail(),
                            list.get(position).getmWebSite()
                    );
                }
            });

        }


    }

}

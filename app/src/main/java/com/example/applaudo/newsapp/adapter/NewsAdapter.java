package com.example.applaudo.newsapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.applaudo.newsapp.R;
import com.example.applaudo.newsapp.models.News;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_LOAD = -1;
    private static final int TYPE_ITEM = -2;

    private ArrayList<News> mNewsList = new ArrayList<>(); //This is so the size returns 0 at first load.
    private OnNewsClicked mCallback;

    //Setter for updating the data after the null list is already "loaded"
    public void setmNewsList(ArrayList<News> mNewsList) {
        this.mNewsList = mNewsList;
        notifyDataSetChanged();
    }

    //Interface to open the details
    public interface OnNewsClicked {
        void onNewsClicked(String headline, String bodyText, String section, String thumbnail, String website);
    }


    public NewsAdapter(OnNewsClicked mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        //Inflates views based on the type
        switch (viewType) {
            case TYPE_LOAD:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_progressbar, parent, false);
                return new LoadViewHolder(v);
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_news_item_list, parent, false);
                return new NewsViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof NewsViewHolder) {
            //Sets the data if the holder is an item
            NewsViewHolder nv = (NewsViewHolder) holder;
            nv.bindView(mNewsList,position);
        } else {
            //Otherwise, just create the progress bar
            LoadViewHolder lv = (LoadViewHolder) holder;
        }

    }

    @Override
    public int getItemCount() {

            if (mNewsList.size()==0){
                //If the list is empty, I will force it to load 1 element which will be only the progress bar
                return 1;
            }
            else {
                return mNewsList.size();
            }
    }

    @Override
    public int getItemViewType(int position) {
        if (mNewsList.size()==0) {
            //To load the progress bar
            return TYPE_LOAD;
        } else {
            //To load items
            return TYPE_ITEM;
        }
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
    class LoadViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;

        public LoadViewHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.recycler_progressbar);
        }
    }

}

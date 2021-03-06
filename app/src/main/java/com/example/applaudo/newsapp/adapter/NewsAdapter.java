package com.example.applaudo.newsapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.applaudo.newsapp.R;
import com.example.applaudo.newsapp.models.News;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private static final int TYPE_LOAD = -1;
    private static final int TYPE_ITEM = -2;

    private ArrayList<News> mNewsList = new ArrayList<>(); //This is so the size returns 0 at first load.
    private ArrayList<News> mNewsListFull; //This is to do the filtering
    private OnNewsClicked mCallback;

    //Setter for updating the data after the null list is already "loaded"
    public void setmNewsList(ArrayList<News> mNewsList) {
        this.mNewsList = mNewsList;
        mNewsListFull = new ArrayList<>(mNewsList); //Creates the copy with the same data as in the list
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
                //If the list is empty, I will force it to load 1 element which will be the progress bar
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
    private class LoadViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;

        private LoadViewHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.recycler_progressbar);
        }
    }

    //region Filtering(SearchView)
    @Override
    public Filter getFilter() {
        return newsFilter;
    }

    private Filter newsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //This runs on the background thread
            List<News> filteredList = new ArrayList<>();

            if(constraint==null || constraint.length() == 0){
                //If there is no input, load everything

                //But first make sure the list is not empty
                //This is because when first loaded, the list is actually empty
                if (mNewsListFull != null) {
                    filteredList.addAll(mNewsListFull);
                }


            } else {
                //Something typed into the SearchView

                String filterPattern = constraint.toString().toLowerCase().trim();

                //Iterate over all the items to find matches

                for (News news : mNewsListFull){
                    if(news.getmHeadline().toLowerCase().contains(filterPattern)){
                        //If the item meets the condition, added to the list for it to be displayed
                        filteredList.add(news);
                    }
                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            //This runs on the UI

            if (results != null) {
                //It first clears the current list
                mNewsList.clear();
                //Fills the list with the filtered data
                mNewsList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        }
    };

    //endregion

}

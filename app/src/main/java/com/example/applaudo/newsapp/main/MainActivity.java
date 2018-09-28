package com.example.applaudo.newsapp.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.applaudo.newsapp.R;
import com.example.applaudo.newsapp.fragments.NewsFragment;

public class MainActivity extends AppCompatActivity {

    NewsFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find the ViewPager in the activity
        ViewPager viewPager = findViewById(R.id.viewpager);
        //Set the adapter
        adapter = new NewsFragmentPagerAdapter(getSupportFragmentManager());
        //Bind the adapter with the ViewPager
        viewPager.setAdapter(adapter);
        //Setting the tablayout
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    //region Adapter for the ViewPager
    private class NewsFragmentPagerAdapter extends FragmentPagerAdapter {
        //Array for the tab tittles
        private int[] mTabTitles = {R.string.tab1,R.string.tab2,R.string.tab3,R.string.tab4};

        NewsFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getString(mTabTitles[position]);
        }

        @Override
        public Fragment getItem(int position) {

            //Instance of the fragment to create
            NewsFragment newsFragment = new NewsFragment();
            //Sends the position to the new Fragment so it can load different data based on the position
            Bundle tabPosition = new Bundle();
            tabPosition.putInt("tabPosition", position);
            newsFragment.setArguments(tabPosition);

            switch (position) {
                //Even though I'll always return the fragment's instance, it needs to be inside a switch
                case 0:
                case 1:
                case 2:
                default:
                    return newsFragment;
            }

        }

        @Override
        public int getCount() {
            return 4;
        }
    }
    //endregion

}

package com.vishistv.musicplayer.Adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by DAT on 8/16/2015.
 */
public class SearchReultViewPagerAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    Context context;

    public SearchReultViewPagerAdapter(FragmentManager manager, Context context) {
        super(manager);
        if (manager.getFragments() != null) {
            manager.getFragments().clear();
        }
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment) {
        mFragmentList.add(fragment);

    }

}

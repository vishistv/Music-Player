package com.vishistv.musicplayer;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.vishistv.musicplayer.Adapters.SearchReultViewPagerAdapter;
import com.vishistv.musicplayer.Adapters.SongListAdapter;
import com.vishistv.musicplayer.Variables.Song;

import java.util.ArrayList;

public class SearchResultFragmentParent extends Fragment implements SongListAdapter.OnSongListener {

    private static final String TAG = "SearchResultFragmentPar";
    View view;
    //Getting maximum item that can fit
    RecyclerView rvDummy;
    SongListAdapter songListAdapter;
    SongListAdapter.OnSongListener sl;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchReultViewPagerAdapter adapter;
    private Context context;
    private int numberOfPages;
    private ArrayList<Song> songList = new ArrayList<>();
    private int itemFitCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.parent_fragment_search_result, container, false);
        getIDs(view);
        setEvents();
        return view;

    }

    private void getIDs(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.vp_search_result);
        viewPager.setVisibility(View.GONE);

        tabLayout = (TabLayout) view.findViewById(R.id.tb_dash);

    }

    private void setEvents() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void getInfo(int count, ArrayList<Song> songList) {
        getItemCountToFit(view, count, songList);
    }

    public void addPage(ArrayList<Song> songList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("songList", songList);
        SearchResultFragment fragmentChild = new SearchResultFragment();
        fragmentChild.setArguments(bundle);
        adapter.addFrag(fragmentChild);
        adapter.notifyDataSetChanged();
        if (adapter.getCount() > 0)
            tabLayout.setupWithViewPager(viewPager);
//        viewPager.setCurrentItem(0);
    }

    void getItemCountToFit(View view, final int count, final ArrayList<Song> orgSongList) {

        adapter = new SearchReultViewPagerAdapter(getFragmentManager(), getContext());
        viewPager.setAdapter(adapter);

        rvDummy = view.findViewById(R.id.rv_dummy);
        rvDummy.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        songList.add(makeFakeObjects());
        songListAdapter = new SongListAdapter(sl, songList, getContext());
        rvDummy.setLayoutManager(layoutManager);
        rvDummy.setAdapter(songListAdapter);

        rvDummy.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                rvDummy.getViewTreeObserver().removeOnPreDrawListener(this);

                itemFitCount = getInitialLoadCount(rvDummy) - 1;

                songList.clear();
                rvDummy.getAdapter().notifyDataSetChanged();
                rvDummy.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);

                if (itemFitCount > 0) {
                    double d = count / (double) itemFitCount;
                    numberOfPages = (int) Math.ceil(d);

                    for (int i = 0; i < numberOfPages; i++) {
                        ArrayList<Song> curPageSongList = new ArrayList<>();
                        for (int j = i * itemFitCount; j < (i * itemFitCount) + itemFitCount; j++) {
                            if (j < orgSongList.size())
                                curPageSongList.add(orgSongList.get(j));
                        }
                        addPage(curPageSongList);
                    }
                }

                return false;
            }
        });
    }

    private Song makeFakeObjects() {
        String s_name = "In The End";
        String al_name = "Hybrid Theory";
        String ar_name = "Linkin Park";

        return new Song(s_name, al_name, ar_name, "", "", "", "");
    }

    private int getInitialLoadCount(RecyclerView recyclerView) {
        int count = 0;

        View firstChild = recyclerView.getChildAt(0);

        Rect bounds = new Rect();
        recyclerView.getDecoratedBoundsWithMargins(firstChild, bounds);
        int recyclerHeightForItems = recyclerView.getHeight() - recyclerView.getPaddingTop()
                - recyclerView.getPaddingBottom();
        count = (recyclerHeightForItems + bounds.height() - 1) / bounds.height();

        return count;
    }

    @Override
    public void onNoteClick(int position) {

    }
}

package com.vishistv.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vishistv.musicplayer.Adapters.SongListAdapter;
import com.vishistv.musicplayer.Variables.Song;

import java.util.ArrayList;

public class SearchResultFragment extends Fragment implements SongListAdapter.OnSongListener {

    private static final String TAG = "SearchResultFragment";

    String childname;
    RecyclerView rvSearchResult;
    SongListAdapter songListAdapter;
    SongListAdapter.OnSongListener sl;
    private ArrayList<Song> songList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        Bundle bundle = getArguments();

        songList = (ArrayList<Song>) getArguments().getSerializable("songList");

        sl = new SongListAdapter.OnSongListener() {
            @Override
            public void onNoteClick(int position) {
                Intent intentPlaySong = new Intent(getActivity(), MediaPlayerActivity.class);
                intentPlaySong.putExtra("song", songList.get(position));
                startActivity(intentPlaySong);
            }
        };

        rvSearchResult = view.findViewById(R.id.rv_search_result);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        songListAdapter = new SongListAdapter(sl, songList, getContext());
        rvSearchResult.setLayoutManager(layoutManager);
        rvSearchResult.setAdapter(songListAdapter);

        return view;
    }


    @Override
    public void onNoteClick(int position) {
//        Intent intentPlaySong = new Intent(getActivity(), MediaPlayerActivity.class);
//        intentPlaySong.putExtra("song", songList.get(position));
//        startActivity(intentPlaySong);
    }
}

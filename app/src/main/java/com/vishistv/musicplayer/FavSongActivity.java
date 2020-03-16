package com.vishistv.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vishistv.musicplayer.Adapters.SongListAdapter;
import com.vishistv.musicplayer.Repositories.SongRepositories;
import com.vishistv.musicplayer.Variables.Song;

import java.util.ArrayList;

public class FavSongActivity extends AppCompatActivity implements SongListAdapter.OnSongListener {
    private static final String TAG = "FavSongActivity";
    RecyclerView rvFavSongs;
    SongListAdapter songListAdapter;
    ArrayList<Song> songList = new ArrayList<>();
    SongListAdapter.OnSongListener sl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_song);

        rvFavSongs = findViewById(R.id.rv_fav_songs);
        rvFavSongs.setLayoutManager(new LinearLayoutManager(this));

        SongRepositories songRepositories = new SongRepositories();


        songRepositories.getAllFavSongs(getApplicationContext()).observe(this, songs -> {
            Log.d(TAG, "onChanged: " + songs);
            if (songs.size() > 0) {
                songList = new ArrayList<Song>(songs);
                songListAdapter = new SongListAdapter(sl, songList, getApplicationContext());
                rvFavSongs.setAdapter(songListAdapter);
            }
        });

        sl = position -> {
            Intent intentPlaySong = new Intent(getApplicationContext(), MediaPlayerActivity.class);
            intentPlaySong.putExtra("song", songList.get(position));
            startActivity(intentPlaySong);
        };
    }

    public void onClickBackButton(View view) {
        super.onBackPressed();
    }

    @Override
    public void onNoteClick(int position) {

    }
}

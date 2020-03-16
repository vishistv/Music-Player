package com.vishistv.musicplayer.Repositories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.vishistv.musicplayer.Helpers.MPlayerDatabase;
import com.vishistv.musicplayer.Variables.Song;

import java.util.List;

import static android.content.ContentValues.TAG;

public class SongRepositories {

    @SuppressLint("StaticFieldLeak")
    public void insertSong(final Song song, final Context context) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MPlayerDatabase.getInstance(context).getSongDAO().insert(song);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteSong(final String tId, final Context context) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                int i = MPlayerDatabase.getInstance(context).getSongDAO().delete(tId);
                Log.d(TAG, "deleteSong: " + i);
                return null;
            }
        }.execute();
    }

    public LiveData<Integer> checkFavSong(String trackId, Context context) {
        return MPlayerDatabase.getInstance(context).getSongDAO().checkFav(trackId);
    }

    public LiveData<List<Song>> getAllFavSongs(Context context) {
        return MPlayerDatabase.getInstance(context).getSongDAO().getAllFavSongs();
    }
}

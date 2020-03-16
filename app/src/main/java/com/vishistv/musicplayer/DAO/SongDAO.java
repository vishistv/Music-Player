package com.vishistv.musicplayer.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.vishistv.musicplayer.Variables.Song;

import java.util.List;


@Dao
public interface SongDAO {

    @Query("SELECT * FROM Song")
    LiveData<List<Song>> getAllFavSongs();

    @Query("DELETE FROM Song WHERE trackId=:s_track_id")
    int delete(String s_track_id);

    @Insert
    void insert(Song song);

    @Query("SELECT COUNT(*) FROM Song WHERE trackId=:s_track_id")
    LiveData<Integer> checkFav(String s_track_id);
}

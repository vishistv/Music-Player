package com.vishistv.musicplayer.Helpers;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.vishistv.musicplayer.DAO.SongDAO;
import com.vishistv.musicplayer.Variables.Song;


@Database(entities = {Song.class}, version = 1, exportSchema = false)
public abstract class MPlayerDatabase extends RoomDatabase {

    private static final String DB_NAME = "MPlayerDatabase.db";
    private static volatile MPlayerDatabase instance;

    public static synchronized MPlayerDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static MPlayerDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                MPlayerDatabase.class,
                DB_NAME).build();
    }

    public abstract SongDAO getSongDAO();
}

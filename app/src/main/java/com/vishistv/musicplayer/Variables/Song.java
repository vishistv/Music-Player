package com.vishistv.musicplayer.Variables;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(indices = {@Index(value = {"trackId"},
        unique = true)})
public class Song implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String songName;
    private String artistName;
    private String albumName;
    private String prevUrl;
    private String albumPic100Url;
    private String albumPic60Url;
    private String trackId;

    public Song(String songName, String artistName, String albumName, String prevUrl, String albumPic100Url, String albumPic60Url, String trackId) {
        this.songName = songName;
        this.artistName = artistName;
        this.albumName = albumName;
        this.prevUrl = prevUrl;
        this.albumPic100Url = albumPic100Url;
        this.albumPic60Url = albumPic60Url;
        this.trackId = trackId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getPrevUrl() {
        return prevUrl;
    }

    public String getAlbumPic100Url() {
        return albumPic100Url;
    }

    public String getAlbumPic60Url() {
        return albumPic60Url;
    }

    public String getTrackId() {
        return trackId;
    }
}

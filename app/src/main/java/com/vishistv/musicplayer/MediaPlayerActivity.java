package com.vishistv.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.vishistv.musicplayer.Repositories.SongRepositories;
import com.vishistv.musicplayer.Variables.Song;

import java.io.IOException;

public class MediaPlayerActivity extends AppCompatActivity {
    private static final String TAG = "MediaPlayerActivity";

    SeekBar sbMusicProgress;
    MediaPlayer mediaPlayer;
    ImageView ivAlbumPic;
    ImageButton ibPlayPause, ibMarkFav, ibSongList;
    TextView tvSongTitle, tvArtistAlbum, tvElapsedTime, tvRemainingTime;

    String songUrl, albumPicUrl, songTitle, songArtistAlbum, trackId;
    boolean favSong = false;
    boolean songPaused = false;
    boolean sonPrepared = false;
    boolean songEnded = false;

    int pauseTime = -1;
    int songDuration;

    int minLeft, secLeft, minElapsed = 0, secElapsed = 0;
    Runnable runnable;
    Song song;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        song = (Song) getIntent().getSerializableExtra("song");
        songTitle = song.getSongName();
        songArtistAlbum = song.getArtistName() + "  |  " + song.getAlbumName();
        songUrl = song.getPrevUrl();
        albumPicUrl = song.getAlbumPic100Url();
        trackId = song.getTrackId();

        SongRepositories songRepositories = new SongRepositories();
        songRepositories.checkFavSong(trackId, getApplicationContext()).observe(this, count -> {
            Log.d(TAG, "onCreate: " + count);
            if (count > 0) {
                favSong = true;
                ibMarkFav.setImageResource(R.drawable.shape_heart_red);
            }
        });

        sbMusicProgress = findViewById(R.id.sb_music_progress);
        ivAlbumPic = findViewById(R.id.iv_album_pic);
        ibPlayPause = findViewById(R.id.ib_play_pause);
        ibMarkFav = findViewById(R.id.ib_mark_fav);
        ibSongList = findViewById(R.id.ib_song_list);
        tvSongTitle = findViewById(R.id.tv_song_title);
        tvArtistAlbum = findViewById(R.id.tv_artist_album);
        tvElapsedTime = findViewById(R.id.tv_elapsed_time);
        tvRemainingTime = findViewById(R.id.tv_remaining_time);

        tvSongTitle.setText(songTitle);
        tvArtistAlbum.setText(songArtistAlbum);
        Picasso.with(getApplicationContext()).load(albumPicUrl).into(ivAlbumPic);

        sbMusicProgress.setEnabled(false);

        runPlayer();

        //Playing songs at random clicks on seekbar
        sbMusicProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mediaPlayer != null && b) {
                    mediaPlayer.seekTo(i * 1000);

                    minElapsed = (int) ((i * 1000 % (1000 * 60 * 60)) / (1000 * 60));
                    secElapsed = (int) (((i * 1000 % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

                    int minTot = (int) ((songDuration % (1000 * 60 * 60)) / (1000 * 60));
                    int secTot = (int) (((songDuration % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

                    minLeft = minTot - minElapsed;
                    secLeft = secTot - secElapsed;

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    void runPlayer() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(songUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    sbMusicProgress.setEnabled(true);
                    sonPrepared = true;
                    songDuration = mediaPlayer.getDuration();
                    sbMusicProgress.setMax(songDuration / 1000);
                    Log.d(TAG, "onPrepared: " + songDuration + " " + songDuration / 1000);
                    mediaPlayer.start();

                    minLeft = (int) ((songDuration % (1000 * 60 * 60)) / (1000 * 60));
                    secLeft = (int) (((songDuration % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

                    tvElapsedTime.setText(minElapsed + ":" + secElapsed);
                    tvRemainingTime.setText(minLeft + ":" + secLeft);

                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayer != null && !songPaused && !songEnded) {
                                int mCurrentPosition = mediaPlayer.getCurrentPosition();

                                //Elapsed Time
                                secElapsed++;
                                if (secElapsed == 60) {
                                    minElapsed++;
                                    secElapsed = 0;
                                }

                                //Remaining Time
                                secLeft--;
                                if (secLeft == -1) {
                                    if (minLeft > 0)
                                        minLeft--;
                                    secLeft = 60;
                                }

                                tvElapsedTime.setText(formatCounter(minElapsed, secElapsed));
                                tvRemainingTime.setText(formatCounter(minLeft, secLeft));
                                sbMusicProgress.setProgress(mCurrentPosition / 1000);

                            }
                            mHandler.postDelayed(this, 1000);
                        }
                    };
                    mHandler.post(runnable);
                }
            });
        } catch (IOException e) {
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                songEnded = true;
                resetPlayer();
            }
        });
    }

    String formatCounter(int min, int sec) {
        StringBuffer buf = new StringBuffer();

        buf.append(String.format("%02d", min)).append(":").append(String.format("%02d", sec));
        return buf.toString();
    }

    public void onClickPlayPause(View view) {
        Log.d(TAG, "onClickPlayPause: " + songEnded + " " + songPaused);
        if (songEnded) {
            songEnded = false;
            ibPlayPause.setImageResource(R.drawable.combined_shape_2);
            runPlayer();
        } else if (songPaused) { //It was paused. So now Play.
            ibPlayPause.setImageResource(R.drawable.combined_shape_2);
            songPaused = false;
            sbMusicProgress.setEnabled(true);
            mediaPlayer.seekTo(pauseTime);
            mediaPlayer.start();

        } else { //It was playing. So now songPaused.
            ibPlayPause.setImageResource(R.drawable.triangle);
            songPaused = true;
            sbMusicProgress.setEnabled(false);
            mediaPlayer.pause();
            pauseTime = mediaPlayer.getCurrentPosition();
        }
    }

    void resetPlayer() {
        mHandler.removeCallbacks(runnable);
        minElapsed = 0;
        secElapsed = 0;
        sbMusicProgress.setEnabled(false);
        tvElapsedTime.setText("--:--");
        tvRemainingTime.setText("--:--");
        ibPlayPause.setImageResource(R.drawable.triangle);
        songPaused = false;
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    @Override
    protected void onStop() {
        if (mediaPlayer.isPlaying()) {
            ibPlayPause.setImageResource(R.drawable.triangle);
            songPaused = true;
            sbMusicProgress.setEnabled(false);
            mediaPlayer.pause();
            pauseTime = mediaPlayer.getCurrentPosition();
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        if (mediaPlayer.isPlaying()) {
            ibPlayPause.setImageResource(R.drawable.triangle);
            songPaused = true;
            sbMusicProgress.setEnabled(false);
            mediaPlayer.pause();
            pauseTime = mediaPlayer.getCurrentPosition();
        }
        super.onPause();
    }

    public void onClickMarkFav(View view) {
        SongRepositories songRepositories = new SongRepositories();
        if (!favSong) {
            songRepositories.insertSong(song, getApplicationContext());
            ibMarkFav.setImageResource(R.drawable.shape_heart_red);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.successfully_marked_fav), Toast.LENGTH_SHORT).show();

        } else {
            favSong = false;
            String tId = song.getTrackId();
            songRepositories.deleteSong(tId, getApplicationContext());
            ibMarkFav.setImageResource(R.drawable.shape_heart);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.successfully_removed_fav), Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickFavSongs(View view) {
        ibPlayPause.setImageResource(R.drawable.triangle);
        songPaused = true;
        sbMusicProgress.setEnabled(false);
        mediaPlayer.pause();
        pauseTime = mediaPlayer.getCurrentPosition();
        startActivity(new Intent(MediaPlayerActivity.this, FavSongActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (mediaPlayer.isPlaying())
            resetPlayer();
        super.onBackPressed();
    }


    public void onClickBackButton(View view) {
        if (mediaPlayer.isPlaying())
            resetPlayer();
        super.onBackPressed();
    }


}

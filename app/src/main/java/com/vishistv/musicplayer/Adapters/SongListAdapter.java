package com.vishistv.musicplayer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vishistv.musicplayer.R;
import com.vishistv.musicplayer.Variables.Song;

import java.util.ArrayList;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {

    private ArrayList<Song> songsList = new ArrayList<>();
    private Context context;
    private OnSongListener onSongListener;


    public SongListAdapter(OnSongListener onSongListener, ArrayList<Song> notesList, Context context) {
        this.songsList = notesList;
        this.context = context;
        this.onSongListener = onSongListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item, parent, false);
        return new ViewHolder(v, onSongListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song songItem = songsList.get(position);
        holder.tvSongTitle.setText(songItem.getSongName());
        holder.tvArtistAlbum.setText(songItem.getArtistName() + "  |  " + songItem.getAlbumName());
        if (!songItem.getAlbumPic60Url().equals(""))
            Picasso.with(context).load(songItem.getAlbumPic60Url()).into(holder.ivAlbumPicPrev);
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public interface OnSongListener {
        void onNoteClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnSongListener mOnSongListener;
        private TextView tvSongTitle;
        private TextView tvArtistAlbum;
        private ImageView ivAlbumPicPrev;

        public ViewHolder(View itemView, OnSongListener onSongListener) {
            super(itemView);
            tvSongTitle = itemView.findViewById(R.id.tv_song_title);
            tvArtistAlbum = itemView.findViewById(R.id.tv_artist_album);
            ivAlbumPicPrev = itemView.findViewById(R.id.iv_album_pic_prev);
            mOnSongListener = onSongListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onSongListener.onNoteClick(getAdapterPosition());
        }

    }
}

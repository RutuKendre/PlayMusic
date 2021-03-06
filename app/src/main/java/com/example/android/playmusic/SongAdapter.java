package com.example.android.playmusic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.playmusic.R;
import com.example.android.playmusic.Song;

import java.util.ArrayList;

public class SongAdapter extends ArrayAdapter<Song> {



    private int mColorResourceId;

    public SongAdapter(Activity context, ArrayList<Song> abcd) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, abcd);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Song currentSong = getItem(position);

        TextView songname = (TextView) listItemView.findViewById(R.id.song_name_view);

        songname.setText(currentSong.getSongName());

        TextView singername = (TextView) listItemView.findViewById(R.id.singer_name_view);

        singername.setText(currentSong.getSingerName());

        listItemView.setTag(position);

        return listItemView;

    }
}
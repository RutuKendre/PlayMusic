package com.example.android.playmusic;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;


public class Song {
    private String mSongName;
    private String mSingerName;
    private int mSongId;

    public Song(String songName,String singerName,int songId)
    {
        mSongName = songName;
        mSingerName = singerName;
        mSongId = songId;


    }


    public String getSongName()
    {
        return mSongName;
    }
    public String getSingerName()
    {
        return mSingerName;
    }

public int getSongId()
{
    return mSongId;
}

}

package com.example.android.playmusic;

public class Song {
    private String mSongName;
    private String mSingerName;
    private int mSongId;
    //private int mImageId;

    public Song(String songName,String singerName,int songId)
    {
        mSongName = songName;
        mSingerName = singerName;
        mSongId = songId;
        // mImageId=imageId;
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
   // public int getImageId()
    //{
     //   return mImageId;
   // }

}

package com.example.android.playmusic;


import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;

public class MusicService extends Service
{

    private final IBinder musicBind = new MusicBinder();
    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared (MediaPlayer mediaPlayer) {
            mediaPlayer.start();

        }
    };



    //media player
    private MediaPlayer mediaPlayer;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//initialize position
        songPosn=0;
//create player
        mediaPlayer = new MediaPlayer();
        startMusicPlayer();
    }
    public void startMusicPlayer()
    {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(mPreparedListener);
        //mediaPlayer.setOnCompletionListener(mCompletionListener);
        //mediaPlayer.setOnErrorListener(mErrorListener);
    }
    public void setList(ArrayList<Song> theSongs){
        songs=theSongs;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public void setSong(int songIndex){
        songPosn=songIndex;
    }
    public void playMethod()
    {
        mediaPlayer.reset();
        Song playSong = songs.get(songPosn);
//get id
        long currSong = playSong.getSongId();
//set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
        try{
            mediaPlayer.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        mediaPlayer.prepareAsync();
    }

}


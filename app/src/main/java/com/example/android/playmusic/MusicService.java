package com.example.android.playmusic;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import java.util.Random;
import android.app.Notification;
import android.app.PendingIntent;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import com.example.android.playmusic.MainActivity;

public class MusicService extends Service {


    //media player
    private MediaPlayer mediaPlayer;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;

    private String songTitle;
    public static final int notificationId = 1;
    public static final String CHANNEL_ID = "music";
    private final IBinder musicBind = new MusicBinder();
    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mediaPlayer.start();

            Intent intent = new Intent(MusicService.this, MainActivity.class);

            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            PendingIntent pendingIntent = PendingIntent.getActivity(MusicService.this, 0, intent, 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MusicService.this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.image)
                    .setContentTitle(" Now Playing")
                    .setContentText(songTitle)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(false);

            NotificationManagerCompat notificationManage = NotificationManagerCompat.from(MusicService.this);

         // notificationId is a unique int for each notification that you must define
            notificationManage.notify(notificationId, mBuilder.build());

        }
    };
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if (mediaPlayer.getCurrentPosition() > 0) {
                mp.reset();
                //playNext();
            }
        }
    };

    private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            mp.reset();
            return false;

        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();



//initialize position
        songPosn = 0;
//create player
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(mPreparedListener);
        mediaPlayer.setOnCompletionListener(mCompletionListener);
        mediaPlayer.setOnErrorListener(mErrorListener);
        createNotificationChannel();

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public void setList(ArrayList<Song> theSongs) {
        songs = theSongs;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public void setSong(int songIndex) {
        songPosn = songIndex;
    }

    public void playMethod() {
        mediaPlayer.reset();
        Song playSong = songs.get(songPosn);
//get id
        songTitle = playSong.getSongName();
        long currSong = playSong.getSongId();
//set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        mediaPlayer.prepareAsync();
    }

    public int getPosn() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getDur() {
        return mediaPlayer.getDuration();
    }

    public boolean isPng() {
        return mediaPlayer.isPlaying();
    }

    public void pausePlayer() {
        mediaPlayer.pause();
    }

    public void seek(int posn) {
        mediaPlayer.seekTo(posn);
    }

    public void go() {
        mediaPlayer.start();
    }


    public int playPrev() {
        songPosn--;
        if (songPosn < 0) {
            songPosn = songs.size() - 1;}
        playMethod();
        return songPosn;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);

    }

    public int playNext() {
        songPosn++;

        if (songPosn >= songs.size()) songPosn = 0;
        playMethod();
        return songPosn;
    }

}

